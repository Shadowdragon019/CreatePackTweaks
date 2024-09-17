package com.roxxane.create_pack_tweaks;

import java.util.function.Consumer;

public class Utils {
    // If it succeeds it return true
    @SuppressWarnings({"unchecked", "UnusedReturnValue"})
    public static <I, O> boolean attemptCastThen(I input, Consumer<O> function) {
        try {
            function.accept((O) input);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}
