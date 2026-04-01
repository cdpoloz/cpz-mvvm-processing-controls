package com.cpz.processing.controls.controls.checkbox.style;

import com.cpz.processing.controls.controls.checkbox.config.CheckboxStyleConfig;

public final class CheckboxDefaultStyles {
   private CheckboxDefaultStyles() {
   }

   public static CheckboxStyle standard() {
      CheckboxStyleConfig var0 = new CheckboxStyleConfig();
      var0.borderWidth = 2.0F;
      var0.borderWidthHover = 3.0F;
      var0.cornerRadius = 5.0F;
      var0.checkInset = 0.24F;
      return new DefaultCheckboxStyle(var0);
   }
}
