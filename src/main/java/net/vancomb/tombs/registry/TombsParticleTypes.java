package net.vancomb.tombs.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vancomb.tombs.Tombs;

public abstract class TombsParticleTypes {
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Tombs.MODID);
	
	public static final RegistryObject<SimpleParticleType> MUZZLE_FLASH = PARTICLE_TYPES.register("muzzle_flash", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> EXCALIBUR_PROJECTILE = PARTICLE_TYPES.register("excalibur_projectile", () -> new SimpleParticleType(true));
}
