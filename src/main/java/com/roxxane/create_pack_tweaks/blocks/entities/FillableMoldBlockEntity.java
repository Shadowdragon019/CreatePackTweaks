package com.roxxane.create_pack_tweaks.blocks.entities;

import com.roxxane.create_pack_tweaks.blocks.state_properties.CptStateProperties;
import com.roxxane.create_pack_tweaks.blocks.state_properties.MaterialState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class FillableMoldBlockEntity extends BlockEntity {
    public static Map<MaterialState, MaterialState> heatingMap = Map.of(
        MaterialState.mushyPaste, MaterialState.mushyBrick
    );
    public static Map<MaterialState, MaterialState> coolingMap = Map.of();
    public int progress = 0;
    public int lastProgress = 0;

    public FillableMoldBlockEntity(
        BlockEntityType<? extends FillableMoldBlockEntity> type,
        BlockPos pPos,
        BlockState pBlockState) {
        super(type, pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("mold.progress", progress);
        tag.putInt("mold.last_progress", lastProgress);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        progress = tag.getInt("mold.progress");
        lastProgress = tag.getInt("mold.last_progress");
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        // If heated
        if (progress >= 200)
            for (var heatingEntry : heatingMap.entrySet())
                if (state.getValue(CptStateProperties.material) == heatingEntry.getKey()) {
                    level.setBlock(pos,
                        state.setValue(CptStateProperties.material, heatingEntry.getValue()),
                        1 | 2);
                    progress = 0;
                }
        // If cooled
        else if (progress <= -200)
            for (var coolingEntry : coolingMap.entrySet())
                if (state.getValue(CptStateProperties.material) == coolingEntry.getKey()) {
                    level.setBlock(pos,
                        state.setValue(CptStateProperties.material, coolingEntry.getValue()),
                        1 | 2);
                    progress = 0;
                }

        // Prevent players from storing progress
        if (state.getValue(CptStateProperties.material) == MaterialState.none)
            progress = 0;

        // Reset if no progress is being made && prevent players from storing progress
        if (progress == lastProgress)
            progress = 0;

        lastProgress = progress;
    }
}