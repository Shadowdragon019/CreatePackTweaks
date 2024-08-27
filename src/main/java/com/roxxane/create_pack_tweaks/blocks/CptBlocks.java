package com.roxxane.create_pack_tweaks.blocks;

import com.roxxane.create_pack_tweaks.CreatePackTweaks;
import com.roxxane.create_pack_tweaks.blocks.entities.FillableMoldBlockEntity;
import com.roxxane.create_pack_tweaks.blocks.state_properties.CptStateProperties;
import com.roxxane.create_pack_tweaks.blocks.state_properties.MaterialState;
import com.roxxane.create_pack_tweaks.items.CptItems;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.ConfiguredModel;

public class CptBlocks {
    public static final BlockEntry<FillableMoldBlock> mushyMold =
        CreatePackTweaks.registrate.block("mushy_mold", FillableMoldBlock::new)
            .item()
                .model(
                    (ctx, provider) ->
                        provider.withExistingParent(ctx.getName(),
                        provider.modLoc("block/casts/mushy/ingot/none"))
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
                                state.getValue(CptStateProperties.shape) + "/" +
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
                (lootTables, block) -> lootTables.add(
                    block,
                    lootTables.createSingleItemTable(block).withPool(
                        lootTables.applyExplosionDecay(
                            block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                                .add(
                                    LootItem.lootTableItem(CptItems.mushyBrick).when(
                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                            .setProperties(
                                                StatePropertiesPredicate.Builder.properties().hasProperty(
                                                    CptStateProperties.material, MaterialState.mushyBrick
                                                )
                                            )
                                    )
                                )
                                .add(
                                    LootItem.lootTableItem(CptItems.mushyPaste).when(
                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                            .setProperties(
                                                StatePropertiesPredicate.Builder.properties().hasProperty(
                                                    CptStateProperties.material, MaterialState.mushyPaste
                                                )
                                            )
                                    )
                                )
                        )
                    )
                )
            )
            .register();

    public static void register() {}
}