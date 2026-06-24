package com.cpz.processing.controls.controls.slider.style;

import processing.core.PFont;
import processing.core.PShape;

/**
 * Immutable render-state record for slider render style.
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
 * @param trackColor track color
 * @param trackStrokeColor track stroke color
 * @param trackStrokeWeight track stroke width
 * @param trackThickness track thickness
 * @param activeTrackColor active track color
 * @param thumbColor thumb color
 * @param thumbStrokeColor thumb stroke color
 * @param thumbStrokeWeight thumb stroke width
 * @param thumbSize thumb size
 * @param textColor value text color
 * @param svgColorMode SVG color mode
 * @param thumbShape optional thumb shape
 * @param showValueText whether value text is displayed
 * @param font optional value-text font
 * @param textSize optional value-text size
 * @author CPZ
 */
public record SliderRenderStyle(int trackColor, int trackStrokeColor, float trackStrokeWeight, float trackThickness, int activeTrackColor, int thumbColor, int thumbStrokeColor, float thumbStrokeWeight, float thumbSize, int textColor, SvgColorMode svgColorMode, PShape thumbShape, boolean showValueText, PFont font, Float textSize) {
   /**
    * Creates a render style using the historical ambient typography behavior.
    */
   public SliderRenderStyle(int trackColor, int trackStrokeColor, float trackStrokeWeight, float trackThickness, int activeTrackColor, int thumbColor, int thumbStrokeColor, float thumbStrokeWeight, float thumbSize, int textColor, SvgColorMode svgColorMode, PShape thumbShape, boolean showValueText) {
      this(trackColor, trackStrokeColor, trackStrokeWeight, trackThickness, activeTrackColor, thumbColor, thumbStrokeColor, thumbStrokeWeight, thumbSize, textColor, svgColorMode, thumbShape, showValueText, null, null);
   }
}
