package com.cpz.processing.controls.controls.toggle.config;

import com.cpz.processing.controls.controls.toggle.style.render.ToggleShapeRenderer;
import com.cpz.processing.controls.core.theme.ThemeProvider;

public final class ToggleStyleConfig {
   public ToggleShapeRenderer shape;
   public Integer offFillOverride;
   public Integer onFillOverride;
   public Integer hoverFillOverride;
   public Integer pressedFillOverride;
   public Integer strokeOverride;
   public Integer[] stateColors;
   public Integer strokeColor;
   public float strokeWidth;
   public float strokeWidthHover;
   public Float hoverBlendWithWhite;
   public Float pressedBlendWithBlack;
   public Integer disabledAlpha;
   public ThemeProvider themeProvider;

   public void setShapeRenderer(ToggleShapeRenderer var1) {
      this.shape = var1;
   }

   public void setThemeProvider(ThemeProvider var1) {
      this.themeProvider = var1;
   }
}
