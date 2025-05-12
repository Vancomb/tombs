package net.vancomb.tombs.world.item;

import java.util.function.Predicate;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.vancomb.tombs.Tombs;
import net.vancomb.tombs.networking.ClientBoundBulletPacket;
import net.vancomb.tombs.networking.NetworkManager;
import net.vancomb.tombs.registry.TombsItems;
import net.vancomb.tombs.world.capabilities.BulletsCapability;

public class HKUSP9PistolItem extends ProjectileWeaponItem {
	public static final Predicate<ItemStack> NO_ITMES = (p_43017_) -> {
		return false;
	};
	
	public HKUSP9PistolItem(Properties pProperties) {
		super(pProperties);
	}
	
	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return NO_ITMES;
	}
	
	@Override
	public int getDefaultProjectileRange() {
		return 25;
	}
	
	@Mod.EventBusSubscriber(modid = Tombs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class EquipEventHandler {
    	
    	// Subscribe equip change event to correct max bullets
    	@SubscribeEvent
    	public static void tombraider$livingEquipChangeEvent(LivingEquipmentChangeEvent event) {
    		if (event.getEntity() instanceof ServerPlayer serverPlayer && (event.getFrom().is(TombsItems.HK_USP9_PISTOL.get()) || event.getTo().is(TombsItems.HK_USP9_PISTOL.get()))) {
    			BulletsCapability.getBullets(serverPlayer).ifPresent(bullets -> {
    				int currentBullets = bullets.getRemainBullets();
    				int pistols = 0;
    				
    				if (serverPlayer.getMainHandItem().is(TombsItems.HK_USP9_PISTOL.get())) pistols++;
    				if (serverPlayer.getOffhandItem().is(TombsItems.HK_USP9_PISTOL.get())) pistols++;
    				
    				int maxBullets = BulletsCapability.getMaxBullets() * pistols;
    				
    				if (maxBullets < currentBullets) {
    					// Correct the current bullets to max bullets
    					bullets.setRemainBullet(maxBullets);
    					
    					// Synchronize remain bullets from server to client
    					NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ClientBoundBulletPacket(bullets.getRemainBullets()));
    				}
    			});
    		}
    	}
    }
}