package fabricish.client.render.entity;

import fabricish.FabricIshMod;
import fabricish.client.model.FabricIshModelLayers;
import fabricish.client.model.TaterModel;
import fabricish.client.render.entity.layer.TaterKnifeLayer;
import fabricish.client.render.entity.state.TaterRenderState;
import fabricish.entity.Tater;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class TaterRenderer extends MobRenderer<Tater, TaterRenderState, TaterModel> {
    private static final Identifier TEXTURE = FabricIshMod.id("textures/entity/tater.png");
    private static final Identifier ANGRY_TEXTURE = FabricIshMod.id("textures/entity/tater_angry.png");
    public static final Identifier TATER_KNIFE = FabricIshMod.id("textures/entity/tater_knife.png");

    public TaterRenderer(EntityRendererProvider.Context context) {
        super(context, new TaterModel(context.bakeLayer(FabricIshModelLayers.TATER)), 0.5f);
        addLayer(new TaterKnifeLayer(this, context.getModelSet()));
    }

    @Override
    public Identifier getTextureLocation(TaterRenderState state) {
        return state.isAngry ? ANGRY_TEXTURE : TEXTURE;
    }

    @Override
    public TaterRenderState createRenderState() {
        return new TaterRenderState();
    }

    @Override
    public void extractRenderState(Tater entity, TaterRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isAngry = entity.isAngry();
    }
}
