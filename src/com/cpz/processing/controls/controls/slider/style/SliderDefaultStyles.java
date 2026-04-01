package com.cpz.processing.controls.controls.slider.style;

import com.cpz.processing.controls.controls.slider.config.SliderStyleConfig;

public final class SliderDefaultStyles {
   private SliderDefaultStyles() {
   }

   public static SliderStyle standard() {
      SliderStyleConfig var0 = new SliderStyleConfig();
      var0.trackStrokeWeight = 1.5F;
      var0.trackStrokeWeightHover = 2.5F;
      var0.trackThickness = 8.0F;
      var0.thumbStrokeWeight = 2.0F;
      var0.thumbStrokeWeightHover = 3.0F;
      var0.thumbSize = 24.0F;
      var0.showValueText = true;
      var0.svgColorMode = SvgColorMode.USE_RENDER_STYLE;
      return new SliderStyle(var0);
   }
}
