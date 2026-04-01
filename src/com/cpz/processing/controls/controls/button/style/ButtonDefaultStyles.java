package com.cpz.processing.controls.controls.button.style;

import com.cpz.processing.controls.controls.button.config.ButtonStyleConfig;

public final class ButtonDefaultStyles {
   private ButtonDefaultStyles() {
   }

   public static ButtonStyle primary() {
      ButtonStyleConfig var0 = new ButtonStyleConfig();
      var0.strokeWeight = 2.0F;
      var0.strokeWeightHover = 3.0F;
      var0.cornerRadius = 10.0F;
      return new DefaultButtonStyle(var0);
   }
}
