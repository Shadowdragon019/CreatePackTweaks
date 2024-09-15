package com.roxxane.create_pack_tweaks.mixins;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
abstract class ItemMixin {
    @Inject(method = "isFireResistant", at = @At("RETURN"), cancellable = true)
    private void isFireResistantInject(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
