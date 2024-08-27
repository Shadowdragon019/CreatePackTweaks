package com.roxxane.create_pack_tweaks.worldgen.features;

import com.roxxane.create_pack_tweaks.CreatePackTweaks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class CptConfiguredFeatures {
    public static ResourceKey<ConfiguredFeature<?, ?>> registryKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(CreatePackTweaks.id, name));
    }

    public static final ResourceKey<ConfiguredFeature<?, ?>> tinyTreeKey = registryKey("tiny_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> tinyBrownMushroom = registryKey("tiny_brown_mushroom");
    public static final ResourceKey<ConfiguredFeature<?, ?>> tinyRedMushroom = registryKey("tiny_red_mushroom");

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
        BootstapContext<ConfiguredFeature<?, ?>> context,
        ResourceKey<ConfiguredFeature<?, ?>> key,
        F feature,
        FC configuration
    ) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        register(context, tinyTreeKey, CptFeatures.tinyTree.get(), FeatureConfiguration.NONE);
        register(context, tinyBrownMushroom, CptFeatures.tinyBrownMushroom.get(), FeatureConfiguration.NONE);
        register(context, tinyRedMushroom, CptFeatures.tinyRedMushroom.get(), FeatureConfiguration.NONE);
    }
}
