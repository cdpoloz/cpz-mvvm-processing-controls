package com.cpz.processing.controls.controls.progressbar.config;

import com.cpz.processing.controls.controls.progressbar.ProgressBar;
import com.cpz.processing.controls.controls.progressbar.ProgressBarFillDirection;
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
 * Loads progress bar config from JSON.
 *
 * @author CPZ
 */
public final class ProgressBarConfigLoader {
    private final PApplet sketch;
    private final Map<String, TooltipStyleConfig> tooltipStyles;

    public ProgressBarConfigLoader(PApplet sketch) {
        this(sketch, Collections.emptyMap());
    }

    public ProgressBarConfigLoader(PApplet sketch, Map<String, TooltipStyleConfig> tooltipStyles) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
        this.tooltipStyles = tooltipStyles == null ? Collections.emptyMap() : tooltipStyles;
    }

    public ProgressBarConfig load(String path) {
        Objects.requireNonNull(path, "path");
        JSONObject root = JsonConfigSupport.unwrapSingleControlDocument(
                JsonConfigSupport.loadRequiredObject(this.sketch, path, "progressbar"),
                path,
                "progressbar",
                "progressbar"
        );
        return this.loadFromJson(root, path);
    }

    public ProgressBarConfig loadFromJson(JSONObject root, String path) {
        Float min = JsonConfigSupport.getOptionalFloat(root, "min");
        Float max = JsonConfigSupport.getOptionalFloat(root, "max");
        Float value = JsonConfigSupport.getOptionalFloat(root, "value");

        return new ProgressBarConfig(
                JsonConfigSupport.getRequiredString(root, "code", path, "progressbar"),
                min != null ? min : 0.0F,
                max != null ? max : 1.0F,
                value != null ? value : 0.0F,
                this.readColor(root, "trackColor", ProgressBar.DEFAULT_TRACK_COLOR, path),
                this.readColor(root, "fillColor", ProgressBar.DEFAULT_FILL_COLOR, path),
                this.readStyleColor(root, "strokeColor", ProgressBar.DEFAULT_STROKE_COLOR, path),
                this.readStrokeWeight(root),
                this.readFillDirection(root),
                JsonConfigSupport.getControlBounds(root, path, "progressbar"),
                root.getBoolean("enabled", true),
                root.getBoolean("visible", true),
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
        Float strokeWeight = JsonConfigSupport.getOptionalFloat(root.getJSONObject("style"), "strokeWeight");
        return strokeWeight != null ? Math.max(0.0F, strokeWeight) : 1.0F;
    }

    private ProgressBarFillDirection readFillDirection(JSONObject root) {
        Object raw = this.readTopLevelOrStyleValue(root, "fillDirection");
        return parseFillDirection(raw);
    }

    private Object readTopLevelOrStyleValue(JSONObject root, String key) {
        if (root.hasKey(key) && !root.isNull(key)) {
            return root.get(key);
        }
        if (!root.hasKey("style") || root.isNull("style")) {
            return null;
        }
        JSONObject style = root.getJSONObject("style");
        return style.hasKey(key) && !style.isNull(key) ? style.get(key) : null;
    }

    private static ProgressBarFillDirection parseFillDirection(Object raw) {
        if (raw == null) {
            return ProgressBar.DEFAULT_FILL_DIRECTION;
        }
        String normalized = String.valueOf(raw)
                .trim()
                .toUpperCase(Locale.ROOT)
                .replace('-', '_')
                .replace(' ', '_');
        try {
            return ProgressBarFillDirection.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            return ProgressBar.DEFAULT_FILL_DIRECTION;
        }
    }
}
