package com.roxxane.create_pack_tweaks.worldgen.features.classes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TinyTreeFeature extends TinyFeature {
    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        var pos = context.origin();
        var level = context.level();
        var log = Blocks.OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
        var leaves = Blocks.OAK_LEAVES.defaultBlockState().setValue(BlockStateProperties.DISTANCE, 1);
        var random = context.random().nextInt(0, 3);

        if (checkIsClear(level, pos, pos.above(2)) && (
            ((random == 0 || random == 1) && checkIsClear(level, pos.offset(-1, 1, -1), pos.offset(1, 3, 1))) ||
            (random == 2 && checkIsClear(level, pos.offset(-2, 1, -2), pos.offset(2, 3, 2)))
        )) {

            tinySetBlock(level, pos, log);
            tinySetBlock(level, pos.above(), log);
            tinySetBlock(level, pos.above(2), log);

            var leavesPoses = new ArrayList<>(List.of(
                new BlockPos(0, 0, 1),
                new BlockPos(0, 0, -1),
                new BlockPos(1, 0, 0),
                new BlockPos(-1, 0, 0),
                new BlockPos(0, 1, 1),
                new BlockPos(0, 1, -1),
                new BlockPos(1, 1, 0),
                new BlockPos(-1, 1, 0),
                new BlockPos(0, 2, 0)
            ));

            if (random >= 1) {
                leavesPoses.add(new BlockPos(-1, 0, -1));
                leavesPoses.add(new BlockPos(-1, 1, -1));
                leavesPoses.add(new BlockPos(1, 0, 1));
                leavesPoses.add(new BlockPos(1, 1, 1));
                leavesPoses.add(new BlockPos(-1, 0, 1));
                leavesPoses.add(new BlockPos(-1, 1, 1));
                leavesPoses.add(new BlockPos(1, 0, -1));
                leavesPoses.add(new BlockPos(1, 1, -1));
                leavesPoses.add(new BlockPos(1, 2, 0));
                leavesPoses.add(new BlockPos(0, 2, 1));
                leavesPoses.add(new BlockPos(-1, 2, 0));
                leavesPoses.add(new BlockPos(0, 2, -1));
            }

            if (random == 2) {
                leavesPoses.add(new BlockPos(2, 0, 0));
                leavesPoses.add(new BlockPos(2, 1, 0));
                leavesPoses.add(new BlockPos(2, 0, -1));
                leavesPoses.add(new BlockPos(2, 0, 1));
                leavesPoses.add(new BlockPos(-2, 0, 0));
                leavesPoses.add(new BlockPos(-2, 1, 0));
                leavesPoses.add(new BlockPos(-2, 0, -1));
                leavesPoses.add(new BlockPos(-2, 0, 1));
                leavesPoses.add(new BlockPos(0, 0, 2));
                leavesPoses.add(new BlockPos(0, 1, 2));
                leavesPoses.add(new BlockPos(-1, 0, 2));
                leavesPoses.add(new BlockPos(1, 0, 2));
                leavesPoses.add(new BlockPos(0, 0, -2));
                leavesPoses.add(new BlockPos(0, 1, -2));
                leavesPoses.add(new BlockPos(-1, 0, -2));
                leavesPoses.add(new BlockPos(1, 0, -2));
            }

            for (var leavesPos : leavesPoses)
                tinySetBlock(level, pos.offset(leavesPos.above()), leaves);

            return true;
        }
        return false;
    }
}