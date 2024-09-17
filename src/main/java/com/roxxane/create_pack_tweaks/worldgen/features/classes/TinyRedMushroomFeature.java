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
import java.util.Map;

public class TinyRedMushroomFeature extends TinyFeature {
    @SuppressWarnings("DataFlowIssue")
    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        var down = BlockStateProperties.DOWN;
        var north = BlockStateProperties.NORTH;
        var east = BlockStateProperties.EAST;
        var south = BlockStateProperties.SOUTH;
        var west = BlockStateProperties.WEST;

        var featurePos = context.origin();
        var level = context.level();

        if (checkIsClear(level, featurePos.offset(-2, 1, -2), featurePos.offset(2, 3, 2)) &&
            checkIsClear(level, featurePos, featurePos.above(2)))
        {
            var stem = Blocks.MUSHROOM_STEM.defaultBlockState()
                .setValue(down, false)
                .setValue(BlockStateProperties.UP, false);

            tinySetBlock(level, featurePos, stem);
            tinySetBlock(level, featurePos.above(), stem);
            tinySetBlock(level, featurePos.above(2), stem);

            for (var entry : Map.ofEntries(
                // Top
                Map.entry(new BlockPos(-1, 2, -1), List.of(down)),
                Map.entry(new BlockPos(-1, 2, 0), List.of(down)),
                Map.entry(new BlockPos(-1, 2, 1), List.of(down)),
                Map.entry(new BlockPos(0, 2, -1), List.of(down)),
                Map.entry(new BlockPos(0, 2, 0), List.of(down)),
                Map.entry(new BlockPos(0, 2, 1), List.of(down)),
                Map.entry(new BlockPos(1, 2, -1), List.of(down)),
                Map.entry(new BlockPos(1, 2, 0), List.of(down)),
                Map.entry(new BlockPos(1, 2, 1), List.of(down)),
                // West
                Map.entry(new BlockPos(2, 0, -1), List.of(west, down)),
                Map.entry(new BlockPos(2, 0, 0), List.of(west, down)),
                Map.entry(new BlockPos(2, 0, 1), List.of(west, down)),
                Map.entry(new BlockPos(2, 1, -1), List.of(west)),
                Map.entry(new BlockPos(2, 1, 0), List.of(west)),
                Map.entry(new BlockPos(2, 1, 1), List.of(west)),
                // East
                Map.entry(new BlockPos(-2, 0, -1), List.of(east, down)),
                Map.entry(new BlockPos(-2, 0, 0), List.of(east, down)),
                Map.entry(new BlockPos(-2, 0, 1), List.of(east, down)),
                Map.entry(new BlockPos(-2, 1, -1), List.of(east)),
                Map.entry(new BlockPos(-2, 1, 0), List.of(east)),
                Map.entry(new BlockPos(-2, 1, 1), List.of(east)),
                // North
                Map.entry(new BlockPos(-1, 0, 2), List.of(north, down)),
                Map.entry(new BlockPos(0, 0, 2), List.of(north, down)),
                Map.entry(new BlockPos(1, 0, 2), List.of(north, down)),
                Map.entry(new BlockPos(-1, 1, 2), List.of(north)),
                Map.entry(new BlockPos(0, 1, 2), List.of(north)),
                Map.entry(new BlockPos(1, 1, 2), List.of(north)),
                // South
                Map.entry(new BlockPos(-1, 0, -2), List.of(south, down)),
                Map.entry(new BlockPos(0, 0, -2), List.of(south, down)),
                Map.entry(new BlockPos(1, 0, -2), List.of(south, down)),
                Map.entry(new BlockPos(-1, 1, -2), List.of(south)),
                Map.entry(new BlockPos(0, 1, -2), List.of(south)),
                Map.entry(new BlockPos(1, 1, -2), List.of(south))
            ).entrySet()) {
                var pos = entry.getKey();
                var properties = entry.getValue();
                var mushroom = Blocks.RED_MUSHROOM_BLOCK.defaultBlockState();

                for (var property : properties)
                    mushroom = mushroom.setValue(property, false);

                tinySetBlock(level, featurePos.offset(pos).above(1), mushroom);
            }

            level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE)
                .get(VegetationFeatures.PATCH_RED_MUSHROOM)
                .place(level, context.chunkGenerator(), context.random(), featurePos);

            level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE)
                .get(VegetationFeatures.PATCH_BROWN_MUSHROOM)
                .place(level, context.chunkGenerator(), context.random(), featurePos);

            return true;
        }

        return false;
    }
}
