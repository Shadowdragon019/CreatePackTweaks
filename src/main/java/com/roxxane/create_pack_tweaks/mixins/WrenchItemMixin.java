package com.roxxane.create_pack_tweaks.mixins;

import com.simibubi.create.content.equipment.wrench.WrenchItem;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WrenchItem.class)
public class WrenchItemMixin {
    @Inject(method = "canWrenchPickup", at = @At("RETURN"), cancellable = true, remap = false)
    private void canWrenchPickupInject(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(state.getBlock().defaultDestroyTime() > -1);
    }
}
