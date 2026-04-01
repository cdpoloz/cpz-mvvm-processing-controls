package com.cpz.processing.controls.controls.numericfield.style;

import com.cpz.processing.controls.controls.numericfield.config.NumericFieldStyleConfig;

public final class NumericFieldDefaultStyles {
   private NumericFieldDefaultStyles() {
   }

   public static NumericFieldStyle standard() {
      return new NumericFieldStyle(new NumericFieldStyleConfig());
   }
}
