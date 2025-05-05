package com.yesman.tombraider;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.yesman.tombraider.registry.TombRaiderCreativeTabs;
import com.yesman.tombraider.registry.TombRaiderItems;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TombRaider.MODID)
public class TombRaider {
    public static final String MODID = "tombraider";
    private static final Logger LOGGER = LogUtils.getLogger();
    
	public TombRaider(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        
        // Register the Deferred Register to the mod event bus so items get registered
        TombRaiderItems.ITEMS.register(modEventBus);
        
        // Register the Deferred Register to the mod event bus so creative tabs get registered
        TombRaiderCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
	
    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModEvents {
        @SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
