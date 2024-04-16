package com.mrcrayfish.furniture.core;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.tileentity.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class ModBlockEntities
{
    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Reference.MOD_ID);

    public static final RegistryObject<BlockEntityType<CabinetBlockEntity>> CABINET = register("cabinet", CabinetBlockEntity::new, () -> new Block[]{ModBlocks.CABINET_OAK.get(), ModBlocks.CABINET_SPRUCE.get(), ModBlocks.CABINET_BIRCH.get(), ModBlocks.CABINET_JUNGLE.get(), ModBlocks.CABINET_ACACIA.get(), ModBlocks.CABINET_DARK_OAK.get(), ModBlocks.CABINET_MANGROVE.get(), ModBlocks.CABINET_STRIPPED_OAK.get(), ModBlocks.CABINET_STRIPPED_SPRUCE.get(), ModBlocks.CABINET_STRIPPED_BIRCH.get(), ModBlocks.CABINET_STRIPPED_JUNGLE.get(), ModBlocks.CABINET_STRIPPED_ACACIA.get(), ModBlocks.CABINET_STRIPPED_DARK_OAK.get(), ModBlocks.CABINET_STRIPPED_MANGROVE.get(), ModBlocks.CABINET_WARPED.get(), ModBlocks.CABINET_STRIPPED_WARPED.get(), ModBlocks.CABINET_CRIMSON.get(), ModBlocks.CABINET_STRIPPED_CRIMSON.get()});
    public static final RegistryObject<BlockEntityType<BedsideCabinetBlockEntity>> BEDSIDE_CABINET = register("bedside_cabinet", BedsideCabinetBlockEntity::new, () -> new Block[]{ModBlocks.BEDSIDE_CABINET_OAK.get(), ModBlocks.BEDSIDE_CABINET_SPRUCE.get(), ModBlocks.BEDSIDE_CABINET_BIRCH.get(), ModBlocks.BEDSIDE_CABINET_JUNGLE.get(), ModBlocks.BEDSIDE_CABINET_ACACIA.get(), ModBlocks.BEDSIDE_CABINET_DARK_OAK.get(), ModBlocks.BEDSIDE_CABINET_MANGROVE.get(), ModBlocks.BEDSIDE_CABINET_STRIPPED_OAK.get(), ModBlocks.BEDSIDE_CABINET_STRIPPED_SPRUCE.get(), ModBlocks.BEDSIDE_CABINET_STRIPPED_BIRCH.get(), ModBlocks.BEDSIDE_CABINET_STRIPPED_JUNGLE.get(), ModBlocks.BEDSIDE_CABINET_STRIPPED_ACACIA.get(), ModBlocks.BEDSIDE_CABINET_STRIPPED_DARK_OAK.get(), ModBlocks.BEDSIDE_CABINET_STRIPPED_MANGROVE.get(), ModBlocks.BEDSIDE_CABINET_WARPED.get(), ModBlocks.BEDSIDE_CABINET_STRIPPED_WARPED.get(), ModBlocks.BEDSIDE_CABINET_CRIMSON.get(), ModBlocks.BEDSIDE_CABINET_STRIPPED_CRIMSON.get()});
    public static final RegistryObject<BlockEntityType<DeskCabinetBlockEntity>> DESK_CABINET = register("desk_cabinet", DeskCabinetBlockEntity::new, () -> new Block[]{ModBlocks.DESK_CABINET_OAK.get(), ModBlocks.DESK_CABINET_SPRUCE.get(), ModBlocks.DESK_CABINET_BIRCH.get(), ModBlocks.DESK_CABINET_JUNGLE.get(), ModBlocks.DESK_CABINET_ACACIA.get(), ModBlocks.DESK_CABINET_DARK_OAK.get(), ModBlocks.DESK_CABINET_MANGROVE.get(), ModBlocks.DESK_CABINET_STRIPPED_OAK.get(), ModBlocks.DESK_CABINET_STRIPPED_SPRUCE.get(), ModBlocks.DESK_CABINET_STRIPPED_BIRCH.get(), ModBlocks.DESK_CABINET_STRIPPED_JUNGLE.get(), ModBlocks.DESK_CABINET_STRIPPED_ACACIA.get(), ModBlocks.DESK_CABINET_STRIPPED_DARK_OAK.get(), ModBlocks.DESK_CABINET_STRIPPED_MANGROVE.get(), ModBlocks.DESK_CABINET_WARPED.get(), ModBlocks.DESK_CABINET_STRIPPED_WARPED.get(), ModBlocks.DESK_CABINET_CRIMSON.get(), ModBlocks.DESK_CABINET_STRIPPED_CRIMSON.get()});
    public static final RegistryObject<BlockEntityType<CrateBlockEntity>> CRATE = register("crate", CrateBlockEntity::new, () -> new Block[]{ModBlocks.CRATE_OAK.get(), ModBlocks.CRATE_SPRUCE.get(), ModBlocks.CRATE_BIRCH.get(), ModBlocks.CRATE_JUNGLE.get(), ModBlocks.CRATE_ACACIA.get(), ModBlocks.CRATE_DARK_OAK.get(), ModBlocks.CRATE_MANGROVE.get(), ModBlocks.CRATE_WARPED.get(), ModBlocks.CRATE_STRIPPED_WARPED.get(), ModBlocks.CRATE_CRIMSON.get(), ModBlocks.CRATE_STRIPPED_CRIMSON.get()});
    public static final RegistryObject<BlockEntityType<MailBoxBlockEntity>> MAIL_BOX = register("mail_box", MailBoxBlockEntity::new, () -> new Block[]{ModBlocks.MAIL_BOX_OAK.get(), ModBlocks.MAIL_BOX_SPRUCE.get(), ModBlocks.MAIL_BOX_BIRCH.get(), ModBlocks.MAIL_BOX_JUNGLE.get(), ModBlocks.MAIL_BOX_ACACIA.get(), ModBlocks.MAIL_BOX_DARK_OAK.get(), ModBlocks.MAIL_BOX_MANGROVE.get(), ModBlocks.MAIL_BOX_STRIPPED_OAK.get(), ModBlocks.MAIL_BOX_STRIPPED_SPRUCE.get(), ModBlocks.MAIL_BOX_STRIPPED_BIRCH.get(), ModBlocks.MAIL_BOX_STRIPPED_JUNGLE.get(), ModBlocks.MAIL_BOX_STRIPPED_ACACIA.get(), ModBlocks.MAIL_BOX_STRIPPED_DARK_OAK.get(), ModBlocks.MAIL_BOX_STRIPPED_MANGROVE.get(), ModBlocks.MAIL_BOX_WARPED.get(), ModBlocks.MAIL_BOX_STRIPPED_WARPED.get(), ModBlocks.MAIL_BOX_CRIMSON.get(), ModBlocks.MAIL_BOX_STRIPPED_CRIMSON.get()});
    public static final RegistryObject<BlockEntityType<TrampolineBlockEntity>> TRAMPOLINE = register("trampoline", TrampolineBlockEntity::new, () -> new Block[]{ModBlocks.TRAMPOLINE.get()});
    public static final RegistryObject<BlockEntityType<CoolerBlockEntity>> COOLER = register("cooler", CoolerBlockEntity::new, () -> new Block[]{ModBlocks.COOLER_WHITE.get(), ModBlocks.COOLER_ORANGE.get(), ModBlocks.COOLER_MAGENTA.get(), ModBlocks.COOLER_LIGHT_BLUE.get(), ModBlocks.COOLER_YELLOW.get(), ModBlocks.COOLER_LIME.get(), ModBlocks.COOLER_PINK.get(), ModBlocks.COOLER_GRAY.get(), ModBlocks.COOLER_LIGHT_GRAY.get(), ModBlocks.COOLER_CYAN.get(), ModBlocks.COOLER_PURPLE.get(), ModBlocks.COOLER_BLUE.get(), ModBlocks.COOLER_BROWN.get(), ModBlocks.COOLER_GREEN.get(), ModBlocks.COOLER_RED.get(), ModBlocks.COOLER_BLACK.get()});
    public static final RegistryObject<BlockEntityType<GrillBlockEntity>> GRILL = register("grill", GrillBlockEntity::new, () -> new Block[]{ModBlocks.GRILL_WHITE.get(), ModBlocks.GRILL_ORANGE.get(), ModBlocks.GRILL_MAGENTA.get(), ModBlocks.GRILL_LIGHT_BLUE.get(), ModBlocks.GRILL_YELLOW.get(), ModBlocks.GRILL_LIME.get(), ModBlocks.GRILL_PINK.get(), ModBlocks.GRILL_GRAY.get(), ModBlocks.GRILL_LIGHT_GRAY.get(), ModBlocks.GRILL_CYAN.get(), ModBlocks.GRILL_PURPLE.get(), ModBlocks.GRILL_BLUE.get(), ModBlocks.GRILL_BROWN.get(), ModBlocks.GRILL_GREEN.get(), ModBlocks.GRILL_RED.get(), ModBlocks.GRILL_BLACK.get()});
    public static final RegistryObject<BlockEntityType<DoorMatBlockEntity>> DOOR_MAT = register("door_mat", DoorMatBlockEntity::new, () -> new Block[]{ModBlocks.DOOR_MAT.get()});
    public static final RegistryObject<BlockEntityType<KitchenDrawerBlockEntity>> KITCHEN_DRAWER = register("kitchen_drawer", KitchenDrawerBlockEntity::new, () -> new Block[]{ModBlocks.KITCHEN_DRAWER_OAK.get(), ModBlocks.KITCHEN_DRAWER_SPRUCE.get(), ModBlocks.KITCHEN_DRAWER_BIRCH.get(), ModBlocks.KITCHEN_DRAWER_JUNGLE.get(), ModBlocks.KITCHEN_DRAWER_ACACIA.get(), ModBlocks.KITCHEN_DRAWER_DARK_OAK.get(), ModBlocks.KITCHEN_DRAWER_MANGROVE.get(), ModBlocks.KITCHEN_DRAWER_STRIPPED_OAK.get(), ModBlocks.KITCHEN_DRAWER_STRIPPED_SPRUCE.get(), ModBlocks.KITCHEN_DRAWER_STRIPPED_BIRCH.get(), ModBlocks.KITCHEN_DRAWER_STRIPPED_JUNGLE.get(), ModBlocks.KITCHEN_DRAWER_STRIPPED_ACACIA.get(), ModBlocks.KITCHEN_DRAWER_STRIPPED_DARK_OAK.get(), ModBlocks.KITCHEN_DRAWER_STRIPPED_MANGROVE.get(), ModBlocks.KITCHEN_DRAWER_WARPED.get(), ModBlocks.KITCHEN_DRAWER_STRIPPED_WARPED.get(), ModBlocks.KITCHEN_DRAWER_CRIMSON.get(), ModBlocks.KITCHEN_DRAWER_STRIPPED_CRIMSON.get(), ModBlocks.KITCHEN_DRAWER_WHITE.get(), ModBlocks.KITCHEN_DRAWER_ORANGE.get(), ModBlocks.KITCHEN_DRAWER_MAGENTA.get(), ModBlocks.KITCHEN_DRAWER_LIGHT_BLUE.get(), ModBlocks.KITCHEN_DRAWER_YELLOW.get(), ModBlocks.KITCHEN_DRAWER_LIME.get(), ModBlocks.KITCHEN_DRAWER_PINK.get(), ModBlocks.KITCHEN_DRAWER_GRAY.get(), ModBlocks.KITCHEN_DRAWER_LIGHT_GRAY.get(), ModBlocks.KITCHEN_DRAWER_CYAN.get(), ModBlocks.KITCHEN_DRAWER_PURPLE.get(), ModBlocks.KITCHEN_DRAWER_BLUE.get(), ModBlocks.KITCHEN_DRAWER_BROWN.get(), ModBlocks.KITCHEN_DRAWER_GREEN.get(), ModBlocks.KITCHEN_DRAWER_RED.get(), ModBlocks.KITCHEN_DRAWER_BLACK.get()});
    public static final RegistryObject<BlockEntityType<KitchenSinkBlockEntity>> KITCHEN_SINK = register("kitchen_sink", KitchenSinkBlockEntity::new, () -> new Block[]{ModBlocks.KITCHEN_SINK_LIGHT_OAK.get(), ModBlocks.KITCHEN_SINK_LIGHT_SPRUCE.get(), ModBlocks.KITCHEN_SINK_LIGHT_BIRCH.get(), ModBlocks.KITCHEN_SINK_LIGHT_JUNGLE.get(), ModBlocks.KITCHEN_SINK_LIGHT_ACACIA.get(), ModBlocks.KITCHEN_SINK_LIGHT_DARK_OAK.get(), ModBlocks.KITCHEN_SINK_LIGHT_CRIMSON.get(), ModBlocks.KITCHEN_SINK_LIGHT_WARPED.get(), ModBlocks.KITCHEN_SINK_LIGHT_MANGROVE.get(), ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_OAK.get(), ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_SPRUCE.get(), ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_BIRCH.get(), ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_JUNGLE.get(), ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_ACACIA.get(), ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_DARK_OAK.get(), ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_CRIMSON.get(), ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_WARPED.get(), ModBlocks.KITCHEN_SINK_LIGHT_STRIPPED_MANGROVE.get(), ModBlocks.KITCHEN_SINK_DARK_OAK.get(), ModBlocks.KITCHEN_SINK_DARK_SPRUCE.get(), ModBlocks.KITCHEN_SINK_DARK_BIRCH.get(), ModBlocks.KITCHEN_SINK_DARK_JUNGLE.get(), ModBlocks.KITCHEN_SINK_DARK_ACACIA.get(), ModBlocks.KITCHEN_SINK_DARK_DARK_OAK.get(), ModBlocks.KITCHEN_SINK_DARK_CRIMSON.get(), ModBlocks.KITCHEN_SINK_DARK_WARPED.get(), ModBlocks.KITCHEN_SINK_DARK_MANGROVE.get(), ModBlocks.KITCHEN_SINK_DARK_STRIPPED_OAK.get(), ModBlocks.KITCHEN_SINK_DARK_STRIPPED_SPRUCE.get(), ModBlocks.KITCHEN_SINK_DARK_STRIPPED_BIRCH.get(), ModBlocks.KITCHEN_SINK_DARK_STRIPPED_JUNGLE.get(), ModBlocks.KITCHEN_SINK_DARK_STRIPPED_ACACIA.get(), ModBlocks.KITCHEN_SINK_DARK_STRIPPED_DARK_OAK.get(), ModBlocks.KITCHEN_SINK_DARK_STRIPPED_CRIMSON.get(), ModBlocks.KITCHEN_SINK_DARK_STRIPPED_WARPED.get(), ModBlocks.KITCHEN_SINK_DARK_STRIPPED_MANGROVE.get(), ModBlocks.KITCHEN_SINK_WHITE.get(), ModBlocks.KITCHEN_SINK_ORANGE.get(), ModBlocks.KITCHEN_SINK_MAGENTA.get(), ModBlocks.KITCHEN_SINK_LIGHT_BLUE.get(), ModBlocks.KITCHEN_SINK_YELLOW.get(), ModBlocks.KITCHEN_SINK_LIME.get(), ModBlocks.KITCHEN_SINK_PINK.get(), ModBlocks.KITCHEN_SINK_GRAY.get(), ModBlocks.KITCHEN_SINK_LIGHT_GRAY.get(), ModBlocks.KITCHEN_SINK_CYAN.get(), ModBlocks.KITCHEN_SINK_PURPLE.get(), ModBlocks.KITCHEN_SINK_BLUE.get(), ModBlocks.KITCHEN_SINK_BROWN.get(), ModBlocks.KITCHEN_SINK_GREEN.get(), ModBlocks.KITCHEN_SINK_RED.get(), ModBlocks.KITCHEN_SINK_BLACK.get()});
    public static final RegistryObject<BlockEntityType<FridgeBlockEntity>> FRIDGE = register("fridge", FridgeBlockEntity::new, () -> new Block[]{ModBlocks.FRIDGE_LIGHT.get(), ModBlocks.FRIDGE_DARK.get()});
    public static final RegistryObject<BlockEntityType<FreezerBlockEntity>> FREEZER = register("freezer", FreezerBlockEntity::new, () -> new Block[]{ModBlocks.FREEZER_LIGHT.get(), ModBlocks.FREEZER_DARK.get()});
    public static final RegistryObject<BlockEntityType<PhotoFrameBlockEntity>> PHOTO_FRAME = register("photo_frame", PhotoFrameBlockEntity::new, () -> new Block[]{ModBlocks.PHOTO_FRAME_WHITE.get(), ModBlocks.PHOTO_FRAME_ORANGE.get(), ModBlocks.PHOTO_FRAME_MAGENTA.get(), ModBlocks.PHOTO_FRAME_LIGHT_BLUE.get(), ModBlocks.PHOTO_FRAME_YELLOW.get(), ModBlocks.PHOTO_FRAME_LIME.get(), ModBlocks.PHOTO_FRAME_PINK.get(), ModBlocks.PHOTO_FRAME_GRAY.get(), ModBlocks.PHOTO_FRAME_LIGHT_GRAY.get(), ModBlocks.PHOTO_FRAME_CYAN.get(), ModBlocks.PHOTO_FRAME_PURPLE.get(), ModBlocks.PHOTO_FRAME_BLUE.get(), ModBlocks.PHOTO_FRAME_BROWN.get(), ModBlocks.PHOTO_FRAME_GREEN.get(), ModBlocks.PHOTO_FRAME_RED.get(), ModBlocks.PHOTO_FRAME_BLACK.get()});

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<Block[]> validBlocksSupplier)
    {
        //Type<?> type = Util.attemptDataFix(TypeReferences.BLOCK_ENTITY, name);
        return REGISTER.register(name, () -> BlockEntityType.Builder.of(supplier, validBlocksSupplier.get()).build(null)); //Null until someone can explain data fixers
    }
}
