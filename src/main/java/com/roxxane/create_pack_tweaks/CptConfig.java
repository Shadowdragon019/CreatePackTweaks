package com.roxxane.create_pack_tweaks;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.shedaniel.math.Rectangle;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class CptConfig {
    private static boolean loaded = false;
    public static HashMap<Block, Item> drillingMap;
    public static HashMap<Item, Item> lavaSmelting;
    public static Vec3 lavaSmeltingInitialVelocity;
    public static int lavaSmeltingConversionChance;
    public static Rectangle lavaSmeltingLavaBucket;

    private static final Path path = Path.of(FMLPaths.CONFIGDIR.get().toString() + "/" + Cpt.id + ".json");

    private static Item getItem(String string) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
    }

    private static Block getBlock(String string) {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(string));
    }

    private static Rectangle getRectangle(JsonObject object) {
        if (object.has("x") && object.has("y") &&
            object.has("width") && object.has("height")
        ) {
            return new Rectangle(
                object.get("x").getAsInt(),
                object.get("y").getAsInt(),
                object.get("width").getAsInt(),
                object.get("height").getAsInt()
            );
        } else {
            return null;
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static JsonObject makeJsonRectangle(int x, int y, int width, int height) {
        var object = new JsonObject();
        object.addProperty("x", x);
        object.addProperty("y", y);
        object.addProperty("width", width);
        object.addProperty("height", height);

        return object;
    }

    public static boolean reload() {
        var reloadSuccess = false;
        try {
            Gson gson = new Gson();
            if (!Files.exists(path)) {
                JsonWriter writer = new JsonWriter(new FileWriter(path.toString()));
                JsonObject defaultData = new JsonObject();

                defaultData.addProperty("lava_smelting_conversion_chance", 10);

                var lavaSmeltingVelocityObject = new JsonObject();
                lavaSmeltingVelocityObject.addProperty("x", 0.15);
                lavaSmeltingVelocityObject.addProperty("y", 0.4);
                lavaSmeltingVelocityObject.addProperty("z", 0.15);
                defaultData.add("lava_smelting_initial_velocity", lavaSmeltingVelocityObject);

                var lavaSmeltingObject = new JsonObject();
                lavaSmeltingObject.addProperty(
                    "create_pack_tweaks:mushy_paste", "create_pack_tweaks:mushy_brick");
                lavaSmeltingObject.addProperty("bedrock", "air");
                defaultData.add("lava_smelting", lavaSmeltingObject);

                var drillingConfig = new JsonObject();
                drillingConfig.addProperty("bedrock", "dirt");
                defaultData.add("drilling_config", drillingConfig);

                defaultData.add("lava_smelting_bucket_config",
                    makeJsonRectangle(31, 14, 16,16));

                // Closing
                gson.toJson(defaultData, writer);
                writer.close();
            }

            JsonReader reader = new JsonReader(new FileReader(path.toString()));
            JsonObject data = gson.fromJson(reader, JsonObject.class);

            drillingMap = new HashMap<>();
            for (var entry : data.getAsJsonObject("drilling_config").entrySet()) {
                drillingMap.put(getBlock(entry.getKey()), getItem(entry.getValue().getAsString()));
            }

            lavaSmelting = new HashMap<>();
            for (var entry : data.getAsJsonObject("lava_smelting").entrySet()) {
                lavaSmelting.put(getItem(entry.getKey()), getItem(entry.getValue().getAsString()));
            }

            lavaSmeltingInitialVelocity = new Vec3(
                data.getAsJsonObject("lava_smelting_initial_velocity").get("x").getAsDouble(),
                data.getAsJsonObject("lava_smelting_initial_velocity").get("y").getAsDouble(),
                data.getAsJsonObject("lava_smelting_initial_velocity").get("z").getAsDouble()
            );

            lavaSmeltingConversionChance = data.get("lava_smelting_conversion_chance").getAsInt();

            lavaSmeltingLavaBucket = getRectangle(data.getAsJsonObject("lava_smelting_bucket_config"));

            reloadSuccess = true;
        } catch (Exception ignored) {
            Cpt.logger.warn("Could not reload config!");
        }

        loaded = true;
        return reloadSuccess;
    }

    public static boolean isLoaded() {
        return loaded;
    }
}
