package net.vancomb.tombs.client.model;

import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VintageSpecModel<T extends LivingEntity> extends HumanoidArmorModel<T> {
	public final ModelPart glasses;
	
	public VintageSpecModel(ModelPart root) {
		super(root);
		
		// disable drawing head, hat model parts-epic fight detects these change so that it skips transforming into epic fight model
		this.head.skipDraw = true;
		this.hat.skipDraw = true;
		
		this.glasses = this.head.getChild("bone");
	}
	
	public static MeshDefinition createMesh() {
		MeshDefinition meshdefinition = HumanoidArmorModel.createBodyLayer(CubeDeformation.NONE);
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition headpartdefinition = partdefinition.getChild("head");
		
		PartDefinition bone = headpartdefinition.addOrReplaceChild("bone",
			CubeListBuilder.create()
				.texOffs(0, 8).addBox(2.9142F, -4.0F, -4.6718F, 1.25F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 6).addBox(0.5F, -4.8F, -4.6818F, 3.25F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 6).addBox(-4.15F, -4.8F, -4.6818F, 3.25F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(4.1642F, -4.0F, -4.6718F, 0.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(2, 8).addBox(-0.5F, -4.5F, -4.6718F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 8).mirror().addBox(-4.1642F, -4.0F, -4.6718F, 1.25F, 1.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-4.1642F, -4.0F, -4.6718F, 0.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false),
			PartPose.offset(0.0F, 0.0F, 0.0F)
		);
		
		bone.addOrReplaceChild("glasses_body_center_1",
			CubeListBuilder.create()
				.texOffs(6, 6).mirror().addBox(-4.1642F, -2.9355F, -2.9713F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(6, 6).addBox(4.1642F, -2.9355F, -2.9713F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
			PartPose.offsetAndRotation(0.0F, 0.0303F, 0.0F, -0.7854F, 0.0F, 0.0F)
		);
		
		bone.addOrReplaceChild("glasses_body_center_2",
			CubeListBuilder.create()
				.texOffs(2, 8).mirror().addBox(-1.4631F, -4.3388F, -4.6718F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false),
			PartPose.offsetAndRotation(0.0F, 0.0303F, 0.0F, 0.0F, 0.0F, 0.3927F)
		);
		
		bone.addOrReplaceChild("glasses_body_center_3",
			CubeListBuilder.create()
				.texOffs(2, 8).addBox(0.4631F, -4.3388F, -4.6718F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
			PartPose.offsetAndRotation(0.0F, 0.0303F, 0.0F, 0.0F, 0.0F, -0.3927F)
		);

		return meshdefinition;
	}
}
