package com.cpz.processing.controls.controls.numericfield.config;

import com.cpz.processing.controls.core.util.JsonConfigSupport;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipJsonSupport;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Loads a minimal numeric field config from a JSON file.
 *
 * @author CPZ
 */
public final class NumericFieldConfigLoader {
    private final PApplet sketch;
    private final Map<String, TooltipStyleConfig> tooltipStyles;

    public NumericFieldConfigLoader(PApplet sketch) {
        this(sketch, Collections.emptyMap());
    }

    public NumericFieldConfigLoader(PApplet sketch, Map<String, TooltipStyleConfig> tooltipStyles) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
        this.tooltipStyles = tooltipStyles == null ? Collections.emptyMap() : tooltipStyles;
    }

    public NumericFieldConfig load(String path) {
        Objects.requireNonNull(path, "path");
        JSONObject root = JsonConfigSupport.unwrapSingleControlDocument(
                JsonConfigSupport.loadRequiredObject(this.sketch, path, "numeric field"),
                path,
                "numericfield",
                "numericfield"
        );
        return this.loadFromJson(root, path);
    }

    public NumericFieldConfig loadFromJson(JSONObject root, String path) {
        String text = root.getString("text", "");
        validateText(text, path);

        return new NumericFieldConfig(
                JsonConfigSupport.getRequiredNonBlankString(root, "code", path, "numericfield"),
                text,
                JsonConfigSupport.getControlBounds(root, path, "numericfield"),
                JsonConfigSupport.getOptionalControlMeasure(root, "textSize", path, "numericfield"),
                root.getBoolean("enabled", true),
                root.getBoolean("visible", true),
                this.readStyle(root, path),
                TooltipJsonSupport.readTooltip(this.sketch, root, path, this.tooltipStyles)
        );
    }

    private NumericFieldConfig.StyleConfig readStyle(JSONObject root, String path) {
        if (!root.hasKey("style") || root.isNull("style")) {
            return null;
        }

        JSONObject style = root.getJSONObject("style");
        return new NumericFieldConfig.StyleConfig(
                JsonConfigSupport.getOptionalColor(style, "backgroundColor", path),
                JsonConfigSupport.getOptionalColor(style, "borderColor", path),
                JsonConfigSupport.getOptionalColor(style, "textColor", path),
                JsonConfigSupport.getOptionalColor(style, "cursorColor", path),
                JsonConfigSupport.getOptionalColor(style, "selectionColor", path),
                JsonConfigSupport.getOptionalColor(style, "selectionTextColor", path),
                JsonConfigSupport.getOptionalFloat(style, "textSize"),
                JsonConfigSupport.getOptionalNonBlankString(style, "font", path, "numeric field style"),
                path
        );
    }

    private static void validateText(String text, String path) {
        int dots = 0;
        int minus = 0;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isDigit(ch)) {
                continue;
            }
            if (ch == '.') {
                dots++;
                if (dots > 1) {
                    throw invalidText(path, text);
                }
                continue;
            }
            if (ch == '-') {
                minus++;
                if (minus > 1 || i != 0) {
                    throw invalidText(path, text);
                }
                continue;
            }
            throw invalidText(path, text);
        }
    }

    private static IllegalArgumentException invalidText(String path, String text) {
        return new IllegalArgumentException(
                "Invalid numeric field text in " + path + ": " + text
                        + ". Expected only digits, an optional leading '-', and at most one '.'."
        );
    }
}
