package com.cpz.processing.controls.controls.slider.style;

import processing.core.PShape;

/**
 * Immutable render-state record for slider render style.
 *
 * Responsibilities:
 * - Resolve visual values from immutable state and theme data.
 * - Keep interaction rules outside the rendering layer.
 *
 * Behavior:
 * - Does not process raw input or mutate the backing model.
 *
 * Notes:
 * - This type belongs to the visual styling pipeline.
 *
 * @author CPZ
 */
public record SliderRenderStyle(int trackColor, int trackStrokeColor, float trackStrokeWeight, float trackThickness, int activeTrackColor, int thumbColor, int thumbStrokeColor, float thumbStrokeWeight, float thumbSize, int textColor, SvgColorMode svgColorMode, PShape thumbShape, boolean showValueText) {
}
