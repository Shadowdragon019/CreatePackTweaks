package com.roxxane.create_pack_tweaks.rei;

import com.roxxane.create_pack_tweaks.Cpt;
import com.roxxane.create_pack_tweaks.CptConfig;
import com.roxxane.create_pack_tweaks.blocks.CptBlocks;
import com.roxxane.create_pack_tweaks.blocks.FillableMoldBlock;
import com.roxxane.create_pack_tweaks.rei.categories.DrillingCategory;
import com.roxxane.create_pack_tweaks.rei.categories.LavaSmeltingCategory;
import com.roxxane.create_pack_tweaks.rei.categories.MoldCoolingCategory;
import com.roxxane.create_pack_tweaks.rei.categories.MoldHeatingCategory;
import com.roxxane.create_pack_tweaks.rei.displays.DrillingDisplay;
import com.roxxane.create_pack_tweaks.rei.displays.LavaSmeltingDisplay;
import com.roxxane.create_pack_tweaks.rei.displays.MoldCoolingDisplay;
import com.roxxane.create_pack_tweaks.rei.displays.MoldHeatingDisplay;
import com.simibubi.create.AllBlocks;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.world.item.Items;

@REIPluginClient
public class CptReiClientPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<MoldHeatingDisplay> moldHeatingCategory =
        CategoryIdentifier.of(Cpt.id, "mold_heating");
    public static final CategoryIdentifier<MoldCoolingDisplay> moldCoolingCategory =
        CategoryIdentifier.of(Cpt.id, "mold_cooling");
    public static final CategoryIdentifier<DrillingDisplay> drillingCategory =
        CategoryIdentifier.of(Cpt.id, "drilling");
    public static final CategoryIdentifier<LavaSmeltingDisplay> lavaSmeltingCategory =
        CategoryIdentifier.of(Cpt.id, "lava_smelting");

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new MoldHeatingCategory());
        registry.addWorkstations(moldHeatingCategory, EntryStacks.of(AllBlocks.ENCASED_FAN));
        registry.addWorkstations(moldHeatingCategory, EntryStacks.of(CptBlocks.mushyMold));

        registry.add(new MoldCoolingCategory());
        registry.addWorkstations(moldCoolingCategory, EntryStacks.of(AllBlocks.ENCASED_FAN));
        registry.addWorkstations(moldCoolingCategory, EntryStacks.of(CptBlocks.mushyMold));

        registry.add(new DrillingCategory());
        registry.addWorkstations(drillingCategory, EntryStacks.of(AllBlocks.MECHANICAL_DRILL));

        registry.add(new LavaSmeltingCategory());
        registry.addWorkstations(lavaSmeltingCategory, EntryStacks.of(Items.LAVA_BUCKET));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        for (var entry : FillableMoldBlock.heatingMap.entrySet()) {
            var materialIn = entry.getKey();
            var materialOut = entry.getValue();
            var itemIn = FillableMoldBlock.materialItemMap.get(materialIn);
            var itemOut = FillableMoldBlock.materialItemMap.get(materialOut);

            registry.add(new MoldHeatingDisplay(
                EntryStacks.of(itemIn.get()),
                EntryStacks.of(itemOut.get())
            ));
        }

        for (var entry : FillableMoldBlock.coolingMap.entrySet()) {
            var materialIn = entry.getKey();
            var materialOut = entry.getValue();
            var itemIn = FillableMoldBlock.materialItemMap.get(materialIn);
            var itemOut = FillableMoldBlock.materialItemMap.get(materialOut);

            registry.add(new MoldCoolingDisplay(
                EntryStacks.of(itemIn.get()),
                EntryStacks.of(itemOut.get())
            ));
        }

        if (CptConfig.isLoaded()){
            for (var entry : CptConfig.drillingMap.entrySet()) {
                var block = entry.getKey();
                var item = entry.getValue();
                registry.add(new DrillingDisplay(
                    EntryStacks.of(block),
                    EntryStacks.of(item)
                ));
            }

            for (var entry : CptConfig.lavaSmelting.entrySet()) {
                var block = entry.getKey();
                var item = entry.getValue();
                registry.add(new LavaSmeltingDisplay(
                    EntryStacks.of(block),
                    EntryStacks.of(item)
                ));
            }
        }
    }
}