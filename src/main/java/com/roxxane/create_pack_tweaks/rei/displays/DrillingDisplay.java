package com.roxxane.create_pack_tweaks.rei.displays;

import com.roxxane.create_pack_tweaks.rei.CptReiClientPlugin;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;

import java.util.Collections;
import java.util.List;

public class DrillingDisplay extends BasicDisplay {
    public DrillingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    public DrillingDisplay(EntryStack<?> in, EntryStack<?> out) {
        this(Collections.singletonList(EntryIngredient.of(in)), Collections.singletonList(EntryIngredient.of(out)));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CptReiClientPlugin.drillingCategory;
    }
}
