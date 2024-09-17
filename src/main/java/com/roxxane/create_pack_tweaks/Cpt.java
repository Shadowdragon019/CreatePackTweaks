package com.roxxane.create_pack_tweaks;

import com.mojang.logging.LogUtils;
import com.roxxane.create_pack_tweaks.blocks.CptBlocks;
import com.roxxane.create_pack_tweaks.blocks.entities.CptBlockEntities;
import com.roxxane.create_pack_tweaks.items.CptItems;
import com.roxxane.create_pack_tweaks.mixin_inferfaces.MergeDelay;
import com.roxxane.create_pack_tweaks.worldgen.features.CptFeatures;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import com.tterrag.registrate.Registrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

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

        MinecraftForge.EVENT_BUS.addListener((TickEvent.PlayerTickEvent event) -> {
            var player = event.player;
            var movementModifier = player.getAttribute(Attributes.MOVEMENT_SPEED);

            assert movementModifier != null;
            movementModifier.removeModifiers();
            movementModifier.setBaseValue(0.25);
        });

        GogglesItem.addIsWearingPredicate((player) -> true);

        MinecraftForge.EVENT_BUS.addListener((LivingFallEvent event) -> smush(event.getEntity()));
        MinecraftForge.EVENT_BUS.addListener((PlayerFlyableFallEvent event) -> smush(event.getEntity()));

        registrate.addRawLang("item.tooltip.create_pack_tweaks.goes_in_mold", "§8Can be placed in molds");
        registrate.addRawLang("item.tooltip.create_pack_tweaks.fire_proof", "§8Lava proof");
        registrate.addRawLang("item.tooltip.create_pack_tweaks.fuel", "§8Regular fuel for blaze burners");
        registrate.addRawLang("item.tooltip.create_pack_tweaks.mold",
            "§8Processing has a 5% chance to break the mold");
        registrate.addRawLang("item.tooltip.create_pack_tweaks.wrench", "§8Can pickup anything destructible");
        registrate.addRawLang("category.create_pack_tweaks.mold_heating", "Mold Heating");
        registrate.addRawLang("category.create_pack_tweaks.mold_cooling", "Mold Cooling");
        registrate.addRawLang("category.create_pack_tweaks.drilling", "Drilling");
        registrate.addRawLang("category.create_pack_tweaks.lava_smelting", "Lava Smelting");
        registrate.addRawLang("category.create_pack_tweaks.smushing", "Smushing");
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

    public void smush(LivingEntity entity) {
        var level = entity.level();
        var pos = entity.position();
        var entities = level.getEntities(entity, AABB.ofSize(pos, 1, 1, 1));

        for (var foundEntity : entities) {
            if (foundEntity instanceof ItemEntity itemEntity) {
                var stack = itemEntity.getItem();
                if (stack.is(Tags.Items.MUSHROOMS)) {
                    stack.shrink(1);

                    itemEntity.setPickUpDelay(60);
                    ((MergeDelay) itemEntity).cpt$setMergeDelay(60);

                    level.addFreshEntity(
                        new ItemEntity(level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(),
                            CptItems.mushyPaste.asStack()));
                }
            }
        }
    }

    public static ResourceLocation makeResLoc(String path) {
        return new ResourceLocation(id, path);
    }
}