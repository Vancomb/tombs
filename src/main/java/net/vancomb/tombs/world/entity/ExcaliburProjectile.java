package net.vancomb.tombs.world.entity;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.vancomb.tombs.registry.TombsEntityTypes;
import net.vancomb.tombs.registry.TombsParticleTypes;

public class ExcaliburProjectile extends Projectile {
	private static final int MAX_LIFETIME = 10;
	
	private final Set<Entity> victims = Sets.newHashSet();
	private float damage;
	private int lifetime;
	
	public ExcaliburProjectile(EntityType<ExcaliburProjectile> entityType, Level level) {
		super(entityType, level);
	}
	
	public ExcaliburProjectile(Level level, LivingEntity shooter, Vec3 shootingDirection) {
		super(TombsEntityTypes.EXCALIBUR_PROJECTILE.get(), level);
		
		this.setOwner(shooter);
		this.setDeltaMovement(shootingDirection.scale(2.0D));
		
		// Calculate the damage of shooter
		if (this.getOwner() instanceof LivingEntity livingentity) {
			this.damage = (float)livingentity.getAttributeValue(Attributes.ATTACK_DAMAGE);
		} else {
			this.damage = 1.0F;
		}
	}
	
	@Override
	protected void defineSynchedData() {
	}
	
	@Override
	public void tick() {
		this.noPhysics = true;
		
		super.tick();
		
		// Get the delta movement
		Vec3 vec3 = this.getDeltaMovement();
		double nextX = this.getX() + vec3.x;
		double nextY = this.getY() + vec3.y;
		double nextZ = this.getZ() + vec3.z;
		
		if (!this.level().isClientSide()) {
			// Get colliding entities
			List<LivingEntity> list1 = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().expandTowards(vec3));
			
			// Hurt colliding entities
			if (!list1.isEmpty()) {
				for (LivingEntity livingentity : list1) {
					
					// Exclude already hit entities and shooter
					if (!this.victims.contains(livingentity) && !this.getOwner().equals(livingentity)) {
						// Hurt the entity with 
						livingentity.hurt(livingentity.level().damageSources().indirectMagic(livingentity, this), this.damage);
						this.victims.add(livingentity);
					}
				}
			}
			
			Vec3 start = this.position();
			Vec3 end = start.add(this.getDeltaMovement());
			HitResult hitresult = this.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
			
			// Remove entity when hits a block
			if (hitresult.getType() == HitResult.Type.BLOCK) {
				this.discard();
			}
			
			// Remove entity when exceeeds max lifetime
			if (this.lifetime++ > MAX_LIFETIME) {
				this.discard();
			}
			
			this.setPos(nextX, nextY, nextZ);
		} else {
			// Spawn particles in client
			this.level().addParticle(TombsParticleTypes.EXCALIBUR_PROJECTILE.get(), this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			this.setPosRaw(nextX, nextY, nextZ);
		}
	}
}
