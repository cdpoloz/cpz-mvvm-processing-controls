package com.cpz.processing.controls.controls.label.config;

import com.cpz.processing.controls.controls.label.style.HorizontalAlign;
import com.cpz.processing.controls.controls.label.style.VerticalAlign;
import com.cpz.processing.controls.controls.label.style.render.LabelAlignMapper;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import processing.core.PFont;

/**
 * Configuration holder for label style config.
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
public class LabelStyleConfig {
   public PFont font;
   public float textSize = 12.0F;
   public Integer textColor;
   public float lineSpacingMultiplier = 1.0F;
   public HorizontalAlign alignX;
   public VerticalAlign alignY;
   public Integer disabledAlpha;
   public ThemeProvider themeProvider;

   /**
    * Creates a label style config.
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public LabelStyleConfig() {
      this.alignX = HorizontalAlign.START;
      this.alignY = VerticalAlign.BASELINE;
   }

   /**
    * Updates align.
    *
    * @param var1 new align
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setAlign(HorizontalAlign var1, VerticalAlign var2) {
      this.alignX = var1 == null ? HorizontalAlign.START : var1;
      this.alignY = var2 == null ? VerticalAlign.BASELINE : var2;
   }

   /**
    * Updates align.
    *
    * @param var1 new align
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setAlign(int var1, int var2) {
      this.setAlign(LabelAlignMapper.fromProcessingHorizontal(var1), LabelAlignMapper.fromProcessingVertical(var2));
   }

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
