package com.cpz.processing.controls.controls.radiogroup.style;

import com.cpz.processing.controls.controls.radiogroup.config.RadioGroupStyleConfig;
import com.cpz.processing.controls.core.theme.ThemeProvider;

/**
 * Style component for radio group default styles.
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
public final class RadioGroupDefaultStyles {
   private RadioGroupDefaultStyles() {
   }

   /**
    * Performs standard.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static RadioGroupStyle standard() {
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
   public static RadioGroupStyle standard(ThemeProvider var0) {
      RadioGroupStyleConfig var1 = new RadioGroupStyleConfig();
      var1.themeProvider = var0;
      return new RadioGroupStyle(var1);
   }
}
