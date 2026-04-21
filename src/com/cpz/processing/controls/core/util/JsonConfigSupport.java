package com.cpz.processing.controls.core.util;

import com.cpz.utils.color.Colors;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.Locale;
import java.util.Objects;

/**
 * Small shared helpers for config-driven JSON loading.
 *
 * @author CPZ
 */
public final class JsonConfigSupport {
    private JsonConfigSupport() {
    }

    public static JSONObject loadRequiredObject(PApplet sketch, String path, String context) {
        Objects.requireNonNull(sketch, "sketch");
        Objects.requireNonNull(path, "path");

        try {
            JSONObject root = sketch.loadJSONObject(path);
            if (root == null) {
                throw new IllegalArgumentException("Could not load " + context + " JSON config: " + path);
            }
            return root;
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Could not load " + context + " JSON config: " + path, ex);
        }
    }

    public static JSONObject unwrapSingleControlDocument(JSONObject root, String path, String expectedType, String context) {
        Objects.requireNonNull(root, "root");
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(expectedType, "expectedType");
        Objects.requireNonNull(context, "context");

        if (!root.hasKey("controls")) {
            return root;
        }

        JSONArray controls = getRequiredArray(root, "controls", path);
        if (controls.size() != 1) {
            throw new IllegalArgumentException(
                    "Invalid 'controls' value in " + path + " for " + context
                            + ". Expected exactly one control entry when using the control-specific loader."
            );
        }

        Object rawControl = controls.get(0);
        if (!(rawControl instanceof JSONObject)) {
            throw new IllegalArgumentException(
                    "Invalid 'controls[0]' value in " + path + " for " + context + ". Expected an object."
            );
        }

        JSONObject controlJson = (JSONObject) rawControl;
        String controlPath = path + " -> controls[0]";
        String rawType = getRequiredString(controlJson, "type", controlPath, "control");
        String normalizedType = normalizeControlType(rawType);
        String normalizedExpectedType = normalizeControlType(expectedType);
        if (!normalizedExpectedType.equals(normalizedType)) {
            throw new IllegalArgumentException(
                    "Invalid 'type' value in " + controlPath + ": " + rawType
                            + ". Expected '" + expectedType + "' for " + context + "."
            );
        }

        return controlJson;
    }

    public static JSONArray getRequiredArray(JSONObject json, String key, String path) {
        if (!json.hasKey(key) || json.isNull(key)) {
            throw new IllegalArgumentException("Missing required key '" + key + "' in " + path + ".");
        }

        Object value = json.get(key);
        if (!(value instanceof JSONArray)) {
            throw new IllegalArgumentException(
                    "Invalid '" + key + "' value in " + path + ": expected an array."
            );
        }

        return (JSONArray) value;
    }

    public static String normalizeControlType(String value) {
        Objects.requireNonNull(value, "value");
        return value.trim().toLowerCase(Locale.ROOT);
    }

    public static Integer getOptionalColor(JSONObject json, String key, String path) {
        if (!json.hasKey(key) || json.isNull(key)) {
            return null;
        }

        return parseColorValue(json.get(key), key, path);
    }

    public static Integer[] getOptionalColorArray(JSONObject json, String key, String path) {
        if (!json.hasKey(key) || json.isNull(key)) {
            return null;
        }

        JSONArray array = json.getJSONArray(key);
        Integer[] result = new Integer[array.size()];
        for (int i = 0; i < array.size(); i++) {
            result[i] = parseColorValue(array.get(i), key + "[" + i + "]", path);
        }
        return result;
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

    public static String getRequiredNonBlankString(JSONObject json, String key, String path, String context) {
        String value = getRequiredString(json, key, path, context);
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Invalid '" + key + "' value in " + path + " for " + context + ": expected a non-blank string."
            );
        }
        return value;
    }

    public static void validatePositiveDimension(String key, float value, String path) {
        if (value <= 0.0f) {
            throw new IllegalArgumentException("Invalid '" + key + "' value in " + path + ": " + value + ". Expected a number greater than 0.");
        }
    }

    public static Integer parseColorValue(Object value, String key, String path) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            return parseColor((String) value, key, path);
        }
        throw new IllegalArgumentException("Unsupported color value for key '" + key + "' in " + path + ": " + value + ". Expected integer or hex string.");
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
