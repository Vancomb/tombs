package net.vancomb.tombs.epicfight.capabilities.item;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.world.InteractionHand;
import net.vancomb.tombs.epicfight.animations.TombsAnimations;
import net.vancomb.tombs.registry.TombsItems;
import yesman.epicfight.api.animation.AnimationManager.AnimationAccessor;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.RangedWeaponCapability;
import yesman.epicfight.world.capabilities.item.Style;

public class HKUSP9Capability extends RangedWeaponCapability {
	protected Map<LivingMotion, AnimationAccessor<? extends StaticAnimation>> twohandedRangeAnimationModifiers = Maps.newHashMap();
	protected Map<LivingMotion, AnimationAccessor<? extends StaticAnimation>> offhandRangeAnimationModifiers = Maps.newHashMap();
	
	protected HKUSP9Capability(CapabilityItem.Builder builder) {
		super(builder);
		
		// Bind living animation modifiers
		this.rangeAnimationModifiers.put(LivingMotions.AIM, TombsAnimations.BIPED_RIGHTHAND_AIM);
		this.offhandRangeAnimationModifiers.put(LivingMotions.AIM, TombsAnimations.BIPED_LEFTHAND_AIM);
		this.twohandedRangeAnimationModifiers.put(LivingMotions.AIM, TombsAnimations.BIPED_TWOHAND_AIM);
	}
	
	@Override
	public Map<LivingMotion, AnimationAccessor<? extends StaticAnimation>> getLivingMotionModifier(LivingEntityPatch<?> playerpatch, InteractionHand hand) {
		Style style = this.getStyle(playerpatch);
		
		if (style == Styles.TWO_HAND) {
			return this.twohandedRangeAnimationModifiers;
		} else if (hand == InteractionHand.MAIN_HAND) {
			return super.getLivingMotionModifier(playerpatch, hand);
		} else {
			return this.offhandRangeAnimationModifiers;
		}
	}
	
	@Override
	public boolean canBePlacedOffhand() {
		return true;
	}
	
	@Override
	public boolean checkOffhandValid(LivingEntityPatch<?> entitypatch) {
		if (entitypatch.getOriginal().getOffhandItem().is(TombsItems.HK_USP9_PISTOL.get())) {
			return true;
		}
		
		return super.checkOffhandValid(entitypatch);
	}
	
	/**
	 * Determines a style of weapon
	 * 
	 * When equipped with the same HK UPS9 Pistol: TWO_HAND
	 * When equipped with another item: ONE_HAND
	 */
	@Override
	public Style getStyle(LivingEntityPatch<?> entitypatch) {
		return entitypatch.getOriginal().getMainHandItem().is(TombsItems.HK_USP9_PISTOL.get()) &&
				entitypatch.getOriginal().getOffhandItem().is(TombsItems.HK_USP9_PISTOL.get())
				? Styles.TWO_HAND : Styles.ONE_HAND;
	}
	
	/**
	 * Set always aiming when holding this item
	 */
	@Override
	public LivingMotion getLivingMotion(LivingEntityPatch<?> entitypatch, InteractionHand hand) {
		// When pistol is offhand and mainhand item is incomaptible (e.g. two handed weapons)
		if (hand == InteractionHand.OFF_HAND && !entitypatch.isOffhandItemValid()) {
			return null;
		}
		
		return entitypatch.getEntityState().inaction() ? null : LivingMotions.AIM;
	}
}
