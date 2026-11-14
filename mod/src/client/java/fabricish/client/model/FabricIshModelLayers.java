package fabricish.client.model;

import fabricish.FabricIshMod;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class FabricIshModelLayers {
    public static final ModelLayerLocation TATER = createMain("tater");
    public static final ModelLayerLocation TATER_KNIFE = register("tater", "tater_knife");

    private static ModelLayerLocation register(String model, String layer) {
        return new ModelLayerLocation(FabricIshMod.id(model), layer);
    }

    private static ModelLayerLocation createMain(String name) {
        return new ModelLayerLocation(FabricIshMod.id(name), "main");
    }

    public static void registerModelLayers() {
        ModelLayerRegistry.registerModelLayer(FabricIshModelLayers.TATER, TaterModel::getTexturedModelData);
        ModelLayerRegistry.registerModelLayer(FabricIshModelLayers.TATER_KNIFE, TaterKnifeModel::getTexturedModelData);
    }
}
