package com.roxxane.create_pack_tweaks.blocks.state_properties;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public enum ShapeState implements StringRepresentable {
    ingot("ingot"),
    test("test");

    public final String name;

    ShapeState(String name) {
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
