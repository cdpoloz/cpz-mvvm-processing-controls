package com.cpz.processing.controls.controls.textfield.style;

import com.cpz.processing.controls.controls.textfield.config.TextFieldStyleConfig;
import com.cpz.processing.controls.core.theme.ThemeProvider;

/**
 * Style component for text field default styles.
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
public final class TextFieldDefaultStyles {
   private TextFieldDefaultStyles() {
   }

   /**
    * Performs standard.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static DefaultTextFieldStyle standard() {
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
   public static DefaultTextFieldStyle standard(ThemeProvider var0) {
      TextFieldStyleConfig var1 = new TextFieldStyleConfig();
      var1.textSize = 16.0F;
      var1.font = null;
      var1.themeProvider = var0;
      return new DefaultTextFieldStyle(var1);
   }
}
