package com.cpz.processing.controls.core.overlay.tooltip.config;

import com.cpz.processing.controls.core.theme.ThemeProvider;
import processing.core.PFont;

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

   public void setThemeProvider(ThemeProvider var1) {
      this.themeProvider = var1;
   }
}
