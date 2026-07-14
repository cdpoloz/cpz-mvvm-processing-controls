package com.cpz.processing.controls.controls.panel;

import com.cpz.processing.controls.controls.panel.config.PanelConfig;
import com.cpz.processing.controls.controls.panel.config.PanelStyleConfig;
import com.cpz.processing.controls.controls.panel.style.PanelStyle;
import processing.core.PApplet;

import java.util.Objects;

/**
 * Factory for creating panel facades from external config.
 *
 * <p>The factory converts optional {@link PanelStyleConfig} values into the
 * runtime {@link PanelStyle}. Only configured properties are applied, so unset
 * colors keep their dynamic theme fallbacks.</p>
 *
 * @author CPZ
 */
public final class PanelFactory {
    private PanelFactory() {
    }

    public static Panel create(PApplet sketch, PanelConfig config) {
        Objects.requireNonNull(sketch, "sketch");
        Objects.requireNonNull(config, "config");

        Panel panel = new Panel(sketch, config.getCode(), config.getBounds());
        if (config.getStyle() != null) {
            panel.setStyle(toStyle(config.getStyle()));
        }
        panel.setEnabled(config.isEnabled());
        panel.setVisible(config.isVisible());
        return panel;
    }

    private static PanelStyle toStyle(PanelStyleConfig config) {
        PanelStyle style = new PanelStyle();
        if (config.getBackgroundVisible() != null) {
            style.setBackgroundVisible(config.getBackgroundVisible());
        }
        if (config.getBackgroundColor() != null) {
            style.setBackgroundColor(config.getBackgroundColor());
        }
        if (config.getStrokeVisible() != null) {
            style.setStrokeVisible(config.getStrokeVisible());
        }
        if (config.getStrokeColor() != null) {
            style.setStrokeColor(config.getStrokeColor());
        }
        if (config.getStrokeWeight() != null) {
            style.setStrokeWeight(config.getStrokeWeight());
        }
        if (config.getCornerRadius() != null) {
            style.setCornerRadius(config.getCornerRadius());
        }
        return style;
    }
}
