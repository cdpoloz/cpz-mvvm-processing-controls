package com.cpz.processing.controls.controls.radiogroup.config;

import com.cpz.processing.controls.core.theme.ThemeProvider;
import processing.core.PFont;

/**
 * Configuration holder for radio group style config.
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
public final class RadioGroupStyleConfig {
   public Integer textOverride;
   public Integer indicatorOverride;
   public Integer backgroundOverride;
   public Integer hoveredBackgroundOverride;
   public Integer pressedBackgroundOverride;
   public Integer selectedDotOverride;
   public float itemHeight = 30.0F;
   public float itemSpacing = 8.0F;
   public float minimumItemHeight = 18.0F;
   public float indicatorOffsetX = 14.0F;
   public float textOffsetX = 32.0F;
   public float indicatorOuterDiameter = 18.0F;
   public float indicatorInnerDiameter = 8.0F;
   public float strokeWeight = 1.8F;
   public float textSize = 16.0F;
   public float cornerRadius = 6.0F;
   public Integer disabledAlpha;
   public PFont font;
   public ThemeProvider themeProvider;

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
