package fabricish.entity;

import fabricish.FabricIshMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

public class FabricIshModEntityTypeIds {
    public static ResourceKey<EntityType<?>> TATHER = create("tather");

    private static ResourceKey<EntityType<?>> create(String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, FabricIshMod.id(name));
    }
}
