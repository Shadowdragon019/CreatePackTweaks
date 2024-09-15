package com.roxxane.create_pack_tweaks.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MushroomBlock.class)
abstract class MushroomBlockMixin extends BushBlock implements BonemealableBlock {
    public MushroomBlockMixin(Properties pProperties) {
        super(pProperties);
    }

    @Shadow @Final private ResourceKey<ConfiguredFeature<?, ?>> feature;

    @Shadow public abstract boolean canSurvive(
        @NotNull BlockState state,
        @NotNull LevelReader level,
        @NotNull BlockPos pos
    );

    /**
     * @author Roxxane
     * @reason Completely new tick mechanic
     */
    @SuppressWarnings({"deprecation", "DataFlowIssue"})
    @Overwrite
    public void randomTick(
        @NotNull BlockState state,
        ServerLevel level,
        @NotNull BlockPos pos,
        @NotNull RandomSource random
    ) {
        if (level.isAreaLoaded(pos, 1) && random.nextInt(7) == 0) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 1 | 3);
            if (
                !level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).get(feature)
                    .place(level, level.getChunkSource()
                        .getGenerator(), random, pos)
            )
                level.setBlock(pos, state, 1 | 3);
        }
    }
}
