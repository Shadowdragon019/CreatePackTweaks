package com.roxxane.create_pack_tweaks.mixins;

import com.roxxane.create_pack_tweaks.CptConfig;
import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.drill.DrillBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBreakingKineticBlockEntity.class)
public abstract class BlockBreakingKineticBlockEntityMixin extends KineticBlockEntity {
    @Unique
    int createAutomatizedMod$oreDropTimer = 0;

    @Unique
    int createAutomatizedMod$maxOreDropTimerSpeed = 200 * 16;

    @Shadow(remap = false) protected BlockPos breakingPos;

    @Shadow(remap = false) protected abstract boolean shouldRun();

    @Shadow(remap = false) protected int breakerId;

    public BlockBreakingKineticBlockEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    //this is weirdly but not noticeably buggy because I can't *remove* progress (no, setting one to -1 doesn't fix the bugs)
    //to see what i mean, place an ore, place a drill & make it mine the ore, remove the power, then rotate the drill with a wrench or replace the block with any other (don't power it again)
    //MC eventually removes all (presumably inactive) block breaking progresses after a while
    @Inject(method = "tick", at = @At("HEAD"), remap = false)
    private void tickInject(CallbackInfo ci) {
        if ((BlockBreakingKineticBlockEntity) (Object) this instanceof DrillBlockEntity) {
            if (level.isClientSide || getSpeed() == 0 || !shouldRun() || level == null || breakingPos == null)
                return;

            Block blockToBreak = level.getBlockState(breakingPos).getBlock();
            if (!CptConfig.isLoaded() || CptConfig.drillingMap.get(blockToBreak) == null)
                createAutomatizedMod$oreDropTimer = 0;
            else {
                createAutomatizedMod$oreDropTimer += (int) (1 * Math.abs(getSpeed()));

                if (createAutomatizedMod$oreDropTimer > createAutomatizedMod$maxOreDropTimerSpeed) {
                    createAutomatizedMod$oreDropTimer = 0;
                    level.addFreshEntity(new ItemEntity(level,
                        breakingPos.getX() + 0.5,
                        breakingPos.getY() + 0.5,
                        breakingPos.getZ() + 0.5, new ItemStack(CptConfig.drillingMap.get(blockToBreak))));
                }
                level.destroyBlockProgress(breakerId, breakingPos,
                    createAutomatizedMod$oreDropTimer / (createAutomatizedMod$maxOreDropTimerSpeed / 10));
            }
        }
    }

    @Inject(method = "invalidate", at = @At("HEAD"), remap = false)
    protected void invalidateInject(CallbackInfo ci) {
        if (level != null && breakingPos != null) {
            level.destroyBlockProgress(breakerId, breakingPos, -1);
        }
    }
}
