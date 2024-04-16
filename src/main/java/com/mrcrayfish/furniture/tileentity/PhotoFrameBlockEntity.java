package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.block.PhotoFrameBlock;
import com.mrcrayfish.furniture.client.DownloadUtils;
import com.mrcrayfish.furniture.core.ModBlockEntities;
import me.srrapero720.watermedia.api.image.ImageAPI;
import me.srrapero720.watermedia.api.image.ImageCache;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringUtil;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.net.URI;
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
    @OnlyIn(Dist.CLIENT) public ImageCache image = null;

    public PhotoFrameBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.PHOTO_FRAME.get(), pos, state);
    }

    public String getPhoto()
    {
        return this.url != null ? this.url : "";
    }

    @OnlyIn(Dist.CLIENT)
    public ImageCache loadPhoto() throws ImageDownloadException
    {
        URI uri = DownloadUtils.createUri(url);
        if(uri == null)
            throw new ImageDownloadException("message.cfm.photo_frame.invalid");

        if(!DownloadUtils.isValidScheme(uri))
            throw new ImageDownloadException("message.cfm.photo_frame.wrong_scheme");

        if(!DownloadUtils.isValidType(uri, "png", "jpg", "jpeg", "gif"))
            throw new ImageDownloadException("message.cfm.photo_frame.unsupported_type");

        if(!DownloadUtils.isTrustedDomain(uri))
            throw new ImageDownloadException("message.cfm.photo_frame.untrusted");

        if (image == null || !image.url.equals(url))
            image = ImageAPI.getCache(url, Minecraft.getInstance());

        if (image.getStatus() == ImageCache.Status.WAITING)
            image.load();

        if (image.getStatus() == ImageCache.Status.FAILED)
            throw new ImageDownloadException("message.cfm.photo_frame.failed", image.getException());

        if (image.isVideo())
            throw new ImageDownloadException("message.cfm.photo_frame.unknown_file");

        return image;
    }

    public int getPhotoWidth()
    {
        BlockState state = getBlockState();
        if (state.getBlock() instanceof PhotoFrameBlock photoFrame)
        {
            return photoFrame.getWidth(state, level, getBlockPos()) - 2;
        }
        return 14;
    }

    public int getPhotoHeight()
    {
        BlockState state = getBlockState();
        if (state.getBlock() instanceof PhotoFrameBlock photoFrame)
        {
            return photoFrame.getHeight(state, level, getBlockPos()) - 2;
        }
        return 14;
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

        if(level != null && level.isClientSide())
        {
            Minecraft.getInstance().submit(() -> {
                try
                {
                    if(StringUtil.isNullOrEmpty(url))
                        releaseImage();
                    else
                        loadPhoto();
                }
                catch (ImageDownloadException e)
                {
                    // Errors will be reported later
                }
            });
        }
    }

    public boolean isStretched()
    {
        return stretch;
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

    private void releaseImage()
    {
        if (image != null)
            image.release();
        image = null;
    }

    @Override
    public void setRemoved()
    {
        super.setRemoved();
        releaseImage();
    }

    public static final class ImageDownloadException extends Exception {
        public ImageDownloadException(String message)
        {
            super(message);
        }

        public ImageDownloadException(String message, Exception cause)
        {
            super(message, cause);
        }
    }
}
