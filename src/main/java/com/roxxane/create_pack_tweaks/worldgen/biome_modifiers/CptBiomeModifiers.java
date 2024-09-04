package com.roxxane.create_pack_tweaks.worldgen.biome_modifiers;

import com.roxxane.create_pack_tweaks.Cpt;
import com.roxxane.create_pack_tweaks.worldgen.features.CptPlacedFeatures;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class CptBiomeModifiers {
    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(
            ForgeRegistries.Keys.BIOME_MODIFIERS,
            new ResourceLocation(Cpt.id, name)
        );
    }

    public static final ResourceKey<BiomeModifier> addTinyTree = registerKey("add_tiny_tree");
    public static final ResourceKey<BiomeModifier> addTinyBrownMushroom = registerKey("add_tiny_brown_mushrooms");
    public static final ResourceKey<BiomeModifier> addTinyRedMushroom = registerKey("add_tiny_red_mushrooms");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(addTinyTree, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(CptPlacedFeatures.tinyTreeKey)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(addTinyBrownMushroom, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(CptPlacedFeatures.tinyBrownMushroom)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(addTinyRedMushroom, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(CptPlacedFeatures.tinyRedMushroom)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));
    }
}
