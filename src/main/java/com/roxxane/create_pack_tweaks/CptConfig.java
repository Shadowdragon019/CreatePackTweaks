package com.roxxane.create_pack_tweaks;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class CptConfig {
    public static boolean loaded = false;
    public static HashMap<Block, Item> drillingMap;

    private static final Path path = Path.of(FMLPaths.CONFIGDIR.get().toString() + "/" + Cpt.id + ".json");

    private static Item getItem(String string) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
    }

    private static Block getBlock(String string) {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(string));
    }

    public static boolean reload() {
        var reloadSuccess = false;
        try {
            Gson gson = new Gson();
            if (!Files.exists(path)) {
                JsonWriter writer = new JsonWriter(new FileWriter(path.toString()));
                JsonObject defaultData = new JsonObject();

                defaultData.add("drilling_config", new JsonObject());

                // Closing
                gson.toJson(defaultData, writer);
                writer.close();
            }

            JsonReader reader = new JsonReader(new FileReader(path.toString()));
            JsonObject data = gson.fromJson(reader, JsonObject.class);

            drillingMap = new HashMap<>();
            for (var entry : data.getAsJsonObject("drilling_config").entrySet()) {
                drillingMap.put(
                    getBlock(entry.getKey()),
                    getItem(entry.getValue().getAsString())
                );
            }

            reloadSuccess = true;
        } catch (Exception ignored) {
            Cpt.logger.warn("Could not reload config!");
        }
        loaded = true;
        return reloadSuccess;
    }

}
