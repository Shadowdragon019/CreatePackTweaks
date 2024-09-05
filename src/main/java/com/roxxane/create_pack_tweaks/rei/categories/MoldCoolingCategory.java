package com.roxxane.create_pack_tweaks.rei.categories;

import com.google.common.collect.Lists;
import com.roxxane.create_pack_tweaks.Cpt;
import com.roxxane.create_pack_tweaks.blocks.FillableMoldBlock;
import com.roxxane.create_pack_tweaks.rei.CptReiClientPlugin;
import com.roxxane.create_pack_tweaks.rei.displays.MoldCoolingDisplay;
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
import net.minecraft.world.item.ItemStack;
import oshi.util.tuples.Pair;

import java.util.List;

public class MoldCoolingCategory implements DisplayCategory<MoldCoolingDisplay> {
    @Override
    public CategoryIdentifier<? extends MoldCoolingDisplay> getCategoryIdentifier() {
        return CptReiClientPlugin.moldCoolingCategory;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("category.create_pack_tweaks.mold_cooling");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(AllBlocks.ENCASED_FAN);
    }

    @Override
    public List<Widget> setupDisplay(MoldCoolingDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() + 32);
        List<Widget> widgets = Lists.newArrayList();

        widgets.add(Widgets.createRecipeBase(new Rectangle(
            bounds.x - 25,
            bounds.y,
            bounds.width + 50,
            bounds.height + 50
        )));

        // If this somehow isn't an ItemStack then may god have mercy on your soul
        var inputItem = (ItemStack) display.getInputEntries().get(0).get(0).cast().getValue();
        var outputItem = (ItemStack) display.getOutputEntries().get(0).get(0).getValue();
        var inputMaterial = FillableMoldBlock.getMaterialFromItem(inputItem.getItem());
        var outputMaterial = FillableMoldBlock.getMaterialFromItem(outputItem.getItem());

        widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 13)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 14))
            .entries(display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 14))
            .entries(display.getOutputEntries().get(0)).markOutput());

        for (var pair : List.of(
            new Pair<>(Cpt.makeResLoc("textures/gui/fan_cooling.png"), new Rectangle(4, -37, 16, 32)),
            new Pair<>(Cpt.makeResLoc("textures/gui/mold/mushy.png"), new Rectangle(4, -4, 16, 16)),
            new Pair<>(Cpt.makeResLoc("textures/gui/mold_material/" + inputMaterial + ".png"), new Rectangle(4, -4, 16, 16)),
            new Pair<>(Cpt.makeResLoc("textures/gui/fan_cooling.png"), new Rectangle(61, -37, 16, 32)),
            new Pair<>(Cpt.makeResLoc("textures/gui/mold/mushy.png"), new Rectangle(61, -4, 16, 16)),
            new Pair<>(Cpt.makeResLoc("textures/gui/mold_material/" + outputMaterial + ".png"), new Rectangle(61, -4, 16, 16))
        )) {
            var rectangle = pair.getB();
            widgets.add(Widgets.createTexturedWidget(pair.getA(), startPoint.x + rectangle.x,
                startPoint.y + rectangle.y, 0.0f, 0.0f, rectangle.width, rectangle.height, rectangle.width,
                rectangle.height));
        }
        return widgets;
    }
}