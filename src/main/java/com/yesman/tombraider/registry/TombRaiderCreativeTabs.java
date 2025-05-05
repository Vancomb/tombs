package com.yesman.tombraider.registry;

import com.yesman.tombraider.TombRaider;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TombRaiderCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TombRaider.MODID);
    
    public static final RegistryObject<CreativeModeTab> TOMB_RADIER_TAB = CREATIVE_MODE_TABS.register("example_tab", () ->
    	CreativeModeTab.builder()
    		// A creative tab order
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            // An item shown on the creative tab
            .icon(() -> TombRaiderItems.HK_USP_PISTOL.get().getDefaultInstance())
            // A list of items that is displayed on the tab
            .displayItems((parameters, output) -> {
                output.accept(TombRaiderItems.HK_USP_PISTOL.get());
            })
            .build()
    );
}
