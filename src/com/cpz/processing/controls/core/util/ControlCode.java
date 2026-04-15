package com.cpz.processing.controls.core.util;

import java.util.Objects;
import java.util.UUID;

/**
 * Small helper for backward-compatible automatic control codes.
 */
public final class ControlCode {
    private ControlCode() {
    }

    public static String auto(String prefix) {
        Objects.requireNonNull(prefix, "prefix");
        return prefix + "-" + UUID.randomUUID();
    }
}
