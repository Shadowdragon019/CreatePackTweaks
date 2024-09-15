package com.roxxane.create_pack_tweaks.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {
    @ModifyConstant(method = "getJumpPower", constant = @Constant(floatValue = 0.42f))
    private float jumpFromGroundModifyConstant(float original) {
        if (((LivingEntity) (Object) this) instanceof Player)
            return 0.85f;
        else
            return original;
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;onGround()Z", ordinal = 2))
    private boolean travelRedirect(LivingEntity instance, @Local(name = "f2") float f2) {
        return true;
    }

    @Redirect(method = "getFrictionInfluencedSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;onGround()Z"))
    private boolean getFrictionInfluencedSpeedRedirect(LivingEntity instance) {
        return true;
    }
}
