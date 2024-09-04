package com.roxxane.create_pack_tweaks.mixins;

import com.roxxane.create_pack_tweaks.CptConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.flag.FeatureFlagSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ReloadableServerResources.class)
abstract class ReloadableServerResourcesMixin {
    @Inject(method = "loadResources", at = @At("HEAD"))
    private static void loadResourcesMixin(
        ResourceManager resourceManager,
        RegistryAccess.Frozen registryAccess,
        FeatureFlagSet enabledFeatures,
        Commands.CommandSelection commandSelection,
        int functionCompilationLevel,
        Executor backgroundExecutor,
        Executor gameExecutor,
        CallbackInfoReturnable<CompletableFuture<ReloadableServerResources>> cir
    ) {
        if (!CptConfig.reload() && Minecraft.getInstance().level != null) {
            for (var player : Minecraft.getInstance().level.players()) {
                player.sendSystemMessage(Component.translatable("chat.create_pack_tweaks.reload_error"));
            }
        }
    }
}
