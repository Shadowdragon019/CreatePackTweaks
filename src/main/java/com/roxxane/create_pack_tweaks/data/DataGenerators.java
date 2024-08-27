package com.roxxane.create_pack_tweaks.data;

import com.roxxane.create_pack_tweaks.CreatePackTweaks;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreatePackTweaks.id, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();

        generator.addProvider(
            event.includeServer(),
            new CptWorldGenProvider(output, lookupProvider)
        );
    }
}
