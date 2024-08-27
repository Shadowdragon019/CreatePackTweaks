package com.roxxane.create_pack_tweaks.blocks.state_properties;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum MaterialState implements StringRepresentable {
    none("none"),
    mushyBrick("mushy_brick"),
    mushyPaste("mushy_paste");

    public final String name;

    MaterialState(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}
