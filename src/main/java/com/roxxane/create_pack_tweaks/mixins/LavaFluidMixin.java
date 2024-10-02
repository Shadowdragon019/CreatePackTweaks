package com.roxxane.create_pack_tweaks.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.LavaFluid;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LavaFluid.class)
abstract class LavaFluidMixin extends FlowingFluid {
	/**
	 * @author Roxxane
	 * @reason Lazily remove all liquid interaction. If you bitch about this fuck you.
	 */
	@Overwrite
	protected void spreadTo(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState blockState,
	    @NotNull Direction direction, @NotNull FluidState fluidState
	) {
		super.spreadTo(level, pos, blockState, direction, fluidState);
	}
}
