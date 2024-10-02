package com.roxxane.create_pack_tweaks.mixins;

import net.minecraftforge.fluids.FluidInteractionRegistry;
import net.minecraftforge.fluids.FluidInteractionRegistry.InteractionInformation;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(FluidInteractionRegistry.class)
abstract class FluidInteractionRegistryMixin {
	/**
	 * @author Roxxane
	 * @reason Lazily remove all liquid interaction. If you bitch about this fuck you.
	 */
	@Overwrite(remap = false)
	public static synchronized void addInteraction(FluidType source, InteractionInformation interaction) {}
}
