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
 *
 * @param boxFillColor box fill color
 * @param borderColor border color
 * @param borderWidth border width
 * @param cornerRadius corner radius
 * @param showCheck whether the check mark is displayed
 * @param checkColor check mark color
 * @param checkInset check mark inset
 * @param checkStrokeWeight check mark stroke width
 * @author CPZ
 */
public record CheckboxRenderStyle(int boxFillColor, int borderColor, float borderWidth, float cornerRadius, boolean showCheck, int checkColor, float checkInset, float checkStrokeWeight) {
}
