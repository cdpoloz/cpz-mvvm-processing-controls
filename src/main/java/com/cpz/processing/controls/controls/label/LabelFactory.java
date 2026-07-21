package com.cpz.processing.controls.controls.label;

import com.cpz.processing.controls.controls.label.config.LabelConfig;
import com.cpz.processing.controls.controls.label.config.LabelStyleConfig;
import com.cpz.processing.controls.controls.label.style.DefaultLabelStyle;
import com.cpz.processing.controls.controls.label.style.HorizontalAlign;
import com.cpz.processing.controls.controls.label.style.VerticalAlign;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipFactory;
import com.cpz.processing.controls.core.util.FontLoader;
import processing.core.PApplet;

import java.util.Objects;

/**
 * Factory for creating the public label facade from external config.
 *
 * @author CPZ
 */
public final class LabelFactory {
    public static Label create(PApplet sketch, LabelConfig config) {
        Objects.requireNonNull(sketch, "sketch");
        Objects.requireNonNull(config, "config");

        Label label = new Label(
                sketch,
                config.getCode(),
                config.getText(),
                config.getBounds()
        );
        label.setEnabled(config.isEnabled());
        label.setVisible(config.isVisible());

        if (config.getStyle() != null) {
            label.setStyle(new DefaultLabelStyle(toStyleConfig(sketch, config.getStyle(), config.getTextSizeMeasure() != null)));
        }
        if (config.getTextSizeMeasure() != null) {
            label.setTextSize(config.getTextSizeMeasure());
        }
        label.setTooltip(TooltipFactory.create(sketch, config.getTooltip()));

        return label;
    }

    private static LabelStyleConfig toStyleConfig(PApplet sketch, LabelConfig.StyleConfig style, boolean deferFontLoad) {
        LabelStyleConfig result = new LabelStyleConfig();
        if (style.getTextSize() != null) {
            result.textSize = style.getTextSize();
        }
        if (style.getFontPath() != null) {
            result.fontResolver = FontLoader.resolver(style.getFontPath(), "label", style.getSourcePath());
            if (!deferFontLoad) {
                result.font = result.fontResolver.load(sketch, result.textSize);
            }
        }
        result.textColor = style.getTextColor();
        if (style.getLineSpacingMultiplier() != null) {
            result.lineSpacingMultiplier = style.getLineSpacingMultiplier();
        }
        result.alignX = style.getAlignX() != null ? style.getAlignX() : HorizontalAlign.START;
        result.alignY = style.getAlignY() != null ? style.getAlignY() : VerticalAlign.BASELINE;
        result.disabledAlpha = style.getDisabledAlpha();
        return result;
    }
}
