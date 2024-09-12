package com.roxxane.create_pack_tweaks;

import com.roxxane.create_pack_tweaks.blocks.CptBlocks;
import com.roxxane.create_pack_tweaks.blocks.FillableMoldBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class CptClientEvents {
    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event) {
        var stack = event.getItemStack();
        var tooltip = event.getToolTip();

        if (stack.getItem().isFireResistant())
            tooltip.add(Component.translatable("item.tooltip.create_pack_tweaks.fire_proof"));

        if (stack.is(CptBlocks.mushyMold.asItem()))
            tooltip.add(Component.translatable("item.tooltip.create_pack_tweaks.mold"));

        for (var item : FillableMoldBlock.materialItemMap.values())
            if (stack.is(item.get()))
                tooltip.add(Component.translatable("item.tooltip.create_pack_tweaks.goes_in_mold"));

        if (stack.getBurnTime(RecipeType.SMELTING) >= 1)
            tooltip.add(Component.translatable("item.tooltip.create_pack_tweaks.fuel"));
    }
}
