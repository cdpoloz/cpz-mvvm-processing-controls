package com.cpz.processing.controls.controls.toggle;

import com.cpz.processing.controls.controls.toggle.config.ToggleConfig;
import com.cpz.processing.controls.controls.toggle.config.ToggleStyleConfig;
import com.cpz.processing.controls.controls.toggle.style.ParametricToggleStyle;
import com.cpz.processing.controls.controls.toggle.style.render.CircleShapeRenderer;
import com.cpz.processing.controls.controls.toggle.style.render.SvgShapeRenderer;
import processing.core.PApplet;

import java.util.Objects;

/**
 * Factory for creating the public toggle facade from external config.
 */
public final class ToggleFactory {
    public static Toggle create(PApplet sketch, ToggleConfig config) {
        Objects.requireNonNull(sketch, "sketch");
        Objects.requireNonNull(config, "config");

        Toggle toggle = new Toggle(
                sketch,
                config.getCode(),
                config.getState(),
                config.getTotalStates(),
                config.getX(),
                config.getY(),
                config.getWidth(),
                config.getHeight()
        );
        toggle.setEnabled(config.isEnabled());
        toggle.setVisible(config.isVisible());

        if (config.getStyle() != null) {
            toggle.setStyle(new ParametricToggleStyle(toStyleConfig(sketch, config.getStyle())));
        }

        return toggle;
    }

    private static ToggleStyleConfig toStyleConfig(PApplet sketch, ToggleConfig.StyleConfig style) {
        ToggleStyleConfig result = new ToggleStyleConfig();
        result.setShapeRenderer(style.getRenderer() != null && "svg".equals(style.getRenderer().getType())
                ? new SvgShapeRenderer(sketch, style.getRenderer().getPath())
                : new CircleShapeRenderer());
        if (style.getOffFillOverride() != null) {
            result.offFillOverride = style.getOffFillOverride();
        }
        if (style.getOnFillOverride() != null) {
            result.onFillOverride = style.getOnFillOverride();
        }
        if (style.getHoverFillOverride() != null) {
            result.hoverFillOverride = style.getHoverFillOverride();
        }
        if (style.getPressedFillOverride() != null) {
            result.pressedFillOverride = style.getPressedFillOverride();
        }
        if (style.getStrokeOverride() != null) {
            result.strokeOverride = style.getStrokeOverride();
        }
        if (style.getStateColors() != null) {
            result.stateColors = style.getStateColors();
        }
        if (style.getStrokeColor() != null) {
            result.strokeColor = style.getStrokeColor();
        }
        if (style.getStrokeWidth() != null) {
            result.strokeWidth = style.getStrokeWidth();
        }
        if (style.getStrokeWidthHover() != null) {
            result.strokeWidthHover = style.getStrokeWidthHover();
        }
        if (style.getHoverBlendWithWhite() != null) {
            result.hoverBlendWithWhite = style.getHoverBlendWithWhite();
        }
        if (style.getPressedBlendWithBlack() != null) {
            result.pressedBlendWithBlack = style.getPressedBlendWithBlack();
        }
        if (style.getDisabledAlpha() != null) {
            result.disabledAlpha = style.getDisabledAlpha();
        }
        return result;
    }
}
