package com.roxxane.create_pack_tweaks.mixins;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LocalPlayer.class)
abstract class LocalPlayerMixin {
    @ModifyVariable(method = "aiStep", at = @At("STORE"))
    private float aiStepModifyVariable_modifyCrouchMovementMultiplier(float original) {
        return 0.333333f;
    }
}
