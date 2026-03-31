package com.cpz.processing.controls.slidercontrol.style;

import com.cpz.processing.controls.common.style.InteractiveStyleHelper;
import com.cpz.processing.controls.slidercontrol.style.render.SliderRenderer;
import com.cpz.processing.controls.slidercontrol.view.SliderGeometry;
import com.cpz.processing.controls.slidercontrol.view.SliderViewState;
import processing.core.PApplet;

public final class SliderStyle {

    private final SliderStyleConfig config;
    private final SliderRenderer renderer;

    public SliderStyle(SliderStyleConfig config) {
        this(config, new SliderRenderer());
    }

    public SliderStyle(SliderStyleConfig config, SliderRenderer renderer) {
        if (config == null) {
            throw new IllegalArgumentException("config must not be null");
        }
        this.config = config;
        this.renderer = renderer == null ? new SliderRenderer() : renderer;
    }

    public void render(PApplet p, SliderViewState state, SliderGeometry geometry, String valueText) {
        int trackColor = InteractiveStyleHelper.resolveFillColor(
                config.trackColor,
                config.trackHoverColor,
                config.trackPressedColor,
                state.hovered(),
                state.pressed() || state.dragging()
        );
        int activeTrackColor = InteractiveStyleHelper.resolveFillColor(
                config.activeTrackColor,
                config.activeTrackHoverColor,
                config.activeTrackPressedColor,
                state.hovered(),
                state.pressed() || state.dragging()
        );
        int thumbColor = InteractiveStyleHelper.resolveFillColor(
                config.thumbColor,
                config.thumbHoverColor,
                config.thumbPressedColor,
                state.hovered(),
                state.pressed() || state.dragging()
        );
        SliderRenderStyle renderStyle = new SliderRenderStyle(
                InteractiveStyleHelper.applyDisabledAlpha(trackColor, state.enabled(), config.disabledAlpha),
                InteractiveStyleHelper.resolveStrokeColor(config.trackStrokeColor, state.enabled(), config.disabledAlpha),
                InteractiveStyleHelper.resolveStrokeWeight(config.trackStrokeWeight, config.trackStrokeWeightHover, state.hovered()),
                config.trackThickness,
                InteractiveStyleHelper.applyDisabledAlpha(activeTrackColor, state.enabled(), config.disabledAlpha),
                InteractiveStyleHelper.applyDisabledAlpha(thumbColor, state.enabled(), config.disabledAlpha),
                InteractiveStyleHelper.resolveStrokeColor(config.thumbStrokeColor, state.enabled(), config.disabledAlpha),
                InteractiveStyleHelper.resolveStrokeWeight(config.thumbStrokeWeight, config.thumbStrokeWeightHover, state.hovered()),
                config.thumbSize,
                InteractiveStyleHelper.resolveStrokeColor(config.textColor, state.enabled(), config.disabledAlpha),
                config.svgColorMode,
                config.thumbShape,
                config.showValueText,
                valueText
        );
        renderer.render(p, geometry, state, renderStyle);
    }
}
