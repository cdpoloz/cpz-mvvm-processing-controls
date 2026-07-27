package com.cpz.processing.controls.controls.indicator.config;

import com.cpz.processing.controls.controls.indicator.Indicator;
import com.cpz.processing.controls.controls.indicator.style.IndicatorStyle;
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
                JsonConfigSupport.getRequiredNonBlankString(root, "code", path, "indicator"),
                root.getBoolean("on", false),
                this.readColor(root, "onColor", Indicator.DEFAULT_ON_COLOR, path),
                this.readColor(root, "offColor", Indicator.DEFAULT_OFF_COLOR, path),
                this.readStyleColor(root, "strokeColor", Indicator.DEFAULT_BORDER_COLOR, path),
                this.readStrokeWeight(root),
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

    private int readStyleColor(JSONObject root, String key, int defaultColor, String path) {
        if (!root.hasKey("style") || root.isNull("style")) {
            return defaultColor;
        }
        Integer styled = JsonConfigSupport.getOptionalColor(root.getJSONObject("style"), key, path);
        return styled != null ? styled : defaultColor;
    }

    private float readStrokeWeight(JSONObject root) {
        if (!root.hasKey("style") || root.isNull("style")) {
            return 1.0F;
        }
        JSONObject style = root.getJSONObject("style");
        Float strokeWeight = JsonConfigSupport.getOptionalFloat(style, "strokeWeight");
        if (strokeWeight == null) {
            strokeWeight = JsonConfigSupport.getOptionalFloat(style, "strokeWidth");
        }
        return strokeWeight != null ? Math.max(0.0F, strokeWeight) : 1.0F;
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
        String rendererPath = JsonConfigSupport.getRequiredString(renderer, "path", path, "style.renderer");
        try {
            String normalizedPath = IndicatorStyle.normalizeRendererPath(rendererPath);
            String validatedType = IndicatorStyle.normalizeRendererType(normalizedType, normalizedPath);
            return new IndicatorConfig.RendererConfig(validatedType, normalizedPath);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Invalid indicator renderer in " + path + " for style.renderer: " + exception.getMessage(),
                    exception
            );
        }
    }
}
