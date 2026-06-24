package com.cpz.processing.controls.controls.radiogroup.style;

/**
 * Immutable render-state record for radio group item render style.
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
 * @param textColor text color
 * @param indicatorStrokeColor indicator stroke color
 * @param indicatorFillColor indicator fill color
 * @param indicatorDotColor selected indicator dot color
 * @param backgroundColor item background color
 * @author CPZ
 */
public record RadioGroupItemRenderStyle(int textColor, int indicatorStrokeColor, int indicatorFillColor, int indicatorDotColor, int backgroundColor) {
}
