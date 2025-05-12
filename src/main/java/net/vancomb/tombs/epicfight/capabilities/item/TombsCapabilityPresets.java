package net.vancomb.tombs.epicfight.capabilities.item;

import java.util.function.Function;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.vancomb.tombs.Tombs;
import net.vancomb.tombs.epicfight.animations.TombsAnimations;
import net.vancomb.tombs.epicfight.capabilities.item.common.TombsWeaponCategory;
import net.vancomb.tombs.registry.TombsSounds;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.CapabilityItem.Styles;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;
import yesman.epicfight.world.capabilities.item.CapabilityItem.ZoomInType;
import yesman.epicfight.world.capabilities.item.RangedWeaponCapability;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

/**
 * Register capability presets for Epic Fight melee & ranged weapons
 */
@Mod.EventBusSubscriber(modid = Tombs.MODID , bus = EventBusSubscriber.Bus.MOD)
public class TombsCapabilityPresets {
	
	// Capability preset for HnK USP9 Pistol
	public static final Function<Item, CapabilityItem.Builder> HK_USP9_PISTOL = (item) -> {
		CapabilityItem.Builder builder = 
			RangedWeaponCapability.builder()
				.zoomInType(ZoomInType.ALWAYS)
				.category(TombsWeaponCategory.USP9)
				.constructor(HKUSP9Capability::new)
		;
		
		return builder;
	};
	
	// Capability preset for Excalibur
	public static final Function<Item, CapabilityItem.Builder> EXCALIBUR = (item) -> {
		CapabilityItem.Builder builder = WeaponCapability.builder()
			.category(WeaponCategories.GREATSWORD)
			.styleProvider((playerpatch) -> Styles.TWO_HAND)
			.collider(ColliderPreset.GREATSWORD)
			.swingSound(TombsSounds.EXCALIBUR_SWING.get())
			.hitSound(EpicFightSounds.BLADE_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLADE.get())
			.canBePlacedOffhand(false)
			.newStyleCombo(Styles.TWO_HAND, TombsAnimations.BIPED_EXCALIBUR_AUTO1, TombsAnimations.BIPED_EXCALIBUR_AUTO2, TombsAnimations.BIPED_EXCALIBUR_AUTO3, Animations.GREATSWORD_DASH, Animations.GREATSWORD_AIR_SLASH)
			.innateSkill(Styles.TWO_HAND, (itemstack) -> EpicFightSkills.STEEL_WHIRLWIND)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_WALK_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_WALK_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_GREATSWORD)
	    	.livingMotionModifier(Styles.TWO_HAND, LivingMotions.JUMP, Animations.BIPED_HOLD_GREATSWORD)
	    	.livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_GREATSWORD)
	    	.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_GREATSWORD)
	    	.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_GREATSWORD)
	    	.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLY, Animations.BIPED_HOLD_GREATSWORD)
	    	.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CREATIVE_FLY, Animations.BIPED_HOLD_GREATSWORD)
	    	.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CREATIVE_IDLE, Animations.BIPED_HOLD_GREATSWORD)
	    	.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.GREATSWORD_GUARD)
	    	.constructor(ExcaliburCapability::new);
		
		return builder;
	};
	
	@SubscribeEvent
	public static void tombs$register(WeaponCapabilityPresetRegistryEvent event) {
		event.getTypeEntry().put(ResourceLocation.fromNamespaceAndPath(Tombs.MODID, "hk_usp9_pistol"), HK_USP9_PISTOL);
		event.getTypeEntry().put(ResourceLocation.fromNamespaceAndPath(Tombs.MODID, "excalibur"), EXCALIBUR);
	}
}
