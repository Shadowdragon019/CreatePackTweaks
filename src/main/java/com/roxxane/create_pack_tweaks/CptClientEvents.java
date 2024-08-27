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
        var goesInMold = false;
        for (var shapeEntry : FillableMoldBlock.materialItemMap.entrySet()) {
            var shape = shapeEntry.getKey();
            for (var materialEntry : shapeEntry.getValue().entrySet()) {
                var item = materialEntry.getValue();

                if (event.getItemStack().is(item.get())) {
                    if (!goesInMold) {
                        goesInMold = true;
                        event.getToolTip().add(Component.translatable("chat.create_pack_tweaks.mold_contains"));
                    }
                    event.getToolTip().add(
                        Component.translatable("chat.create_pack_tweaks.mold_contains_" + shape.name));
                }
            }
        }
    }
}
