package com.cpz.processing.controls.controls.button.style;

import processing.core.PFont;

/**
 * Immutable render-state record for button render style.
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
 * @param fillColor fill color
 * @param strokeColor stroke color
 * @param strokeWeight stroke width
 * @param textColor text color
 * @param cornerRadius corner radius
 * @param showText whether text is displayed
 * @param text displayed text
 * @param font optional text font
 * @param textSize optional text size
 * @author CPZ
 */
public record ButtonRenderStyle(int fillColor, int strokeColor, float strokeWeight, int textColor, float cornerRadius, boolean showText, String text, PFont font, Float textSize) {
   /**
    * Creates a render style using the historical ambient typography behavior.
    */
   public ButtonRenderStyle(int fillColor, int strokeColor, float strokeWeight, int textColor, float cornerRadius, boolean showText, String text) {
      this(fillColor, strokeColor, strokeWeight, textColor, cornerRadius, showText, text, null, null);
   }
}
