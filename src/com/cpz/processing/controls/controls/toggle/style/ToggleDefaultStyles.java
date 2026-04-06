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
    * @param var0 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static ToggleStyle circular(ThemeProvider var0) {
      ToggleStyleConfig var1 = new ToggleStyleConfig();
      var1.shape = new CircleShapeRenderer();
      var1.strokeWidth = 2.0F;
      var1.strokeWidthHover = 3.0F;
      var1.themeProvider = var0;
      return new ParametricToggleStyle(var1);
   }
}
