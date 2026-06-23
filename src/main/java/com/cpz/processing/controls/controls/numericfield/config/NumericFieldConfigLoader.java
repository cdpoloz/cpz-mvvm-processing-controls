package com.cpz.processing.controls.controls.numericfield.config;

import com.cpz.processing.controls.core.util.JsonConfigSupport;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.util.Objects;

/**
 * Loads a minimal numeric field config from a JSON file.
 *
 * @author CPZ
 */
public final class NumericFieldConfigLoader {
    private final PApplet sketch;

    public NumericFieldConfigLoader(PApplet sketch) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
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
        float width = root.getFloat("width");
        float height = root.getFloat("height");
        JsonConfigSupport.validatePositiveDimension("width", width, path);
        JsonConfigSupport.validatePositiveDimension("height", height, path);

        String text = root.getString("text", "");
        validateText(text, path);

        return new NumericFieldConfig(
                JsonConfigSupport.getRequiredString(root, "code", path, "numericfield"),
                text,
                root.getFloat("x"),
                root.getFloat("y"),
                width,
                height,
                root.getBoolean("enabled", true),
                root.getBoolean("visible", true),
                this.readStyle(root, path)
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
                JsonConfigSupport.getOptionalFloat(style, "textSize")
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
