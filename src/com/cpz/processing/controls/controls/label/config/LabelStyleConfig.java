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
 *
 * @author CPZ
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
    * @param horizontalAlign new align
    * @param verticalAlign parameter used by this operation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setAlign(HorizontalAlign horizontalAlign, VerticalAlign verticalAlign) {
      this.alignX = horizontalAlign == null ? HorizontalAlign.START : horizontalAlign;
      this.alignY = verticalAlign == null ? VerticalAlign.BASELINE : verticalAlign;
   }

   /**
    * Updates align.
    *
    * @param horizontalAlign Processing horizontal alignment
    * @param verticalAlign Processing vertical alignment
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setAlign(int horizontalAlign, int verticalAlign) {
      this.setAlign(LabelAlignMapper.fromProcessingHorizontal(horizontalAlign), LabelAlignMapper.fromProcessingVertical(verticalAlign));
   }

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
