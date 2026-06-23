package com.cpz.processing.controls.controls.textfield.style;

import processing.core.PFont;

/**
 * Immutable render-state record for text field render style.
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
public record TextFieldRenderStyle(int backgroundColor, int borderColor, int textColor, int cursorColor, int selectionColor, int selectionTextColor, float textSize, PFont font) {
}
