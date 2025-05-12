package net.vancomb.tombs.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ExcaliburParticle extends TextureSheetParticle {
	
	private static final ParticleRenderType EXCALIBUR_PARTICLE_TYPE = new ParticleRenderType() {
		@Override
		@SuppressWarnings("deprecation")
		public void begin(BufferBuilder p_107455_, TextureManager p_107456_) {
			RenderSystem.depthMask(false);
			RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
			RenderSystem.enableBlend();
			Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
			
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			p_107455_.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
		}
		
		@Override
		public void end(Tesselator p_107458_) {
			p_107458_.end();
		}
		
		@Override
		public String toString() {
			return "PARTICLE_SHEET_TRANSPARENT";
		}
	};
	
	private final SpriteSet sprites;
	
	protected ExcaliburParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet sprites) {
		super(pLevel, pX, pY, pZ);
		
		this.sprites = sprites;
		this.lifetime = 6;
		this.quadSize = 0.9F;
		this.setSpriteFromAge(sprites);
	}
	
	@Override
	public void tick() {
		super.tick();
		this.setSpriteFromAge(this.sprites);
	}
	
	@Override
	public void setSpriteFromAge(SpriteSet pSprite) {
		if (!this.removed) {
			if (this.age < 2) {
				this.setSprite(pSprite.get(0, this.lifetime));
			} else {
				this.setSprite(pSprite.get(this.age, this.lifetime));
			}
		}
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return EXCALIBUR_PARTICLE_TYPE;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;
		
		public Provider(SpriteSet spriteSet) {
	         this.spriteSet = spriteSet;
	    }
	    
		@Override
		public Particle createParticle(SimpleParticleType particleType, ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new ExcaliburParticle(clientLevel, x, y, z, this.spriteSet);
		}
	}
}
