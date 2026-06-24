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
 * @param backgroundColor background color
 * @param borderColor border color
 * @param textColor text color
 * @param cursorColor cursor color
 * @param selectionColor selection background color
 * @param selectionTextColor selected text color
 * @param textSize text size
 * @param font text font
 * @author CPZ
 */
public record TextFieldRenderStyle(int backgroundColor, int borderColor, int textColor, int cursorColor, int selectionColor, int selectionTextColor, float textSize, PFont font) {
}
