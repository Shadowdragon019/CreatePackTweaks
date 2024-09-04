package com.roxxane.create_pack_tweaks.rei.categories;

import com.google.common.collect.Lists;
import com.roxxane.create_pack_tweaks.Cpt;
import com.roxxane.create_pack_tweaks.rei.CptReiClientPlugin;
import com.roxxane.create_pack_tweaks.rei.displays.MoldHeatingDisplay;
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

import java.util.List;

public class MoldHeatingCategory implements DisplayCategory<MoldHeatingDisplay> {
    @Override
    public CategoryIdentifier<? extends MoldHeatingDisplay> getCategoryIdentifier() {
        return CptReiClientPlugin.moldHeatingCategory;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("category.create_pack_tweaks.mold_heating");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(AllBlocks.ENCASED_FAN);
    }

    @Override
    public List<Widget> setupDisplay(MoldHeatingDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() + 32);
        List<Widget> widgets = Lists.newArrayList();

        widgets.add(Widgets.createRecipeBase(new Rectangle(
            bounds.x - 25,
            bounds.y,
            bounds.width + 50,
            bounds.height + 50
        )));

        System.out.println(new Rectangle(
            bounds.x - 25,
            bounds.y,
            bounds.width,
            bounds.height + 50
        ));

        //display.getInputEntries().get(0).get(0).getValue();

        widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4 + 9)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5 + 9)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5 + 9)).entries(display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5 + 9)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
        widgets.add(Widgets.createTexturedWidget(Cpt.resourceLocation("textures/gui/fan_heating.png"), startPoint.x + 3 + 1, startPoint.y + 5 + 9 - 48, 0.0f, 0.0f, 16, 32, 16, 32));
        //widgets.add(Widgets.createTexturedWidget(Cpt.resourceLocation("textures/gui/mold/" +  + ".png"), startPoint.x + 3 + 1, startPoint.y + 5 + 9 - 16, 0.0f, 0.0f, 16, 16, 16, 16));;
        widgets.add(Widgets.createTexturedWidget(Cpt.resourceLocation("textures/gui/mold/ingot/mushy_brick.png"), startPoint.x + 3 + 1, startPoint.y + 5 + 9 - 16, 0.0f, 0.0f, 16, 16, 16, 16));
        widgets.add(Widgets.createTexturedWidget(Cpt.resourceLocation("textures/gui/mold/ingot/mushy_paste.png"), startPoint.x + 3 + 1, startPoint.y + 5 + 9 - 16, 0.0f, 0.0f, 16, 16, 16, 16));
        return widgets;
    }
}