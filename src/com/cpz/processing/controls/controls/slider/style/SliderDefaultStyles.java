package com.cpz.processing.controls.controls.slider.style;

import com.cpz.processing.controls.controls.slider.config.SliderStyleConfig;
import com.cpz.processing.controls.core.theme.ThemeProvider;

public final class SliderDefaultStyles {
   private SliderDefaultStyles() {
   }

   public static SliderStyle standard() {
      return standard((ThemeProvider)null);
   }

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
