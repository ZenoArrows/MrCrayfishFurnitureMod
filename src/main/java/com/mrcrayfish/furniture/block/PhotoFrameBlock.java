package com.mrcrayfish.furniture.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mrcrayfish.furniture.client.ClientHandler;
import com.mrcrayfish.furniture.tileentity.PhotoFrameBlockEntity;
import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class PhotoFrameBlock extends FurnitureHorizontalBlock implements EntityBlock
{
    public static final BooleanProperty LEFT = BooleanProperty.create("left");
    public static final BooleanProperty RIGHT = BooleanProperty.create("right");
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");

    public final ImmutableMap<BlockState, VoxelShape> SHAPES;

    public PhotoFrameBlock(Properties properties)
    {
        super(properties.dynamicShape());
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
    public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context)
    {
        if (!isMasterFrame(state))
            return SHAPES.get(state);

        // Prevent the frame that's in control of rendering the photo from being culled away
        int width = getWidth(state, reader, pos), height = getHeight(state, reader, pos);
        return VoxelShapeHelper.rotate(Block.box(0, 0, 15, width, height, 16),
                state.getValue(DIRECTION).getClockWise());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context)
    {
        return SHAPES.get(state);
    }

    public boolean connectsTo(BlockState state, Direction direction)
    {
        return state.getBlock() == this && state.getValue(DIRECTION) == direction;
    }

    public boolean canConnect(BlockState state, BooleanProperty side)
    {
        return state.getBlock() == this && state.getValue(side);
    }

    public boolean isConnected(BlockState state)
    {
        return state.getValue(LEFT) || state.getValue(RIGHT) || state.getValue(TOP) || state.getValue(BOTTOM);
    }

    public boolean isMasterFrame(BlockState state)
    {
        return !state.getValue(RIGHT) && !state.getValue(BOTTOM);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockGetter blockgetter = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        if(direction.getAxis().isVertical())
            direction = context.getHorizontalDirection().getOpposite();

        BlockState left = blockgetter.getBlockState(blockpos.relative(direction.getClockWise()));
        BlockState right = blockgetter.getBlockState(blockpos.relative(direction.getCounterClockWise()));
        BlockState top = blockgetter.getBlockState(blockpos.above());
        BlockState bottom = blockgetter.getBlockState(blockpos.below());

        // First we try to connect to any loose frames or one-block wide row or column of frames we can connect to.
        BlockState state = super.getStateForPlacement(context).setValue(DIRECTION, direction);
        state = state.setValue(LEFT, left == state || left == state.setValue(LEFT, Boolean.TRUE))
                .setValue(RIGHT, right == state || right == state.setValue(RIGHT, Boolean.TRUE))
                .setValue(TOP, top == state || top == state.setValue(TOP, Boolean.TRUE))
                .setValue(BOTTOM, bottom == state || bottom == state.setValue(BOTTOM, Boolean.TRUE));

        if(!isConnected(state))
            return state;

        // If we managed to connect to another frame, then connect to any other neighbouring frames to create a rectangle.
        return state.setValue(LEFT, connectsTo(left, direction))
                .setValue(RIGHT, connectsTo(right, direction))
                .setValue(TOP, connectsTo(top, direction))
                .setValue(BOTTOM, connectsTo(bottom, direction));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction side, BlockState otherState, LevelAccessor accessor, BlockPos blockpos, BlockPos otherPos)
    {
        Direction direction = state.getValue(DIRECTION);
        BlockPos left = blockpos.relative(direction.getClockWise());
        BlockPos right = blockpos.relative(direction.getCounterClockWise());
        BlockPos top = blockpos.above();
        BlockPos bottom = blockpos.below();

        if(side == Direction.UP)
        {
            if(canConnect(otherState, BOTTOM))
                return state.setValue(TOP, Boolean.TRUE)
                        .setValue(LEFT, otherState.getValue(LEFT) && connectsTo(accessor.getBlockState(left), direction))
                        .setValue(RIGHT, otherState.getValue(RIGHT) && connectsTo(accessor.getBlockState(right), direction));
            else
                return state.setValue(TOP, Boolean.FALSE);
        }
        else if(side == Direction.DOWN)
        {
            if(canConnect(otherState, TOP))
                return state.setValue(BOTTOM, Boolean.TRUE)
                        .setValue(LEFT, otherState.getValue(LEFT) && connectsTo(accessor.getBlockState(left), direction))
                        .setValue(RIGHT, otherState.getValue(RIGHT) && connectsTo(accessor.getBlockState(right), direction));
            else
                return state.setValue(BOTTOM, Boolean.FALSE);
        }
        else if(side == direction.getClockWise())
        {
            if(canConnect(otherState, RIGHT))
                return state.setValue(LEFT, Boolean.TRUE)
                        .setValue(TOP, otherState.getValue(TOP) && connectsTo(accessor.getBlockState(top), direction))
                        .setValue(BOTTOM, otherState.getValue(BOTTOM) && connectsTo(accessor.getBlockState(bottom), direction));
            else
                return state.setValue(LEFT, Boolean.FALSE);
        }
        else if(side == direction.getCounterClockWise())
        {
            if(canConnect(otherState, LEFT))
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

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new PhotoFrameBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        // Find the bottom-right corner of the frame
        Direction direction = state.getValue(DIRECTION).getCounterClockWise();
        while(!isMasterFrame(state))
        {
            if(state.getValue(RIGHT))
                pos = pos.relative(direction);
            if(state.getValue(BOTTOM))
                pos = pos.below();
            state = level.getBlockState(pos);
            if (state.getBlock() != this)
                return InteractionResult.FAIL;
        }

        if(level.isClientSide())
            ClientHandler.showEditValueContainerScreen(level, pos, Component.translatable("gui.cfm.photo_frame_settings"));
        return InteractionResult.SUCCESS;
    }

    public int getWidth(BlockState state, BlockGetter reader, BlockPos pos)
    {
        // Count the number of frames until we hit the left-most edge
        int width = 16;
        Direction left = state.getValue(DIRECTION).getClockWise();
        for(int i = 1; state.getBlock() == this && state.getValue(LEFT);
            state = reader.getBlockState(pos.relative(left, i++)))
        {
            width += 16;
        }
        return width;
    }

    public int getHeight(BlockState state, BlockGetter reader, BlockPos pos)
    {
        // Count the number of frames until we hit the top-most edge
        int height = 16;
        for(int i = 1; state.getBlock() == this && state.getValue(TOP);
            state = reader.getBlockState(pos.above(i++)))
        {
            height += 16;
        }
        return height;
    }
}
