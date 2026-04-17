package com.cpz.processing.controls.controls.dropdown.config;

import com.cpz.processing.controls.core.util.JsonConfigSupport;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Loads a minimal drop down config from a JSON file.
 */
public final class DropDownConfigLoader {
    private final PApplet sketch;

    public DropDownConfigLoader(PApplet sketch) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
    }

    public DropDownConfig load(String path) {
        Objects.requireNonNull(path, "path");
        JSONObject root = JsonConfigSupport.unwrapSingleControlDocument(
                JsonConfigSupport.loadRequiredObject(this.sketch, path, "drop down"),
                path,
                "dropdown",
                "dropdown"
        );
        return this.loadFromJson(root, path);
    }

    public DropDownConfig loadFromJson(JSONObject root, String path) {
        float width = root.getFloat("width");
        float height = root.getFloat("height");
        JsonConfigSupport.validatePositiveDimension("width", width, path);
        JsonConfigSupport.validatePositiveDimension("height", height, path);

        List<String> items = this.readItems(root, path);
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Invalid 'items' value in " + path + ": expected at least one item.");
        }

        int selectedIndex = root.getInt("selectedIndex", -1);
        if (selectedIndex < -1 || selectedIndex >= items.size()) {
            throw new IllegalArgumentException("Invalid 'selectedIndex' value in " + path + ": " + selectedIndex + ". Expected -1 or a value between 0 and " + (items.size() - 1) + ".");
        }

        return new DropDownConfig(
                JsonConfigSupport.getRequiredString(root, "code", path, "dropdown"),
                items,
                selectedIndex,
                root.getFloat("x"),
                root.getFloat("y"),
                width,
                height,
                root.getBoolean("enabled", true),
                root.getBoolean("visible", true),
                this.readStyle(root, path)
        );
    }

    private List<String> readItems(JSONObject root, String path) {
        if (!root.hasKey("items") || root.isNull("items")) {
            throw new IllegalArgumentException("Missing required key 'items' in " + path + " for dropdown.");
        }

        JSONArray itemsJson = root.getJSONArray("items");
        List<String> items = new ArrayList<>(itemsJson.size());
        for (int i = 0; i < itemsJson.size(); i++) {
            Object raw = itemsJson.get(i);
            if (!(raw instanceof String)) {
                throw new IllegalArgumentException("Invalid item value in " + path + " at index " + i + ": " + raw + ". Expected a string.");
            }
            items.add((String) raw);
        }
        return items;
    }

    private DropDownConfig.StyleConfig readStyle(JSONObject root, String path) {
        if (!root.hasKey("style") || root.isNull("style")) {
            return null;
        }

        JSONObject style = root.getJSONObject("style");
        return new DropDownConfig.StyleConfig(
                JsonConfigSupport.getOptionalColor(style, "baseFillOverride", path),
                JsonConfigSupport.getOptionalColor(style, "listFillOverride", path),
                JsonConfigSupport.getOptionalColor(style, "textOverride", path),
                JsonConfigSupport.getOptionalColor(style, "borderOverride", path),
                JsonConfigSupport.getOptionalColor(style, "hoverItemOverlayOverride", path),
                JsonConfigSupport.getOptionalColor(style, "selectedItemOverlayOverride", path),
                JsonConfigSupport.getOptionalColor(style, "focusedBorderOverride", path),
                JsonConfigSupport.getOptionalFloat(style, "cornerRadius"),
                JsonConfigSupport.getOptionalFloat(style, "listCornerRadius"),
                JsonConfigSupport.getOptionalFloat(style, "strokeWeight"),
                JsonConfigSupport.getOptionalFloat(style, "focusedStrokeWeight"),
                JsonConfigSupport.getOptionalFloat(style, "textSize"),
                JsonConfigSupport.getOptionalFloat(style, "itemHeight"),
                JsonConfigSupport.getOptionalFloat(style, "textPadding"),
                JsonConfigSupport.getOptionalFloat(style, "arrowPadding"),
                JsonConfigSupport.getOptionalInt(style, "maxVisibleItems"),
                JsonConfigSupport.getOptionalInt(style, "disabledAlpha")
        );
    }
}
