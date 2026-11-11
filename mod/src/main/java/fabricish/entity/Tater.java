package fabricish.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

// Noo, tater isn't a monster :<
public class Tater extends Monster {
    public Tater(Level level) {
        super(FabricIshModEntityTypes.TATER, level);
    }

    public Tater(EntityType<? extends Tater> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createTaterAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 30.0)
            .add(Attributes.MOVEMENT_SPEED, 0.6F)
            .add(Attributes.FOLLOW_RANGE, 40.0)
            .add(Attributes.ARMOR, 4.0);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        GroundPathNavigation groundPathNavigation = new GroundPathNavigation(this, level);
        groundPathNavigation.setCanOpenDoors(true);
        return groundPathNavigation;
    }

    @Override
    protected void registerGoals() {
        //this.goalSelector
    }
}
