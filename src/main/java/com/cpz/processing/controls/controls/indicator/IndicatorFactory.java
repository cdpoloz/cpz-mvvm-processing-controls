package com.cpz.processing.controls.controls.indicator;

import com.cpz.processing.controls.controls.indicator.config.IndicatorConfig;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipFactory;
import java.util.Objects;
import processing.core.PApplet;

/**
 * Factory for creating the public indicator facade from external config.
 *
 * @author CPZ
 */
public final class IndicatorFactory {
    private IndicatorFactory() {
    }

    public static Indicator create(PApplet sketch, IndicatorConfig config) {
        Objects.requireNonNull(sketch, "sketch");
        Objects.requireNonNull(config, "config");

        Indicator indicator = new Indicator(
                sketch,
                config.getCode(),
                config.getBounds(),
                config.getRenderer() != null && "svg".equals(config.getRenderer().getType())
                        ? config.getRenderer().getPath()
                        : null
        );
        indicator.setOn(config.isOn());
        indicator.setOnColor(config.getOnColor());
        indicator.setOffColor(config.getOffColor());
        indicator.setEnabled(config.isEnabled());
        indicator.setVisible(config.isVisible());
        indicator.setTooltip(TooltipFactory.create(sketch, config.getTooltip()));
        return indicator;
    }
}
