package com.roxxane.create_pack_tweaks.worldgen.features;

import com.roxxane.create_pack_tweaks.Cpt;
import com.roxxane.create_pack_tweaks.worldgen.features.classes.TinyBrownMushroomFeature;
import com.roxxane.create_pack_tweaks.worldgen.features.classes.TinyRedMushroomFeature;
import com.roxxane.create_pack_tweaks.worldgen.features.classes.TinyTreeFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CptFeatures {
    private static final DeferredRegister<Feature<?>> features = DeferredRegister.create(
        ForgeRegistries.FEATURES,
        Cpt.id
    );

    public static final RegistryObject<TinyTreeFeature> tinyTree = features.register(
        "tiny_tree",
        TinyTreeFeature::new
    );
    public static final RegistryObject<TinyBrownMushroomFeature> tinyBrownMushroom = features.register(
            "tiny_brown_mushroom",
            TinyBrownMushroomFeature::new
    );
    public static final RegistryObject<TinyRedMushroomFeature> tinyRedMushroom = features.register(
            "tiny_red_mushroom",
            TinyRedMushroomFeature::new
    );

    public static void register(IEventBus modEventBus) {
        features.register(modEventBus);
    }
}
