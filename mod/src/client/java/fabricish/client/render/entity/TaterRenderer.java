package fabricish.client.render.entity;

import fabricish.FabricIshMod;
import fabricish.client.model.FabricIshModelLayers;
import fabricish.client.model.TaterModel;
import fabricish.client.render.entity.state.TaterRenderState;
import fabricish.entity.Tater;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class TaterRenderer extends MobRenderer<Tater, TaterRenderState, TaterModel> {
    private static final Identifier TEXTURE = FabricIshMod.id("textures/entity/tater.png");

    public TaterRenderer(EntityRendererProvider.Context context) {
        super(context, new TaterModel(context.bakeLayer(FabricIshModelLayers.TATER)), 1.0f);
    }

    @Override
    public Identifier getTextureLocation(TaterRenderState state) {
        return TEXTURE;
    }

    @Override
    public TaterRenderState createRenderState() {
        return new TaterRenderState();
    }
}
