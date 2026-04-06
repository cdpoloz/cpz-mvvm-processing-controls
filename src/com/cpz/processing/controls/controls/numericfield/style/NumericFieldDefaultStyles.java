package com.cpz.processing.controls.controls.numericfield.style;

import com.cpz.processing.controls.controls.numericfield.config.NumericFieldStyleConfig;
import com.cpz.processing.controls.core.theme.ThemeProvider;

public final class NumericFieldDefaultStyles {
   private NumericFieldDefaultStyles() {
   }

   public static NumericFieldStyle standard() {
      return standard((ThemeProvider)null);
   }

   public static NumericFieldStyle standard(ThemeProvider var0) {
      NumericFieldStyleConfig var1 = new NumericFieldStyleConfig();
      var1.themeProvider = var0;
      return new NumericFieldStyle(var1);
   }
}
