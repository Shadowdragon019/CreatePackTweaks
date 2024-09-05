package com.roxxane.create_pack_tweaks.blocks;

import com.roxxane.create_pack_tweaks.Cpt;
import com.roxxane.create_pack_tweaks.blocks.entities.FillableMoldBlockEntity;
import com.roxxane.create_pack_tweaks.blocks.state_properties.CptStateProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;

public class CptBlocks {
    public static final BlockEntry<FillableMoldBlock> mushyMold =
        Cpt.registrate.block("mushy_mold", FillableMoldBlock::new)
            .item()
                .model(
                    (ctx, provider) ->
                        provider.withExistingParent(ctx.getName(),
                        provider.modLoc("block/casts/mushy/none"))
                )
                .build()
            .blockEntity(FillableMoldBlockEntity::new)
                .build()
            .blockstate(
                (ctx, provider) ->
                    provider.getVariantBuilder(ctx.getEntry()).forAllStatesExcept(
                        state -> ConfiguredModel.builder()
                            .modelFile(provider.models().getExistingFile(provider.modLoc(
                            "block/casts/mushy/" +
                                state.getValue(CptStateProperties.material)
                            )))
                            .rotationY(((int) state.getValue(
                                BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360
                            )
                            .build(),
                        BlockStateProperties.WATERLOGGED
                    )
            )
            .loot(
                (lootTables, block) -> lootTables.add(block, lootTables.createSingleItemTable(block))
            )
            .register();

    public static void register() {}
}