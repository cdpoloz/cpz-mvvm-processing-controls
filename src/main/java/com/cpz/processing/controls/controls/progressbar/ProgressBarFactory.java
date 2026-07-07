package com.cpz.processing.controls.controls.progressbar;

import com.cpz.processing.controls.controls.progressbar.config.ProgressBarConfig;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipFactory;
import java.util.Objects;
import processing.core.PApplet;

/**
 * Factory for creating the public progress bar facade from external config.
 *
 * @author CPZ
 */
public final class ProgressBarFactory {
    private ProgressBarFactory() {
    }

    public static ProgressBar create(PApplet sketch, ProgressBarConfig config) {
        Objects.requireNonNull(sketch, "sketch");
        Objects.requireNonNull(config, "config");

        ProgressBar progressBar = new ProgressBar(sketch, config.getCode(), config.getBounds());
        progressBar.setRange(config.getMin(), config.getMax());
        progressBar.setValue(config.getValue());
        progressBar.setTrackColor(config.getTrackColor());
        progressBar.setFillColor(config.getFillColor());
        progressBar.setStrokeColor(config.getStrokeColor());
        progressBar.setStrokeWeight(config.getStrokeWeight());
        progressBar.setEnabled(config.isEnabled());
        progressBar.setVisible(config.isVisible());
        progressBar.setTooltip(TooltipFactory.create(sketch, config.getTooltip()));
        return progressBar;
    }
}
