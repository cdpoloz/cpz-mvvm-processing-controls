package com.cpz.processing.controls.core.overlay.tooltip.style;

import processing.core.PFont;

/**
 * Immutable render-state record for tooltip render style.
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
 * @param backgroundColor background color
 * @param textColor text color
 * @param strokeColor stroke color
 * @param strokeWeight stroke width
 * @param textSize text size
 * @param textPadding text padding
 * @param cornerRadius corner radius
 * @param minHeight minimum height
 * @param font text font
 * @author CPZ
 */
public record TooltipRenderStyle(int backgroundColor, int textColor, int strokeColor, float strokeWeight, float textSize, float textPadding, float cornerRadius, float minHeight, PFont font) {
}
