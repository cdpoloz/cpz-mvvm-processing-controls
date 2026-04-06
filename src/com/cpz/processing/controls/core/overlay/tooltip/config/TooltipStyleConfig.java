package com.cpz.processing.controls.core.overlay.tooltip.config;

import com.cpz.processing.controls.core.theme.ThemeProvider;
import processing.core.PFont;

/**
 * Configuration holder for tooltip style config.
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
public final class TooltipStyleConfig {
   public Integer backgroundOverride;
   public Integer textOverride;
   public Integer borderOverride;
   public float textSize = 14.0F;
   public float textPadding = 10.0F;
   public float cornerRadius = 8.0F;
   public float minHeight = 32.0F;
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
