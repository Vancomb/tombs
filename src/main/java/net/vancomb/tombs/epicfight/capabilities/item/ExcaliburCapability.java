package net.vancomb.tombs.epicfight.capabilities.item;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.vancomb.tombs.world.entity.ExcaliburProjectile;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.DashAttackAnimation;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

public class ExcaliburCapability extends WeaponCapability {
	protected ExcaliburCapability(CapabilityItem.Builder builder) {
		super(builder);
	}
	
	// Create projectiles
	@Override
	public void onStrike(LivingEntityPatch<?> entitypatch, AttackAnimation animation) {
		if (!animation.isBasicAttackAnimation() || animation instanceof DashAttackAnimation) {
			return;
		}
		
		LivingEntity shooter = entitypatch.getOriginal();
		float spreadDegree = 3.0F;
		
		// Create excalibur projectile entities
		ExcaliburProjectile excaliburprojectile1 = new ExcaliburProjectile(shooter.level(), shooter, shooter.calculateViewVector(shooter.getXRot(), shooter.getYRot() - spreadDegree));
		ExcaliburProjectile excaliburprojectile2 = new ExcaliburProjectile(shooter.level(), shooter, shooter.calculateViewVector(shooter.getXRot(), shooter.getYRot()));
		ExcaliburProjectile excaliburprojectile3 = new ExcaliburProjectile(shooter.level(), shooter, shooter.calculateViewVector(shooter.getXRot(), shooter.getYRot() + spreadDegree));
		
		// Set the position to shooter's middle height position
		Vec3 spawnPosition = shooter.position().add(0.0D, shooter.getBbHeight() * 0.5F + 0.2F, 0.0D);
		float xRotClamped = Mth.clamp(shooter.getXRot(), -30.0F, 30.0F);
		
		excaliburprojectile1.setPos(spawnPosition.add(shooter.calculateViewVector(xRotClamped, shooter.getYRot() - spreadDegree).scale(4.0F)));
		excaliburprojectile2.setPos(spawnPosition.add(shooter.calculateViewVector(xRotClamped, shooter.getYRot()).scale(4.0F)));
		excaliburprojectile3.setPos(spawnPosition.add(shooter.calculateViewVector(xRotClamped, shooter.getYRot() + spreadDegree).scale(4.0F)));
		
		// Add entity to world
		shooter.level().addFreshEntity(excaliburprojectile1);
		shooter.level().addFreshEntity(excaliburprojectile2);
		shooter.level().addFreshEntity(excaliburprojectile3);
	}
}
