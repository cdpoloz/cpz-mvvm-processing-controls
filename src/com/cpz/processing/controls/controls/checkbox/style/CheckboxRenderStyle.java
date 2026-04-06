package com.cpz.processing.controls.controls.checkbox.style;

/**
 * Immutable render-state record for checkbox render style.
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
public record CheckboxRenderStyle(int boxFillColor, int borderColor, float borderWidth, float cornerRadius, boolean showCheck, int checkColor, float checkInset, float checkStrokeWeight) {
}
