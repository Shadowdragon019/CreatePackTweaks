package com.roxxane.create_pack_tweaks.data;

import com.roxxane.create_pack_tweaks.Cpt;
import com.roxxane.create_pack_tweaks.worldgen.biome_modifiers.CptBiomeModifiers;
import com.roxxane.create_pack_tweaks.worldgen.features.CptConfiguredFeatures;
import com.roxxane.create_pack_tweaks.worldgen.features.CptPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CptWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder builder = new RegistrySetBuilder()
        .add(Registries.CONFIGURED_FEATURE, CptConfiguredFeatures::bootstrap)
        .add(Registries.PLACED_FEATURE, CptPlacedFeatures::bootstrap)
        .add(ForgeRegistries.Keys.BIOME_MODIFIERS, CptBiomeModifiers::bootstrap);

    public CptWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, builder, Set.of(Cpt.id));
    }
}
