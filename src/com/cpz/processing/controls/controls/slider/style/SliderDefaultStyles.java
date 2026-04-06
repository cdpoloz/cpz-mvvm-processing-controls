package com.cpz.processing.controls.controls.slider.style;

import com.cpz.processing.controls.controls.slider.config.SliderStyleConfig;
import com.cpz.processing.controls.core.theme.ThemeProvider;

/**
 * Style component for slider default styles.
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
public final class SliderDefaultStyles {
   private SliderDefaultStyles() {
   }

   /**
    * Performs standard.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static SliderStyle standard() {
      return standard((ThemeProvider)null);
   }

   /**
    * Performs standard.
    *
    * @param var0 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static SliderStyle standard(ThemeProvider var0) {
      SliderStyleConfig var1 = new SliderStyleConfig();
      var1.trackStrokeWeight = 1.5F;
      var1.trackStrokeWeightHover = 2.5F;
      var1.trackThickness = 8.0F;
      var1.thumbStrokeWeight = 2.0F;
      var1.thumbStrokeWeightHover = 3.0F;
      var1.thumbSize = 24.0F;
      var1.showValueText = true;
      var1.svgColorMode = SvgColorMode.USE_RENDER_STYLE;
      var1.themeProvider = var0;
      return new SliderStyle(var1);
   }
}
