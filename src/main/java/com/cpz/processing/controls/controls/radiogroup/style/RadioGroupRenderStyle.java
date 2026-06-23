package com.cpz.processing.controls.controls.radiogroup.style;

import java.util.List;
import processing.core.PFont;

/**
 * Immutable render-state record for radio group render style.
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
public record RadioGroupRenderStyle(List itemStyles, float indicatorOuterDiameter, float indicatorInnerDiameter, float strokeWeight, float textSize, float cornerRadius, PFont font) {
}
