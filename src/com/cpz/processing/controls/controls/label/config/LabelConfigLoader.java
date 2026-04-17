package com.cpz.processing.controls.controls.label.config;

import com.cpz.processing.controls.controls.label.style.HorizontalAlign;
import com.cpz.processing.controls.controls.label.style.VerticalAlign;
import com.cpz.processing.controls.core.util.JsonConfigSupport;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.util.Locale;
import java.util.Objects;

/**
 * Loads a minimal label config from a JSON file.
 */
public final class LabelConfigLoader {
    private final PApplet sketch;

    public LabelConfigLoader(PApplet sketch) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
    }

    public LabelConfig load(String path) {
        Objects.requireNonNull(path, "path");
        JSONObject root = this.sketch.loadJSONObject(path);
        if (root == null) {
            throw new IllegalArgumentException("Could not load label JSON config: " + path);
        }

        float width = root.getFloat("width");
        float height = root.getFloat("height");
        JsonConfigSupport.validatePositiveDimension("width", width, path);
        JsonConfigSupport.validatePositiveDimension("height", height, path);

        return new LabelConfig(
                JsonConfigSupport.getRequiredString(root, "code", path, "label"),
                root.getString("text", ""),
                root.getFloat("x"),
                root.getFloat("y"),
                width,
                height,
                root.getBoolean("enabled", true),
                root.getBoolean("visible", true),
                this.readStyle(root, path)
        );
    }

    private LabelConfig.StyleConfig readStyle(JSONObject root, String path) {
        if (!root.hasKey("style") || root.isNull("style")) {
            return null;
        }

        JSONObject style = root.getJSONObject("style");
        return new LabelConfig.StyleConfig(
                JsonConfigSupport.getOptionalFloat(style, "textSize"),
                JsonConfigSupport.getOptionalColor(style, "textColor", path),
                JsonConfigSupport.getOptionalFloat(style, "lineSpacingMultiplier"),
                readHorizontalAlign(style, path),
                readVerticalAlign(style, path),
                JsonConfigSupport.getOptionalInt(style, "disabledAlpha")
        );
    }

    private HorizontalAlign readHorizontalAlign(JSONObject style, String path) {
        if (!style.hasKey("alignX") || style.isNull("alignX")) {
            return null;
        }
        String raw = style.getString("alignX");
        String normalized = normalizeEnum(raw);
        if ("START".equals(normalized)) {
            return HorizontalAlign.START;
        }
        if ("CENTER".equals(normalized)) {
            return HorizontalAlign.CENTER;
        }
        if ("END".equals(normalized)) {
            return HorizontalAlign.END;
        }
        throw new IllegalArgumentException("Unsupported label alignX in " + path + ": " + raw + ". Supported values: start, center, end.");
    }

    private VerticalAlign readVerticalAlign(JSONObject style, String path) {
        if (!style.hasKey("alignY") || style.isNull("alignY")) {
            return null;
        }
        String raw = style.getString("alignY");
        String normalized = normalizeEnum(raw);
        if ("TOP".equals(normalized)) {
            return VerticalAlign.TOP;
        }
        if ("CENTER".equals(normalized)) {
            return VerticalAlign.CENTER;
        }
        if ("BOTTOM".equals(normalized)) {
            return VerticalAlign.BOTTOM;
        }
        if ("BASELINE".equals(normalized)) {
            return VerticalAlign.BASELINE;
        }
        throw new IllegalArgumentException("Unsupported label alignY in " + path + ": " + raw + ". Supported values: top, center, bottom, baseline.");
    }

    private static String normalizeEnum(String raw) {
        return raw.trim().toUpperCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
    }
}
