package com.cpz.processing.controls.controls.dropdown.style;

import processing.core.PFont;

/**
 * Immutable render-state record for drop down render style.
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
public record DropDownRenderStyle(int baseFillColor, int listFillColor, int itemHoverColor, int itemSelectedColor, int strokeColor, float strokeWeight, int textColor, int arrowColor, float cornerRadius, float listCornerRadius, float textSize, float itemHeight, float textPadding, float arrowPadding, int maxVisibleItems, PFont font) {
}
