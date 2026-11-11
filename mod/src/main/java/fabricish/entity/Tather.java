package fabricish.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

// Noo, tather isn't a monster :<
public class Tather extends Monster {
    public Tather(Level level) {
        super(FabricIshModEntityTypes.TATHER, level);
    }

    public Tather(EntityType<? extends Tather> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createTatherAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 300.0)
            .add(Attributes.MOVEMENT_SPEED, 0.6F)
            .add(Attributes.FLYING_SPEED, 0.6F)
            .add(Attributes.FOLLOW_RANGE, 40.0)
            .add(Attributes.ARMOR, 4.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector
    }
}
