package com.mrcrayfish.furniture.item;

import com.mrcrayfish.furniture.block.AbstractDisplayBlock;
import com.mrcrayfish.furniture.core.ModSounds;
import com.mrcrayfish.furniture.tileentity.DisplayBlockEntity;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * Author: MrCrayfish
 */
public class TVRemoteItem extends Item implements CreativeItem
{
    public TVRemoteItem(Item.Properties builder)
    {
        super(builder);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn)
    {
        if(Screen.hasShiftDown())
        {
            tooltip.add(Component.translatable("item.cfm.tv_remote.tooltip"));
        }
        else
        {
            tooltip.add(Component.translatable("gui.cfm.tooltip"));
        }
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context)
    {
        return activateTelevision(context.getLevel(), context.getPlayer(), context.getItemInHand());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack itemStack = player.getItemInHand(hand);
        return new InteractionResultHolder<>(activateTelevision(level, player, itemStack), itemStack);
    }

    public InteractionResult activateTelevision(Level level, Player player, ItemStack itemStack)
    {
        Vec3 startVec = player.getEyePosition();
        Vec3 endVec = startVec.add(player.getLookAngle().normalize().scale(16));

        BlockHitResult result = level.clip(new ClipContext(startVec, endVec, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        if(result != null && result.getType() == HitResult.Type.BLOCK)
        {
            BlockPos pos = result.getBlockPos();
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if(tileEntity instanceof DisplayBlockEntity display)
            {
                if(player.isShiftKeyDown())
                {
                    display.setPowered(!display.isPowered());
                }
                else
                {
                    if(display.nextChannel())
                    {
                        level.playSound(null, pos, ModSounds.BLOCK_WHITE_NOISE.get(), SoundSource.BLOCKS, 0.5F, 1.0F);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void fill(Consumer<ItemStack> output)
    {
        output.accept(new ItemStack(this));
    }
}
