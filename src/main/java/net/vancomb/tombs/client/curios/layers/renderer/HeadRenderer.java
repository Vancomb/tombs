package net.vancomb.tombs.client.curios.layers.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vancomb.tombs.Tombs;
import net.vancomb.tombs.client.model.TombsModelLayers;
import net.vancomb.tombs.client.model.VintageSpecModel;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer.HumanoidRender;

@OnlyIn(Dist.CLIENT)
public class HeadRenderer implements HumanoidRender {
	private static final ResourceLocation VINTAGE_SPECS_TEXTURE = ResourceLocation.fromNamespaceAndPath(Tombs.MODID, "textures/model/armor/vintage_specs.png");
    private final VintageSpecModel<LivingEntity> vintageSpecsModel = new VintageSpecModel<> (Minecraft.getInstance().getEntityModels().bakeLayer(TombsModelLayers.VINTAGE_SPECS));
    
    public HeadRenderer() {
    	this.vintageSpecsModel.setAllVisible(false);
    	this.vintageSpecsModel.head.visible = true;
    }
    
	@Override
	public HumanoidModel<LivingEntity> getModel(ItemStack arg0, SlotContext arg1) {
		return this.vintageSpecsModel;
	}
	
	@Override
	public ResourceLocation getModelTexture(ItemStack arg0, SlotContext arg1) {
		return VINTAGE_SPECS_TEXTURE;
	}
}
