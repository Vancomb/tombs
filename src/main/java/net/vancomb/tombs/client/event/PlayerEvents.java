package net.vancomb.tombs.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.MouseButton;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vancomb.tombs.Tombs;
import net.vancomb.tombs.client.input.TombsKeyMappings;
import net.vancomb.tombs.epicfight.animations.TombsAnimations;
import net.vancomb.tombs.registry.TombsItems;
import net.vancomb.tombs.world.capabilities.item.BulletsCapability;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Tombs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public abstract class PlayerEvents {
	// Variables that handle key down
	private static boolean leftDown;
	
	@SubscribeEvent
	public static void tombs$mouseButtonPress(MouseButton.Pre event) {
		// Check which button pressed (0: left, 1: right)
		if (event.getButton() != 0) {
			return;
		}
		
		// Set downed state depending on key action (1: press 0: release)
		leftDown = event.getAction() == 1;
		
		// when button downed
		if (event.getAction() != 1) {
			return;
		}
		
		if (!checkInGame()) {
			return;
		}
		
		Player player = Minecraft.getInstance().player;
		
		BulletsCapability.getBullets(player).ifPresent(bullets -> {
			if (bullets.tryFiring(player)) {
			}
			
			event.setCanceled(true);
		});
	}
	
	@SubscribeEvent
	public static void tombs$tickClient(TickEvent.ClientTickEvent event) {
		if (!checkInGame()) {
			return;
		}
		
		if (event.phase == TickEvent.Phase.START) {
			Player player = Minecraft.getInstance().player;
			
			BulletsCapability.getBullets(player).ifPresent(bullets -> {
				// Count cooldown
				bullets.tick();
				
				// Check if key downed
				if (leftDown) {
					bullets.tryFiring(player);
				}
			});
		}
	}
	
	// Execute reloading
	@SubscribeEvent
	public static void tombs$keyPress(InputEvent.Key event) {
		if (event.getKey() != TombsKeyMappings.RELOAD.getKey().getValue()) {
			return;
		}
		
		// when button downed
		if (event.getAction() != 1) {
			return;
		}
		
		if (!checkInGame()) {
			return;
		}
		
		Player player = Minecraft.getInstance().player;
		
		// Reload when player's holding pistol either left or right hand
		if ((player.getMainHandItem().is(TombsItems.HK_USP9_PISTOL.get()) || player.getOffhandItem().is(TombsItems.HK_USP9_PISTOL.get()))) {
			EpicFightCapabilities.<LivingEntity, LivingEntityPatch<LivingEntity>>getParameterizedEntityPatch(player, LivingEntity.class, LivingEntityPatch.class).ifPresent(entitypatch -> {
				
				// Cancel reload when playing an action animation
				if (entitypatch.getEntityState().inaction()) {
					return;
				}
				
				// Check each hands if holding a pistol or not
				boolean mainhand = entitypatch.getOriginal().getMainHandItem().is(TombsItems.HK_USP9_PISTOL.get());
				boolean offhand = entitypatch.getOriginal().getOffhandItem().is(TombsItems.HK_USP9_PISTOL.get());
				
				// Play reload animation depending on item state
				if (mainhand && offhand) {
					entitypatch.playAnimationInClientSide(TombsAnimations.BIPED_RELOAD_TWOHAND, 0.0F);
				} else if (mainhand) {
					entitypatch.playAnimationInClientSide(TombsAnimations.BIPED_RELOAD_RIGHTHAND, 0.0F);
				} else if (offhand) {
					entitypatch.playAnimationInClientSide(TombsAnimations.BIPED_RELOAD_LEFTHAND, 0.0F);
				}
			});
		}
	}
	
	// Check if player in game and no gui screen is opened
	private static boolean checkInGame() {
		// when the world is simulated
		if (Minecraft.getInstance().isPaused() || Minecraft.getInstance().screen != null) {
			return false;
		}
		
		Player player = Minecraft.getInstance().player;
		
		// when player not null
		if (player == null) {
			return false;
		}
		
		return true;
	}
}
