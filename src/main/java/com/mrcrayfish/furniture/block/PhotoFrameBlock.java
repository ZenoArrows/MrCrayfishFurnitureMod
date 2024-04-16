package com.mrcrayfish.furniture.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class PhotoFrameBlock extends FurnitureHorizontalBlock
{
    public static final BooleanProperty LEFT = BooleanProperty.create("left");
    public static final BooleanProperty RIGHT = BooleanProperty.create("right");
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");

    public final ImmutableMap<BlockState, VoxelShape> SHAPES;

    public PhotoFrameBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(LEFT, Boolean.valueOf(false)).setValue(RIGHT, Boolean.valueOf(false)).setValue(TOP, Boolean.valueOf(false)).setValue(BOTTOM, Boolean.valueOf(false)));
        SHAPES = this.generateShapes(this.getStateDefinition().getPossibleStates());
    }

    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            final VoxelShape[] BOXES = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.box(15, 0, 0, 16, 16, 16), Direction.WEST));
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(BOXES[state.getValue(DIRECTION).get2DDataValue()]);
            builder.put(state, VoxelShapeHelper.combineAll(shapes));
        }
        return builder.build();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context)
    {
        return SHAPES.get(state);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter reader, BlockPos pos)
    {
        return SHAPES.get(state);
    }

    public boolean connectsTo(BlockState state, Direction direction)
    {
        return state.getBlock() == this && state.getValue(DIRECTION) == direction;
    }

    public boolean isConnected(BlockState state, BooleanProperty side)
    {
        return state.getBlock() == this && state.getValue(side);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockGetter blockgetter = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        if (direction.getAxis().isVertical())
            direction = context.getHorizontalDirection().getOpposite();

        BlockState left = blockgetter.getBlockState(blockpos.relative(direction.getClockWise()));
        BlockState right = blockgetter.getBlockState(blockpos.relative(direction.getCounterClockWise()));
        BlockState top = blockgetter.getBlockState(blockpos.above());
        BlockState bottom = blockgetter.getBlockState(blockpos.below());

        return super.getStateForPlacement(context)
                .setValue(DIRECTION, direction)
                .setValue(LEFT, Boolean.valueOf(connectsTo(left, direction)))
                .setValue(RIGHT, Boolean.valueOf(connectsTo(right, direction)))
                .setValue(TOP, Boolean.valueOf(connectsTo(top, direction)))
                .setValue(BOTTOM, Boolean.valueOf(connectsTo(bottom, direction)));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction side, BlockState otherState, LevelAccessor accessor, BlockPos blockpos, BlockPos otherPos)
    {
        Direction direction = state.getValue(DIRECTION);
        BlockPos left = blockpos.relative(direction.getClockWise());
        BlockPos right = blockpos.relative(direction.getCounterClockWise());
        BlockPos top = blockpos.above();
        BlockPos bottom = blockpos.below();

        if (side == Direction.UP)
        {
            if (isConnected(otherState, BOTTOM))
                return state.setValue(TOP, Boolean.TRUE)
                        .setValue(LEFT, otherState.getValue(LEFT) && connectsTo(accessor.getBlockState(left), direction))
                        .setValue(RIGHT, otherState.getValue(RIGHT) && connectsTo(accessor.getBlockState(right), direction));
            else
                return state.setValue(TOP, Boolean.FALSE);
        }
        else if (side == Direction.DOWN)
        {
            if (isConnected(otherState, TOP))
                return state.setValue(BOTTOM, Boolean.TRUE)
                        .setValue(LEFT, otherState.getValue(LEFT) && connectsTo(accessor.getBlockState(left), direction))
                        .setValue(RIGHT, otherState.getValue(RIGHT) && connectsTo(accessor.getBlockState(right), direction));
            else
                return state.setValue(BOTTOM, Boolean.FALSE);
        }
        else if (side == direction.getClockWise())
        {
            if (isConnected(otherState, RIGHT))
                return state.setValue(LEFT, Boolean.TRUE)
                        .setValue(TOP, otherState.getValue(TOP) && connectsTo(accessor.getBlockState(top), direction))
                        .setValue(BOTTOM, otherState.getValue(BOTTOM) && connectsTo(accessor.getBlockState(bottom), direction));
            else
                return state.setValue(LEFT, Boolean.FALSE);
        }
        else if (side == direction.getCounterClockWise())
        {
            if (isConnected(otherState, LEFT))
                return state.setValue(RIGHT, Boolean.TRUE)
                        .setValue(TOP, otherState.getValue(TOP) && connectsTo(accessor.getBlockState(top), direction))
                        .setValue(BOTTOM, otherState.getValue(BOTTOM) && connectsTo(accessor.getBlockState(bottom), direction));
            else
                return state.setValue(RIGHT, Boolean.FALSE);
        }
        return super.updateShape(state, side, otherState, accessor, blockpos, otherPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(LEFT, RIGHT, TOP, BOTTOM);
    }
}
