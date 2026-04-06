package com.cpz.processing.controls.controls.textfield.config;

import com.cpz.processing.controls.core.theme.ThemeProvider;
import processing.core.PFont;

/**
 * Configuration holder for text field style config.
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
 */
public final class TextFieldStyleConfig {
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
   public PFont font;
   public ThemeProvider themeProvider;

   /**
    * Updates theme provider.
    *
    * @param var1 new theme provider
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setThemeProvider(ThemeProvider var1) {
      this.themeProvider = var1;
   }
}
