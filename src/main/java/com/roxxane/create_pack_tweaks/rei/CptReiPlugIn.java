package com.roxxane.create_pack_tweaks.rei;

import com.roxxane.create_pack_tweaks.rei.displays.DrillingDisplay;
import com.roxxane.create_pack_tweaks.rei.displays.LavaSmeltingDisplay;
import com.roxxane.create_pack_tweaks.rei.displays.MoldCoolingDisplay;
import com.roxxane.create_pack_tweaks.rei.displays.MoldHeatingDisplay;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;

@SuppressWarnings("unused")
public class CptReiPlugIn implements REIServerPlugin {
    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(CptReiClientPlugin.moldHeatingCategory,
            BasicDisplay.Serializer.ofSimpleRecipeLess(MoldHeatingDisplay::new));

        registry.register(CptReiClientPlugin.moldCoolingCategory,
            BasicDisplay.Serializer.ofSimpleRecipeLess(MoldCoolingDisplay::new));

        registry.register(CptReiClientPlugin.drillingCategory,
            BasicDisplay.Serializer.ofSimpleRecipeLess(DrillingDisplay::new));

        registry.register(CptReiClientPlugin.lavaSmeltingCategory,
            BasicDisplay.Serializer.ofSimpleRecipeLess(LavaSmeltingDisplay::new));
    }
}