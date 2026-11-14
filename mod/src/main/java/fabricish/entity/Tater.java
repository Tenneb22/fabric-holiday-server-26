package fabricish.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class Tater extends TamableAnimal {
    private static final EntityDataAccessor<Boolean> IS_ANGRY = SynchedEntityData.defineId(Tater.class, EntityDataSerializers.BOOLEAN);
    private static final String TAG_IS_ANGRY = "IS_ANGRY";

    public Tater(EntityType<? extends Tater> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createTaterAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 30.0)
            .add(Attributes.MOVEMENT_SPEED, 0.6F)
            .add(Attributes.FOLLOW_RANGE, 40.0)
            .add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        GroundPathNavigation groundPathNavigation = new GroundPathNavigation(this, level);
        groundPathNavigation.setCanOpenDoors(true);
        return groundPathNavigation;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.75, true));
        goalSelector.addGoal(2, new PanicGoal(this, 0.75));
        goalSelector.addGoal(3, new FollowOwnerGoal(this, 0.5, 10, 2.5f));
        goalSelector.addGoal(4, new RandomStrollGoal(this, 0.3));
        goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Tater.class, true, this::canAttack));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::canAttack));
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

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(IS_ANGRY, false);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean(TAG_IS_ANGRY, isAngry());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);

        if (input.getBooleanOr(TAG_IS_ANGRY, false)) {
            makeAngry();
        }
    }

    // automatically ignores the owner
    private boolean canAttack(LivingEntity target, ServerLevel level) {
        return isAngry();
    }

    public boolean isAngry() {
        return this.entityData.get(IS_ANGRY);
    }

    // don't allow calming the tater again (for now)
    public void makeAngry() {
        this.entityData.set(IS_ANGRY, true);
    }
}
