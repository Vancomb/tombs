package net.vancomb.tombs.world.item;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vancomb.tombs.Tombs;
import net.vancomb.tombs.client.model.TombsModelLayers;
import net.vancomb.tombs.client.model.VintageSpecModel;
import net.vancomb.tombs.registry.TombsItems;
import net.vancomb.tombs.world.item.shared.TombsArmorMaterials;

public class VintageSpecsItem extends ArmorItem {
    public VintageSpecsItem(Properties pProperties) {
        super(TombsArmorMaterials.VINTAGE_SPECS, ArmorItem.Type.HELMET, pProperties);
    }
    
    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {
    	// Assign a custom client model extensions of the item
    	consumer.accept(new IClientItemExtensions() {
    		/**
    		 * Return the vintage spec model which will override the vanilla armor model
    		 */
    		@NotNull
    	    public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
    			// Initialize the model later to wait Minecraft loads all vanilla models
    			if (ModelHolder.vintageSpecsModel == null) {
    				ModelHolder.vintageSpecsModel = new VintageSpecModel<> (Minecraft.getInstance().getEntityModels().bakeLayer(TombsModelLayers.VINTAGE_SPECS));
    			}
    			
    	        return ModelHolder.vintageSpecsModel;
    	    }
    	});
    }
    
    // Return armor texture file location
    @Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return Tombs.MODID + ":textures/model/armor/vintage_specs.png";
	}
    
    @Mod.EventBusSubscriber(modid = Tombs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class EquipEventHandler {
    	
    	// Subscribe equip change event to apply luck mob effect
    	@SubscribeEvent
    	public static void tombraider$livingEquipChangeEvent(LivingEquipmentChangeEvent event) {
    		if (event.getSlot() == EquipmentSlot.HEAD) {
    			if (event.getTo().is(TombsItems.VINTAGE_SPECTACLES.get())) {
    				event.getEntity().addEffect(new MobEffectInstance(MobEffects.LUCK, -1, 0, false, false, true));
        		} else if (event.getFrom().is(TombsItems.VINTAGE_SPECTACLES.get())) {
        			event.getEntity().removeEffect(MobEffects.LUCK);
        		}
    		}
    	}
    }
    
    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(modid = Tombs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModelHolder {
    	// Define vintage spec model
		private static HumanoidModel<?> vintageSpecsModel;
    	
		// When resourace reloaded remove the old model instance
    	@SubscribeEvent
    	public static void tombraider$addLayersEvent(EntityRenderersEvent.AddLayers event) {
    		vintageSpecsModel = null;
    	}
    }
}