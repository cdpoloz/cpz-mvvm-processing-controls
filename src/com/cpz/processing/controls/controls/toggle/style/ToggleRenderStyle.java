package com.cpz.processing.controls.controls.toggle.style;

/**
 * Immutable render-state record for toggle render style.
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
public record ToggleRenderStyle(int fillColor, int strokeColor, float strokeWeight) {
}
