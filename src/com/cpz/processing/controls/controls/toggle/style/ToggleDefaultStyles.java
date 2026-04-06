package com.cpz.processing.controls.controls.toggle.style;

import com.cpz.processing.controls.controls.toggle.config.ToggleStyleConfig;
import com.cpz.processing.controls.controls.toggle.style.render.CircleShapeRenderer;
import com.cpz.processing.controls.core.theme.ThemeProvider;

public final class ToggleDefaultStyles {
   private ToggleDefaultStyles() {
   }

   public static ToggleStyle circular() {
      return circular((ThemeProvider)null);
   }

   public static ToggleStyle circular(ThemeProvider var0) {
      ToggleStyleConfig var1 = new ToggleStyleConfig();
      var1.shape = new CircleShapeRenderer();
      var1.strokeWidth = 2.0F;
      var1.strokeWidthHover = 3.0F;
      var1.themeProvider = var0;
      return new ParametricToggleStyle(var1);
   }
}
