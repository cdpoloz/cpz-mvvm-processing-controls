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
 *
 * @author CPZ
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
    * @param themeProvider parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static LabelStyle defaultText(ThemeProvider themeProvider) {
      LabelStyleConfig config = new LabelStyleConfig();
      config.font = null;
      config.textSize = 12.0F;
      config.textColor = null;
      config.lineSpacingMultiplier = 1.0F;
      config.alignX = HorizontalAlign.START;
      config.alignY = VerticalAlign.BASELINE;
      config.disabledAlpha = null;
      config.themeProvider = themeProvider;
      return new DefaultLabelStyle(config);
   }
}
