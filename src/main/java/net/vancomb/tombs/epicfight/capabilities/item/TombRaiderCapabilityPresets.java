package net.vancomb.tombs.epicfight.capabilities.item;

import java.util.function.Function;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.vancomb.tombs.Tombs;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.CapabilityItem.ZoomInType;
import yesman.epicfight.world.capabilities.item.RangedWeaponCapability;

/**
 * Register capability presets for Epic Fight melee & ranged weapons
 */
@Mod.EventBusSubscriber(modid = Tombs.MODID , bus = EventBusSubscriber.Bus.MOD)
public class TombRaiderCapabilityPresets {
	
	// Capability preset for HK_USP9_PISTOL
	public static final Function<Item, CapabilityItem.Builder> HK_USP9_PISTOL = (item) -> {
		CapabilityItem.Builder builder = 
			RangedWeaponCapability.builder()
				.zoomInType(ZoomInType.ALWAYS)
				.constructor(HKUSP9Capability::new)
		;
		
		return builder;
	};
	
	@SubscribeEvent
	public static void register(WeaponCapabilityPresetRegistryEvent event) {
		event.getTypeEntry().put(ResourceLocation.fromNamespaceAndPath(Tombs.MODID, "hk_usp9_pistol"), HK_USP9_PISTOL);
	}
}
