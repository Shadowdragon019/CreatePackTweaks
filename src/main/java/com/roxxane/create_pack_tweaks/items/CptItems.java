package com.roxxane.create_pack_tweaks.items;

import com.roxxane.create_pack_tweaks.Cpt;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public class CptItems {
    public static final ItemEntry<Item> mushyBrick =
        Cpt.registrate.item("mushy_brick", Item::new)
           .register();

    public static final ItemEntry<Item> mushyPaste =
        Cpt.registrate.item("mushy_paste", Item::new)
            .register();

    public static final ItemEntry<Item> smallPileOfMushyPaste =
        Cpt.registrate.item("small_pile_of_mushy_paste", Item::new)
            .register();

    public static void register() {}
}
