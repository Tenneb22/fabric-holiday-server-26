package fabricish.client.render.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fabricish.client.model.FabricIshModelLayers;
import fabricish.client.model.TaterKnifeModel;
import fabricish.client.model.TaterModel;
import fabricish.client.render.entity.TaterRenderer;
import fabricish.client.render.entity.state.TaterRenderState;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Quaternionf;

public class TaterKnifeLayer extends RenderLayer<TaterRenderState, TaterModel> {
    private final Quaternionf tmpQuaternion = new Quaternionf();
    private final TaterKnifeModel model;

    public TaterKnifeLayer(RenderLayerParent<TaterRenderState, TaterModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new TaterKnifeModel(modelSet.bakeLayer(FabricIshModelLayers.TATER_KNIFE));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, TaterRenderState state, float yRot, float xRot) {
        if (!state.isAngry) return;

        poseStack.pushPose();
        poseStack.translate(-0.4, 1.25, 0);

        int overlayCoords = LivingEntityRenderer.getOverlayCoords(state, 0.0F); // ?
        submitNodeCollector.submitModel(
            this.model, state, poseStack, RenderTypes.entityCutout(TaterRenderer.TATER_KNIFE), lightCoords, overlayCoords, state.outlineColor
        );

        poseStack.popPose();
    }
}
