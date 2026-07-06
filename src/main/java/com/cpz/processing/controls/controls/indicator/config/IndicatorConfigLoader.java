package com.cpz.processing.controls.controls.indicator.config;

import com.cpz.processing.controls.controls.indicator.Indicator;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipJsonSupport;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.util.JsonConfigSupport;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import processing.core.PApplet;
import processing.data.JSONObject;

/**
 * Loads indicator config from JSON.
 *
 * @author CPZ
 */
public final class IndicatorConfigLoader {
    private final PApplet sketch;
    private final Map<String, TooltipStyleConfig> tooltipStyles;

    public IndicatorConfigLoader(PApplet sketch) {
        this(sketch, Collections.emptyMap());
    }

    public IndicatorConfigLoader(PApplet sketch, Map<String, TooltipStyleConfig> tooltipStyles) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
        this.tooltipStyles = tooltipStyles == null ? Collections.emptyMap() : tooltipStyles;
    }

    public IndicatorConfig load(String path) {
        Objects.requireNonNull(path, "path");
        JSONObject root = JsonConfigSupport.unwrapSingleControlDocument(
                JsonConfigSupport.loadRequiredObject(this.sketch, path, "indicator"),
                path,
                "indicator",
                "indicator"
        );
        return this.loadFromJson(root, path);
    }

    public IndicatorConfig loadFromJson(JSONObject root, String path) {
        return new IndicatorConfig(
                JsonConfigSupport.getRequiredString(root, "code", path, "indicator"),
                root.getBoolean("on", false),
                this.readColor(root, "onColor", Indicator.DEFAULT_ON_COLOR, path),
                this.readColor(root, "offColor", Indicator.DEFAULT_OFF_COLOR, path),
                JsonConfigSupport.getControlBounds(root, path, "indicator"),
                root.getBoolean("enabled", true),
                root.getBoolean("visible", true),
                this.readRenderer(root, path),
                TooltipJsonSupport.readTooltip(this.sketch, root, path, this.tooltipStyles)
        );
    }

    private int readColor(JSONObject root, String key, int defaultColor, String path) {
        Integer topLevel = JsonConfigSupport.getOptionalColor(root, key, path);
        if (topLevel != null) {
            return topLevel;
        }
        if (!root.hasKey("style") || root.isNull("style")) {
            return defaultColor;
        }
        Integer styled = JsonConfigSupport.getOptionalColor(root.getJSONObject("style"), key, path);
        return styled != null ? styled : defaultColor;
    }

    private IndicatorConfig.RendererConfig readRenderer(JSONObject root, String path) {
        if (!root.hasKey("style") || root.isNull("style")) {
            return null;
        }
        JSONObject style = root.getJSONObject("style");
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

        return new IndicatorConfig.RendererConfig(normalizedType, normalizedPath);
    }
}
