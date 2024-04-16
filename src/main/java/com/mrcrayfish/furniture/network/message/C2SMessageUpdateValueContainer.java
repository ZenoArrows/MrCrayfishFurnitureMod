package com.mrcrayfish.furniture.network.message;

import com.mrcrayfish.furniture.client.gui.screen.components.ValueComponent;
import com.mrcrayfish.furniture.network.play.ServerPlayHandler;
import com.mrcrayfish.furniture.tileentity.IValueContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Author: MrCrayfish
 */
public class C2SMessageUpdateValueContainer implements IMessage<C2SMessageUpdateValueContainer>
{
    private Map<String, String> entryMap;
    private BlockPos pos;

    public C2SMessageUpdateValueContainer() {}

    public C2SMessageUpdateValueContainer(Map<String, String> valueEntries, BlockPos containerPos)
    {
        this.pos = containerPos;
        this.entryMap = valueEntries;
    }

    public C2SMessageUpdateValueContainer(List<ValueComponent> valueEntries, IValueContainer valueContainer)
    {
        this.pos = valueContainer.getContainerPos();
        this.entryMap = valueEntries.stream().collect(Collectors.toMap(ValueComponent::getId, ValueComponent::getValue));
    }

    @Override
    public void encode(C2SMessageUpdateValueContainer message, FriendlyByteBuf buf)
    {
        buf.writeInt(message.entryMap.size());
        message.entryMap.forEach((key, value) ->
        {
            buf.writeUtf(key);
            buf.writeUtf(value);
        });
        buf.writeBlockPos(message.pos);
    }

    @Override
    public C2SMessageUpdateValueContainer decode(FriendlyByteBuf buf)
    {
        int size = buf.readInt();
        Map<String, String> map = new HashMap<>();
        for(int i = 0; i < size; i++)
        {
            String id = buf.readUtf();
            String value = buf.readUtf();
            map.put(id, value);
        }
        return new C2SMessageUpdateValueContainer(map, buf.readBlockPos());
    }

    @Override
    public void handle(C2SMessageUpdateValueContainer message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> IMessage.callServerConsumer(message, supplier, ServerPlayHandler::handleUpdateValueContainerMessage));
        supplier.get().setPacketHandled(true);
    }

    public Map<String, String> getEntryMap()
    {
        return entryMap;
    }

    public BlockPos getPos()
    {
        return pos;
    }
}
