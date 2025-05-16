package net.vancomb.tombs;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vancomb.tombs.client.curios.layers.renderer.HeadRenderer;
import net.vancomb.tombs.networking.NetworkManager;
import net.vancomb.tombs.registry.TombsCreativeTabs;
import net.vancomb.tombs.registry.TombsEntityTypes;
import net.vancomb.tombs.registry.TombsItems;
import net.vancomb.tombs.registry.TombsPaintingVariants;
import net.vancomb.tombs.registry.TombsParticleTypes;
import net.vancomb.tombs.registry.TombsSounds;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod(Tombs.MODID)
public class Tombs {
    public static final String MODID = "tombs";
    
	public Tombs(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        
        // Register the Deferred Register to the mod event bus so items get registered
        TombsItems.ITEMS.register(modEventBus);
        
        // Register the Deferred Register to the mod event bus so creative tabs get registered
        TombsCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        
        // Register the Deferred Register to the mod event bus so sounds get registered
        TombsSounds.SOUND_EVENTS.register(modEventBus);
        
        // Register the Deferred Register to the mod event bus so particles get registered
        TombsParticleTypes.PARTICLE_TYPES.register(modEventBus);
        
        // Register the Deferred Register to the mod event bus so painting variants get registered
        TombsPaintingVariants.PAINTING_VARIANTS.register(modEventBus);
        
        // Register the Deferred Register to the mod event bus so entity types get registered
        TombsEntityTypes.ENTITIES.register(modEventBus);
        
        modEventBus.addListener(this::doCommonStuff);
        
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
	
	private void doCommonStuff(final FMLCommonSetupEvent event) {
		// Register packets for client-server communication
		event.enqueueWork(NetworkManager::registerPackets);
	}
	
	@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			CuriosRendererRegistry.register(TombsItems.VINTAGE_SPECTACLES.get(), HeadRenderer::new);
		}
    }
}
