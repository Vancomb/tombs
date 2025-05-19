package net.vancomb.tombs.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.vancomb.tombs.Tombs;
import net.vancomb.tombs.networking.ClientBoundBulletPacket;
import net.vancomb.tombs.networking.NetworkManager;
import net.vancomb.tombs.registry.TombsItems;
import net.vancomb.tombs.world.capabilities.BulletsCapability;

@Mod.EventBusSubscriber(modid = Tombs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingEntityEvents {
	@SubscribeEvent
	public static void tombs$livingEquipChangeEvent(LivingEquipmentChangeEvent event) {
		
		// Subscribe equip change event to correct max bullets
		if (event.getEntity() instanceof ServerPlayer serverPlayer && (event.getFrom().is(TombsItems.HK_USP9_PISTOL.get()) || event.getTo().is(TombsItems.HK_USP9_PISTOL.get()))) {
			BulletsCapability.getBullets(serverPlayer).ifPresent(bullets -> {
				int currentBullets = bullets.getRemainBullets();
				int pistols = 0;
				
				if (serverPlayer.getMainHandItem().is(TombsItems.HK_USP9_PISTOL.get())) pistols++;
				if (serverPlayer.getOffhandItem().is(TombsItems.HK_USP9_PISTOL.get())) pistols++;
				
				pistols = Math.max(pistols, 1);
				int maxBullets = BulletsCapability.getMaxBullets() * pistols;
				
				if (maxBullets < currentBullets) {
					// Correct the current bullets to max bullets
					bullets.setRemainBullet(maxBullets);
					
					// Synchronize remain bullets from server to client
					NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ClientBoundBulletPacket(bullets.getRemainBullets()));
				}
			});
		}
		
		// Handle Vintage spec equip to apply/remove luck effect
		if (event.getSlot() == EquipmentSlot.HEAD) {
			if (event.getTo().is(TombsItems.VINTAGE_SPECTACLES.get())) {
				event.getEntity().addEffect(new MobEffectInstance(MobEffects.LUCK, -1, 0, false, false, true));
    		} else if (event.getFrom().is(TombsItems.VINTAGE_SPECTACLES.get())) {
    			event.getEntity().removeEffect(MobEffects.LUCK);
    		}
		}
	}
}
