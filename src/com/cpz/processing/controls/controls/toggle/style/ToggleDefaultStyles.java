package com.cpz.processing.controls.controls.toggle.style;

import com.cpz.processing.controls.controls.toggle.config.ToggleStyleConfig;
import com.cpz.processing.controls.controls.toggle.style.render.CircleShapeRenderer;

public final class ToggleDefaultStyles {
   private ToggleDefaultStyles() {
   }

   public static ToggleStyle circular() {
      ToggleStyleConfig var0 = new ToggleStyleConfig();
      var0.shape = new CircleShapeRenderer();
      var0.strokeWidth = 2.0F;
      var0.strokeWidthHover = 3.0F;
      return new ParametricToggleStyle(var0);
   }
}
