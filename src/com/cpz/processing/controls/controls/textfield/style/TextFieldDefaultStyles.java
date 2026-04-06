package com.cpz.processing.controls.controls.textfield.style;

import com.cpz.processing.controls.controls.textfield.config.TextFieldStyleConfig;
import com.cpz.processing.controls.core.theme.ThemeProvider;

public final class TextFieldDefaultStyles {
   private TextFieldDefaultStyles() {
   }

   public static DefaultTextFieldStyle standard() {
      return standard((ThemeProvider)null);
   }

   public static DefaultTextFieldStyle standard(ThemeProvider var0) {
      TextFieldStyleConfig var1 = new TextFieldStyleConfig();
      var1.textSize = 16.0F;
      var1.font = null;
      var1.themeProvider = var0;
      return new DefaultTextFieldStyle(var1);
   }
}
