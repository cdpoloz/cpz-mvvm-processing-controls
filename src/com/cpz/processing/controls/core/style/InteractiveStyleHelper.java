package com.cpz.processing.controls.core.style;

import com.cpz.processing.controls.core.util.Colors;
import processing.core.PApplet;

/**
 * Style component for interactive style helper.
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
public final class InteractiveStyleHelper {
   private InteractiveStyleHelper() {
   }

   /**
    * Resolves fill color.
    *
    * @param baseFillColor normal fill color
    * @param hoverFillColor fill color used while hovered
    * @param pressedFillColor fill color used while pressed
    * @param hovered whether the control is hovered
    * @param pressed whether the control is pressed
    * @return resolved fill color
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public static int resolveFillColor(int baseFillColor, int hoverFillColor, int pressedFillColor, boolean hovered, boolean pressed) {
      if (pressed) {
         return pressedFillColor;
      } else {
         return hovered ? hoverFillColor : baseFillColor;
      }
   }

   /**
    * Resolves fill color with overlays.
    *
    * @param baseFillColor normal fill color
    * @param hoverOverlay overlay applied while hovered
    * @param pressedOverlay overlay applied while pressed
    * @param hovered whether the control is hovered
    * @param pressed whether the control is pressed
    * @return resolved fill color with overlays
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public static int resolveFillColorWithOverlays(int baseFillColor, int hoverOverlay, int pressedOverlay, boolean hovered, boolean pressed) {
      return resolveFillColor(baseFillColor, applyOverlay(baseFillColor, hoverOverlay), applyOverlay(baseFillColor, pressedOverlay), hovered, pressed);
   }

   /**
    * Resolves fill color.
    *
    * @param sketch Processing sketch used for color interpolation
    * @param baseFillColor normal fill color
    * @param hoverBlend blend factor for the hover color
    * @param pressedBlend blend factor for the pressed color
    * @param hovered whether the control is hovered
    * @param pressed whether the control is pressed
    * @return resolved fill color
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public static int resolveFillColor(PApplet sketch, int baseFillColor, float hoverBlend, float pressedBlend, boolean hovered, boolean pressed) {
      int hoverFillColor = sketch.lerpColor(baseFillColor, sketch.color(255), hoverBlend);
      int pressedFillColor = sketch.lerpColor(baseFillColor, sketch.color(0), pressedBlend);
      return resolveFillColor(baseFillColor, hoverFillColor, pressedFillColor, hovered, pressed);
   }

   /**
    * Performs apply disabled alpha.
    *
    * @param color source color
    * @param enabled whether the control is enabled
    * @param disabledAlpha alpha applied while disabled
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int applyDisabledAlpha(int color, boolean enabled, int disabledAlpha) {
      return enabled ? color : Colors.alpha(disabledAlpha, color);
   }

   /**
    * Performs apply overlay.
    *
    * @param baseColor base color
    * @param overlayColor ARGB overlay color
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int applyOverlay(int baseColor, int overlayColor) {
      float overlayAlpha = (float)(overlayColor >>> 24 & 255) / 255.0F;
      int alpha = blendChannel(baseColor >>> 24 & 255, overlayColor >>> 24 & 255, overlayAlpha);
      int red = blendChannel(baseColor >>> 16 & 255, overlayColor >>> 16 & 255, overlayAlpha);
      int green = blendChannel(baseColor >>> 8 & 255, overlayColor >>> 8 & 255, overlayAlpha);
      int blue = blendChannel(baseColor & 255, overlayColor & 255, overlayAlpha);
      return Colors.argb(alpha, red, green, blue);
   }

   /**
    * Resolves stroke weight.
    *
    * @param baseStrokeWeight normal stroke weight
    * @param hoverStrokeWeight stroke weight used while hovered
    * @param hovered whether the control is hovered
    * @return resolved stroke weight
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public static float resolveStrokeWeight(float baseStrokeWeight, float hoverStrokeWeight, boolean hovered) {
      return hovered ? hoverStrokeWeight : baseStrokeWeight;
   }

   /**
    * Resolves stroke color.
    *
    * @param color source stroke color
    * @param enabled whether the control is enabled
    * @param disabledAlpha alpha applied while disabled
    * @return resolved stroke color
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public static int resolveStrokeColor(int color, boolean enabled, int disabledAlpha) {
      return applyDisabledAlpha(color, enabled, disabledAlpha);
   }

   private static int blendChannel(int baseChannel, int overlayChannel, float amount) {
      return Math.round((float)baseChannel + (float)(overlayChannel - baseChannel) * amount);
   }
}
