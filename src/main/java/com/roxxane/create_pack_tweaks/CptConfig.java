package com.roxxane.create_pack_tweaks;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.shedaniel.math.Rectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unused")
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
    public static HashMap<Ingredient, ArrayList<MutableComponent>> tooltips;

    private static final Path path = Path.of(FMLPaths.CONFIGDIR.get().toString() + "/" + Cpt.id + ".json");

    private static Vec3 getVec3(JsonObject object) {
        return new Vec3(object.get("x").getAsDouble(), object.get("y").getAsDouble(), object.get("z").getAsDouble());
    }

    private static Item getItem(String string) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
    }

    private static Block getBlock(String string) {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(string));
    }

    @SuppressWarnings("DataFlowIssue")
    private static TagKey<Item> getItemTag(String string) {
        return ForgeRegistries.ITEMS.tags().
            getTag(TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation(string))).getKey();
    }

    private static Item[] getItems(JsonArray array) {
        var items = new Item[array.size()];
        Arrays.setAll(items, index -> getItem(array.get(index).getAsString()));

        return items;
    }

    private static Ingredient getIngredient(Object object) {
        if (object instanceof JsonArray array)
            return Ingredient.of(getItems(array));
        else if (object instanceof JsonElement element)
            if (element.getAsString().startsWith("#"))
                return Ingredient.of(getItemTag(element.getAsString().substring(1)));
            else
                return Ingredient.of(getItem(element.getAsString()));
        else throw new IllegalArgumentException("Could not parse ingredient, " + object);
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

    private static <Key, Value> HashMap<Key, Value> getHashMap(JsonObject jsonObject,
       BiFunction<String, JsonElement, Key> keyFunction, BiFunction<String, JsonElement, Value> valueFunction)
    {
        var map = new HashMap<Key, Value>();
        for (var entry : jsonObject.entrySet())
            map.put(keyFunction.apply(entry.getKey(), entry.getValue()),
                valueFunction.apply(entry.getKey(), entry.getValue()));
        return map;
    }

    private static <Key, Value> HashMap<Key, Value> getHashMap(JsonObject jsonObject,
        Function<String, Key> keyFunction, Function<JsonElement, Value> valueFunction)
    {
        var map = new HashMap<Key, Value>();
        for (var entry : jsonObject.entrySet())
            map.put(keyFunction.apply(entry.getKey()), valueFunction.apply(entry.getValue()));
        return map;
    }

    private static <Key, Value> HashMap<Key, Value> getHashMap(JsonArray jsonArray,
        Function<JsonElement, Key> keyFunction, Function<JsonElement, Value> valueFunction)
    {
        var map = new HashMap<Key, Value>();
        for (var element : jsonArray)
            map.put(keyFunction.apply(element), valueFunction.apply(element));
        return map;
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

    private static JsonObject makeJsonObject(Consumer<JsonObject> function) {
        var object = new JsonObject();
        function.accept(object);
        return object;
    }

    private static JsonObject makeJsonObject(Object... objects) {
        if (objects.length % 2 != 0)
            throw new IllegalArgumentException("Cannot map all keys to values");

        var index = -1;
        final var jsonObject = new JsonObject();
        String key = null;

        for (var object : objects) {
            index++;
            if (index % 2 == 0)
                key = (String) object;
            else {
                if (object instanceof JsonElement)
                    jsonObject.add(key, (JsonElement) object);
                else {
                    if (object instanceof String)
                        jsonObject.addProperty(key, (String) object);
                    else if (object instanceof Number)
                        jsonObject.addProperty(key, (Number) object);
                    else if (object instanceof Boolean)
                        jsonObject.addProperty(key, (Boolean) object);
                    else if (object instanceof Character)
                        jsonObject.addProperty(key, (Character) object);
                    else
                        throw new IllegalStateException("Could not add property to JsonObject");
                }
                key = null;
            }
        }
        return jsonObject;
    }

    private static JsonArray makeJsonArray(Consumer<JsonArray> function) {
        var array = new JsonArray();
        function.accept(array);
        return array;
    }

    private static JsonArray makeJsonArray(Object... objects) {
        var array = new JsonArray();

        for (var object : objects)
            if (object instanceof JsonElement)
                array.add((JsonElement) object);
            else if (object instanceof String)
                array.add((String) object);
            else if (object instanceof Number)
                array.add((Number) object);
            else if (object instanceof Boolean)
                array.add((Boolean) object);
            else if (object instanceof Character)
                array.add((Character) object);
            else
                throw new IllegalStateException("Could not add element to JsonArray");
        return array;
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

                defaultData.add("lava_smelting_initial_velocity", makeJsonObject(
                    "x", 0.15, "y", 0.4, "z", 0.15
                ));

                defaultData.add("lava_smelting", makeJsonObject(
                    "create_pack_tweaks:mushy_paste", "create_pack_tweaks:mushy_brick",
                    "bedrock", "air"
                ));

                defaultData.add("drilling_config", makeJsonObject("bedrock", "dirt"));

                defaultData.add("lava_smelting_bucket_config",
                    makeJsonRectangle(31, 14, 16,16));

                defaultData.add("smushing_smushing_config",
                    makeJsonRectangle(7, -13, 10,25));

                defaultData.addProperty("item_initial_merge_delay", 30);

                defaultData.add("redstone_color_multipliers", makeJsonArray(makeJsonObject(
                    "multiplier", 0.8, "min", 1, "max", 15
                )));

                defaultData.add("tooltips", makeJsonArray(makeJsonObject(
                    "items", makeJsonArray("dirt", "bedrock"),
                    "tooltip", makeJsonArray("SPOOPY")
                )));
                /*
                tooltip: [
                    {
                        items items
                        tooltip [text]
                    }
                ]
                 */

                // Closing
                gson.toJson(defaultData, writer);
                writer.close();
            }

            JsonReader reader = new JsonReader(new FileReader(path.toString()));
            JsonObject data = gson.fromJson(reader, JsonObject.class);

            drillingMap = getHashMap(data.getAsJsonObject("drilling_config"),
                CptConfig::getBlock, value -> getItem(value.getAsString()));

            lavaSmelting = getHashMap(data.getAsJsonObject("lava_smelting"),
                CptConfig::getItem, key -> getItem(key.getAsString()));
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

            tooltips = getHashMap(data.getAsJsonArray("tooltips"),
                object -> getIngredient(object.getAsJsonObject().get("items")),
                object -> getArrayList(object.getAsJsonObject().get("tooltip").getAsJsonArray(),
                    tooltip -> Component.translatable(tooltip.getAsString())
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
