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
 *
 * @author CPZ
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
    * @param themeProvider parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static SliderStyle standard(ThemeProvider themeProvider) {
      SliderStyleConfig config = new SliderStyleConfig();
      config.trackStrokeWeight = 1.5F;
      config.trackStrokeWeightHover = 2.5F;
      config.trackThickness = 8.0F;
      config.thumbStrokeWeight = 2.0F;
      config.thumbStrokeWeightHover = 3.0F;
      config.thumbSize = 24.0F;
      config.showValueText = true;
      config.svgColorMode = SvgColorMode.USE_RENDER_STYLE;
      config.themeProvider = themeProvider;
      return new SliderStyle(config);
   }
}
