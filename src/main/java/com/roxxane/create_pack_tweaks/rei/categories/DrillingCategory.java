package com.roxxane.create_pack_tweaks.rei.categories;

import com.google.common.collect.Lists;
import com.roxxane.create_pack_tweaks.Cpt;
import com.roxxane.create_pack_tweaks.rei.CptReiClientPlugin;
import com.roxxane.create_pack_tweaks.rei.displays.DrillingDisplay;
import com.simibubi.create.AllBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class DrillingCategory implements DisplayCategory<DrillingDisplay> {
    @Override
    public CategoryIdentifier<? extends DrillingDisplay> getCategoryIdentifier() {
        return CptReiClientPlugin.drillingCategory;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("category.create_pack_tweaks.drilling");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(AllBlocks.MECHANICAL_DRILL);
    }

    @Override
    public List<Widget> setupDisplay(DrillingDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4 + 9)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5 + 9)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5 + 9)).entries(display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5 + 9)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
        widgets.add(Widgets.createTexturedWidget(new ResourceLocation(Cpt.id, "textures/gui/drilling_drill.png"), startPoint.x + 3, startPoint.y + 5 + 9 - 23, 0.0f, 0.0f, 18, 21, 18, 21));
        return widgets;
    }
}
