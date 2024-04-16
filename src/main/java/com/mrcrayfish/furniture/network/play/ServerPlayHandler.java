package com.mrcrayfish.furniture.network.play;

import com.mrcrayfish.furniture.FurnitureMod;
import com.mrcrayfish.furniture.common.mail.Mail;
import com.mrcrayfish.furniture.common.mail.PostOffice;
import com.mrcrayfish.furniture.inventory.container.CrateMenu;
import com.mrcrayfish.furniture.inventory.container.PostBoxMenu;
import com.mrcrayfish.furniture.network.message.*;
import com.mrcrayfish.furniture.tileentity.CrateBlockEntity;
import com.mrcrayfish.furniture.tileentity.DoorMatBlockEntity;
import com.mrcrayfish.furniture.tileentity.IValueContainer;
import com.mrcrayfish.furniture.tileentity.MailBoxBlockEntity;
import com.mrcrayfish.furniture.util.BlockEntityUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

/**
 * Author: MrCrayfish
 */
public class ServerPlayHandler
{
    public static void handleLockCrateMessage(ServerPlayer player, C2SMessageLockCrate message)
    {
        if(!(player.containerMenu instanceof CrateMenu crateMenu))
            return;

        CrateBlockEntity crate = crateMenu.getBlockEntity();
        if(crate == null)
            return;

        if(crate.getOwner() == null)
        {
            crate.setOwner(player.getUUID());
        }

        if(player.getUUID().equals(crate.getOwner()))
        {
            crate.setLocked(!crate.isLocked());
            crate.removeUnauthorisedPlayers();
        }
    }

    public static void handleOpenMailBoxMessage(ServerPlayer player, C2SMessageOpenMailBox message)
    {
        Level level = player.level();
        if(!level.isLoaded(message.getPos()))
            return;

        if(!(level.getBlockEntity(message.getPos()) instanceof MailBoxBlockEntity blockEntity))
            return;

        if(!blockEntity.stillValid(player))
            return;

        BlockEntityUtil.sendUpdatePacket(blockEntity);
        NetworkHooks.openScreen(player, blockEntity, message.getPos());
    }

    public static void handleSendMailMessage(ServerPlayer player, C2SMessageSendMail message)
    {
        if(!(player.containerMenu instanceof PostBoxMenu postBox))
            return;

        if(postBox.getMail().isEmpty())
            return;

        Mail mail = new Mail("Yo", postBox.getMail(), player.getName().getString());
        if(!PostOffice.sendMailToPlayer(message.getPlayerId(), message.getMailBoxId(), mail))
{
            player.sendSystemMessage(Component.translatable("message.cfm.mail_queue_full"));
            return;
        }

        postBox.removeMail();
    }

    public static void handleSetDoorMatMessage(ServerPlayer player, C2SMessageSetDoorMat message)
    {
        Level level = player.level();
        if(!level.isLoaded(message.getPos()))
            return;

        if(!(level.getBlockEntity(message.getPos()) instanceof DoorMatBlockEntity doorMat))
            return;

        doorMat.setMessage(message.getMessage());
    }

    public static void handleSetMailBoxNameMessage(ServerPlayer player, C2SMessageSetMailBoxName message)
    {
        Level level = player.level();
        if(!level.isLoaded(message.getPos()))
            return;

        if(!(level.getBlockEntity(message.getPos()) instanceof MailBoxBlockEntity mailBox))
            return;

        if(!player.getUUID().equals(mailBox.getOwnerId()))
            return;

        if(!mailBox.stillValid(player))
            return;

        if(!PostOffice.setMailBoxName(player.getUUID(), mailBox.getId(), message.getName()))
            return;

        BlockEntityUtil.sendUpdatePacket(mailBox);
        NetworkHooks.openScreen(player, mailBox, message.getPos());
    }

    public static void handleUpdateValueContainerMessage(ServerPlayer player, C2SMessageUpdateValueContainer message)
    {
        Level level = player.level();
        if(!level.isLoaded(message.getPos()))
            return;

        BlockEntity entity = level.getBlockEntity(message.getPos());
        if(entity instanceof IValueContainer container)
        {
            String result = container.updateEntries(message.getEntryMap(), player);
            if(result != null)
            {
                FurnitureMod.LOGGER.warn(result);
                return;
            }
            BlockEntityUtil.sendUpdatePacket(entity);
        }
    }
}
