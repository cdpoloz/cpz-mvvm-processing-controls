package com.cpz.processing.controls.slidercontrol.style;

import processing.core.PShape;

public final class SliderStyleConfig {

    public int trackColor;
    public int trackHoverColor;
    public int trackPressedColor;
    public int trackStrokeColor;
    public float trackStrokeWeight;
    public float trackStrokeWeightHover;
    public float trackThickness;

    public int activeTrackColor;
    public int activeTrackHoverColor;
    public int activeTrackPressedColor;

    public int thumbColor;
    public int thumbHoverColor;
    public int thumbPressedColor;
    public int thumbStrokeColor;
    public float thumbStrokeWeight;
    public float thumbStrokeWeightHover;
    public float thumbSize;

    public int textColor;
    public int disabledAlpha;
    public boolean showValueText = true;
    public SvgColorMode svgColorMode = SvgColorMode.USE_RENDER_STYLE;
    public PShape thumbShape;
}
