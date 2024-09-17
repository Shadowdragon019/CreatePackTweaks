package com.roxxane.create_pack_tweaks.rei.displays;

import com.roxxane.create_pack_tweaks.rei.CptReiClientPlugin;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;

import java.util.List;

public class SmushingDisplay extends BasicDisplay {
    public SmushingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    @Override
    public CategoryIdentifier<SmushingDisplay> getCategoryIdentifier() {
        return CptReiClientPlugin.smushingCategory;
    }

}
