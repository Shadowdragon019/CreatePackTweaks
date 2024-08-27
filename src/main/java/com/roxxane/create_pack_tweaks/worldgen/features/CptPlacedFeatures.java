package com.roxxane.create_pack_tweaks.worldgen.features;

import com.roxxane.create_pack_tweaks.CreatePackTweaks;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class CptPlacedFeatures {
    public static ResourceKey<PlacedFeature> registryKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(CreatePackTweaks.id, name));
    }

    public static final ResourceKey<PlacedFeature> tinyTreeKey = registryKey("tiny_tree");
    public static final ResourceKey<PlacedFeature> tinyBrownMushroom = registryKey("tiny_brown_mushroom");
    public static final ResourceKey<PlacedFeature> tinyRedMushroom = registryKey("tiny_red_mushroom");

    public static void register(
        BootstapContext<PlacedFeature> context,
        ResourceKey<PlacedFeature> key,
        Holder<ConfiguredFeature<?, ?>> configured,
        List<PlacementModifier> modifiers
    ) {
        context.register(key, new PlacedFeature(configured, List.copyOf(modifiers)));
    }

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(
            context,
            tinyTreeKey,
            configuredFeatures.getOrThrow(CptConfiguredFeatures.tinyTreeKey),
            List.of(
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BlockPredicateFilter.forPredicate(
                    BlockPredicate.matchesTag(new Vec3i(0, -1, 0), BlockTags.DIRT)
                ),
                CountPlacement.of(2)
            )
        );

        register(
            context,
            tinyBrownMushroom,
            configuredFeatures.getOrThrow(CptConfiguredFeatures.tinyBrownMushroom),
            List.of(
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BlockPredicateFilter.forPredicate(
                    BlockPredicate.matchesTag(new Vec3i(0, -1, 0), BlockTags.MUSHROOM_GROW_BLOCK)
                ),
                CountPlacement.of(2)
            )
        );

        register(
            context,
            tinyRedMushroom,
            configuredFeatures.getOrThrow(CptConfiguredFeatures.tinyRedMushroom),
            List.of(
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BlockPredicateFilter.forPredicate(
                    BlockPredicate.matchesTag(new Vec3i(0, -1, 0), BlockTags.MUSHROOM_GROW_BLOCK)
                ),
                CountPlacement.of(2)
            )
        );
    }
}
