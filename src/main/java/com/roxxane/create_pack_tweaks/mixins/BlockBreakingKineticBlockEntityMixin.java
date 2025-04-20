package com.roxxane.create_pack_tweaks.mixins;

import com.roxxane.create_pack_tweaks.CptConfig;
import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.drill.DrillBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBreakingKineticBlockEntity.class)
abstract class BlockBreakingKineticBlockEntityMixin extends KineticBlockEntity {
	@Unique
	int fct$ore_drop_timer = 0;

	@Unique
	final int fct$max_ore_drop_timer_speed = 200 * 16;

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

			var block_to_break = level.getBlockState(breakingPos).getBlock();
			if (!CptConfig.isLoaded() || CptConfig.drillingMap.get(block_to_break) == null)
				fct$ore_drop_timer = 0;
			else {
				fct$ore_drop_timer += (int) (1 * Math.abs(getSpeed()));

				if (fct$ore_drop_timer > fct$max_ore_drop_timer_speed) {
					var drill_state = level.getBlockState(worldPosition);
					var drop_item_pos =
						worldPosition.relative(drill_state.getValue(BlockStateProperties.FACING), -1);

					level.addFreshEntity(new ItemEntity(level,
						drop_item_pos.getX() + 0.5,
						drop_item_pos.getY() + 0.5,
						drop_item_pos.getZ() + 0.5,
						new ItemStack(CptConfig.drillingMap.get(block_to_break))));

					fct$ore_drop_timer = 0;
				}
				level.destroyBlockProgress(breakerId, breakingPos,
					fct$ore_drop_timer / (fct$max_ore_drop_timer_speed / 10));
			}
		}
	}

	@Inject(method = "invalidate", at = @At("HEAD"), remap = false)
	protected void invalidateInject(CallbackInfo ci) {
		super.invalidate();
		// TODO: Test
		//if (level != null && breakingPos != null)
		//level.destroyBlockProgress(breakerId, breakingPos, -1);
	}
}
