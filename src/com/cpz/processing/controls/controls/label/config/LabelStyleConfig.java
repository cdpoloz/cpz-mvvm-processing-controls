package com.cpz.processing.controls.controls.label.config;

import com.cpz.processing.controls.controls.label.style.HorizontalAlign;
import com.cpz.processing.controls.controls.label.style.VerticalAlign;
import com.cpz.processing.controls.controls.label.style.render.LabelAlignMapper;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import processing.core.PFont;

public class LabelStyleConfig {
   public PFont font;
   public float textSize = 12.0F;
   public Integer textColor;
   public float lineSpacingMultiplier = 1.0F;
   public HorizontalAlign alignX;
   public VerticalAlign alignY;
   public Integer disabledAlpha;
   public ThemeProvider themeProvider;

   public LabelStyleConfig() {
      this.alignX = HorizontalAlign.START;
      this.alignY = VerticalAlign.BASELINE;
   }

   public void setAlign(HorizontalAlign var1, VerticalAlign var2) {
      this.alignX = var1 == null ? HorizontalAlign.START : var1;
      this.alignY = var2 == null ? VerticalAlign.BASELINE : var2;
   }

   public void setAlign(int var1, int var2) {
      this.setAlign(LabelAlignMapper.fromProcessingHorizontal(var1), LabelAlignMapper.fromProcessingVertical(var2));
   }

   public void setThemeProvider(ThemeProvider var1) {
      this.themeProvider = var1;
   }
}
