package com.cpz.processing.controls.controls.radiogroup.config;

import com.cpz.processing.controls.core.util.JsonConfigSupport;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Loads a minimal radio group config from a JSON file.
 */
public final class RadioGroupConfigLoader {
    private final PApplet sketch;

    public RadioGroupConfigLoader(PApplet sketch) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
    }

    public RadioGroupConfig load(String path) {
        Objects.requireNonNull(path, "path");
        JSONObject root = JsonConfigSupport.unwrapSingleControlDocument(
                JsonConfigSupport.loadRequiredObject(this.sketch, path, "radio group"),
                path,
                "radiogroup",
                "radiogroup"
        );
        return this.loadFromJson(root, path);
    }

    public RadioGroupConfig loadFromJson(JSONObject root, String path) {
        float width = root.getFloat("width");
        JsonConfigSupport.validatePositiveDimension("width", width, path);

        List<String> options = this.readOptions(root, path);
        if (options.isEmpty()) {
            throw new IllegalArgumentException("Invalid 'options' value in " + path + ": expected at least one option.");
        }

        int selectedIndex = root.getInt("selectedIndex", -1);
        if (selectedIndex < -1 || selectedIndex >= options.size()) {
            throw new IllegalArgumentException("Invalid 'selectedIndex' value in " + path + ": " + selectedIndex + ". Expected -1 or a value between 0 and " + (options.size() - 1) + ".");
        }

        return new RadioGroupConfig(
                JsonConfigSupport.getRequiredString(root, "code", path, "radiogroup"),
                options,
                selectedIndex,
                root.getFloat("x"),
                root.getFloat("y"),
                width,
                root.getBoolean("enabled", true),
                root.getBoolean("visible", true),
                this.readStyle(root, path)
        );
    }

    private List<String> readOptions(JSONObject root, String path) {
        if (!root.hasKey("options") || root.isNull("options")) {
            throw new IllegalArgumentException("Missing required key 'options' in " + path + " for radiogroup.");
        }

        JSONArray optionsJson = root.getJSONArray("options");
        List<String> options = new ArrayList<>(optionsJson.size());
        for (int i = 0; i < optionsJson.size(); i++) {
            Object raw = optionsJson.get(i);
            if (!(raw instanceof String)) {
                throw new IllegalArgumentException("Invalid option value in " + path + " at index " + i + ": " + raw + ". Expected a string.");
            }
            options.add((String) raw);
        }
        return options;
    }

    private RadioGroupConfig.StyleConfig readStyle(JSONObject root, String path) {
        if (!root.hasKey("style") || root.isNull("style")) {
            return null;
        }

        JSONObject style = root.getJSONObject("style");
        return new RadioGroupConfig.StyleConfig(
                JsonConfigSupport.getOptionalColor(style, "textOverride", path),
                JsonConfigSupport.getOptionalColor(style, "indicatorOverride", path),
                JsonConfigSupport.getOptionalColor(style, "backgroundOverride", path),
                JsonConfigSupport.getOptionalColor(style, "hoveredBackgroundOverride", path),
                JsonConfigSupport.getOptionalColor(style, "pressedBackgroundOverride", path),
                JsonConfigSupport.getOptionalColor(style, "selectedDotOverride", path),
                JsonConfigSupport.getOptionalFloat(style, "itemHeight"),
                JsonConfigSupport.getOptionalFloat(style, "itemSpacing"),
                JsonConfigSupport.getOptionalFloat(style, "minimumItemHeight"),
                JsonConfigSupport.getOptionalFloat(style, "indicatorOffsetX"),
                JsonConfigSupport.getOptionalFloat(style, "textOffsetX"),
                JsonConfigSupport.getOptionalFloat(style, "indicatorOuterDiameter"),
                JsonConfigSupport.getOptionalFloat(style, "indicatorInnerDiameter"),
                JsonConfigSupport.getOptionalFloat(style, "strokeWeight"),
                JsonConfigSupport.getOptionalFloat(style, "textSize"),
                JsonConfigSupport.getOptionalFloat(style, "cornerRadius"),
                JsonConfigSupport.getOptionalInt(style, "disabledAlpha")
        );
    }
}
