package com.cpz.processing.controls.controls.textfield.style;

import com.cpz.processing.controls.controls.textfield.config.TextFieldStyleConfig;

public final class TextFieldDefaultStyles {
   private TextFieldDefaultStyles() {
   }

   public static DefaultTextFieldStyle standard() {
      TextFieldStyleConfig var0 = new TextFieldStyleConfig();
      var0.textSize = 16.0F;
      var0.font = null;
      return new DefaultTextFieldStyle(var0);
   }
}
