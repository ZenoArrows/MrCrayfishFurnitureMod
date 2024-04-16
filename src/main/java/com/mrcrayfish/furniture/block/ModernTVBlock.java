package com.mrcrayfish.furniture.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mrcrayfish.furniture.tileentity.DoorMatBlockEntity;
import com.mrcrayfish.furniture.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class ModernTVBlock extends AbstractDisplayBlock implements EntityBlock
{
    public static final BooleanProperty MOUNTED = BooleanProperty.create("mounted");

    public ModernTVBlock(Properties properties)
    {
        super(properties, 24, 13.5F, 3.5, -0.35);
        this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(MOUNTED, Boolean.FALSE));
    }

    protected ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states, boolean collision)
    {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            VoxelShape box;
            if(collision)
            {
                if(state.getValue(MOUNTED))
                    box = Block.box(13, 3, -4, 15, 18, 20);
                else
                    box = Block.box(7, 0, -4, 10, 18, 20);
            }
            else
            {
                if(state.getValue(MOUNTED))
                    box = Block.box(12, 2, -5, 16, 19, 21);
                else
                    box = Block.box(6, 0, -5, 11, 19, 21);
            }
            final VoxelShape[] BOXES = VoxelShapeHelper.getRotatedShapes(box);
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(BOXES[state.getValue(DIRECTION).get2DDataValue()]);
            builder.put(state, VoxelShapeHelper.combineAll(shapes));
        }
        return builder.build();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState state = super.getStateForPlacement(context);
        Direction direction = context.getClickedFace();
        return state.setValue(MOUNTED, direction.getAxis().isHorizontal());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(MOUNTED);
    }

    @Override
    public double getScreenZOffset(BlockState state)
    {
        if(state.getValue(MOUNTED))
        {
            return 4.65;
        }
        return super.getScreenZOffset(state);
    }
}
