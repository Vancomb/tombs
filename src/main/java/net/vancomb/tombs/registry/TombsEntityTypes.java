package net.vancomb.tombs.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vancomb.tombs.Tombs;
import net.vancomb.tombs.world.entity.ExcaliburProjectile;

public abstract class TombsEntityTypes {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Tombs.MODID);
	
	public static final RegistryObject<EntityType<ExcaliburProjectile>> EXCALIBUR_PROJECTILE = ENTITIES.register("excalibur_projectile", () ->
		EntityType.Builder.<ExcaliburProjectile>of(ExcaliburProjectile::new, MobCategory.MISC)
			.fireImmune()
			.sized(0.9F, 0.9F)
			.clientTrackingRange(6)
			.updateInterval(1)
			.noSummon()
			.noSave()
		.build("excalibur_projectile")
	);
}
