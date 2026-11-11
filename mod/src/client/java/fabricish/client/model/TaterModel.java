package fabricish.client.model;

import fabricish.client.render.entity.state.TaterRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class TaterModel extends EntityModel<TaterRenderState> {
    public TaterModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition root = modelData.getRoot();
        root.addOrReplaceChild(
            PartNames.BODY,
            CubeListBuilder.create().addBox(
                -4,-12,-4,
                8,12,8
            ),
            PartPose.offset(0, 24, 0)
        );

        return LayerDefinition.create(modelData, 32, 32);
    }
}
