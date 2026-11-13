package fabricish.entity;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class Tater extends TamableAnimal {
    public Tater(Level level) {
        super(FabricIshModEntityTypes.TATER, level);
    }

    public Tater(EntityType<? extends Tater> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createTaterAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 30.0)
            .add(Attributes.MOVEMENT_SPEED, 0.6F)
            .add(Attributes.FOLLOW_RANGE, 40.0);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        GroundPathNavigation groundPathNavigation = new GroundPathNavigation(this, level);
        groundPathNavigation.setCanOpenDoors(true);
        return groundPathNavigation;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FollowOwnerGoal(this, 1, 5, 1.5f));
        goalSelector.addGoal(2, new RandomStrollGoal(this, 1));
        goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0f));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (isTame()) {
            if (this.isOwnedBy(player)) {
                if (this.isFood(itemStack) && this.getHealth() < this.getMaxHealth()) {
                    if (!this.level().isClientSide()) {
                        this.feed(player, hand, itemStack, 1.0F, 1.0F);
                    }

                    return InteractionResult.SUCCESS;
                }

                InteractionResult parentInteraction = super.mobInteract(player, hand);
                if (!parentInteraction.consumesAction()) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                    return InteractionResult.SUCCESS;
                }

                return parentInteraction;
            }
        } else if (isFood(itemStack)) {
            if (!this.level().isClientSide()) {
                this.usePlayerItem(player, hand, itemStack);
                this.tryToTame(player);
                this.setPersistenceRequired();
                this.playEatingSound();
            }

            return InteractionResult.SUCCESS;
        }

        InteractionResult interact = super.mobInteract(player, hand);
        if (interact.consumesAction()) {
            this.setPersistenceRequired();
        }

        return interact;
    }

    private void tryToTame(Player player) {
        if (this.random.nextInt(3) == 0) {
            this.tame(player);
            this.setOrderedToSit(true);
            this.level().broadcastEntityEvent(this, (byte)7);
        } else {
            this.level().broadcastEntityEvent(this, (byte)6);
        }
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(Items.POTATO);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return null;
    }
}
