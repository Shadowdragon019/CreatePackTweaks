package com.roxxane.create_pack_tweaks;

import com.roxxane.create_pack_tweaks.blocks.FillableMoldBlock;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class CptClientEvents {
    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event) {
        for (var item : FillableMoldBlock.materialItemMap.values())
            if (event.getItemStack().is(item.get()))
                event.getToolTip().add(Component.translatable("chat.create_pack_tweaks.goes_in_mold"));
    }
}
