package com.cpz.processing.controls.core.overlay.tooltip.config;

import com.cpz.processing.controls.core.util.JsonConfigSupport;
import processing.data.JSONObject;

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

        JSONObject tooltip = root.getJSONObject("tooltip");
        return new TooltipConfig(
                tooltip.getString("text", ""),
                tooltip.getBoolean("enabled", true),
                readStyle(tooltip, path)
        );
    }

    private static TooltipConfig.StyleConfig readStyle(JSONObject tooltip, String path) {
        if (!tooltip.hasKey("style") || tooltip.isNull("style")) {
            return null;
        }

        JSONObject style = tooltip.getJSONObject("style");
        return new TooltipConfig.StyleConfig(
                JsonConfigSupport.getOptionalColor(style, "backgroundColor", path),
                JsonConfigSupport.getOptionalColor(style, "textColor", path),
                JsonConfigSupport.getOptionalColor(style, "borderColor", path),
                JsonConfigSupport.getOptionalFloat(style, "textPadding"),
                JsonConfigSupport.getOptionalFloat(style, "offset"),
                JsonConfigSupport.getOptionalFloat(style, "cornerRadius"),
                JsonConfigSupport.getOptionalFloat(style, "textSize"),
                JsonConfigSupport.getOptionalNonBlankString(style, "font", path, "tooltip style"),
                path
        );
    }
}
