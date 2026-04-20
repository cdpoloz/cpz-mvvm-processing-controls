package com.cpz.processing.controls.controls.toggle.style;

import com.cpz.processing.controls.controls.toggle.config.ToggleStyleConfig;
import com.cpz.processing.controls.controls.toggle.style.render.CircleShapeRenderer;
import com.cpz.processing.controls.core.theme.ThemeProvider;

/**
 * Style component for toggle default styles.
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
public final class ToggleDefaultStyles {
   private ToggleDefaultStyles() {
   }

   /**
    * Performs circular.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static ToggleStyle circular() {
      return circular((ThemeProvider)null);
   }

   /**
    * Performs circular.
    *
    * @param themeProvider parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static ToggleStyle circular(ThemeProvider themeProvider) {
      ToggleStyleConfig config = new ToggleStyleConfig();
      config.shape = new CircleShapeRenderer();
      config.strokeWidth = 2.0F;
      config.strokeWidthHover = 3.0F;
      config.themeProvider = themeProvider;
      return new ParametricToggleStyle(config);
   }
}
