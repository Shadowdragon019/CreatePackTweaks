package com.roxxane.create_pack_tweaks;

import com.google.common.collect.ImmutableMap;
import com.roxxane.create_pack_tweaks.blocks.CptBlocks;
import com.roxxane.create_pack_tweaks.blocks.FillableMoldBlock;
import com.simibubi.create.AllItems;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.List;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class CptClientEvents {
    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event) {
        var stack = event.getItemStack();
        var tooltip = event.getToolTip();

        if (stack.getItem().isFireResistant())
            tooltip.add(Component.translatable("item.tooltip.create_pack_tweaks.fire_proof"));

        if (ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) >= 1)
            tooltip.add(Component.translatable("item.tooltip.create_pack_tweaks.fuel"));

        for (var entry : new ImmutableMap.Builder<Collection<Item>, String>()
            .put(List.of(AllItems.WRENCH.get()), "wrench")
            .put(FillableMoldBlock.materialItemMap.values().stream().map(NonNullSupplier::get).toList(), "goes_in_mold")
            .put(List.of(CptBlocks.mushyMold.asItem()), "mold")
        .build().entrySet())
            for (var item : entry.getKey())
                if (stack.is(item))
                    tooltip.add(Component.translatable("item.tooltip.create_pack_tweaks." + entry.getValue()));
    }
}
