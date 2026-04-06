package com.cpz.processing.controls.controls.radiogroup.style;

import com.cpz.processing.controls.controls.radiogroup.config.RadioGroupStyleConfig;
import com.cpz.processing.controls.core.theme.ThemeProvider;

public final class RadioGroupDefaultStyles {
   private RadioGroupDefaultStyles() {
   }

   public static RadioGroupStyle standard() {
      return standard((ThemeProvider)null);
   }

   public static RadioGroupStyle standard(ThemeProvider var0) {
      RadioGroupStyleConfig var1 = new RadioGroupStyleConfig();
      var1.themeProvider = var0;
      return new RadioGroupStyle(var1);
   }
}
