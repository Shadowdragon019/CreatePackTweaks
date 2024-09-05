package com.roxxane.create_pack_tweaks.blocks.state_properties;

import net.minecraft.world.level.block.state.properties.EnumProperty;

public class CptStateProperties {
    public static final EnumProperty<MaterialState> material =
        EnumProperty.create("material", MaterialState.class);
}
