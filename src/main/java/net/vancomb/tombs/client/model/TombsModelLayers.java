package net.vancomb.tombs.client.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vancomb.tombs.Tombs;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Tombs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public abstract class TombsModelLayers {
	public static final ModelLayerLocation VINTAGE_SPECS = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Tombs.MODID, "vintage_specs"), "main");
	
	@SubscribeEvent
	public static void tombraider$registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(VINTAGE_SPECS, () -> LayerDefinition.create(VintageSpecModel.createMesh(), 16, 16));
	}
}
