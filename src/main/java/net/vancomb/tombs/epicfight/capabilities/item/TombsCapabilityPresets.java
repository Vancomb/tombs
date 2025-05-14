package net.vancomb.tombs.epicfight.capabilities.item;

import java.util.function.Function;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
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
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
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
	
	// Capability preset for Survivor Pickaxe
	public static final Function<Item, CapabilityItem.Builder> SURVIVOR_PICKAXE = (item) -> {
		WeaponCapability.Builder builder = WeaponCapability.builder()
			.category(WeaponCategories.PICKAXE)
			.styleProvider((playerpatch) -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == WeaponCategories.PICKAXE ? Styles.TWO_HAND : Styles.ONE_HAND)
			.swingSound(EpicFightSounds.WHOOSH_SMALL.get())
			.hitSound(EpicFightSounds.BLADE_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLADE.get())
			.collider(ColliderPreset.DAGGER)
			.weaponCombinationPredicator((entitypatch) -> EpicFightCapabilities.getItemStackCapability(entitypatch.getOriginal().getOffhandItem()).getWeaponCategory() == WeaponCategories.PICKAXE)
			.newStyleCombo(Styles.ONE_HAND, TombsAnimations.BIPED_SURVIVOR_PICKAXE_AUTO1, TombsAnimations.BIPED_SURVIVOR_PICKAXE_AUTO2, Animations.DAGGER_DASH, TombsAnimations.BIPED_SURVIVOR_PICKAXE_AIRSLASH)
			.newStyleCombo(Styles.TWO_HAND, TombsAnimations.BIPED_SURVIVOR_PICKAXE_DUAL_AUTO1, TombsAnimations.BIPED_SURVIVOR_PICKAXE_DUAL_AUTO2, TombsAnimations.BIPED_SURVIVOR_PICKAXE_DUAL_AUTO3, TombsAnimations.BIPED_SURVIVOR_PICKAXE_DUAL_AUTO4, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)
			.innateSkill(Styles.ONE_HAND, (itemstack) -> EpicFightSkills.EVISCERATE)
			.innateSkill(Styles.TWO_HAND, (itemstack) -> EpicFightSkills.BLADE_RUSH)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, TombsAnimations.BIPED_SURVIVOR_PICKAXE_DUAL_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, TombsAnimations.BIPED_SURVIVOR_PICKAXE_DUAL_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, TombsAnimations.BIPED_SURVIVOR_PICKAXE_DUAL_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, TombsAnimations.BIPED_SURVIVOR_PICKAXE_DUAL_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_DUAL)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, TombsAnimations.BIPED_SURVIVOR_PICKAXE_DUAL_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, TombsAnimations.BIPED_SURVIVOR_PICKAXE_DUAL_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLOAT, TombsAnimations.BIPED_SURVIVOR_PICKAXE_DUAL_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FALL, TombsAnimations.BIPED_SURVIVOR_PICKAXE_DUAL_IDLE);
		
		return builder;
	};
	
	/**
	 * Register capability presets. Used by weapon capability datapack under /data/tombs/capabilities/weapons
	 */
	@SubscribeEvent
	public static void tombs$register(WeaponCapabilityPresetRegistryEvent event) {
		event.getTypeEntry().put(ResourceLocation.fromNamespaceAndPath(Tombs.MODID, "hk_usp9_pistol"), HK_USP9_PISTOL);
		event.getTypeEntry().put(ResourceLocation.fromNamespaceAndPath(Tombs.MODID, "excalibur"), EXCALIBUR);
		event.getTypeEntry().put(ResourceLocation.fromNamespaceAndPath(Tombs.MODID, "survivor_pickaxe"), SURVIVOR_PICKAXE);
	}
}
