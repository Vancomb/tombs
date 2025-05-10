package net.vancomb.tombs.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MuzzleFlashParticle extends TextureSheetParticle {
	private float initialQuadSize;
	
	protected MuzzleFlashParticle(ClientLevel pLevel, double pX, double pY, double pZ) {
		super(pLevel, pX, pY, pZ);
		
		this.lifetime = 0;
		this.initialQuadSize = 0.3F * (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F;
	}
	
	@Override
	public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
		super.render(pBuffer, pRenderInfo, pPartialTicks);
		
		// decide the quad size in render tick
		this.quadSize = this.initialQuadSize * (float)Math.sin((this.age + pPartialTicks) * Math.PI);
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;
		
		public Provider(SpriteSet spriteSet) {
	         this.spriteSet = spriteSet;
	    }
	    
		@Override
		public Particle createParticle(SimpleParticleType particleType, ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			MuzzleFlashParticle particle = new MuzzleFlashParticle(clientLevel, x, y, z);
			particle.pickSprite(this.spriteSet);
			return particle;
		}
	}
}
