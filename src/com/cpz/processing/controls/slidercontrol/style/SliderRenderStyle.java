package com.cpz.processing.controls.slidercontrol.style;

import processing.core.PShape;

public record SliderRenderStyle(int trackColor,
                                int trackStrokeColor,
                                float trackStrokeWeight,
                                float trackThickness,
                                int activeTrackColor,
                                int thumbColor,
                                int thumbStrokeColor,
                                float thumbStrokeWeight,
                                float thumbSize,
                                int textColor,
                                SvgColorMode svgColorMode,
                                PShape thumbShape,
                                boolean showValueText) {
}
