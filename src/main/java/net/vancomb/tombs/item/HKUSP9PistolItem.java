package net.vancomb.tombs.item;

import java.util.function.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.vancomb.tombs.epicfight.animations.TombsAnimations;
import net.vancomb.tombs.registry.TombsSounds;
import net.vancomb.tombs.world.capabilities.item.BulletsCapability;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class HKUSP9PistolItem extends ProjectileWeaponItem {
	public static final Predicate<ItemStack> NO_ITMES = (p_43017_) -> {
		return false;
	};
	
	public HKUSP9PistolItem(Properties pProperties) {
		super(pProperties);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		BulletsCapability bulletCap = BulletsCapability.getBullets(itemstack).orElse(null);
		
		if (bulletCap == null) {
			return InteractionResultHolder.fail(itemstack);
		} else {
			// Check if ammo remains
			if (bulletCap.canShot()) {
				bulletCap.consume();
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
					EpicFightCapabilities.getUnparameterizedEntityPatch(player, LivingEntityPatch.class).ifPresent(entitypatch -> {
						entitypatch.getAnimator().playAnimation(hand == InteractionHand.OFF_HAND ? TombsAnimations.BIPED_LEFTHAND_SHOT : TombsAnimations.BIPED_RIGHTHAND_SHOT, 0.0F);
					});
					
					if (bulletCap.canShot()) {
						Minecraft.getInstance().gui.setOverlayMessage(Component.literal(bulletCap.getBulletsLeft() + " remain in " + (hand == InteractionHand.MAIN_HAND ? "right hand" : "left hand")), false);
					} else {
						Minecraft.getInstance().gui.setOverlayMessage(Component.literal("No bullet in " + (hand == InteractionHand.MAIN_HAND ? "right hand" : "left hand")), false);
					}
				}
				
				return InteractionResultHolder.consume(itemstack);
			} else {
				if (level.isClientSide()) {
					// Play the shooting sound in local
					level.playLocalSound(player.blockPosition(), TombsSounds.PISTOL_EMPTY.get(), SoundSource.PLAYERS, 1.0F, 1.0F, false);
					Minecraft.getInstance().gui.setOverlayMessage(Component.literal("No bullet in " + (hand == InteractionHand.MAIN_HAND ? "right hand" : "left hand")), false);
				}
				
				return InteractionResultHolder.fail(itemstack);
			}
		}
	}
	
	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return NO_ITMES;
	}
	
	@Override
	public int getDefaultProjectileRange() {
		return 25;
	}
}