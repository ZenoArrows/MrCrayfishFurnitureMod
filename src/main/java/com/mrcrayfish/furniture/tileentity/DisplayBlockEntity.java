package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.block.AbstractDisplayBlock;
import com.mrcrayfish.furniture.client.DownloadUtils;
import com.mrcrayfish.furniture.core.ModBlockEntities;
import me.srrapero720.watermedia.api.player.SyncVideoPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringUtil;
import net.minecraft.world.level.block.entity.BlockEntity;
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
public class DisplayBlockEntity extends BlockEntity implements IValueContainer
{
    private String url = null;
    private boolean stretched = false;
    private boolean muted = false;
    private boolean powered = false;
    private List<String> channels = new ArrayList<>();
    private int currentChannel = 0;
    @OnlyIn(Dist.CLIENT) public SyncVideoPlayer player = null;

    public DisplayBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.DISPLAY.get(), pos, state);
    }

    public boolean isPowered()
    {
        return powered;
    }

    @Nullable
    public String getCurrentChannel()
    {
        if(channels.size() > 0 && currentChannel >= 0 && currentChannel < channels.size())
        {
            return channels.get(currentChannel);
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public SyncVideoPlayer loadVideo() throws VideoDownloadException
    {
        if(player == null)
        {
            player = new SyncVideoPlayer(Minecraft.getInstance());
            player.setMuteMode(muted);
        }
        else if (player.isMuted() != muted)
        {
            player.setMuteMode(muted);
        }

        if (player.isEnded())
            currentChannel = (currentChannel + 1) % channels.size();

        URI uri = DownloadUtils.createUri(getCurrentChannel());
        if(uri == null)
            throw new VideoDownloadException("message.cfm.video.invalid");

        if(!DownloadUtils.isValidScheme(uri))
            throw new VideoDownloadException("message.cfm.video.wrong_scheme");

        if(!DownloadUtils.isTrustedDomain(uri))
            throw new VideoDownloadException("message.cfm.video.untrusted");

        if(!channels.get(currentChannel).equals(url))
            player.start(getCurrentChannel());
        url = channels.get(currentChannel);

        if(player.isBroken())
            throw new VideoDownloadException("message.cfm.video.failed");

        if(!player.isValid())
            throw new VideoDownloadException("message.cfm.video.unknown_file");

        return player;
    }

    public float getScreenWidth()
    {
        BlockState state = getBlockState();
        if(state.getBlock() instanceof AbstractDisplayBlock display)
        {
            return display.getScreenWidth(state);
        }
        return 16;
    }

    public float getScreenHeight()
    {
        BlockState state = getBlockState();
        if(state.getBlock() instanceof AbstractDisplayBlock display)
        {
            return display.getScreenHeight(state);
        }
        return 16;
    }

    public double getScreenYOffset()
    {
        BlockState state = getBlockState();
        if(state.getBlock() instanceof AbstractDisplayBlock display)
        {
            return display.getScreenYOffset(state);
        }
        return 0.0;
    }

    public double getScreenZOffset()
    {
        BlockState state = getBlockState();
        if(state.getBlock() instanceof AbstractDisplayBlock display)
        {
            return display.getScreenZOffset(state);
        }
        return 0.0;
    }

    @Override
    public void load(CompoundTag compound)
    {
        super.load(compound);
        channels.clear();
        if(compound.contains("Channels", Tag.TAG_LIST))
        {
            compound.getList("Channels", Tag.TAG_STRING).forEach(tag -> {
                if(tag instanceof StringTag url)
                    channels.add(url.getAsString());
            });
        }
        if(compound.contains("CurrentChannel", Tag.TAG_INT))
        {
            this.currentChannel = compound.getInt("CurrentChannel");
        }
        if(compound.contains("Powered", Tag.TAG_BYTE))
        {
            this.powered = compound.getBoolean("Powered");
            if(!this.powered && player != null)
            {
                player.release();
                player = null;
            }
        }
        if(compound.contains("Stretch", Tag.TAG_BYTE))
        {
            this.stretched = compound.getBoolean("Stretch");
        }
    }

    public boolean isStretched()
    {
        return stretched;
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        ListTag channelList = new ListTag();
        channels.forEach(url -> channelList.add(StringTag.valueOf(url)));
        tag.put("Channels", channelList);
        tag.putInt("CurrentChannel", currentChannel);
        tag.putBoolean("Crop", stretched);
        tag.putBoolean("Powered", powered);
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
        List<Entry> entries = new ArrayList<>(3);
        for(int i = 0; i < 3; i++)
        {
            String url = "";
            if(channels.size() > 0 && i < channels.size())
            {
                url = channels.get(i);
            }
            entries.add(new Entry("channel_" + i, "Channel #" + (i + 1), Entry.Type.TEXT_FIELD, url));
        }
        entries.add(new Entry("stretched", "Stretch to Screen", Entry.Type.TOGGLE, this.stretched));
        entries.add(new Entry("muted", "Muted", Entry.Type.TOGGLE, this.muted));
        entries.add(new Entry("powered", "Powered", Entry.Type.TOGGLE, this.powered));
        return entries;
    }

    @Override
    public String updateEntries(Map<String, String> entries, ServerPlayer player)
    {
        channels.clear();
        for(int i = 0; i < 3; i++)
        {
            String url = entries.get("channel_" + i);
            if(!StringUtil.isNullOrEmpty(url))
            {
                channels.add(url);
            }
        }
        this.stretched = Boolean.valueOf(entries.get("stretched"));
        this.muted = Boolean.valueOf(entries.get("muted"));
        this.powered = Boolean.valueOf(entries.get("powered"));
        this.setChanged();
        return null;
    }

    @Override
    public BlockPos getContainerPos()
    {
        return getBlockPos();
    }

    @Override
    public void setRemoved()
    {
        super.setRemoved();
        if (player != null)
            player.release();
        player = null;
    }

    public static final class VideoDownloadException extends Exception {
        public VideoDownloadException(String message)
        {
            super(message);
        }

        public VideoDownloadException(String message, Exception cause)
        {
            super(message, cause);
        }
    }
}
