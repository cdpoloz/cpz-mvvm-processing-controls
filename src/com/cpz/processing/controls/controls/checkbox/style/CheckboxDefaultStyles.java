package com.cpz.processing.controls.controls.checkbox.style;

import com.cpz.processing.controls.controls.checkbox.config.CheckboxStyleConfig;
import com.cpz.processing.controls.core.theme.ThemeProvider;

/**
 * Style component for checkbox default styles.
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
public final class CheckboxDefaultStyles {
   private CheckboxDefaultStyles() {
   }

   /**
    * Performs standard.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static CheckboxStyle standard() {
      return standard((ThemeProvider)null);
   }

   /**
    * Performs standard.
    *
    * @param themeProvider parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static CheckboxStyle standard(ThemeProvider themeProvider) {
      CheckboxStyleConfig config = new CheckboxStyleConfig();
      config.borderWidth = 2.0F;
      config.borderWidthHover = 3.0F;
      config.cornerRadius = 5.0F;
      config.checkInset = 0.24F;
      config.themeProvider = themeProvider;
      return new DefaultCheckboxStyle(config);
   }
}
