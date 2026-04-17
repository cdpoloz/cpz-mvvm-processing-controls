package com.cpz.processing.controls.controls.textfield.config;

import com.cpz.processing.controls.core.util.JsonConfigSupport;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.util.Objects;

/**
 * Loads a minimal text field config from a JSON file.
 */
public final class TextFieldConfigLoader {
    private final PApplet sketch;

    public TextFieldConfigLoader(PApplet sketch) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
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
        float width = root.getFloat("width");
        float height = root.getFloat("height");
        JsonConfigSupport.validatePositiveDimension("width", width, path);
        JsonConfigSupport.validatePositiveDimension("height", height, path);

        return new TextFieldConfig(
                JsonConfigSupport.getRequiredString(root, "code", path, "textfield"),
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
                JsonConfigSupport.getOptionalFloat(style, "textSize")
        );
    }
}
