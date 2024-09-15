package com.roxxane.create_pack_tweaks.mixins;

import net.minecraft.client.Camera;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
abstract class CameraMixin {
    @Inject(method = "getFluidInCamera", at = @At("HEAD"), cancellable = true)
    private void getFluidInCameraInject(CallbackInfoReturnable<FogType> cir) {
        cir.setReturnValue(FogType.NONE);
    }
}
