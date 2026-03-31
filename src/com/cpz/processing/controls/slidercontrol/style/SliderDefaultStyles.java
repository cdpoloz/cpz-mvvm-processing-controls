package com.cpz.processing.controls.slidercontrol.style;

public final class SliderDefaultStyles {

    private SliderDefaultStyles() {
    }

    public static SliderStyle standard() {
        SliderStyleConfig config = new SliderStyleConfig();
        config.trackStrokeWeight = 1.5f;
        config.trackStrokeWeightHover = 2.5f;
        config.trackThickness = 8f;
        config.thumbStrokeWeight = 2f;
        config.thumbStrokeWeightHover = 3f;
        config.thumbSize = 24f;
        config.showValueText = true;
        config.svgColorMode = SvgColorMode.USE_RENDER_STYLE;
        return new SliderStyle(config);
    }
}
