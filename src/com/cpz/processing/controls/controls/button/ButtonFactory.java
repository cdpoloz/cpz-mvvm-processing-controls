package com.cpz.processing.controls.controls.button;

import com.cpz.processing.controls.controls.button.config.ButtonConfig;
import com.cpz.processing.controls.controls.button.config.ButtonStyleConfig;
import com.cpz.processing.controls.controls.button.style.DefaultButtonStyle;
import com.cpz.processing.controls.controls.button.style.render.SvgButtonRenderer;
import processing.core.PApplet;

import java.util.Objects;

/**
 * Factory for creating the public button facade from external config.
 */
public final class ButtonFactory {
    public static Button create(PApplet sketch, ButtonConfig config) {
        Objects.requireNonNull(sketch, "sketch");
        Objects.requireNonNull(config, "config");

        Button button = new Button(sketch, config.getText(), config.getX(), config.getY(), config.getWidth(), config.getHeight());
        button.setEnabled(config.isEnabled());
        button.setVisible(config.isVisible());

        if (config.getStyle() != null) {
            button.setStyle(new DefaultButtonStyle(toStyleConfig(sketch, config.getStyle())));
        }

        return button;
    }

    private static ButtonStyleConfig toStyleConfig(PApplet sketch, ButtonConfig.StyleConfig style) {
        ButtonStyleConfig result = new ButtonStyleConfig();
        if (style.getBaseColor() != null) {
            result.baseColor = style.getBaseColor();
        }
        if (style.getTextColor() != null) {
            result.textColor = style.getTextColor();
        }
        if (style.getStrokeColor() != null) {
            result.strokeColor = style.getStrokeColor();
        }
        if (style.getStrokeWeight() != null) {
            result.strokeWeight = style.getStrokeWeight();
        }
        if (style.getStrokeWeightHover() != null) {
            result.strokeWeightHover = style.getStrokeWeightHover();
        }
        if (style.getCornerRadius() != null) {
            result.cornerRadius = style.getCornerRadius();
        }
        if (style.getDisabledAlpha() != null) {
            result.disabledAlpha = style.getDisabledAlpha();
        }
        if (style.getHoverBlendWithWhite() != null) {
            result.hoverBlendWithWhite = style.getHoverBlendWithWhite();
        }
        if (style.getPressedBlendWithBlack() != null) {
            result.pressedBlendWithBlack = style.getPressedBlendWithBlack();
        }
        if (style.getRenderer() != null && "svg".equals(style.getRenderer().getType())) {
            result.setRenderer(new SvgButtonRenderer(sketch, style.getRenderer().getPath()));
        }
        return result;
    }
}
