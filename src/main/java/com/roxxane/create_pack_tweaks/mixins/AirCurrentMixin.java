package com.roxxane.create_pack_tweaks.mixins;

import com.roxxane.create_pack_tweaks.blocks.entities.FillableMoldBlockEntity;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import com.simibubi.create.content.kinetics.fan.processing.AllFanProcessingTypes;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AirCurrent.class)
abstract class AirCurrentMixin {
    @Shadow(remap = false) public AABB bounds;

    @Shadow(remap = false) public Direction direction;

    @Shadow(remap = false) public abstract FanProcessingType getTypeAt(float offset);

    @Unique
    private void createPackTweaks$spawnParticle(Level level, BlockPos pos, ParticleOptions type) {
        if (level.random.nextInt(0, 5) == 0)
            level.addParticle(type,
                pos.getX() + level.random.nextDouble(),
                pos.getY() + 0.5,
                pos.getZ() + level.random.nextDouble(),
                0, 1 / 16f, 0);
    }

    @Inject(method = "tickAffectedEntities", at = @At("HEAD"), remap = false)
    private void tickAffectedHandlersInject(Level level, CallbackInfo ci) {
        var start = new BlockPos((int) bounds.minX, (int) bounds.minY, (int) bounds.minZ);

        // bounds can occasionally be (0 0 0) (0 0 0)
        // if we didn't implement this check it'd try to place 100k+ blocks from the fan's blockpos to 0 0 0
        if ((int) bounds.getXsize() != 0 && (int) bounds.getYsize() != 0 && (int) bounds.getZsize() != 0)
            for (var pos : BlockPos.betweenClosed(start,
                new BlockPos((int) bounds.maxX, (int) bounds.maxY, (int) bounds.maxZ).offset(-1, -1, -1)
            )) {
                var blockEntity = level.getBlockEntity(pos);

                if (blockEntity instanceof FillableMoldBlockEntity) {
                    var distanceFromFan = VecHelper.alignedDistanceToFace(
                        new Vec3(start.getX(), start.getY(), start.getZ()), pos, direction);
                    var type = getTypeAt((float) distanceFromFan);

                    if (type == AllFanProcessingTypes.SMOKING || type == AllFanProcessingTypes.BLASTING) {
                        ((FillableMoldBlockEntity) blockEntity).progress++;
                        createPackTweaks$spawnParticle(level, pos, ParticleTypes.SMOKE);
                    } else if (type == AllFanProcessingTypes.SPLASHING) {
                        ((FillableMoldBlockEntity) blockEntity).progress--;
                        createPackTweaks$spawnParticle(level, pos, ParticleTypes.CAMPFIRE_COSY_SMOKE);
                    }
                }
            }
    }
}