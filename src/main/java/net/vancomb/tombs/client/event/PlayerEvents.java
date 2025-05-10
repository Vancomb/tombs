package net.vancomb.tombs.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Tombs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public abstract class PlayerEvents {
	private static final int PISTOL_FIRE_COOLDOWN_TICKS = 8;
	
	// Cooldown ticks for guns in each hand
	private static int leftButtonCooldown;
	private static int rightButtonCooldown;
	
	// Variables that handle key down
	private static boolean leftDown;
	private static boolean rightDown;
	
	// An event fired when the player clicks mouse buttons
	@SubscribeEvent
	public static void tombraider$mouseButtonPress(MouseButton.Pre event) {
		// Save button down state
		switch (event.getButton()) {
		case 0 -> {
			leftDown = event.getAction() == 1;
		}
		case 1 -> {
			rightDown = event.getAction() == 1;
		}
		}
		
		// when button downed
		if (event.getAction() != 1) {
			return;
		}
		
		if (!checkInGame()) {
			return;
		}
		
		Player player = Minecraft.getInstance().player;
		
		switch (event.getButton()) {
		// left click
		case 0 -> {
			if (
				player.getOffhandItem().is(TombsItems.HK_USP9_PISTOL.get()) &&
				player.getMainHandItem().is(TombsItems.HK_USP9_PISTOL.get())
			) {
				if (leftButtonCooldown <= 0) {
					firePistolInHand(player, InteractionHand.OFF_HAND);
				}
				event.setCanceled(true);
			}
		}
		// right click
		case 1 -> {
			if (TombsItems.HK_USP9_PISTOL.get().equals(player.getMainHandItem().getItem())) {
				if (rightButtonCooldown <= 0) {
					firePistolInHand(player, InteractionHand.MAIN_HAND);
				}
				event.setCanceled(true);
			}
		}
		}
	}
	
	@SubscribeEvent
	public static void tombraider$keyPress(InputEvent.Key event) {
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
	
	@SubscribeEvent
	public static void tombraider$tickClient(TickEvent.ClientTickEvent event) {
		if (!checkInGame()) {
			return;
		}
		
		if (event.phase == TickEvent.Phase.START) {
			// Decrease the cooldown ticks
			if (leftButtonCooldown > 0) leftButtonCooldown--;
			if (rightButtonCooldown > 0) rightButtonCooldown--;
			
			Player player = Minecraft.getInstance().player;
			
			if (leftDown) {
				if (
					player.getOffhandItem().is(TombsItems.HK_USP9_PISTOL.get()) &&
					player.getMainHandItem().is(TombsItems.HK_USP9_PISTOL.get())
				) {
					if (leftButtonCooldown <= 0) {
						firePistolInHand(player, InteractionHand.OFF_HAND);
					}
				}
			}
			
			if (rightDown) {
				if (TombsItems.HK_USP9_PISTOL.get().equals(player.getMainHandItem().getItem())) {
					if (rightButtonCooldown <= 0) {
						firePistolInHand(player, InteractionHand.MAIN_HAND);
					}
				}
			}
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
	
	private static void firePistolInHand(Player player, InteractionHand hand) {
		InteractionResult result = Minecraft.getInstance().gameMode.useItem(player, hand);
		
		// Cancel the behind process if pistol successfully fired
		if (result.consumesAction()) {
			Minecraft.getInstance().gameRenderer.itemInHandRenderer.itemUsed(hand);
		}
		
		if (hand == InteractionHand.MAIN_HAND) {
			rightButtonCooldown = PISTOL_FIRE_COOLDOWN_TICKS;
		} else {
			leftButtonCooldown = PISTOL_FIRE_COOLDOWN_TICKS;
		}
	}
}
