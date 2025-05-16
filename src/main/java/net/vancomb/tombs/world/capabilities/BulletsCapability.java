package net.vancomb.tombs.world.capabilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.IntTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.vancomb.tombs.Tombs;
import net.vancomb.tombs.epicfight.animations.TombsAnimations;
import net.vancomb.tombs.epicfight.capabilities.item.common.TombsWeaponCategory;
import net.vancomb.tombs.networking.ClientBoundBulletPacket;
import net.vancomb.tombs.networking.NetworkManager;
import net.vancomb.tombs.networking.ServerBoundShotPacket;
import net.vancomb.tombs.registry.TombsItems;
import net.vancomb.tombs.registry.TombsSounds;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

/**
 * A capability object that counts left bullets
 */
public class BulletsCapability {
	
	// Max bullets when reload
	private static final int MAX_BULLETS = 20;
	
	// Cooldown ticks to next fire
	private static final int COOLDOWN_TICKS = 10;
	
	// Max accelerating when shot successively
	private static final int MAX_FIRE_ACCELRATION = 4;
	
	public static int getMaxBullets() {
		return MAX_BULLETS;
	}
	
	// A capability key class to get an instance of bullets capability from item stack
	public static final Capability<BulletsCapability> BULLETS_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
	
	// A helper method gets bullet capability from itemstack
	public static LazyOptional<BulletsCapability> getBullets(Player player) {
		return player == null ? LazyOptional.empty() : player.getCapability(BULLETS_CAPABILITY);
	}
	
	private int accelration;
	private int remainBullets;
	private int cooldown = 0;
	private InteractionHand shotHand = InteractionHand.MAIN_HAND;
	
	public BulletsCapability(Player player) {
		this.remainBullets = MAX_BULLETS;
	}
	
	public int getRemainBullets() {
		return this.remainBullets;
	}
	
	public boolean canShot(Player player) {
		return (!player.level().isClientSide() || this.cooldown <= 0) && this.remainBullets > 0;
	}
	
	// Reload a clip
	public void bulletReload(LivingEntity player) {
		int pistols = 0;
		
		if (player.getMainHandItem().is(TombsItems.HK_USP9_PISTOL.get())) pistols++;
		if (player.getOffhandItem().is(TombsItems.HK_USP9_PISTOL.get())) pistols++;
		
		this.remainBullets = MAX_BULLETS * pistols;
	}
	
	public void setRemainBullet(int bullets) {
		this.remainBullets = bullets;
	}
	
	public void tick() {
		if (this.cooldown > -1) --this.cooldown;
	}
	
	public boolean tryFiring(Player player) {
		LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(player, LivingEntityPatch.class);
		
		if (entitypatch == null) {
			return false;
		}
		
		if (!entitypatch.getEntityState().canUseItem()) {
			return false;
		}
		
		boolean mainhandPistol = entitypatch.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getWeaponCategory() == TombsWeaponCategory.USP9;
		boolean offhandPistol = entitypatch.getAdvancedHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == TombsWeaponCategory.USP9;
		
		if (!this.canShot(player)) {
			if (player.level().isClientSide() && this.cooldown <= 0 && this.isHoldingCompatibleWeapon(player)) {
				// Play the shooting sound in local
				player.level().playLocalSound(player.blockPosition(), TombsSounds.PISTOL_EMPTY.get(), SoundSource.PLAYERS, 1.0F, 1.0F, false);
				this.setCooldown(mainhandPistol && offhandPistol);
			}
			
			return false;
		}
		
		if (mainhandPistol && offhandPistol) {
			this.shotHand = this.shotHand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
			this.shotPistolInHand(player.level(), player, this.shotHand);
		} else if (mainhandPistol) {
			this.shotPistolInHand(player.level(), player, InteractionHand.MAIN_HAND);
		} else if (offhandPistol) {
			this.shotPistolInHand(player.level(), player, InteractionHand.OFF_HAND);
		}
		
		if (this.cooldown == 0) {
			this.accelration = Math.min(this.accelration + 1, MAX_FIRE_ACCELRATION);
		} else {
			this.accelration = 0;
		}
		
		this.setCooldown(mainhandPistol && offhandPistol);
		
		return true;
	}
	
	public boolean isHoldingCompatibleWeapon(Player player) {
		LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(player, LivingEntityPatch.class);
		
		if (entitypatch == null) {
			return player.getMainHandItem().is(TombsItems.HK_USP9_PISTOL.get()) || player.getOffhandItem().is(TombsItems.HK_USP9_PISTOL.get());
		} else {
			boolean mainhandPistol = entitypatch.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getWeaponCategory() == TombsWeaponCategory.USP9;
			boolean offhandPistol = entitypatch.getAdvancedHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == TombsWeaponCategory.USP9;
			return mainhandPistol || offhandPistol;
		}
	}
	
	private void setCooldown(boolean twohand) {
		this.cooldown = (twohand ? (COOLDOWN_TICKS - this.accelration) / 2 : COOLDOWN_TICKS - this.accelration);
	}
	
	private void shotPistolInHand(Level level, Player player, InteractionHand hand) {
		--this.remainBullets;
		
		// Execute gunfire
		if (!level.isClientSide()) {
			// Play a shooting sound to other players
			level.playSound(player, player.blockPosition(), TombsSounds.PISTOL_SHOT.get(), SoundSource.PLAYERS);
			
			double maxDistance = 200.0D;
			
			// Conduct ray-tracing to find a entity that is hit first
			Vec3 vec31 = player.getViewVector(1.0F);
			EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(
				  player																						// excluded entity to ray-trace
				, player.getEyePosition()																		// the beginning position of the ray
				, player.getEyePosition().add(vec31.scale(maxDistance))											// the end position of the ray
				, player.getBoundingBox().expandTowards(vec31.scale(maxDistance)).inflate(1.0D, 1.0D, 1.0D)		// Bounding Box to select entities to conduct ray-cast
				, (p_234237_) -> {
					return !p_234237_.isSpectator() && p_234237_.isPickable();
				}																								// A validator for an hit entity
				, maxDistance * maxDistance																		// A squared value of max distance
			);
			
			// If a hit entity exists, hurt the target
			if (entityhitresult != null) {
				int invulTime = entityhitresult.getEntity().invulnerableTime;
				
				// To ignore the invul time, set it to zero and record the previous invul time so that we can revert after hurting an entity
				entityhitresult.getEntity().invulnerableTime = 0;
				entityhitresult.getEntity().hurt(player.damageSources().sting(player), 5.0F);
				entityhitresult.getEntity().invulnerableTime = invulTime;
			}
		} else {
			// Play the shooting sound in local
			level.playLocalSound(player.blockPosition(), TombsSounds.PISTOL_SHOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F, false);
			
			// Play the shot animation
			EpicFightCapabilities.<LivingEntity, LivingEntityPatch<LivingEntity>>getParameterizedEntityPatch(player, LivingEntity.class, LivingEntityPatch.class).ifPresent(entitypatch -> {
				entitypatch.playAnimationInClientSide(hand == InteractionHand.OFF_HAND ? TombsAnimations.BIPED_LEFTHAND_SHOT : TombsAnimations.BIPED_RIGHTHAND_SHOT, 0.0F);
			});
			
			NetworkManager.INSTANCE.sendToServer(new ServerBoundShotPacket(player.getId()));
		}
	}
	
	/**
	 * A capability provider class, implements {@link INBTSerializable} to save the left bullet count
	 */
	public static class Provider implements ICapabilityProvider, INBTSerializable<IntTag> {
		private final BulletsCapability bulletsCapability;
		private final LazyOptional<BulletsCapability> lazyOptional;
		
		public Provider(Player player) {
			this.bulletsCapability = new BulletsCapability(player);
			this.lazyOptional = LazyOptional.of(() -> this.bulletsCapability);
		}
		
		/**
		 * Return a bullet capability if the given key is {@link BulletsCapability#BULLETS_CAPABILITY}}
		 */
		@Override
		public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capabilityKey, @Nullable Direction side) {
			return capabilityKey == BulletsCapability.BULLETS_CAPABILITY ? this.lazyOptional.cast() : LazyOptional.empty();
		}
		
		/**
		 * Serialize to Tag to save data
		 */
		@Override
		public IntTag serializeNBT() {
			return IntTag.valueOf(this.bulletsCapability.getRemainBullets());
		}
		
		/**
		 * Read data from Tag
		 */
		@Override
		public void deserializeNBT(IntTag nbt) {
			this.bulletsCapability.remainBullets = nbt.getAsInt();
		}
	}
	
	@Mod.EventBusSubscriber(modid = Tombs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ModBusEvent {
		@SubscribeEvent
		public static void registerCapabilities(RegisterCapabilitiesEvent event) {
			event.register(BulletsCapability.class);
		}
	}
	
	/**
	 * An inner class to register capability attach event
	 */
	@Mod.EventBusSubscriber(modid = Tombs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class ForgeEvent {
		// Capability key
		private static final ResourceLocation BULLETS = ResourceLocation.fromNamespaceAndPath(EpicFightMod.MODID, "bullets");
		
		@SubscribeEvent
		public static void attachItemCapabilityEvent(AttachCapabilitiesEvent<Entity> event) {
			
			/**
			 * Attach the bullet capability if ItemStack holds {@link USP9PistolItem}
			 */
			if (EntityType.PLAYER.equals(event.getObject().getType()) && !event.getCapabilities().containsKey(BULLETS)) {
				event.addCapability(BULLETS, new BulletsCapability.Provider((Player)event.getObject()));
			}
		}
		
		@SubscribeEvent
		public static void entityJoinLevelEvent(EntityJoinLevelEvent event) {
			if (event.getEntity() instanceof ServerPlayer serverPlayer) {
				BulletsCapability.getBullets(serverPlayer).ifPresent(bullets -> {
					// Synchronize remain bullets from server to client
					NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ClientBoundBulletPacket(bullets.getRemainBullets()));
				});
			}
		}
	}
}
