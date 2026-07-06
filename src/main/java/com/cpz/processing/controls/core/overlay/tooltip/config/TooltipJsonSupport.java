package com.cpz.processing.controls.core.overlay.tooltip.config;

import com.cpz.processing.controls.core.style.TypographySupport;
import com.cpz.processing.controls.core.util.FontLoader;
import com.cpz.processing.controls.core.util.JsonConfigSupport;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Shared JSON reader for optional tooltip blocks.
 *
 * @author CPZ
 */
public final class TooltipJsonSupport {
    private TooltipJsonSupport() {
    }

    public static TooltipConfig readTooltip(JSONObject root, String path) {
        if (root == null || !root.hasKey("tooltip") || root.isNull("tooltip")) {
            return null;
        }

        Object rawTooltip = root.get("tooltip");
        if (rawTooltip instanceof String) {
            return readTooltipText((String) rawTooltip);
        }
        if (!(rawTooltip instanceof JSONObject)) {
            throw new IllegalArgumentException(
                    "Invalid 'tooltip' value in " + path + ": expected a string or an object."
            );
        }
        return readTooltipObject((JSONObject) rawTooltip, path);
    }

    public static TooltipConfig readTooltip(PApplet sketch, JSONObject root, String path, Map<String, TooltipStyleConfig> stylePresets) {
        if (root == null || !root.hasKey("tooltip") || root.isNull("tooltip")) {
            return null;
        }

        Object rawTooltip = root.get("tooltip");
        if (rawTooltip instanceof String) {
            return readTooltipText((String) rawTooltip);
        }
        if (!(rawTooltip instanceof JSONObject)) {
            throw new IllegalArgumentException(
                    "Invalid 'tooltip' value in " + path + ": expected a string or an object."
            );
        }
        return readTooltipObject(sketch, (JSONObject) rawTooltip, path, stylePresets);
    }

    public static TooltipConfig readTooltipDocument(PApplet sketch, JSONObject root, String path) {
        Objects.requireNonNull(root, "root");

        Map<String, TooltipStyleConfig> stylePresets = readTooltipStyles(sketch, root, path);
        return readTooltipObject(sketch, root, path, stylePresets);
    }

    public static Map<String, TooltipStyleConfig> readTooltipStyles(PApplet sketch, JSONObject root, String path) {
        if (root == null || !root.hasKey("tooltipStyles") || root.isNull("tooltipStyles")) {
            return Collections.emptyMap();
        }

        Objects.requireNonNull(sketch, "sketch");

        Object rawStyles = root.get("tooltipStyles");
        if (!(rawStyles instanceof JSONObject)) {
            throw new IllegalArgumentException(
                    "Invalid 'tooltipStyles' value in " + path + ": expected an object."
            );
        }

        JSONObject styles = (JSONObject) rawStyles;
        Map<String, TooltipStyleConfig> result = new LinkedHashMap<>();
        for (Object rawKey : styles.keys()) {
            String name = String.valueOf(rawKey).trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException(
                        "Invalid tooltip style name in " + path + ": expected a non-blank key."
                );
            }
            Object rawStyle = styles.get(String.valueOf(rawKey));
            if (!(rawStyle instanceof JSONObject)) {
                throw new IllegalArgumentException(
                        "Invalid tooltip style '" + name + "' in " + path + ": expected an object."
                );
            }
            result.put(name, createStyleConfig(sketch, readStyleObject((JSONObject) rawStyle, path)));
        }
        return Collections.unmodifiableMap(result);
    }

    public static TooltipStyleConfig createStyleConfig(PApplet sketch, TooltipConfig.StyleConfig style) {
        TooltipStyleConfig result = new TooltipStyleConfig();
        return applyStyleConfig(sketch, result, style);
    }

    public static TooltipStyleConfig applyStyleConfig(PApplet sketch, TooltipStyleConfig target, TooltipConfig.StyleConfig style) {
        TooltipStyleConfig result = target == null ? new TooltipStyleConfig() : target;
        if (style == null) {
            return result;
        }

        if (style.getBackgroundColor() != null) {
            result.setBackgroundColor(style.getBackgroundColor());
        }
        if (style.getTextColor() != null) {
            result.setTextColor(style.getTextColor());
        }
        if (style.getBorderColor() != null) {
            result.setBorderColor(style.getBorderColor());
        }
        if (style.getTextPadding() != null) {
            result.setTextPadding(style.getTextPadding());
        }
        if (style.getOffset() != null) {
            result.setOffset(style.getOffset());
        }
        if (style.getCornerRadius() != null) {
            result.setCornerRadius(style.getCornerRadius());
        }
        if (style.getStrokeWeight() != null) {
            result.setStrokeWeight(style.getStrokeWeight());
        }
        if (style.getTextSize() != null) {
            result.setTextSize(style.getTextSize());
        }
        if (style.getFontPath() != null) {
            Objects.requireNonNull(sketch, "sketch");
            result.setFont(FontLoader.load(
                    sketch,
                    style.getFontPath(),
                    result.textSize > 0.0F ? result.textSize : TypographySupport.DEFAULT_CUSTOM_FONT_SIZE,
                    "tooltip",
                    style.getSourcePath()
            ));
        }
        return result;
    }

    private static TooltipConfig readTooltipObject(JSONObject tooltip, String path) {
        return new TooltipConfig(
                tooltip.getString("text", ""),
                tooltip.getBoolean("enabled", true),
                readStyle(tooltip, path),
                JsonConfigSupport.getOptionalNonBlankString(tooltip, "styleRef", path, "tooltip"),
                null
        );
    }

    private static TooltipConfig readTooltipText(String text) {
        return new TooltipConfig(text, true, null, null, null);
    }

    private static TooltipConfig readTooltipObject(PApplet sketch, JSONObject tooltip, String path, Map<String, TooltipStyleConfig> stylePresets) {
        TooltipConfig config = readTooltipObject(tooltip, path);
        if (config.getStyleRef() == null) {
            return config;
        }

        TooltipStyleConfig preset = stylePresets == null ? null : stylePresets.get(config.getStyleRef());
        if (preset == null) {
            String available = stylePresets == null || stylePresets.isEmpty()
                    ? "none"
                    : String.join(", ", stylePresets.keySet());
            throw new IllegalArgumentException(
                    "Unknown tooltip styleRef '" + config.getStyleRef() + "' in " + path
                            + ". Define it under root 'tooltipStyles'. Available tooltip styles: " + available + "."
            );
        }

        TooltipStyleConfig resolved = new TooltipStyleConfig(preset);
        applyStyleConfig(sketch, resolved, config.getStyle());
        return new TooltipConfig(config.getText(), config.isEnabled(), config.getStyle(), config.getStyleRef(), resolved);
    }

    private static TooltipConfig.StyleConfig readStyle(JSONObject tooltip, String path) {
        if (!tooltip.hasKey("style") || tooltip.isNull("style")) {
            return null;
        }

        return readStyleObject(tooltip.getJSONObject("style"), path);
    }

    private static TooltipConfig.StyleConfig readStyleObject(JSONObject style, String path) {
        return new TooltipConfig.StyleConfig(
                JsonConfigSupport.getOptionalColor(style, "backgroundColor", path),
                JsonConfigSupport.getOptionalColor(style, "textColor", path),
                JsonConfigSupport.getOptionalColor(style, "borderColor", path),
                readTextPadding(style),
                JsonConfigSupport.getOptionalFloat(style, "offset"),
                JsonConfigSupport.getOptionalFloat(style, "cornerRadius"),
                JsonConfigSupport.getOptionalFloat(style, "strokeWeight"),
                JsonConfigSupport.getOptionalFloat(style, "textSize"),
                JsonConfigSupport.getOptionalNonBlankString(style, "font", path, "tooltip style"),
                path
        );
    }

    private static Float readTextPadding(JSONObject style) {
        Float textPadding = JsonConfigSupport.getOptionalFloat(style, "textPadding");
        return textPadding == null ? JsonConfigSupport.getOptionalFloat(style, "padding") : textPadding;
    }
}
