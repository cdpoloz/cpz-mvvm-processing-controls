package com.cpz.processing.controls.controls.button.config;

import com.cpz.processing.controls.core.util.Colors;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.util.Objects;
import java.util.Locale;

/**
 * Loads a minimal button config from a JSON file.
 */
public final class ButtonConfigLoader {
    private final PApplet sketch;

    public ButtonConfigLoader(PApplet sketch) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
    }

    public ButtonConfig load(String path) {
        Objects.requireNonNull(path, "path");
        JSONObject root = this.sketch.loadJSONObject(path);
        if (root == null) {
            throw new IllegalArgumentException("Could not load button JSON config: " + path);
        }

        float width = root.getFloat("width");
        float height = root.getFloat("height");
        this.validatePositiveDimension("width", width, path);
        this.validatePositiveDimension("height", height, path);

        return new ButtonConfig(
                root.getString("text"),
                root.getFloat("x"),
                root.getFloat("y"),
                width,
                height,
                root.getBoolean("enabled", true),
                root.getBoolean("visible", true),
                this.readStyle(root, path)
        );
    }

    private ButtonConfig.StyleConfig readStyle(JSONObject root, String path) {
        if (!root.hasKey("style") || root.isNull("style")) {
            return null;
        }

        JSONObject style = root.getJSONObject("style");
        return new ButtonConfig.StyleConfig(
                this.getOptionalColor(style, "baseColor", path),
                this.getOptionalColor(style, "textColor", path),
                this.getOptionalColor(style, "strokeColor", path),
                this.getOptionalFloat(style, "strokeWeight"),
                this.getOptionalFloat(style, "strokeWeightHover"),
                this.getOptionalFloat(style, "cornerRadius"),
                this.getOptionalInt(style, "disabledAlpha"),
                this.getOptionalFloat(style, "hoverBlendWithWhite"),
                this.getOptionalFloat(style, "pressedBlendWithBlack"),
                this.readRenderer(style, path)
        );
    }

    private ButtonConfig.RendererConfig readRenderer(JSONObject style, String path) {
        if (!style.hasKey("renderer") || style.isNull("renderer")) {
            return null;
        }

        JSONObject renderer = style.getJSONObject("renderer");
        String type = this.getRequiredString(renderer, "type", path, "style.renderer");
        String normalizedType = type.trim().toLowerCase(Locale.ROOT);
        if (!"svg".equals(normalizedType)) {
            throw new IllegalArgumentException("Unsupported renderer type in " + path + ": " + type + ". Supported values in this iteration: svg.");
        }

        String rendererPath = this.getRequiredString(renderer, "path", path, "style.renderer");
        String normalizedPath = rendererPath.trim();
        if (normalizedPath.isEmpty()) {
            throw new IllegalArgumentException("Invalid 'path' value in " + path + " for style.renderer: \"" + rendererPath + "\". Expected a non-empty SVG path.");
        }

        return new ButtonConfig.RendererConfig(normalizedType, normalizedPath);
    }

    private Integer getOptionalColor(JSONObject json, String key, String path) {
        if (!json.hasKey(key) || json.isNull(key)) {
            return null;
        }

        Object value = json.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            return this.parseColor((String) value, key, path);
        }
        throw new IllegalArgumentException("Unsupported color value for key '" + key + "' in " + path + ": " + value + ". Expected integer or hex string.");
    }

    private Integer getOptionalInt(JSONObject json, String key) {
        if (!json.hasKey(key) || json.isNull(key)) {
            return null;
        }
        return json.getInt(key);
    }

    private Float getOptionalFloat(JSONObject json, String key) {
        if (!json.hasKey(key) || json.isNull(key)) {
            return null;
        }
        return json.getFloat(key);
    }

    private String getRequiredString(JSONObject json, String key, String path, String context) {
        if (!json.hasKey(key) || json.isNull(key)) {
            throw new IllegalArgumentException("Missing required key '" + key + "' in " + path + " for " + context + ".");
        }
        return json.getString(key);
    }

    private int parseColor(String value, String key, String path) {
        String normalized = value.trim();
        if (normalized.startsWith("#")) {
            normalized = normalized.substring(1);
        }

        if (normalized.length() == 6) {
            int rgb = (int) Long.parseLong(normalized, 16);
            return Colors.rgb((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255);
        }
        // 8-digit hexadecimal colors use the #AARRGGBB format.
        if (normalized.length() == 8) {
            return (int) Long.parseLong(normalized, 16);
        }

        throw new IllegalArgumentException("Unsupported color format for key '" + key + "' in " + path + ": " + value + ". Expected #RRGGBB or #AARRGGBB.");
    }

    private void validatePositiveDimension(String key, float value, String path) {
        if (value <= 0.0f) {
            throw new IllegalArgumentException("Invalid '" + key + "' value in " + path + ": " + value + ". Expected a number greater than 0.");
        }
    }
}
