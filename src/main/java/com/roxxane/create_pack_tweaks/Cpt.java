package com.roxxane.create_pack_tweaks;

import com.mojang.logging.LogUtils;
import com.roxxane.create_pack_tweaks.blocks.CptBlocks;
import com.roxxane.create_pack_tweaks.blocks.entities.CptBlockEntities;
import com.roxxane.create_pack_tweaks.items.CptItems;
import com.roxxane.create_pack_tweaks.worldgen.features.CptFeatures;
import com.tterrag.registrate.Registrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// TODO: Category for mold fan interaction
@SuppressWarnings("unused")
@Mod(Cpt.id)
public class Cpt {
    public static final String id = "create_pack_tweaks";
    public static final String displayName = "Create Pack Tweaks";
    public static final Logger logger = LogUtils.getLogger();
    public static final Registrate registrate = Registrate.create(id);

    public Cpt() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CptBlocks.register();
        CptItems.register();
        CptBlockEntities.register();
        CptFeatures.register(modEventBus);

        registrate.addRawLang("chat.create_pack_tweaks.mold_contains", "§8Goes in molds:");
        registrate.addRawLang("chat.create_pack_tweaks.mold_contains_ingot", "§8 - Ingot");
        registrate.addRawLang("category.create_pack_tweaks.mold_heating", "Mold Heating");
        registrate.addRawLang("category.create_pack_tweaks.drilling", "Drilling");
        registrate.addRawLang("chat.create_pack_tweaks.reload_error", "§c" + displayName + " failed to reload!");

    }

    public static ResourceLocation resourceLocation(String path) {
        return new ResourceLocation(id, path);
    }
}