package fabricish.client.model;

import fabricish.client.render.entity.animation.TaterAnimations;
import fabricish.client.render.entity.state.TaterRenderState;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class TaterKnifeModel extends EntityModel<TaterRenderState> {
    private final KeyframeAnimation usingKnifeAnimation;

    public TaterKnifeModel(ModelPart root) {
        super(root);
        this.usingKnifeAnimation = TaterAnimations.USING_KNIFE.bake(root);
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition root = modelData.getRoot();

        PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition edge = main.addOrReplaceChild("edge", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -0.5F, -14.0F, 4.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
            .texOffs(0, 12).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 1.5708F, 0.0F));

        return LayerDefinition.create(modelData, 32, 32);
    }

    @Override
    public void setupAnim(TaterRenderState state) {
        super.setupAnim(state);

        if (state.usingKnife.isStarted()) {
            this.usingKnifeAnimation.apply(state.usingKnife, state.ageInTicks);
        }
    }
}
