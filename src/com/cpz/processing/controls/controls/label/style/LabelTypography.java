package com.cpz.processing.controls.controls.label.style;

import processing.core.PFont;

/**
 * Immutable data record for label typography.
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
 */
public record LabelTypography(PFont font, float textSize, float lineSpacingMultiplier, HorizontalAlign textAlignHorizontal, VerticalAlign textAlignVertical) {
}
