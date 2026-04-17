package com.cpz.processing.controls.controls.label;

import com.cpz.processing.controls.controls.label.config.LabelConfig;
import com.cpz.processing.controls.controls.label.config.LabelStyleConfig;
import com.cpz.processing.controls.controls.label.style.DefaultLabelStyle;
import com.cpz.processing.controls.controls.label.style.HorizontalAlign;
import com.cpz.processing.controls.controls.label.style.VerticalAlign;
import processing.core.PApplet;

import java.util.Objects;

/**
 * Factory for creating the public label facade from external config.
 */
public final class LabelFactory {
    public static Label create(PApplet sketch, LabelConfig config) {
        Objects.requireNonNull(sketch, "sketch");
        Objects.requireNonNull(config, "config");

        Label label = new Label(
                sketch,
                config.getCode(),
                config.getText(),
                config.getX(),
                config.getY(),
                config.getWidth(),
                config.getHeight()
        );
        label.setEnabled(config.isEnabled());
        label.setVisible(config.isVisible());

        if (config.getStyle() != null) {
            label.setStyle(new DefaultLabelStyle(toStyleConfig(config.getStyle())));
        }

        return label;
    }

    private static LabelStyleConfig toStyleConfig(LabelConfig.StyleConfig style) {
        LabelStyleConfig result = new LabelStyleConfig();
        result.font = null;
        if (style.getTextSize() != null) {
            result.textSize = style.getTextSize();
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
