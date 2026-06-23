package com.cpz.processing.controls.controls.slider.style;

/**
 * Enumeration of svg color mode values.
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
public enum SvgColorMode {
   USE_RENDER_STYLE,
   USE_SVG_ORIGINAL;

   // $FF: synthetic method
   private static SvgColorMode[] $values() {
      return new SvgColorMode[]{USE_RENDER_STYLE, USE_SVG_ORIGINAL};
   }
}
