package com.roxxane.create_pack_tweaks.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.equipment.wrench.WrenchItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WrenchItem.class)
abstract class WrenchItemMixin {
    @Redirect(method = "useOn", at = @At(value = "INVOKE", remap = false,
        target = "Lcom/simibubi/create/content/equipment/wrench/WrenchItem;canWrenchPickup(Lnet/minecraft/world/level/block/state/BlockState;)Z"))
    private boolean useOnInject(WrenchItem instance, BlockState state, @Local(argsOnly = true) UseOnContext context) {
        return state.getDestroySpeed(context.getLevel(), context.getClickedPos()) > -1;
    }
}
