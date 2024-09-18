package com.roxxane.create_pack_tweaks.mixins.embeddium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.FluidRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FluidRenderer.class)
public class FluidRendererMixin {
    @WrapOperation(method = "render", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/material/FluidState;getFlow(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 renderWrapOperation(FluidState instance, BlockGetter level, BlockPos pos, Operation<Vec3> original) {
        return Vec3.ZERO;
    }
}
