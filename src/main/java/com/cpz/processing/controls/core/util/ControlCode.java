package com.cpz.processing.controls.core.util;

import java.util.Objects;
import java.util.UUID;

/**
 * Small helper for backward-compatible automatic control codes.
 *
 * @author CPZ
 */
public final class ControlCode {
    private ControlCode() {
    }

    public static String auto(String prefix) {
        Objects.requireNonNull(prefix, "prefix");
        return prefix + "-" + UUID.randomUUID();
    }

    /**
     * Validates an explicit control code.
     *
     * @param code control identity
     * @return the validated code
     * @throws IllegalArgumentException when {@code code} is null, empty, or blank
     */
    public static String requireNonBlank(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("code must be a non-blank string.");
        }
        return code;
    }
}
