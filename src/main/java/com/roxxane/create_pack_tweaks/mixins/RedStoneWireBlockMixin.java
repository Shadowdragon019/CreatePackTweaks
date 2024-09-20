package com.roxxane.create_pack_tweaks.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.roxxane.create_pack_tweaks.CptConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RedStoneWireBlock.class)
public class RedStoneWireBlockMixin {
    @Unique
    private static Vec3 cpt$modifyColor(int power, Vec3 color) {
        for (var element : CptConfig.redstoneColorMultipliers)
            if (power >= element.b() && power <= element.c())
                color = color.multiply(element.a(), element.a(), element.a());
        return color;
    }

    @WrapOperation(method = "getColorForPower", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/util/Mth;color(FFF)I"))
    private static int getColorForPowerWrapOperation(float r, float g, float b, Operation<Integer> original,
        @Local(argsOnly = true) int power, @Local Vec3 color)
    {
        color = cpt$modifyColor(power, color);
        return Mth.color((float) color.x, (float) color.y, (float) color.z);
    }

    @WrapOperation(method = "animateTick", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/block/RedStoneWireBlock;spawnParticlesAlongLine(Lnet/minecraft/world/level/Level;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/core/Direction;Lnet/minecraft/core/Direction;FF)V"))
    private void animateTickWrapOperation(RedStoneWireBlock instance, Level level, RandomSource random, BlockPos pos,
        Vec3 color, Direction directionA, Direction directionB, float min, float nax, Operation<Void> original,
        @Local int power)
    {
        original.call(instance, level, random, pos, cpt$modifyColor(power, color), directionA, directionB, min, nax);
    }
}
