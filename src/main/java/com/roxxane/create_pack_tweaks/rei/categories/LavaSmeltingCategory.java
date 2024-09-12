package com.roxxane.create_pack_tweaks.rei.categories;

import com.google.common.collect.Lists;
import com.roxxane.create_pack_tweaks.rei.CptReiClientPlugin;
import com.roxxane.create_pack_tweaks.rei.displays.LavaSmeltingDisplay;
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
import net.minecraft.world.item.Items;

import java.util.List;

import static com.roxxane.create_pack_tweaks.CptConfig.lavaSmeltingLavaBucket;

public class LavaSmeltingCategory implements DisplayCategory<LavaSmeltingDisplay> {
    @Override
    public CategoryIdentifier<? extends LavaSmeltingDisplay> getCategoryIdentifier() {
        return CptReiClientPlugin.lavaSmeltingCategory;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("category.create_pack_tweaks.lava_smelting");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Items.LAVA_BUCKET);
    }

    @Override
    public List<Widget> setupDisplay(LavaSmeltingDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 13)));
        widgets.add(Widgets.createTexturedWidget(new ResourceLocation("textures/item/lava_bucket.png"),
            startPoint.x + lavaSmeltingLavaBucket.x, startPoint.y + lavaSmeltingLavaBucket.y, 0.0f, 0.0f, lavaSmeltingLavaBucket.width, lavaSmeltingLavaBucket.height, lavaSmeltingLavaBucket.width, lavaSmeltingLavaBucket.height));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 14)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 14)).entries(display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 14)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
        return widgets;
    }
}
