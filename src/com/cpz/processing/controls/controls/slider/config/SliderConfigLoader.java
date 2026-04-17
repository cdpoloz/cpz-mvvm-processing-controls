package com.cpz.processing.controls.controls.slider.config;

import com.cpz.processing.controls.controls.slider.model.SliderOrientation;
import com.cpz.processing.controls.controls.slider.model.SnapMode;
import com.cpz.processing.controls.controls.slider.style.SvgColorMode;
import com.cpz.processing.controls.core.util.JsonConfigSupport;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;

/**
 * Loads a minimal slider config from a JSON file.
 */
public final class SliderConfigLoader {
    private final PApplet sketch;

    public SliderConfigLoader(PApplet sketch) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
    }

    public SliderConfig load(String path) {
        Objects.requireNonNull(path, "path");
        JSONObject root = this.sketch.loadJSONObject(path);
        if (root == null) {
            throw new IllegalArgumentException("Could not load slider JSON config: " + path);
        }

        float width = root.getFloat("width");
        float height = root.getFloat("height");
        JsonConfigSupport.validatePositiveDimension("width", width, path);
        JsonConfigSupport.validatePositiveDimension("height", height, path);

        BigDecimal min = getRequiredBigDecimal(root, "min", path);
        BigDecimal max = getRequiredBigDecimal(root, "max", path);
        if (min.compareTo(max) >= 0) {
            throw new IllegalArgumentException("Invalid slider range in " + path + ": min must be < max.");
        }

        BigDecimal step = getRequiredBigDecimal(root, "step", path);
        if (step.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid 'step' value in " + path + ": " + step + ". Expected a number greater than 0.");
        }

        BigDecimal value = getRequiredBigDecimal(root, "value", path);
        if (value.compareTo(min) < 0 || value.compareTo(max) > 0) {
            throw new IllegalArgumentException("Invalid 'value' value in " + path + ": " + value + ". Expected a value between min and max.");
        }

        return new SliderConfig(
                JsonConfigSupport.getRequiredString(root, "code", path, "slider"),
                min,
                max,
                step,
                value,
                root.getFloat("x"),
                root.getFloat("y"),
                width,
                height,
                readOrientation(root, path),
                readSnapMode(root, path),
                root.getBoolean("enabled", true),
                root.getBoolean("visible", true),
                this.readStyle(root, path)
        );
    }

    private SliderConfig.StyleConfig readStyle(JSONObject root, String path) {
        if (!root.hasKey("style") || root.isNull("style")) {
            return null;
        }

        JSONObject style = root.getJSONObject("style");
        return new SliderConfig.StyleConfig(
                JsonConfigSupport.getOptionalColor(style, "trackOverride", path),
                JsonConfigSupport.getOptionalColor(style, "trackHoverOverride", path),
                JsonConfigSupport.getOptionalColor(style, "trackPressedOverride", path),
                JsonConfigSupport.getOptionalColor(style, "progressOverride", path),
                JsonConfigSupport.getOptionalColor(style, "progressHoverOverride", path),
                JsonConfigSupport.getOptionalColor(style, "progressPressedOverride", path),
                JsonConfigSupport.getOptionalColor(style, "thumbOverride", path),
                JsonConfigSupport.getOptionalColor(style, "thumbHoverOverride", path),
                JsonConfigSupport.getOptionalColor(style, "thumbPressedOverride", path),
                JsonConfigSupport.getOptionalColor(style, "trackStrokeOverride", path),
                JsonConfigSupport.getOptionalColor(style, "thumbStrokeOverride", path),
                JsonConfigSupport.getOptionalColor(style, "textOverride", path),
                JsonConfigSupport.getOptionalColor(style, "trackColor", path),
                JsonConfigSupport.getOptionalColor(style, "trackHoverColor", path),
                JsonConfigSupport.getOptionalColor(style, "trackPressedColor", path),
                JsonConfigSupport.getOptionalColor(style, "trackStrokeColor", path),
                JsonConfigSupport.getOptionalFloat(style, "trackStrokeWeight"),
                JsonConfigSupport.getOptionalFloat(style, "trackStrokeWeightHover"),
                JsonConfigSupport.getOptionalFloat(style, "trackThickness"),
                JsonConfigSupport.getOptionalColor(style, "activeTrackColor", path),
                JsonConfigSupport.getOptionalColor(style, "activeTrackHoverColor", path),
                JsonConfigSupport.getOptionalColor(style, "activeTrackPressedColor", path),
                JsonConfigSupport.getOptionalColor(style, "thumbColor", path),
                JsonConfigSupport.getOptionalColor(style, "thumbHoverColor", path),
                JsonConfigSupport.getOptionalColor(style, "thumbPressedColor", path),
                JsonConfigSupport.getOptionalColor(style, "thumbStrokeColor", path),
                JsonConfigSupport.getOptionalFloat(style, "thumbStrokeWeight"),
                JsonConfigSupport.getOptionalFloat(style, "thumbStrokeWeightHover"),
                JsonConfigSupport.getOptionalFloat(style, "thumbSize"),
                JsonConfigSupport.getOptionalColor(style, "textColor", path),
                JsonConfigSupport.getOptionalInt(style, "disabledAlpha"),
                readOptionalBoolean(style, "showValueText"),
                readSvgColorMode(style, path),
                this.readRenderer(style, path)
        );
    }

    private SliderConfig.RendererConfig readRenderer(JSONObject style, String path) {
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

        return new SliderConfig.RendererConfig(normalizedType, normalizedPath);
    }

    private static SliderOrientation readOrientation(JSONObject root, String path) {
        if (!root.hasKey("orientation") || root.isNull("orientation")) {
            return SliderOrientation.HORIZONTAL;
        }
        String raw = JsonConfigSupport.getRequiredString(root, "orientation", path, "slider");
        String normalized = normalizeEnum(raw);
        if ("HORIZONTAL".equals(normalized)) {
            return SliderOrientation.HORIZONTAL;
        }
        if ("VERTICAL".equals(normalized)) {
            return SliderOrientation.VERTICAL;
        }
        throw new IllegalArgumentException("Unsupported slider orientation in " + path + ": " + raw + ". Supported values: horizontal, vertical.");
    }

    private static SnapMode readSnapMode(JSONObject root, String path) {
        if (!root.hasKey("snapMode") || root.isNull("snapMode")) {
            return SnapMode.ALWAYS;
        }
        String raw = JsonConfigSupport.getRequiredString(root, "snapMode", path, "slider");
        String normalized = normalizeEnum(raw);
        if ("ALWAYS".equals(normalized)) {
            return SnapMode.ALWAYS;
        }
        if ("ON_RELEASE".equals(normalized)) {
            return SnapMode.ON_RELEASE;
        }
        throw new IllegalArgumentException("Unsupported slider snapMode in " + path + ": " + raw + ". Supported values: always, on_release.");
    }

    private static SvgColorMode readSvgColorMode(JSONObject style, String path) {
        if (!style.hasKey("svgColorMode") || style.isNull("svgColorMode")) {
            return null;
        }
        String raw = style.getString("svgColorMode");
        String normalized = normalizeEnum(raw);
        if ("USE_RENDER_STYLE".equals(normalized)) {
            return SvgColorMode.USE_RENDER_STYLE;
        }
        if ("USE_SVG_ORIGINAL".equals(normalized)) {
            return SvgColorMode.USE_SVG_ORIGINAL;
        }
        throw new IllegalArgumentException("Unsupported slider svgColorMode in " + path + ": " + raw + ". Supported values: use_render_style, use_svg_original.");
    }

    private static BigDecimal getRequiredBigDecimal(JSONObject root, String key, String path) {
        if (!root.hasKey(key) || root.isNull(key)) {
            throw new IllegalArgumentException("Missing required key '" + key + "' in " + path + " for slider.");
        }
        return parseBigDecimal(root.get(key), key, path);
    }

    private static BigDecimal parseBigDecimal(Object value, String key, String path) {
        if (value instanceof Number || value instanceof String) {
            try {
                return new BigDecimal(value.toString().trim());
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Invalid decimal value for key '" + key + "' in " + path + ": " + value + ".", ex);
            }
        }
        throw new IllegalArgumentException("Unsupported decimal value for key '" + key + "' in " + path + ": " + value + ". Expected number or decimal string.");
    }

    private static Boolean readOptionalBoolean(JSONObject json, String key) {
        if (!json.hasKey(key) || json.isNull(key)) {
            return null;
        }
        return json.getBoolean(key);
    }

    private static String normalizeEnum(String raw) {
        return raw.trim().toUpperCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
    }
}
