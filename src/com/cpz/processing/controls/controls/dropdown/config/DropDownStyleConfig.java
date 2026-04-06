package com.cpz.processing.controls.controls.dropdown.config;

import com.cpz.processing.controls.core.theme.ThemeProvider;
import processing.core.PFont;

/**
 * Configuration holder for drop down style config.
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
public final class DropDownStyleConfig {
   public Integer baseFillOverride;
   public Integer listFillOverride;
   public Integer textOverride;
   public Integer borderOverride;
   public Integer hoverItemOverlayOverride;
   public Integer selectedItemOverlayOverride;
   public Integer focusedBorderOverride;
   public float cornerRadius = 8.0F;
   public float listCornerRadius = 8.0F;
   public float strokeWeight = 1.5F;
   public float focusedStrokeWeight = 2.0F;
   public float textSize = 16.0F;
   public float itemHeight = 34.0F;
   public float textPadding = 12.0F;
   public float arrowPadding = 14.0F;
   public int maxVisibleItems = 6;
   public Integer disabledAlpha;
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
