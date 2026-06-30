package com.cpz.processing.controls.core.overlay.tooltip;

import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipConfig;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipJsonSupport;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.util.JsonConfigSupport;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.util.Objects;

/**
 * Factory for materializing tooltips from configuration.
 *
 * @author CPZ
 */
public final class TooltipFactory {
    private TooltipFactory() {
    }

    public static Tooltip create(PApplet sketch, TooltipConfig config) {
        if (config == null) {
            return null;
        }
        Objects.requireNonNull(sketch, "sketch");

        TooltipStyleConfig style = config.getResolvedStyle();
        if (style == null) {
            if (config.getStyleRef() != null) {
                throw new IllegalArgumentException(
                        "Unresolved tooltip styleRef '" + config.getStyleRef()
                                + "'. Use a loader that receives root 'tooltipStyles' or remove the styleRef."
                );
            }
            style = TooltipJsonSupport.createStyleConfig(sketch, config.getStyle());
        }
        Tooltip tooltip = new Tooltip(config.getText(), style);
        tooltip.setEnabled(config.isEnabled());
        return tooltip;
    }

    public static Tooltip loadFromJson(PApplet sketch, String path) {
        Objects.requireNonNull(sketch, "sketch");
        Objects.requireNonNull(path, "path");

        JSONObject root = JsonConfigSupport.loadRequiredObject(sketch, path, "tooltip");
        return create(sketch, TooltipJsonSupport.readTooltipDocument(sketch, root, path));
    }
}
