package net.vancomb.tombs.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vancomb.tombs.Tombs;

public abstract class TombsCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Tombs.MODID);
    
    public static final RegistryObject<CreativeModeTab> TOMB_RADIER_TAB = CREATIVE_MODE_TABS.register("tombraider_tab", () ->
    	CreativeModeTab.builder()
    		// A creative tab order
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            // A title of creative tab when hovered
            .title(Component.translatable("creative_tab.tombraider"))
            // An item shown on the creative tab
            .icon(() -> TombsItems.HK_USP9_PISTOL.get().getDefaultInstance())
            // A list of items that is displayed on the tab
            .displayItems((parameters, output) -> {
                output.accept(TombsItems.HK_USP9_PISTOL.get());
                output.accept(TombsItems.SURVIVOR_PICKAXE.get());
                output.accept(TombsItems.EXCALIBUR.get());
                output.accept(TombsItems.VINTAGE_SPECTACLES.get());
            })
            .build()
    );
}
