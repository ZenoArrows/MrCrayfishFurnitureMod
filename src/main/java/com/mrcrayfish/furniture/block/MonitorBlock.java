package com.mrcrayfish.furniture.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class MonitorBlock extends AbstractDisplayBlock implements EntityBlock
{
    public static final BooleanProperty PORTRAIT = BooleanProperty.create("portrait");

    public MonitorBlock(Properties properties)
    {
        super(properties, 20, 15, 3.5, -5.1);
        this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(PORTRAIT, Boolean.FALSE));
    }

    protected ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states, boolean collision)
    {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            VoxelShape box;
            if(collision)
            {
                if(state.getValue(PORTRAIT))
                    box = Block.box(3, 0, 2, 13, 19, 14);
                else
                    box = Block.box(3, 0, -2, 13, 18, 18);
            }
            else
            {
                if(state.getValue(PORTRAIT))
                    box = Block.box(2, 0, 1, 14, 20, 15);
                else
                    box = Block.box(2, 0, -3, 14, 19, 19);
            }
            final VoxelShape[] BOXES = VoxelShapeHelper.getRotatedShapes(box);
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(BOXES[state.getValue(DIRECTION).get2DDataValue()]);
            builder.put(state, VoxelShapeHelper.combineAll(shapes));
        }
        return builder.build();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        if(player.isShiftKeyDown())
        {
            level.setBlock(pos, state.setValue(PORTRAIT, !state.getValue(PORTRAIT)), 3);
            return InteractionResult.SUCCESS;
        }
        return super.use(state, level, pos, player, hand, result);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(PORTRAIT);
    }

    @Override
    public float getScreenWidth(BlockState state)
    {
        if(state.getValue(PORTRAIT))
        {
            return 12;
        }
        return super.getScreenWidth(state);
    }

    @Override
    public float getScreenHeight(BlockState state)
    {
        if(state.getValue(PORTRAIT))
        {
            return 16;
        }
        return super.getScreenHeight(state);
    }
}
