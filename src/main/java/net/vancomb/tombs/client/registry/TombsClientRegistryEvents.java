package net.vancomb.tombs.client.registry;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.vancomb.tombs.Tombs;
import net.vancomb.tombs.client.particle.MuzzleFlashParticle;
import net.vancomb.tombs.registry.TombsParticleTypes;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Tombs.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public abstract class TombsClientRegistryEvents {
	@SubscribeEvent
	public static void tombraider$registerParticleProvidersEvent(final RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(TombsParticleTypes.MUZZLE_FLASH.get(), MuzzleFlashParticle.Provider::new);
	}
}
