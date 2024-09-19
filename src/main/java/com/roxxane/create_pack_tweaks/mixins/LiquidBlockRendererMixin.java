package com.roxxane.create_pack_tweaks.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(LiquidBlockRenderer.class)
public abstract class LiquidBlockRendererMixin {
    @WrapOperation(method = "tesselate", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/material/FluidState;getFlow(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 tesselateWrapOperation(FluidState instance, BlockGetter pLevel, BlockPos pPos,
        Operation<Vec3> original)
    {
        return Vec3.ZERO;
    }

    @WrapOperation(method = "tesselate", at = @At(value = "INVOKE", remap = false,
        target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;vertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDFFFFFFI)V"),
        slice = @Slice(from = @At(value = "INVOKE", remap = false,
            target = "Lnet/minecraft/core/Direction;getAxis()Lnet/minecraft/core/Direction$Axis;")))
    private void tesselateWrapOperation(LiquidBlockRenderer instance, VertexConsumer consumer, double x, double y,
        double z, float red, float green, float blue, float alpha, float u, float v, int packedLight,
        Operation<Void> original, @Local TextureAtlasSprite sprite, @Local(name = "f53") float f53,
        @Local(name = "f33") float f33, @Local(name = "f32") float f32, @Local(name = "f34") float f34,
        @Local(name = "f35") float f35)
    {
        var minU = sprite.getU(0);
        var maxU = sprite.getU(16);
        var minV = sprite.getV(0);
        var maxV = sprite.getV(16);

        if (u == f53 && v == f33)
            original.call(instance, consumer, x, y, z, red, green, blue, alpha, minU, minV, packedLight);
        else if (u == f32 && v == f34)
            original.call(instance, consumer, x, y, z, red, green, blue, alpha, minU, maxV, packedLight);
        else if (u == f32 && v == f35)
            original.call(instance, consumer, x, y, z, red, green, blue, alpha, maxU, maxV, packedLight);
        else if (u == f53 && v == f35)
            original.call(instance, consumer, x, y, z, red, green, blue, alpha, maxU, minV, packedLight);
        else
            throw new IllegalStateException("Could not render vertex due to mismatching UV");
    }
}