package com.roxxane.create_pack_tweaks.mixins;

import com.roxxane.create_pack_tweaks.CptConfig;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Unique
    private Item createPackTweaks$getSelf() {
        return (Item) (Object) this;
    }

    @Inject(method = "isFireResistant", at = @At("RETURN"), cancellable = true)
    private void isFireResistantInject(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() ||
            (CptConfig.isLoaded() &&
            (CptConfig.lavaSmelting.containsKey(createPackTweaks$getSelf()) ||
                CptConfig.lavaSmelting.containsValue(createPackTweaks$getSelf())
            ))
        );
    }
}
