package com.cpz.processing.controls.controls.checkbox.config;

import com.cpz.processing.controls.core.util.JsonConfigSupport;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.util.Locale;
import java.util.Objects;

/**
 * Loads a minimal checkbox config from a JSON file.
 */
public final class CheckboxConfigLoader {
    private final PApplet sketch;

    public CheckboxConfigLoader(PApplet sketch) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
    }

    public CheckboxConfig load(String path) {
        Objects.requireNonNull(path, "path");
        JSONObject root = this.sketch.loadJSONObject(path);
        if (root == null) {
            throw new IllegalArgumentException("Could not load checkbox JSON config: " + path);
        }

        float width = root.getFloat("width");
        float height = root.getFloat("height");
        JsonConfigSupport.validatePositiveDimension("width", width, path);
        JsonConfigSupport.validatePositiveDimension("height", height, path);

        return new CheckboxConfig(
                JsonConfigSupport.getRequiredString(root, "code", path, "checkbox"),
                root.getBoolean("checked", false),
                root.getFloat("x"),
                root.getFloat("y"),
                width,
                height,
                root.getBoolean("enabled", true),
                root.getBoolean("visible", true),
                this.readStyle(root, path)
        );
    }

    private CheckboxConfig.StyleConfig readStyle(JSONObject root, String path) {
        if (!root.hasKey("style") || root.isNull("style")) {
            return null;
        }

        JSONObject style = root.getJSONObject("style");
        return new CheckboxConfig.StyleConfig(
                JsonConfigSupport.getOptionalColor(style, "checkedFillOverride", path),
                JsonConfigSupport.getOptionalColor(style, "uncheckedFillOverride", path),
                JsonConfigSupport.getOptionalColor(style, "hoverFillOverride", path),
                JsonConfigSupport.getOptionalColor(style, "pressedFillOverride", path),
                JsonConfigSupport.getOptionalColor(style, "checkOverride", path),
                JsonConfigSupport.getOptionalColor(style, "strokeOverride", path),
                JsonConfigSupport.getOptionalColor(style, "boxColor", path),
                JsonConfigSupport.getOptionalColor(style, "boxHoverColor", path),
                JsonConfigSupport.getOptionalColor(style, "boxPressedColor", path),
                JsonConfigSupport.getOptionalColor(style, "checkColor", path),
                JsonConfigSupport.getOptionalColor(style, "borderColor", path),
                JsonConfigSupport.getOptionalFloat(style, "borderWidth"),
                JsonConfigSupport.getOptionalFloat(style, "borderWidthHover"),
                JsonConfigSupport.getOptionalFloat(style, "cornerRadius"),
                JsonConfigSupport.getOptionalInt(style, "disabledAlpha"),
                JsonConfigSupport.getOptionalFloat(style, "checkInset"),
                this.readRenderer(style, path)
        );
    }

    private CheckboxConfig.RendererConfig readRenderer(JSONObject style, String path) {
        if (!style.hasKey("renderer") || style.isNull("renderer")) {
            return null;
        }

        JSONObject renderer = style.getJSONObject("renderer");
        String type = JsonConfigSupport.getRequiredString(renderer, "type", path, "style.renderer");
        String normalizedType = type.trim().toLowerCase(Locale.ROOT);
        if (!"svg".equals(normalizedType)) {
            throw new IllegalArgumentException("Unsupported renderer type in " + path + ": " + type + ". Supported values in this iteration: svg.");
        }

        String rendererPath = JsonConfigSupport.getRequiredString(renderer, "path", path, "style.renderer");
        String normalizedPath = rendererPath.trim();
        if (normalizedPath.isEmpty()) {
            throw new IllegalArgumentException("Invalid 'path' value in " + path + " for style.renderer: \"" + rendererPath + "\". Expected a non-empty SVG path.");
        }

        return new CheckboxConfig.RendererConfig(normalizedType, normalizedPath);
    }
}
