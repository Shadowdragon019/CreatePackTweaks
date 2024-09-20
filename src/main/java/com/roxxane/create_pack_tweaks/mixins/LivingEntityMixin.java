package com.roxxane.create_pack_tweaks.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeLivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity implements Attackable, IForgeLivingEntity {
    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Unique
    private boolean cpt$isPlayer() {
        return (LivingEntity) (Object) this instanceof Player;
    }

    @ModifyConstant(method = "getJumpPower", constant = @Constant(floatValue = 0.42f))
    private float jumpFromGroundModifyConstant(float original) {
        if (cpt$isPlayer())
            return 0.85f;
        else
            return original;
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;onGround()Z", ordinal = 2))
    private boolean travelRedirect(LivingEntity instance, @Local(name = "f2") float f2) {
        if (cpt$isPlayer())
            return true;
        else
            return onGround();
    }

    @Redirect(method = "getFrictionInfluencedSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;onGround()Z"))
    private boolean getFrictionInfluencedSpeedRedirect(LivingEntity instance) {
        if (cpt$isPlayer())
            return true;
        else
            return onGround();
    }

    @ModifyConstant(method = "aiStep", constant = @Constant(intValue = 10))
    private int aiStepModifyConstant_removeJumpDelay(int original) {
        if (cpt$isPlayer())
            return 0;
        else
            return original;
    }

    @ModifyVariable(method = "setSprinting", at = @At("HEAD"), argsOnly = true)
    private boolean setSprintingModifyVariable(boolean original) {
        return false;
    }
}
