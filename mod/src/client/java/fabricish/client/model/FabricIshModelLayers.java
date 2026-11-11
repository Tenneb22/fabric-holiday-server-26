package fabricish.client.model;

import fabricish.FabricIshMod;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class FabricIshModelLayers {
    public static final ModelLayerLocation TATHER = createMain("tather");

    private static ModelLayerLocation createMain(String name) {
        return new ModelLayerLocation(FabricIshMod.id(name), "main");
    }

    public static void registerModelLayers() {
        ModelLayerRegistry.registerModelLayer(FabricIshModelLayers.TATHER, TatherModel::getTexturedModelData);
    }
}
