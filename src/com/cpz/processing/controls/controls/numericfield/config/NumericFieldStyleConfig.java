package com.cpz.processing.controls.controls.numericfield.config;

import com.cpz.processing.controls.core.theme.ThemeProvider;
import processing.core.PFont;

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
   public PFont font;
   public ThemeProvider themeProvider;

   public void setThemeProvider(ThemeProvider var1) {
      this.themeProvider = var1;
   }
}
