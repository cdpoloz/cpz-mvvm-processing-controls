package com.cpz.processing.controls.controls.label.style;

/**
 * Immutable render-state record for label render style.
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
public record LabelRenderStyle(String text, int textColor, LabelTypography typography) {
}
