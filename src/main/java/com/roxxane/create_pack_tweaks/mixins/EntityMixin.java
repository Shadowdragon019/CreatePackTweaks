package com.roxxane.create_pack_tweaks.mixins;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(Entity.class)
abstract class EntityMixin {
    @Unique
    private boolean cpt$isPlayer() {
        return (Entity) (Object) this instanceof Player;
    }

    // Disable swimming
    @Inject(method = "isSwimming", at = @At("HEAD"), cancellable = true)
    private void isSwimmingInject(CallbackInfoReturnable<Boolean> cir) {
        if (cpt$isPlayer())
            cir.setReturnValue(false);
    }

    // Nuke the hell out of liquid physics
    @Inject(method = "updateFluidHeightAndDoFluidPushing(Ljava/util/function/Predicate;)V",
        at = @At("HEAD"), cancellable = true, remap = false)
    private void updateFluidHeightAndDoFluidPushingInject(Predicate<FluidState> shouldUpdate, CallbackInfo ci) {
        if (cpt$isPlayer())
            ci.cancel();
    }

    @Inject(method = "isSprinting", at = @At("HEAD"), cancellable = true)
    private void isSprintingInject(CallbackInfoReturnable<Boolean> cir) {
        if (cpt$isPlayer())
            cir.setReturnValue(false);
    }
}
