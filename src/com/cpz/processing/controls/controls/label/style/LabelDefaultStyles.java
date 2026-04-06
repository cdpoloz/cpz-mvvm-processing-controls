package com.cpz.processing.controls.controls.label.style;

import com.cpz.processing.controls.controls.label.config.LabelStyleConfig;
import com.cpz.processing.controls.core.theme.ThemeProvider;

/**
 * Style component for label default styles.
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
public final class LabelDefaultStyles {
   private LabelDefaultStyles() {
   }

   /**
    * Performs default text.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static LabelStyle defaultText() {
      return defaultText((ThemeProvider)null);
   }

   /**
    * Performs default text.
    *
    * @param var0 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static LabelStyle defaultText(ThemeProvider var0) {
      LabelStyleConfig var1 = new LabelStyleConfig();
      var1.font = null;
      var1.textSize = 12.0F;
      var1.textColor = null;
      var1.lineSpacingMultiplier = 1.0F;
      var1.alignX = HorizontalAlign.START;
      var1.alignY = VerticalAlign.BASELINE;
      var1.disabledAlpha = null;
      var1.themeProvider = var0;
      return new DefaultLabelStyle(var1);
   }
}
