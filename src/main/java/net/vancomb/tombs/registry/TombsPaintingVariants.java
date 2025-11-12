package net.vancomb.tombs.registry;

import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vancomb.tombs.Tombs;

public class TombsPaintingVariants {
	// Register painting variants
	public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS = DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, Tombs.MODID);

	public static final RegistryObject<PaintingVariant> AVALON = PAINTING_VARIANTS.register("avalon", () -> new PaintingVariant(32, 48));

}
