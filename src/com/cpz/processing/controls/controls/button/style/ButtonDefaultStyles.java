package com.cpz.processing.controls.controls.button.style;

import com.cpz.processing.controls.controls.button.config.ButtonStyleConfig;
import com.cpz.processing.controls.core.theme.ThemeProvider;

/**
 * Style component for button default styles.
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
public final class ButtonDefaultStyles {
   private ButtonDefaultStyles() {
   }

   /**
    * Performs primary.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static ButtonStyle primary() {
      return primary((ThemeProvider)null);
   }

   /**
    * Performs primary.
    *
    * @param var0 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static ButtonStyle primary(ThemeProvider var0) {
      ButtonStyleConfig var1 = new ButtonStyleConfig();
      var1.strokeWeight = 2.0F;
      var1.strokeWeightHover = 3.0F;
      var1.cornerRadius = 10.0F;
      var1.themeProvider = var0;
      return new DefaultButtonStyle(var1);
   }
}
