package com.roxxane.create_pack_tweaks.mixins;

import com.roxxane.create_pack_tweaks.worldgen.features.CptConfiguredFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.OakTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OakTreeGrower.class)
abstract class OakTreeGrowerMixin {
    @Inject(method = "getConfiguredFeature", at = @At("RETURN"), cancellable = true)
    private void getConfiguredFeatureInject(
        RandomSource random,
        boolean bzz,
        CallbackInfoReturnable<ResourceKey<ConfiguredFeature<?, ?>>> cir
    ) {
        cir.setReturnValue(CptConfiguredFeatures.tinyTreeKey);
    }
}