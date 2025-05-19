package net.vancomb.tombs.curios;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.vancomb.tombs.client.curios.layers.renderer.HeadRenderer;
import net.vancomb.tombs.registry.TombsItems;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

public class CuriosCompat {
	
	public static void forgeEventBus(IEventBus forgeEventBus) {
		forgeEventBus.<CurioChangeEvent>addListener((event) -> {
    		if ("head".equals(event.getIdentifier())) {
    			if (event.getTo().is(TombsItems.VINTAGE_SPECTACLES.get())) {
    				event.getEntity().addEffect(new MobEffectInstance(MobEffects.LUCK, -1, 0, false, false, true));
        		} else if (event.getFrom().is(TombsItems.VINTAGE_SPECTACLES.get())) {
        			event.getEntity().removeEffect(MobEffects.LUCK);
        		}
    		}
    	});
	}
	
	public static void modEventBus(IEventBus modEventBus) {
		modEventBus.<FMLClientSetupEvent>addListener(event -> {
			CuriosRendererRegistry.register(TombsItems.VINTAGE_SPECTACLES.get(), HeadRenderer::new);
		});
	}
}
