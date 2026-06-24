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
 *
 * @param baseFillColor collapsed control fill color
 * @param listFillColor expanded list fill color
 * @param itemHoverColor hovered item color
 * @param itemSelectedColor selected item color
 * @param strokeColor stroke color
 * @param strokeWeight stroke width
 * @param textColor text color
 * @param arrowColor arrow color
 * @param cornerRadius control corner radius
 * @param listCornerRadius list corner radius
 * @param textSize text size
 * @param itemHeight item height
 * @param textPadding text padding
 * @param arrowPadding arrow padding
 * @param maxVisibleItems maximum number of visible items
 * @param font text font
 * @author CPZ
 */
public record DropDownRenderStyle(int baseFillColor, int listFillColor, int itemHoverColor, int itemSelectedColor, int strokeColor, float strokeWeight, int textColor, int arrowColor, float cornerRadius, float listCornerRadius, float textSize, float itemHeight, float textPadding, float arrowPadding, int maxVisibleItems, PFont font) {
}
