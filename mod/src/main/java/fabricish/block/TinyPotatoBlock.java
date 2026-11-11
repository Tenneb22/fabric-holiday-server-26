package fabricish.block;

import fabricish.entity.FabricIshModEntityTypes;
import fabricish.entity.Tather;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class TinyPotatoBlock extends Block {
    private static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = box(4, 0, 4, 12, 12, 12);

    private static BlockPattern tatherPattern;

    protected TinyPotatoBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
        checkSpawn(level, pos, state);
    }

    private static void checkSpawn(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        boolean isTinyPotato = state.is(FabricIshBlocks.TINY_POTATO);
        if (!isTinyPotato) return;

        BlockPattern.BlockPatternMatch match = getTatherPattern().find(level, pos);
        if (match == null) return;

        Tather tather = FabricIshModEntityTypes.TATHER.create(level, EntitySpawnReason.TRIGGERED);
        if (tather == null) return;

        BlockPos spawnPos = match.getBlock(1, 2, 0).getPos();
        //tather.snapTo(Vec3.atLowerCornerOf(pos));
        //tather.snapTo(Vec3.atLowerCornerOf(spawnPos));
        tather.snapTo(
            spawnPos.getX() + 0.5, spawnPos.getY() + 0.55, spawnPos.getZ() + 0.5, match.getForwards().getAxis() == Direction.Axis.X ? 0.0F : 90.0F, 0.0F
        );
        tather.yBodyRot = match.getForwards().getAxis() == Direction.Axis.X ? 0.0F : 90.0F;

        CarvedPumpkinBlock.clearPatternBlocks(level, match);

        level.addFreshEntity(tather);
        CarvedPumpkinBlock.updatePatternBlocks(level, match);
    }

    private static BlockPattern getTatherPattern() {
        if (tatherPattern == null) {
            tatherPattern = BlockPatternBuilder.start()
                .aisle("+++", "###", "-#-")
                .where('+', block -> block.getState().is(FabricIshBlocks.TINY_POTATO))
                .where('#', block -> block.getState().is(Blocks.DIRT))
                .where('-', block -> block.getState().isAir())
                .build();
        }
        return tatherPattern;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected BlockState rotate(final BlockState state, final Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(final BlockState state, final Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
