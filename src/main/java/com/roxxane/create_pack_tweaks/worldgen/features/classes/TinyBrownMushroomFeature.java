package com.roxxane.create_pack_tweaks.worldgen.features.classes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TinyBrownMushroomFeature extends TinyFeature {
    @SuppressWarnings("DataFlowIssue")
    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        var pos = context.origin();
        var level = context.level();

        if (
            checkIsClear(level, pos.offset(-2, 3, -2), pos.offset(2, 3, 2)) &&
            checkIsClear(level, pos.offset(0, 0, 0), pos.offset(0, 2, 0))
        ) {
            var stem = Blocks.MUSHROOM_STEM.defaultBlockState()
                .setValue(BlockStateProperties.DOWN, false)
                .setValue(BlockStateProperties.UP, false);
            var mushroom = Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState()
                .setValue(BlockStateProperties.DOWN, false);

            tinySetBlock(level, pos, stem);
            tinySetBlock(level, pos.above(), stem);
            tinySetBlock(level, pos.above(2), stem);

            for (var mushroomPos : List.of(
                new BlockPos(-2, 0, -1),
                new BlockPos(-2, 0, 0),
                new BlockPos(-2, 0, 1),
                new BlockPos(-1, 0, -2),
                new BlockPos(-1, 0, -1),
                new BlockPos(-1, 0, 0),
                new BlockPos(-1, 0, 1),
                new BlockPos(-1, 0, 2),
                new BlockPos(0, 0, -2),
                new BlockPos(0, 0, -1),
                new BlockPos(0, 0, 0),
                new BlockPos(0, 0, 1),
                new BlockPos(0, 0, 2),
                new BlockPos(1, 0, -2),
                new BlockPos(1, 0, -1),
                new BlockPos(1, 0, 0),
                new BlockPos(1, 0, 1),
                new BlockPos(1, 0, 2),
                new BlockPos(2, 0, 1),
                new BlockPos(2, 0, 0),
                new BlockPos(2, 0, -1)
            ))
                tinySetBlock(level, pos.offset(mushroomPos).above(3), mushroom);

            level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE)
                .get(VegetationFeatures.PATCH_BROWN_MUSHROOM)
                .place(level, context.chunkGenerator(), context.random(), pos);

            level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE)
                .get(VegetationFeatures.PATCH_RED_MUSHROOM)
                .place(level, context.chunkGenerator(), context.random(), pos);

            return true;
        }
        return false;
    }
}