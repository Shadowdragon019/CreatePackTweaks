package com.roxxane.create_pack_tweaks.mixins.embeddium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadViewMutable;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.FluidRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(FluidRenderer.class)
public class FluidRendererMixin {
    @WrapOperation(method = "render", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/material/FluidState;getFlow(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 renderWrapOperation(FluidState instance, BlockGetter level, BlockPos pos, Operation<Vec3> original) {
        return Vec3.ZERO;
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", remap = false,
        target = "Lme/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/FluidRenderer;setVertex(Lme/jellysquid/mods/sodium/client/model/quad/ModelQuadViewMutable;IFFFFF)V"),
        slice = @Slice(from = @At(value = "INVOKE", remap = false, ordinal = 2,
            target = "Lme/jellysquid/mods/sodium/client/model/quad/ModelQuadViewMutable;setSprite(Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V"),
            to = @At(value = "INVOKE",
                target = "Lnet/minecraft/core/Direction;getAxis()Lnet/minecraft/core/Direction$Axis;")))
    private void renderWrapOperation(ModelQuadViewMutable quad, int i, float x, float y, float z, float u, float v,
        Operation<Void> original, @Local TextureAtlasSprite sprite) {
        var minU = sprite.getU(0);
        var maxU = sprite.getU(16);
        var minV = sprite.getV(0);
        var maxV = sprite.getV(16);

        if (i == 0)
            original.call(quad, i, x, y, z, minU, minV);
        else if (i == 1)
            original.call(quad, i, x, y, z, minU, maxV);
        else if (i == 2)
            original.call(quad, i, x, y, z, maxU, maxV);
        else if (i == 3)
            original.call(quad, i, x, y, z, maxU, minV);
        else
            throw new IllegalArgumentException(String.format("i should be between 0-3, was %s", i));
    }
}