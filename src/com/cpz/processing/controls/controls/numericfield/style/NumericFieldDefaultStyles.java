package com.cpz.processing.controls.controls.numericfield.style;

import com.cpz.processing.controls.controls.numericfield.config.NumericFieldStyleConfig;
import com.cpz.processing.controls.core.theme.ThemeProvider;

/**
 * Style component for numeric field default styles.
 *
 * Responsibilities:
 * - Resolve visual values from immutable state and theme data.
 * - Keep interaction rules outside the rendering layer.
 *
 * Behavior:
 * - Does not process raw input or mutate the backing model.
 *
 * Notes:
 * - This type belongs to the visual styling pipeline.
 */
public final class NumericFieldDefaultStyles {
   private NumericFieldDefaultStyles() {
   }

   /**
    * Performs standard.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static NumericFieldStyle standard() {
      return standard((ThemeProvider)null);
   }

   /**
    * Performs standard.
    *
    * @param var0 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static NumericFieldStyle standard(ThemeProvider var0) {
      NumericFieldStyleConfig var1 = new NumericFieldStyleConfig();
      var1.themeProvider = var0;
      return new NumericFieldStyle(var1);
   }
}
