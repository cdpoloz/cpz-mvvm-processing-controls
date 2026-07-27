package com.cpz.processing.controls.controls.textfield.config;

import com.cpz.processing.controls.core.util.JsonConfigSupport;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipJsonSupport;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Loads a minimal text field config from a JSON file.
 *
 * @author CPZ
 */
public final class TextFieldConfigLoader {
    private final PApplet sketch;
    private final Map<String, TooltipStyleConfig> tooltipStyles;

    public TextFieldConfigLoader(PApplet sketch) {
        this(sketch, Collections.emptyMap());
    }

    public TextFieldConfigLoader(PApplet sketch, Map<String, TooltipStyleConfig> tooltipStyles) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
        this.tooltipStyles = tooltipStyles == null ? Collections.emptyMap() : tooltipStyles;
    }

    public TextFieldConfig load(String path) {
        Objects.requireNonNull(path, "path");
        JSONObject root = JsonConfigSupport.unwrapSingleControlDocument(
                JsonConfigSupport.loadRequiredObject(this.sketch, path, "text field"),
                path,
                "textfield",
                "textfield"
        );
        return this.loadFromJson(root, path);
    }

    public TextFieldConfig loadFromJson(JSONObject root, String path) {
        return new TextFieldConfig(
                JsonConfigSupport.getRequiredNonBlankString(root, "code", path, "textfield"),
                root.getString("text", ""),
                JsonConfigSupport.getControlBounds(root, path, "textfield"),
                JsonConfigSupport.getOptionalControlMeasure(root, "textSize", path, "textfield"),
                root.getBoolean("enabled", true),
                root.getBoolean("visible", true),
                this.readStyle(root, path),
                TooltipJsonSupport.readTooltip(this.sketch, root, path, this.tooltipStyles)
        );
    }

    private TextFieldConfig.StyleConfig readStyle(JSONObject root, String path) {
        if (!root.hasKey("style") || root.isNull("style")) {
            return null;
        }

        JSONObject style = root.getJSONObject("style");
        return new TextFieldConfig.StyleConfig(
                JsonConfigSupport.getOptionalColor(style, "backgroundColor", path),
                JsonConfigSupport.getOptionalColor(style, "borderColor", path),
                JsonConfigSupport.getOptionalColor(style, "textColor", path),
                JsonConfigSupport.getOptionalColor(style, "cursorColor", path),
                JsonConfigSupport.getOptionalColor(style, "selectionColor", path),
                JsonConfigSupport.getOptionalColor(style, "selectionTextColor", path),
                JsonConfigSupport.getOptionalFloat(style, "textSize"),
                JsonConfigSupport.getOptionalNonBlankString(style, "font", path, "text field style"),
                path
        );
    }
}
