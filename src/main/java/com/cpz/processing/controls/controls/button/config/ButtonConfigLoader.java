package com.cpz.processing.controls.controls.button.config;

import com.cpz.processing.controls.core.util.JsonConfigSupport;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipJsonSupport;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Loads a minimal button config from a JSON file.
 *
 * @author CPZ
 */
public final class ButtonConfigLoader {
    private final PApplet sketch;
    private final Map<String, TooltipStyleConfig> tooltipStyles;

    public ButtonConfigLoader(PApplet sketch) {
        this(sketch, Collections.emptyMap());
    }

    public ButtonConfigLoader(PApplet sketch, Map<String, TooltipStyleConfig> tooltipStyles) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
        this.tooltipStyles = tooltipStyles == null ? Collections.emptyMap() : tooltipStyles;
    }

    public ButtonConfig load(String path) {
        Objects.requireNonNull(path, "path");
        JSONObject root = JsonConfigSupport.unwrapSingleControlDocument(
                JsonConfigSupport.loadRequiredObject(this.sketch, path, "button"),
                path,
                "button",
                "button"
        );
        return this.loadFromJson(root, path);
    }

    public ButtonConfig loadFromJson(JSONObject root, String path) {
        return new ButtonConfig(
                JsonConfigSupport.getRequiredNonBlankString(root, "code", path, "button"),
                root.getString("text"),
                JsonConfigSupport.getControlBounds(root, path, "button"),
                JsonConfigSupport.getOptionalControlMeasure(root, "textSize", path, "button"),
                root.getBoolean("enabled", true),
                root.getBoolean("visible", true),
                this.readStyle(root, path),
                TooltipJsonSupport.readTooltip(this.sketch, root, path, this.tooltipStyles)
        );
    }

    private ButtonConfig.StyleConfig readStyle(JSONObject root, String path) {
        if (!root.hasKey("style") || root.isNull("style")) {
            return null;
        }

        JSONObject style = root.getJSONObject("style");
        return new ButtonConfig.StyleConfig(
                JsonConfigSupport.getOptionalColor(style, "baseColor", path),
                JsonConfigSupport.getOptionalColor(style, "textColor", path),
                JsonConfigSupport.getOptionalColor(style, "strokeColor", path),
                JsonConfigSupport.getOptionalFloat(style, "strokeWeight"),
                JsonConfigSupport.getOptionalFloat(style, "strokeWeightHover"),
                JsonConfigSupport.getOptionalFloat(style, "cornerRadius"),
                JsonConfigSupport.getOptionalInt(style, "disabledAlpha"),
                JsonConfigSupport.getOptionalFloat(style, "hoverBlendWithWhite"),
                JsonConfigSupport.getOptionalFloat(style, "pressedBlendWithBlack"),
                this.readRenderer(style, path),
                JsonConfigSupport.getOptionalNonBlankString(style, "font", path, "button style"),
                JsonConfigSupport.getOptionalFloat(style, "textSize"),
                path
        );
    }

    private ButtonConfig.RendererConfig readRenderer(JSONObject style, String path) {
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

        return new ButtonConfig.RendererConfig(normalizedType, normalizedPath);
    }
}
