package com.cpz.processing.controls.controls.button.config;

import com.cpz.processing.controls.controls.button.style.render.ButtonRenderer;
import com.cpz.processing.controls.core.theme.ThemeProvider;

public final class ButtonStyleConfig {
   public Integer fillOverride;
   public Integer textOverride;
   public Integer strokeOverride;
   public Integer baseColor;
   public Integer textColor;
   public Integer strokeColor;
   public float strokeWeight;
   public float strokeWeightHover;
   public float cornerRadius;
   public Integer disabledAlpha;
   public Float hoverBlendWithWhite;
   public Float pressedBlendWithBlack;
   public ButtonRenderer renderer;
   public ThemeProvider themeProvider;

   public void setRenderer(ButtonRenderer var1) {
      this.renderer = var1;
   }

   public void setThemeProvider(ThemeProvider var1) {
      this.themeProvider = var1;
   }
}
