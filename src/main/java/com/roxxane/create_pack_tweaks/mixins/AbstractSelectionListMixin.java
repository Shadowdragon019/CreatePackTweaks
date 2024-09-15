package com.roxxane.create_pack_tweaks.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.controls.KeyBindsList.KeyEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
@Mixin(AbstractSelectionList.class)
abstract class AbstractSelectionListMixin {
    @Shadow @Final private List children;

    // Remove sprint keybind
    @Redirect(method = "addEntry", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private <E> boolean addEntryRedirect(List instance, E entry) {
        if ((AbstractSelectionList) (Object) this instanceof KeyBindsList &&
            entry instanceof KeyEntry && ((KeyEntry) entry).key == Minecraft.getInstance().options.keySprint)
            return false;
        else {
            children.add(entry);
            return true;
        }
    }
}