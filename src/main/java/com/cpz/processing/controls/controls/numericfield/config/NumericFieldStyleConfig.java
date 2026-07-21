package com.cpz.processing.controls.controls.numericfield.config;

import com.cpz.processing.controls.core.theme.ThemeProvider;
import com.cpz.processing.controls.core.util.FontLoader;
import processing.core.PFont;

/**
 * Configuration holder for numeric field style config.
 *
 * Responsibilities:
 * - Collect configuration values for a public component.
 * - Keep initialization details outside the runtime pipeline.
 *
 * Behavior:
 * - Keeps the public role isolated from unrelated concerns.
 *
 * Notes:
 * - This type is part of the public project surface.
 *
 * @author CPZ
 */
public final class NumericFieldStyleConfig {
   public Integer backgroundOverride;
   public Integer borderOverride;
   public Integer textOverride;
   public Integer cursorOverride;
   public Integer selectionOverride;
   public Integer selectionTextOverride;
   public Integer backgroundColor;
   public Integer borderColor;
   public Integer textColor;
   public Integer cursorColor;
   public Integer selectionColor;
   public Integer selectionTextColor;
   public float textSize = 16.0F;

   /**
    * Optional control font used consistently for measurement and rendering.
    * {@code null} preserves the active Processing font.
    */
   public PFont font;
   /**
    * Optional JSON font resolver. When present, the control refreshes
    * {@link #font} for the currently resolved text size.
    */
   public FontLoader.FontResolver fontResolver;
   public ThemeProvider themeProvider;

   /**
    * Updates theme provider.
    *
    * @param themeProvider new theme provider
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setThemeProvider(ThemeProvider themeProvider) {
      this.themeProvider = themeProvider;
   }
}
