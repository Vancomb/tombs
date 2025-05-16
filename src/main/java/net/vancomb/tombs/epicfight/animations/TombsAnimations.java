package net.vancomb.tombs.epicfight.animations;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.vancomb.tombs.Tombs;
import net.vancomb.tombs.networking.NetworkManager;
import net.vancomb.tombs.networking.ServerBoundReloadPacket;
import net.vancomb.tombs.registry.TombsParticleTypes;
import net.vancomb.tombs.registry.TombsSounds;
import net.vancomb.tombs.world.capabilities.BulletsCapability;
import yesman.epicfight.api.animation.AnimationManager.AnimationAccessor;
import yesman.epicfight.api.animation.AnimationManager.AnimationBuilder;
import yesman.epicfight.api.animation.AnimationManager.AnimationRegistryEvent;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationEvent.Side;
import yesman.epicfight.api.animation.property.AnimationEvent.SimpleEvent;
import yesman.epicfight.api.animation.property.AnimationProperty.AttackAnimationProperty;
import yesman.epicfight.api.animation.property.AnimationProperty.StaticAnimationProperty;
import yesman.epicfight.api.animation.types.AimAnimation;
import yesman.epicfight.api.animation.types.AirSlashAnimation;
import yesman.epicfight.api.animation.types.BasicAttackAnimation;
import yesman.epicfight.api.animation.types.DashAttackAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.ReboundAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.animation.types.AttackAnimation.Phase;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.Armatures;

@Mod.EventBusSubscriber(modid = Tombs.MODID, bus = Bus.MOD)
public class TombsAnimations {
	public static AnimationAccessor<AimAnimation> BIPED_LEFTHAND_AIM;
	public static AnimationAccessor<AimAnimation> BIPED_RIGHTHAND_AIM;
	public static AnimationAccessor<AimAnimation> BIPED_TWOHAND_AIM;
	public static AnimationAccessor<ReboundAnimation> BIPED_LEFTHAND_SHOT;
	public static AnimationAccessor<ReboundAnimation> BIPED_RIGHTHAND_SHOT;
	public static AnimationAccessor<StaticAnimation> BIPED_RELOAD_LEFTHAND;
	public static AnimationAccessor<StaticAnimation> BIPED_RELOAD_RIGHTHAND;
	public static AnimationAccessor<StaticAnimation> BIPED_RELOAD_TWOHAND;
	public static AnimationAccessor<StaticAnimation> BIPED_SURVIVOR_PICKAXE_DUAL_IDLE;
	
	public static AnimationAccessor<BasicAttackAnimation> BIPED_EXCALIBUR_AUTO1;
	public static AnimationAccessor<BasicAttackAnimation> BIPED_EXCALIBUR_AUTO2;
	public static AnimationAccessor<BasicAttackAnimation> BIPED_EXCALIBUR_AUTO3;
	public static AnimationAccessor<BasicAttackAnimation> BIPED_SURVIVOR_PICKAXE_AUTO1;
	public static AnimationAccessor<BasicAttackAnimation> BIPED_SURVIVOR_PICKAXE_AUTO2;
	public static AnimationAccessor<DashAttackAnimation> BIPED_SURVIVOR_PICKAXE_DASH;
	public static AnimationAccessor<AirSlashAnimation> BIPED_SURVIVOR_PICKAXE_AIRSLASH;
	public static AnimationAccessor<BasicAttackAnimation> BIPED_SURVIVOR_PICKAXE_DUAL_AUTO1;
	public static AnimationAccessor<BasicAttackAnimation> BIPED_SURVIVOR_PICKAXE_DUAL_AUTO2;
	public static AnimationAccessor<BasicAttackAnimation> BIPED_SURVIVOR_PICKAXE_DUAL_AUTO3;
	public static AnimationAccessor<BasicAttackAnimation> BIPED_SURVIVOR_PICKAXE_DUAL_AUTO4;
	
	// Add event subscriber that registers epic fight animations
	@SubscribeEvent
	public static void registerAnimations(AnimationRegistryEvent event) {
		event.newBuilder(Tombs.MODID, TombsAnimations::build);
	}
	
	// Build animations
	public static void build(AnimationBuilder builder) {
		
		// The first parameter is path of animation file under assets/${modid}/animmodels/animations/
		
		// Define aim animation. Receives each registry name of animations when looking up, forward, down, and lying (when swim and flying)
		BIPED_LEFTHAND_AIM = builder.nextAccessor("biped/lefthand_aim", (accessor) -> new AimAnimation(true, accessor, "biped/lefthand_aim_mid", "biped/lefthand_aim_up", "biped/lefthand_aim_down", "biped/lefthand_aim_lying", Armatures.BIPED));
		BIPED_RIGHTHAND_AIM = builder.nextAccessor("biped/righthand_aim", (accessor) -> new AimAnimation(true, accessor, "biped/righthand_aim_mid", "biped/righthand_aim_up", "biped/righthand_aim_down", "biped/righthand_aim_lying", Armatures.BIPED));
		BIPED_TWOHAND_AIM = builder.nextAccessor("biped/twohand_aim", (accessor) -> new AimAnimation(true, accessor, "biped/twohand_aim_mid", "biped/twohand_aim_up", "biped/twohand_aim_down", "biped/twohand_aim_lying", Armatures.BIPED));
		
		BIPED_LEFTHAND_SHOT = builder.nextAccessor("biped/lefthand_shot", (accessor) ->
			new ReboundAnimation(0.05F, false, accessor, "biped/lefthand_shot_mid", "biped/lefthand_shot_up", "biped/lefthand_shot_down", "biped/lefthand_shot_lying", Armatures.BIPED)
				.addEvents(StaticAnimationProperty.ON_BEGIN_EVENTS, SimpleEvent.create(ReusableSources.MUZZLE_FLASH, Side.CLIENT).params(Armatures.BIPED.get().toolL))
		);
		
		BIPED_RIGHTHAND_SHOT = builder.nextAccessor("biped/righthand_shot", (accessor) ->
			new ReboundAnimation(0.05F, false, accessor, "biped/righthand_shot_mid", "biped/righthand_shot_up", "biped/righthand_shot_down", "biped/righthand_shot_lying", Armatures.BIPED)
				.addEvents(StaticAnimationProperty.ON_BEGIN_EVENTS, SimpleEvent.create(ReusableSources.MUZZLE_FLASH, Side.CLIENT).params(Armatures.BIPED.get().toolR))
		);
		
		BIPED_RELOAD_LEFTHAND = builder.nextAccessor("biped/reload_lefthand", (accessor) ->
			new StaticAnimation(false, accessor, Armatures.BIPED)
				.addEvents(StaticAnimationProperty.ON_BEGIN_EVENTS, SimpleEvent.create(Animations.ReusableSources.PLAY_SOUND, Side.CLIENT).params(TombsSounds.PISTOL_RELOAD.get()))
				.addEvents(StaticAnimationProperty.ON_END_EVENTS, SimpleEvent.create(ReusableSources.RELOAD, Side.LOCAL_CLIENT))
				.newTimePair(0.0F, 10.0F)
					.addStateRemoveOld(EntityState.INACTION, true)
					.addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false)
					.addStateRemoveOld(EntityState.CAN_SKILL_EXECUTION, false)
		);
		
		BIPED_RELOAD_RIGHTHAND = builder.nextAccessor("biped/reload_righthand", (accessor) ->
			new StaticAnimation(false, accessor, Armatures.BIPED)
				.addEvents(StaticAnimationProperty.ON_BEGIN_EVENTS, SimpleEvent.create(Animations.ReusableSources.PLAY_SOUND, Side.CLIENT).params(TombsSounds.PISTOL_RELOAD.get()))
				.addEvents(StaticAnimationProperty.ON_END_EVENTS, SimpleEvent.create(ReusableSources.RELOAD, Side.LOCAL_CLIENT))
				.newTimePair(0.0F, 10.0F)
					.addStateRemoveOld(EntityState.INACTION, true)
					.addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false)
					.addStateRemoveOld(EntityState.CAN_SKILL_EXECUTION, false)
		);
		
		BIPED_RELOAD_TWOHAND = builder.nextAccessor("biped/reload_twohand", (accessor) ->
			new StaticAnimation(false, accessor, Armatures.BIPED)
				.addEvents(StaticAnimationProperty.ON_BEGIN_EVENTS, SimpleEvent.create(Animations.ReusableSources.PLAY_SOUND, Side.CLIENT).params(TombsSounds.PISTOL_RELOAD.get()))
				.addEvents(StaticAnimationProperty.ON_END_EVENTS, SimpleEvent.create(ReusableSources.RELOAD, Side.LOCAL_CLIENT))
				.newTimePair(0.0F, 10.0F)
					.addStateRemoveOld(EntityState.INACTION, true)
					.addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false)
					.addStateRemoveOld(EntityState.CAN_SKILL_EXECUTION, false)
		);
		
		BIPED_SURVIVOR_PICKAXE_DUAL_IDLE = builder.nextAccessor("biped/survivor_pickaxe_dual_idle", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED));
		
		BIPED_EXCALIBUR_AUTO1 = builder.nextAccessor("biped/excalibur_auto1", (accessor) ->
			new BasicAttackAnimation(0.15F, 0.25F, 0.35F, 0.5F, null, Armatures.BIPED.get().toolR, accessor, Armatures.BIPED)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.15F)
		);
		
		BIPED_EXCALIBUR_AUTO2 = builder.nextAccessor("biped/excalibur_auto2", (accessor) ->
			new BasicAttackAnimation(0.2F, 0.3F, 0.4F, 0.65F, null, Armatures.BIPED.get().toolR, accessor, Armatures.BIPED)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.15F)
		);
		
		BIPED_EXCALIBUR_AUTO3 = builder.nextAccessor("biped/excalibur_auto3", (accessor) ->
			new BasicAttackAnimation(0.1F, 0.3F, 0.45F, 0.6F, null, Armatures.BIPED.get().toolR, accessor, Armatures.BIPED)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.15F)
		);
		
		BIPED_SURVIVOR_PICKAXE_AUTO1 = builder.nextAccessor("biped/survivor_pickaxe_auto1", (accessor) ->
			new BasicAttackAnimation(0.15F, 0.2F, 0.3F, 0.5F, null, Armatures.BIPED.get().toolR, accessor, Armatures.BIPED)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.4F)
		);
		
		BIPED_SURVIVOR_PICKAXE_AUTO2 = builder.nextAccessor("biped/survivor_pickaxe_auto2", (accessor) ->
			new BasicAttackAnimation(0.2F, 0.2F, 0.3F, 0.5F, null, Armatures.BIPED.get().toolR, accessor, Armatures.BIPED)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.4F)
		);
		
		BIPED_SURVIVOR_PICKAXE_DASH = builder.nextAccessor("biped/survivor_pickaxe_dash", (accessor) ->
			new DashAttackAnimation(0.15F, 0.25F, 0.25F, 0.35F, 0.5F, null, Armatures.BIPED.get().toolR, accessor, Armatures.BIPED, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.4F)
		);
		
		BIPED_SURVIVOR_PICKAXE_AIRSLASH = builder.nextAccessor("biped/survivor_pickaxe_airslash", (accessor) ->
			new AirSlashAnimation(0.15F, 0.4F, 0.45F, 0.75F, null, Armatures.BIPED.get().toolR, accessor, Armatures.BIPED)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.4F)
		);
		
		BIPED_SURVIVOR_PICKAXE_DUAL_AUTO1 = builder.nextAccessor("biped/survivor_pickaxe_dual_auto1", (accessor) ->
			new BasicAttackAnimation(0.15F, accessor, Armatures.BIPED,
					new Phase(0.0F, 0.05F, 0.1F, 0.15F, 0.15F, InteractionHand.OFF_HAND, Armatures.BIPED.get().toolL, null),
					new Phase(0.15F, 0.25F, 0.35F, 0.55F, 0.55F, InteractionHand.OFF_HAND, Armatures.BIPED.get().toolL, null)
				)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.4F)
		);
		
		BIPED_SURVIVOR_PICKAXE_DUAL_AUTO2 = builder.nextAccessor("biped/survivor_pickaxe_dual_auto2", (accessor) ->
			new BasicAttackAnimation(0.05F, 0.25F, 0.3F, 0.5F, null, Armatures.BIPED.get().toolR, accessor, Armatures.BIPED)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.4F)
		);
		
		BIPED_SURVIVOR_PICKAXE_DUAL_AUTO3 = builder.nextAccessor("biped/survivor_pickaxe_dual_auto3", (accessor) ->
			new BasicAttackAnimation(0.15F, accessor, Armatures.BIPED,
					new Phase(0.0F, 0.05F, 0.15F, 0.25F, 0.25F, InteractionHand.MAIN_HAND, Armatures.BIPED.get().toolR, null),
					new Phase(0.25F, 0.25F, 0.35F, 0.55F, 0.55F, InteractionHand.OFF_HAND, Armatures.BIPED.get().toolL, null)
				)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.4F)
		);
		
		BIPED_SURVIVOR_PICKAXE_DUAL_AUTO4 = builder.nextAccessor("biped/survivor_pickaxe_dual_auto4", (accessor) ->
			new BasicAttackAnimation(0.1F, 0.35F, 0.45F, 0.6F, null, Armatures.BIPED.get().toolR, accessor, Armatures.BIPED)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.4F)
		);
	}
	
	// An inner class that gathers animation events together
	public static abstract class ReusableSources {
		
		public static final AnimationEvent.E1<Joint> MUZZLE_FLASH = (entitypatch, animation, params) -> {
			OpenMatrix4f animationBoundMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getAnimator().getPose(1.0F), params.first());
			OpenMatrix4f modelMatrix = MathUtils.getModelMatrixIntegral(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 180.0F + entitypatch.getYRotO(), 180.0F + entitypatch.getYRot(), 1.0F, 0.9375F, 0.9375F, 0.9375F);
			animationBoundMatrix.mulFront(modelMatrix);
			Vec3 particlepos = entitypatch.getOriginal().position().add(OpenMatrix4f.transform(animationBoundMatrix, new Vec3(0.0D, -1.0D, -0.3D)));
			
			entitypatch.getOriginal().level().addParticle(TombsParticleTypes.MUZZLE_FLASH.get(), particlepos.x, particlepos.y, particlepos.z, 0.0D, 0.0D, 0.0D);
		};
		
		public static final AnimationEvent.E0 RELOAD = (entitypatch, animation, params) -> {
			
			// Reload pistols in each hand
			entitypatch.getOriginal().getCapability(BulletsCapability.BULLETS_CAPABILITY).ifPresent(bulletsCapability -> {
				bulletsCapability.bulletReload(entitypatch.getOriginal());
			});
			
			// Create a reload packet with player entity's id
			ServerBoundReloadPacket reloadPacket = new ServerBoundReloadPacket(entitypatch.getOriginal().getId());
			
			// Send packet
			NetworkManager.INSTANCE.sendToServer(reloadPacket);
		};
	}
}
