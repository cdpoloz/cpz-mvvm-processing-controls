package com.cpz.processing.controls.controls.toggle.config;

import com.cpz.processing.controls.core.util.JsonConfigSupport;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.util.Locale;
import java.util.Objects;

/**
 * Loads a minimal toggle config from a JSON file.
 */
public final class ToggleConfigLoader {
    private final PApplet sketch;

    public ToggleConfigLoader(PApplet sketch) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
    }

    public ToggleConfig load(String path) {
        Objects.requireNonNull(path, "path");
        JSONObject root = this.sketch.loadJSONObject(path);
        if (root == null) {
            throw new IllegalArgumentException("Could not load toggle JSON config: " + path);
        }

        float width = root.getFloat("width");
        float height = root.getFloat("height");
        JsonConfigSupport.validatePositiveDimension("width", width, path);
        JsonConfigSupport.validatePositiveDimension("height", height, path);

        int totalStates = root.getInt("totalStates", 2);
        if (totalStates <= 0) {
            throw new IllegalArgumentException("Invalid 'totalStates' value in " + path + ": " + totalStates + ". Expected an integer greater than 0.");
        }

        int state = root.getInt("state", 0);
        if (state < 0 || state >= totalStates) {
            throw new IllegalArgumentException("Invalid 'state' value in " + path + ": " + state + ". Expected a value between 0 and " + (totalStates - 1) + ".");
        }

        return new ToggleConfig(
                JsonConfigSupport.getRequiredString(root, "code", path, "toggle"),
                state,
                totalStates,
                root.getFloat("x"),
                root.getFloat("y"),
                width,
                height,
                root.getBoolean("enabled", true),
                root.getBoolean("visible", true),
                this.readStyle(root, path)
        );
    }

    private ToggleConfig.StyleConfig readStyle(JSONObject root, String path) {
        if (!root.hasKey("style") || root.isNull("style")) {
            return null;
        }

        JSONObject style = root.getJSONObject("style");
        return new ToggleConfig.StyleConfig(
                JsonConfigSupport.getOptionalColor(style, "offFillOverride", path),
                JsonConfigSupport.getOptionalColor(style, "onFillOverride", path),
                JsonConfigSupport.getOptionalColor(style, "hoverFillOverride", path),
                JsonConfigSupport.getOptionalColor(style, "pressedFillOverride", path),
                JsonConfigSupport.getOptionalColor(style, "strokeOverride", path),
                JsonConfigSupport.getOptionalColorArray(style, "stateColors", path),
                JsonConfigSupport.getOptionalColor(style, "strokeColor", path),
                JsonConfigSupport.getOptionalFloat(style, "strokeWidth"),
                JsonConfigSupport.getOptionalFloat(style, "strokeWidthHover"),
                JsonConfigSupport.getOptionalFloat(style, "hoverBlendWithWhite"),
                JsonConfigSupport.getOptionalFloat(style, "pressedBlendWithBlack"),
                JsonConfigSupport.getOptionalInt(style, "disabledAlpha"),
                this.readRenderer(style, path)
        );
    }

    private ToggleConfig.RendererConfig readRenderer(JSONObject style, String path) {
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

        return new ToggleConfig.RendererConfig(normalizedType, normalizedPath);
    }
}
