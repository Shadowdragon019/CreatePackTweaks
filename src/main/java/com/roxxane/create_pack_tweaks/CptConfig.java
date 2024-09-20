package com.roxxane.create_pack_tweaks;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.shedaniel.math.Rectangle;
import net.minecraft.Util;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CptConfig {
    public record Tuple<A, B, C>(A a, B b, C c) {}

    private static boolean loaded = false;
    public static HashMap<Block, Item> drillingMap;
    public static HashMap<Item, Item> lavaSmelting;
    public static Vec3 lavaSmeltingInitialVelocity;
    public static int lavaSmeltingConversionChance;
    public static Rectangle lavaSmeltingLavaBucket;
    public static Rectangle smushingSmushingTexture;
    public static int itemInitialMergeDelay;
    public static ArrayList<Tuple<Float, Integer, Integer>> redstoneColorMultipliers;

    private static final Path path = Path.of(FMLPaths.CONFIGDIR.get().toString() + "/" + Cpt.id + ".json");

    private static Vec3 getVec3(JsonObject object) {
        return new Vec3(object.get("x").getAsDouble(), object.get("y").getAsDouble(), object.get("z").getAsDouble());
    }

    private static Item getItem(String string) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
    }

    private static Item getItem(JsonElement jsonElement) {
        return getItem(jsonElement.getAsString());
    }

    private static Block getBlock(String string) {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(string));
    }

    @SuppressWarnings("unused")
    private static Block getBlock(JsonElement jsonElement) {
        return getBlock(jsonElement.getAsString());
    }

    private static Rectangle getRectangle(JsonObject object) {
        if (object.has("x") && object.has("y") &&
            object.has("width") && object.has("height")
        )
            return new Rectangle(object.get("x").getAsInt(), object.get("y").getAsInt(),
                object.get("width").getAsInt(), object.get("height").getAsInt());
        else
            return null;
    }

    private static <T> ArrayList<T> getArrayList(JsonArray jsonArray, Function<JsonElement, T> function) {
        var array = new ArrayList<T>();
        for (var element : jsonArray)
            array.add(function.apply(element));

        return array;
    }

    @SuppressWarnings("unused")
    private static <Key, Value> HashMap<Key, Value> getHashMap(JsonObject jsonObject,
                                                               BiFunction<String, JsonElement, Key> keyFunction, BiFunction<String, JsonElement, Value> valueFunction)
    {
        var object = new HashMap<Key, Value>();
        for (var entry : jsonObject.entrySet())
            object.put(keyFunction.apply(entry.getKey(), entry.getValue()),
                valueFunction.apply(entry.getKey(), entry.getValue()));

        return object;
    }

    private static <Key, Value> HashMap<Key, Value> getHashMap(JsonObject jsonObject,
        Function<String, Key> keyFunction, Function<JsonElement, Value> valueFunction)
    {
        var object = new HashMap<Key, Value>();
        for (var entry : jsonObject.entrySet())
            object.put(keyFunction.apply(entry.getKey()), valueFunction.apply(entry.getValue()));

        return object;
    }

    private static <A, B, C> Tuple<A, B, C> getTuple(JsonObject object, Function<JsonObject, A> aFunction,
        Function<JsonObject, B> bFunction, Function<JsonObject, C> cFunction)
    {
        return new Tuple<>(aFunction.apply(object), bFunction.apply(object), cFunction.apply(object));
    }

    private static JsonObject makeJsonRectangle(int x, int y, int width, int height) {
        var object = new JsonObject();
        object.addProperty("x", x);
        object.addProperty("y", y);
        object.addProperty("width", width);
        object.addProperty("height", height);

        return object;
    }

    @SuppressWarnings("CallToPrintStackTrace")
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

                defaultData.add("smushing_smushing_config",
                    makeJsonRectangle(7, -13, 10,25));

                defaultData.addProperty("item_initial_merge_delay", 30);

                defaultData.add("redstone_color_multipliers", Util.make(new JsonArray(),
                    array -> array.add(Util.make(new JsonObject(), object ->
                {
                    object.addProperty("multiplier", 0.8);
                    object.addProperty("min", 1);
                    object.addProperty("max", 15);
                }))));

                // Closing
                gson.toJson(defaultData, writer);
                writer.close();
            }

            JsonReader reader = new JsonReader(new FileReader(path.toString()));
            JsonObject data = gson.fromJson(reader, JsonObject.class);

            drillingMap = getHashMap(data.getAsJsonObject("drilling_config"),
                CptConfig::getBlock, CptConfig::getItem);

            lavaSmelting = getHashMap(data.getAsJsonObject("lava_smelting"),
                CptConfig::getItem, CptConfig::getItem);
            lavaSmeltingInitialVelocity = getVec3(data.getAsJsonObject("lava_smelting_initial_velocity"));
            lavaSmeltingConversionChance = data.get("lava_smelting_conversion_chance").getAsInt();
            lavaSmeltingLavaBucket = getRectangle(data.getAsJsonObject("lava_smelting_bucket_config"));

            smushingSmushingTexture = getRectangle(data.getAsJsonObject("smushing_smushing_config"));

            itemInitialMergeDelay = data.get("item_initial_merge_delay").getAsInt();

            redstoneColorMultipliers = getArrayList(data.getAsJsonArray("redstone_color_multipliers"),
                element -> getTuple(element.getAsJsonObject(),
                    object -> object.get("multiplier").getAsFloat(),
                    object -> object.get("min").getAsInt(),
                    object -> object.get("max").getAsInt()
                ));

            reloadSuccess = true;
        } catch (Exception exception) {
            Cpt.logger.warn("Could not reload Create Pack Tweaks config!");
            exception.printStackTrace();
        }

        loaded = true;
        return reloadSuccess;
    }

    public static boolean isLoaded() {
        return loaded;
    }
}
