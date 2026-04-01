package com.cpz.processing.controls.controls.radiogroup.style;

import com.cpz.processing.controls.controls.radiogroup.config.RadioGroupStyleConfig;

public final class RadioGroupDefaultStyles {
   private RadioGroupDefaultStyles() {
   }

   public static RadioGroupStyle standard() {
      return new RadioGroupStyle(new RadioGroupStyleConfig());
   }
}
