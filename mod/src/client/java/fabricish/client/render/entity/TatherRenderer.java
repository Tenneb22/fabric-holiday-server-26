package fabricish.client.render.entity;

import fabricish.FabricIshMod;
import fabricish.client.model.FabricIshModelLayers;
import fabricish.client.model.TatherModel;
import fabricish.client.render.entity.state.TatherRenderState;
import fabricish.entity.Tather;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class TatherRenderer extends MobRenderer<Tather, TatherRenderState, TatherModel> {
    private static final Identifier TEXTURE = FabricIshMod.id("textures/entity/tather.png");

    public TatherRenderer(EntityRendererProvider.Context context) {
        super(context, new TatherModel(context.bakeLayer(FabricIshModelLayers.TATHER)), 1.0f);
    }

    @Override
    public Identifier getTextureLocation(TatherRenderState state) {
        return TEXTURE;
    }

    @Override
    public TatherRenderState createRenderState() {
        return new TatherRenderState();
    }
}
