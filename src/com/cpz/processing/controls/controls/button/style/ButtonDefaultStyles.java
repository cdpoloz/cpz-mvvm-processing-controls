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
 *
 * @author CPZ
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
    * @param themeProvider parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static ButtonStyle primary(ThemeProvider themeProvider) {
      ButtonStyleConfig config = new ButtonStyleConfig();
      config.strokeWeight = 2.0F;
      config.strokeWeightHover = 3.0F;
      config.cornerRadius = 10.0F;
      config.themeProvider = themeProvider;
      return new DefaultButtonStyle(config);
   }
}
