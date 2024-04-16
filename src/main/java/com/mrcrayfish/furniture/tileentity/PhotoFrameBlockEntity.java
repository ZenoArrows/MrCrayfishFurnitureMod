package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class PhotoFrameBlockEntity extends BlockEntity implements IValueContainer
{
    private String url = null;
    private boolean stretch = false;

    protected PhotoFrameBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    public PhotoFrameBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.PHOTO_FRAME.get(), pos, state);
    }

    public String getPhoto()
    {
        return this.url != null ? this.url : "";
    }

    @Override
    public void load(CompoundTag compound)
    {
        super.load(compound);
        if(compound.contains("Photo", Tag.TAG_STRING))
        {
            this.url = compound.getString("Photo");
        }
        if(compound.contains("Stretch", Tag.TAG_BYTE))
        {
            this.stretch = compound.getBoolean("Stretch");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        if(this.url != null)
        {
            tag.putString("Photo", this.url);
        }
        tag.putBoolean("Stretch", this.stretch);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        return this.saveWithFullMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
    {
        this.load(pkt.getTag());
    }

    @Override
    public List<Entry> getEntries()
    {
        List<IValueContainer.Entry> entries = new ArrayList<>();
        entries.add(new IValueContainer.Entry("photo", "Photo URL", Entry.Type.TEXT_FIELD, this.url));
        entries.add(new IValueContainer.Entry("stretch", "Stretch to Border", Entry.Type.TOGGLE, this.stretch));
        return entries;
    }

    @Override
    public String updateEntries(Map<String, String> entries, ServerPlayer player)
    {
        this.url = entries.get("photo");
        this.stretch = Boolean.valueOf(entries.get("stretch"));
        this.setChanged();
        return null;
    }

    @Override
    public BlockPos getContainerPos()
    {
        return getBlockPos();
    }
}
