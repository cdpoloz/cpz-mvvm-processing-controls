package com.cpz.processing.controls.core.util;

import processing.data.JSONObject;

/**
 * Small shared helpers for config-driven JSON loading.
 */
public final class JsonConfigSupport {
    private JsonConfigSupport() {
    }

    public static Integer getOptionalColor(JSONObject json, String key, String path) {
        if (!json.hasKey(key) || json.isNull(key)) {
            return null;
        }

        Object value = json.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            return parseColor((String) value, key, path);
        }
        throw new IllegalArgumentException("Unsupported color value for key '" + key + "' in " + path + ": " + value + ". Expected integer or hex string.");
    }

    public static Integer getOptionalInt(JSONObject json, String key) {
        if (!json.hasKey(key) || json.isNull(key)) {
            return null;
        }
        return json.getInt(key);
    }

    public static Float getOptionalFloat(JSONObject json, String key) {
        if (!json.hasKey(key) || json.isNull(key)) {
            return null;
        }
        return json.getFloat(key);
    }

    public static String getRequiredString(JSONObject json, String key, String path, String context) {
        if (!json.hasKey(key) || json.isNull(key)) {
            throw new IllegalArgumentException("Missing required key '" + key + "' in " + path + " for " + context + ".");
        }
        return json.getString(key);
    }

    public static void validatePositiveDimension(String key, float value, String path) {
        if (value <= 0.0f) {
            throw new IllegalArgumentException("Invalid '" + key + "' value in " + path + ": " + value + ". Expected a number greater than 0.");
        }
    }

    private static int parseColor(String value, String key, String path) {
        String normalized = value.trim();
        if (normalized.startsWith("#")) {
            normalized = normalized.substring(1);
        }

        try {
            if (normalized.length() == 6) {
                int rgb = (int) Long.parseLong(normalized, 16);
                return Colors.rgb((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255);
            }
            // 8-digit hexadecimal colors use the #AARRGGBB format.
            if (normalized.length() == 8) {
                return (int) Long.parseLong(normalized, 16);
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Unsupported color format for key '" + key + "' in " + path + ": " + value + ". Expected #RRGGBB or #AARRGGBB.", ex);
        }

        throw new IllegalArgumentException("Unsupported color format for key '" + key + "' in " + path + ": " + value + ". Expected #RRGGBB or #AARRGGBB.");
    }
}
