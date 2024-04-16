package com.mrcrayfish.furniture;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Author: MrCrayfish
 */
public class FurnitureConfig
{
    public static class Client
    {
        public final ForgeConfigSpec.BooleanValue drawCollisionShapes;
        public final ForgeConfigSpec.ConfigValue<List<String>> trustedUrlDomains;

        Client(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Client configuration settings").push("client");
            this.drawCollisionShapes = builder
                    .comment("Draws the collision shape rather than the selection shape when hovering blocks. Used for debugging collisions.")
                    .translation("cfm.configgui.drawCollisionShapes")
                    .define("drawCollisionShapes", false);
            this.trustedUrlDomains = builder
                    .comment("List of trusted domains to download images for the TV and Photo Frame. For example, the domain of the URL (https://i.imgur.com/Jvh1OQm.jpeg) is i.imgur.com")
                    .translation("cfm.configgui.trustedUrlDomains")
                    .define("trustedUrlDomains", new ArrayList<>(List.of("i.imgur.com")));
            builder.pop();
        }
    }

    public static class Common
    {
        public final ForgeConfigSpec.IntValue maxMailQueue;
        public final ForgeConfigSpec.IntValue pullMailInterval;

        Common(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Mail configuration settings").push("mail");
            this.maxMailQueue = builder
                    .comment("The maximum amount of mail that can be in a player's mail queue.")
                    .translation("cfm.configgui.maxMailQueue")
                    .defineInRange("maxMailQueue", 20, 1, Integer.MAX_VALUE);
            this.pullMailInterval = builder
                    .comment("The interval in ticks for mail boxes to pull mail from the player's queue")
                    .translation("cfm.configgui.pullMailInterval")
                    .defineInRange("pullMailInterval", 20, 1, Integer.MAX_VALUE);
            builder.pop();
        }
    }

    static final ForgeConfigSpec clientSpec;
    public static final FurnitureConfig.Client CLIENT;

    static final ForgeConfigSpec commonSpec;
    public static final FurnitureConfig.Common COMMON;

    static
    {
        final Pair<FurnitureConfig.Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(FurnitureConfig.Client::new);
        clientSpec = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();

        final Pair<FurnitureConfig.Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(FurnitureConfig.Common::new);
        commonSpec = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
    }
}
