package net.vancomb.tombs.client.registry;

import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.vancomb.tombs.Tombs;
import net.vancomb.tombs.client.particle.ExcaliburParticle;
import net.vancomb.tombs.client.particle.MuzzleFlashParticle;
import net.vancomb.tombs.registry.TombsEntityTypes;
import net.vancomb.tombs.registry.TombsParticleTypes;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Tombs.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public abstract class TombsClientRegistryEvents {
	
	// Register particle providers
	@SubscribeEvent
	public static void tombs$registerParticleProvidersEvent(final RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(TombsParticleTypes.MUZZLE_FLASH.get(), MuzzleFlashParticle.Provider::new);
		event.registerSpriteSet(TombsParticleTypes.EXCALIBUR_PROJECTILE.get(), ExcaliburParticle.Provider::new);
	}
	
	// Register entity renderers
	@SubscribeEvent
	public static void tombs$registerRenderersEvent(final RegisterRenderers event) {
		event.registerEntityRenderer(TombsEntityTypes.EXCALIBUR_PROJECTILE.get(), NoopRenderer::new);
	}
}
