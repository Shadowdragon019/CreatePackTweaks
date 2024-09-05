package com.roxxane.create_pack_tweaks;

import com.mojang.logging.LogUtils;
import com.roxxane.create_pack_tweaks.blocks.CptBlocks;
import com.roxxane.create_pack_tweaks.blocks.entities.CptBlockEntities;
import com.roxxane.create_pack_tweaks.items.CptItems;
import com.roxxane.create_pack_tweaks.worldgen.features.CptFeatures;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import com.tterrag.registrate.Registrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// TODO: Category for mold fan interaction
@SuppressWarnings("unused")
@Mod(Cpt.id)
public class Cpt {
    public static final String id = "create_pack_tweaks";
    public static final String displayName = "Create Pack Tweaks";
    public static final Logger logger = LogUtils.getLogger();
    public static final Registrate registrate = Registrate.create(id);

    public Cpt() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CptBlocks.register();
        CptItems.register();
        CptBlockEntities.register();
        CptFeatures.register(modEventBus);

        MinecraftForge.EVENT_BUS.addListener(this::wrench);

        GogglesItem.addIsWearingPredicate((player) -> true);

        registrate.addRawLang("item.tooltip.create_pack_tweaks.goes_in_mold", "§8Can be placed in molds");
        registrate.addRawLang("item.tooltip.create_pack_tweaks.mold",
            "§8Processing has a 5% chance to break the mold");
        registrate.addRawLang("category.create_pack_tweaks.mold_heating", "Mold Heating");
        registrate.addRawLang("category.create_pack_tweaks.mold_cooling", "Mold Cooling");
        registrate.addRawLang("category.create_pack_tweaks.drilling", "Drilling");
        registrate.addRawLang("chat.create_pack_tweaks.reload_error", "§c" + displayName + " failed to reload!");
    }

    public void wrench(PlayerInteractEvent.RightClickBlock event) {
        var level = event.getLevel();
        var player = event.getEntity();
        var hand = event.getHand();
        var item = event.getEntity().getItemInHand(hand);
        var hitResult = event.getHitVec();

        if (item.isEmpty() && hand == InteractionHand.MAIN_HAND)
            if (AllItems.WRENCH.get().useOn(new UseOnContext(level, player, hand, item, hitResult))
                == InteractionResult.SUCCESS)
                player.swing(hand);
    }

    public static ResourceLocation makeResLoc(String path) {
        return new ResourceLocation(id, path);
    }
}