package com.roxxane.create_pack_tweaks.worldgen.features.classes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

public abstract class TinyFeature extends Feature<NoneFeatureConfiguration> {
    public TinyFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public void tinySetBlock(@NotNull WorldGenLevel level, @NotNull BlockPos pos, @NotNull BlockState state) {
        if (level.getBlockState(pos).isAir()) {
            level.setBlock(pos, state, 1 | 2);
        }
    }

    public boolean checkIsClear(WorldGenLevel level, BlockPos start, BlockPos end) {
        if (end.getY() >= level.getHeight()) return false;
        for (int x = start.getX(); x <= end.getX(); x++)
            for (int y = start.getY(); y <= end.getY(); y++)
                for (int z = start.getZ(); z <= end.getZ(); z++)
                    if (!level.getBlockState(new BlockPos(x, y, z)).isAir())
                        return false;
        return true;
    }
}