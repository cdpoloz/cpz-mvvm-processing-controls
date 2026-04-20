package com.cpz.processing.controls.controls.slider;

import com.cpz.processing.controls.controls.slider.config.SliderConfig;
import com.cpz.processing.controls.controls.slider.config.SliderStyleConfig;
import com.cpz.processing.controls.controls.slider.style.SliderStyle;
import processing.core.PApplet;
import processing.core.PShape;

import java.util.Objects;

/**
 * Factory for creating the public slider facade from external config.
 *
 * @author CPZ
 */
public final class SliderFactory {
    public static Slider create(PApplet sketch, SliderConfig config) {
        Objects.requireNonNull(sketch, "sketch");
        Objects.requireNonNull(config, "config");

        Slider slider = new Slider(
                sketch,
                config.getCode(),
                config.getMin(),
                config.getMax(),
                config.getStep(),
                config.getValue(),
                config.getX(),
                config.getY(),
                config.getWidth(),
                config.getHeight(),
                config.getOrientation(),
                config.getSnapMode()
        );
        slider.setEnabled(config.isEnabled());
        slider.setVisible(config.isVisible());

        if (config.getStyle() != null) {
            slider.setStyle(new SliderStyle(toStyleConfig(sketch, config.getStyle())));
        }

        return slider;
    }

    private static SliderStyleConfig toStyleConfig(PApplet sketch, SliderConfig.StyleConfig style) {
        SliderStyleConfig result = new SliderStyleConfig();
        result.trackOverride = style.getTrackOverride();
        result.trackHoverOverride = style.getTrackHoverOverride();
        result.trackPressedOverride = style.getTrackPressedOverride();
        result.progressOverride = style.getProgressOverride();
        result.progressHoverOverride = style.getProgressHoverOverride();
        result.progressPressedOverride = style.getProgressPressedOverride();
        result.thumbOverride = style.getThumbOverride();
        result.thumbHoverOverride = style.getThumbHoverOverride();
        result.thumbPressedOverride = style.getThumbPressedOverride();
        result.trackStrokeOverride = style.getTrackStrokeOverride();
        result.thumbStrokeOverride = style.getThumbStrokeOverride();
        result.textOverride = style.getTextOverride();
        result.trackColor = style.getTrackColor();
        result.trackHoverColor = style.getTrackHoverColor();
        result.trackPressedColor = style.getTrackPressedColor();
        result.trackStrokeColor = style.getTrackStrokeColor();
        if (style.getTrackStrokeWeight() != null) {
            result.trackStrokeWeight = style.getTrackStrokeWeight();
        }
        if (style.getTrackStrokeWeightHover() != null) {
            result.trackStrokeWeightHover = style.getTrackStrokeWeightHover();
        }
        if (style.getTrackThickness() != null) {
            result.trackThickness = style.getTrackThickness();
        }
        result.activeTrackColor = style.getActiveTrackColor();
        result.activeTrackHoverColor = style.getActiveTrackHoverColor();
        result.activeTrackPressedColor = style.getActiveTrackPressedColor();
        result.thumbColor = style.getThumbColor();
        result.thumbHoverColor = style.getThumbHoverColor();
        result.thumbPressedColor = style.getThumbPressedColor();
        result.thumbStrokeColor = style.getThumbStrokeColor();
        if (style.getThumbStrokeWeight() != null) {
            result.thumbStrokeWeight = style.getThumbStrokeWeight();
        }
        if (style.getThumbStrokeWeightHover() != null) {
            result.thumbStrokeWeightHover = style.getThumbStrokeWeightHover();
        }
        if (style.getThumbSize() != null) {
            result.thumbSize = style.getThumbSize();
        }
        result.textColor = style.getTextColor();
        result.disabledAlpha = style.getDisabledAlpha();
        if (style.getShowValueText() != null) {
            result.showValueText = style.getShowValueText();
        }
        if (style.getSvgColorMode() != null) {
            result.svgColorMode = style.getSvgColorMode();
        }
        if (style.getRenderer() != null && "svg".equals(style.getRenderer().getType())) {
            result.thumbShape = loadThumbShape(sketch, style.getRenderer().getPath());
        }
        return result;
    }

    private static PShape loadThumbShape(PApplet sketch, String path) {
        PShape shape = sketch.loadShape(path);
        if (shape == null && path.startsWith("data/")) {
            shape = sketch.loadShape(path.substring("data/".length()));
        }
        if (shape == null) {
            throw new IllegalArgumentException("Could not load slider SVG thumb: " + path);
        }
        return shape;
    }
}
