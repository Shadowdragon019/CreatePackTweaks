package com.roxxane.create_pack_tweaks.mixins;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
abstract class PlayerMixin {
    @Unique
    private Player cpt$self() { return (Player) (Object) this;}

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickInject(CallbackInfo ci) {
        if (!cpt$self().isCrouching())
            cpt$self().setMaxUpStep(2);
        else
            cpt$self().setMaxUpStep(0.5f);
    }

    /*
    @Redirect(method = "causeFallDamage", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Abilities;mayfly:Z"))
    private boolean causeFallDamageRedirect(Abilities instance) {

    }
    */
}
