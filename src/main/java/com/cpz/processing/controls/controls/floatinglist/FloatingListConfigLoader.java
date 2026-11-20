package com.cpz.processing.controls.controls.floatinglist;

import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.core.util.FontLoader;
import com.cpz.processing.controls.core.util.JsonConfigSupport;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.util.Objects;

/** Applies presentation patches from standalone JSON to an existing floating list. */
public final class FloatingListConfigLoader {
    private static final String CONTEXT = "floating list";

    private FloatingListConfigLoader() {}

    /** Loads a standalone document using the sketch's normal Processing resource lookup. */
    public static void apply(PApplet sketch, String path, FloatingListPanel<?, ?> panel) {
        applyFromJson(JsonConfigSupport.loadRequiredObject(sketch, path, CONTEXT), path, panel);
    }

    /**
     * Applies present fields, keeping existing values for omitted or null fields.
     * Parsing and validation complete before changes are applied. A font resource
     * is loaded lazily at the effective text size when drawn.
     */
    public static void applyFromJson(JSONObject root, String path, FloatingListPanel<?, ?> panel) {
        Objects.requireNonNull(root, "root");
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(panel, "panel");
        var document = new Section(root, path);
        String title = document.string("title", panel.getTitle());
        FloatingListPanel.Measures measures;
        FloatingListStyle style;
        FontLoader.FontResolver fontResolver;
        try {
            measures = readMeasures(document.child("measures"), panel.getMeasures());
            var visual = document.child("style");
            style = readStyle(visual, panel.getStyle());
            String font = JsonConfigSupport.getOptionalNonBlankString(visual.json(), "font", visual.path(), CONTEXT);
            fontResolver = font == null ? null : FontLoader.resolver(font, CONTEXT, visual.path());
        } catch (IllegalArgumentException invalid) {
            throw new IllegalArgumentException("Invalid floating list config in " + path + ": " + invalid.getMessage(),
                    invalid);
        }
        panel.applyPresentation(measures, style, title, fontResolver);
    }

    private static FloatingListPanel.Measures readMeasures(Section section, FloatingListPanel.Measures current) {
        return new FloatingListPanel.Measures(section.measure("width", current.width()),
                section.measure("maxHeight", current.maxHeight()), section.measure("rowHeight", current.rowHeight()),
                section.measure("headerHeight", current.headerHeight()),
                section.measure("scrollbarWidth", current.scrollbarWidth()),
                section.measure("closeButtonWidth", current.closeButtonWidth()),
                section.measure("closeButtonHeight", current.closeButtonHeight()),
                section.measure("canvasMargin", current.canvasMargin()), section.measure("anchorGap", current.anchorGap()),
                section.measure("padding", current.padding()), section.measure("minThumbSize", current.minThumbSize()));
    }

    private static FloatingListStyle readStyle(Section section, FloatingListStyle current) {
        var frame = section.child("frame");
        var scrollbar = section.child("scrollbar");
        var button = section.child("closeButton");
        var oldFrame = current.frame();
        var oldScrollbar = current.scrollbar();
        var oldButton = current.closeButton();
        return new FloatingListStyle(
                new FloatingListStyle.Frame(frame.color("backgroundColor", oldFrame.backgroundColor()),
                        frame.color("borderColor", oldFrame.borderColor()),
                        frame.measure("strokeWeight", oldFrame.strokeWeight()),
                        frame.measure("cornerRadius", oldFrame.cornerRadius())),
                section.color("headerColor", current.headerColor()),
                readText(section.child("title"), current.title()), readText(section.child("empty"), current.empty()),
                new FloatingListStyle.Scrollbar(scrollbar.color("trackColor", oldScrollbar.trackColor()),
                        scrollbar.color("thumbColor", oldScrollbar.thumbColor()),
                        scrollbar.color("hoverColor", oldScrollbar.hoverColor()),
                        scrollbar.color("pressedColor", oldScrollbar.pressedColor()),
                        scrollbar.measure("cornerRadius", oldScrollbar.cornerRadius())),
                new FloatingListStyle.CloseButton(button.color("backgroundColor", oldButton.backgroundColor()),
                        button.color("hoverColor", oldButton.hoverColor()), button.color("pressedColor", oldButton.pressedColor()),
                        button.color("borderColor", oldButton.borderColor()), button.measure("strokeWeight", oldButton.strokeWeight()),
                        button.measure("hoverStrokeWeight", oldButton.hoverStrokeWeight()),
                        button.measure("cornerRadius", oldButton.cornerRadius()),
                        button.color("textColor", oldButton.textColor()), oldButton.font(),
                        button.measure("textSize", oldButton.textSize())));
    }

    private static FloatingListStyle.Text readText(Section section, FloatingListStyle.Text current) {
        return new FloatingListStyle.Text(section.color("color", current.color()), current.font(),
                section.measure("size", current.size()), section.measure("paddingLeft", current.paddingLeft()),
                section.measure("paddingRight", current.paddingRight()));
    }

    private record Section(JSONObject json, String path) {
        private boolean absent(String key) {
            return !this.json.hasKey(key) || this.json.isNull(key);
        }

        private Section child(String key) {
            String childPath = this.path + " -> " + key;
            if (this.absent(key)) return new Section(new JSONObject(), childPath);
            if (this.json.get(key) instanceof JSONObject object) return new Section(object, childPath);
            throw new IllegalArgumentException("Expected an object at " + childPath);
        }

        private String string(String key, String fallback) {
            if (this.absent(key)) return fallback;
            if (this.json.get(key) instanceof String value) return value;
            throw new IllegalArgumentException("Expected a string at " + this.path + " -> " + key);
        }

        private ControlMeasure measure(String key, ControlMeasure fallback) {
            var value = JsonConfigSupport.getOptionalStyleMeasure(this.json, key, this.path, CONTEXT);
            return value == null ? fallback : value;
        }

        private Integer color(String key, Integer fallback) {
            var value = JsonConfigSupport.getOptionalColor(this.json, key, this.path);
            return value == null ? fallback : value;
        }
    }
}
