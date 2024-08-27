package com.roxxane.create_pack_tweaks.items;

import com.roxxane.create_pack_tweaks.CreatePackTweaks;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public class CptItems {
    public static final ItemEntry<Item> mushyBrick =
        CreatePackTweaks.registrate.item("mushy_brick", Item::new)
           .register();

    public static final ItemEntry<Item> mushyPaste =
        CreatePackTweaks.registrate.item("mushy_paste", Item::new)
            .register();

    public static final ItemEntry<Item> smallPileOfMushyPaste =
        CreatePackTweaks.registrate.item("small_pile_of_mushy_paste", Item::new)
            .register();

    public static void register() {}
}
