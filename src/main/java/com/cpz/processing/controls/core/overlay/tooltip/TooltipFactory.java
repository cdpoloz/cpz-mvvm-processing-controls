package com.cpz.processing.controls.core.overlay.tooltip;

import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipConfig;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.style.TypographySupport;
import com.cpz.processing.controls.core.util.FontLoader;
import processing.core.PApplet;

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

        TooltipStyleConfig style = toStyleConfig(sketch, config.getStyle());
        Tooltip tooltip = new Tooltip(config.getText(), style);
        tooltip.setEnabled(config.isEnabled());
        return tooltip;
    }

    private static TooltipStyleConfig toStyleConfig(PApplet sketch, TooltipConfig.StyleConfig style) {
        TooltipStyleConfig result = new TooltipStyleConfig();
        if (style == null) {
            return result;
        }
        result.backgroundOverride = style.getBackgroundColor();
        result.textOverride = style.getTextColor();
        result.borderOverride = style.getBorderColor();
        if (style.getTextPadding() != null) {
            result.textPadding = Math.max(0.0F, style.getTextPadding());
        }
        if (style.getOffset() != null) {
            result.offset = Math.max(0.0F, style.getOffset());
        }
        if (style.getCornerRadius() != null) {
            result.cornerRadius = Math.max(0.0F, style.getCornerRadius());
        }
        if (style.getTextSize() != null) {
            result.textSize = style.getTextSize();
        }
        if (style.getFontPath() != null) {
            result.font = FontLoader.load(
                    sketch,
                    style.getFontPath(),
                    result.textSize > 0.0F ? result.textSize : TypographySupport.DEFAULT_CUSTOM_FONT_SIZE,
                    "tooltip",
                    style.getSourcePath()
            );
        }
        return result;
    }
}
