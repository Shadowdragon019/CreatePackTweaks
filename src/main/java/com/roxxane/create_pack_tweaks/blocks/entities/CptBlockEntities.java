package com.roxxane.create_pack_tweaks.blocks.entities;

import com.roxxane.create_pack_tweaks.blocks.CptBlocks;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.minecraftforge.registries.ForgeRegistries;

public class CptBlockEntities {
    public static final BlockEntityEntry<FillableMoldBlockEntity> mold =
        BlockEntityEntry.cast(CptBlocks.mushyMold.getSibling(ForgeRegistries.BLOCK_ENTITY_TYPES));

    public static void register() {}
}
