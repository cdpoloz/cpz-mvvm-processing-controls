package com.cpz.processing.controls.controls.checkbox.style;

import com.cpz.processing.controls.controls.checkbox.config.CheckboxStyleConfig;
import com.cpz.processing.controls.core.theme.ThemeProvider;

public final class CheckboxDefaultStyles {
   private CheckboxDefaultStyles() {
   }

   public static CheckboxStyle standard() {
      return standard((ThemeProvider)null);
   }

   public static CheckboxStyle standard(ThemeProvider var0) {
      CheckboxStyleConfig var1 = new CheckboxStyleConfig();
      var1.borderWidth = 2.0F;
      var1.borderWidthHover = 3.0F;
      var1.cornerRadius = 5.0F;
      var1.checkInset = 0.24F;
      var1.themeProvider = var0;
      return new DefaultCheckboxStyle(var1);
   }
}
