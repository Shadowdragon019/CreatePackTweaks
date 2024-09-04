package com.roxxane.create_pack_tweaks.rei;

import com.roxxane.create_pack_tweaks.Cpt;
import com.roxxane.create_pack_tweaks.CptConfig;
import com.roxxane.create_pack_tweaks.blocks.CptBlocks;
import com.roxxane.create_pack_tweaks.blocks.FillableMoldBlock;
import com.roxxane.create_pack_tweaks.rei.categories.DrillingCategory;
import com.roxxane.create_pack_tweaks.rei.categories.MoldHeatingCategory;
import com.roxxane.create_pack_tweaks.rei.displays.MoldHeatingDisplay;
import com.roxxane.create_pack_tweaks.rei.displays.DrillingDisplay;
import com.simibubi.create.AllBlocks;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;

@REIPluginClient
public class CptReiClientPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<MoldHeatingDisplay> moldHeatingCategory =
        CategoryIdentifier.of(Cpt.id, "mold_heating");
    public static final CategoryIdentifier<DrillingDisplay> drillingCategory =
        CategoryIdentifier.of(Cpt.id, "drilling");

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new MoldHeatingCategory());
        registry.addWorkstations(moldHeatingCategory, EntryStacks.of(AllBlocks.ENCASED_FAN));
        registry.addWorkstations(moldHeatingCategory, EntryStacks.of(CptBlocks.mushyMold));

        registry.add(new DrillingCategory());
        registry.addWorkstations(drillingCategory, EntryStacks.of(AllBlocks.MECHANICAL_DRILL));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        for (var shapeEntry : FillableMoldBlock.heatingMap.entrySet()) {
            var shape = shapeEntry.getKey();
            for (var heatingEntry : shapeEntry.getValue().entrySet()) {
                var materialIn = heatingEntry.getKey();
                var materialOut = heatingEntry.getValue();
                var itemIn = FillableMoldBlock.materialItemMap.get(shape).get(materialIn);
                var itemOut = FillableMoldBlock.materialItemMap.get(shape).get(materialOut);

                registry.add(new MoldHeatingDisplay(
                    EntryStacks.of(itemIn),
                    EntryStacks.of(itemOut)
                ));
            }
        }

        for (var entry : CptConfig.drillingMap.entrySet()) {
            var block = entry.getKey();
            var item = entry.getValue();
            registry.add(new DrillingDisplay(
                EntryStacks.of(block),
                EntryStacks.of(item)
            ));
        }
    }
}