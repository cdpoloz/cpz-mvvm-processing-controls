package com.cpz.processing.controls.controls.slider.config;

import com.cpz.processing.controls.controls.slider.style.SvgColorMode;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import processing.core.PShape;

/**
 * Configuration holder for slider style config.
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
public final class SliderStyleConfig {
   public Integer trackOverride;
   public Integer trackHoverOverride;
   public Integer trackPressedOverride;
   public Integer progressOverride;
   public Integer progressHoverOverride;
   public Integer progressPressedOverride;
   public Integer thumbOverride;
   public Integer thumbHoverOverride;
   public Integer thumbPressedOverride;
   public Integer trackStrokeOverride;
   public Integer thumbStrokeOverride;
   public Integer textOverride;
   public Integer trackColor;
   public Integer trackHoverColor;
   public Integer trackPressedColor;
   public Integer trackStrokeColor;
   public float trackStrokeWeight;
   public float trackStrokeWeightHover;
   public float trackThickness;
   public Integer activeTrackColor;
   public Integer activeTrackHoverColor;
   public Integer activeTrackPressedColor;
   public Integer thumbColor;
   public Integer thumbHoverColor;
   public Integer thumbPressedColor;
   public Integer thumbStrokeColor;
   public float thumbStrokeWeight;
   public float thumbStrokeWeightHover;
   public float thumbSize;
   public Integer textColor;
   public Integer disabledAlpha;
   public boolean showValueText = true;
   public SvgColorMode svgColorMode;
   public PShape thumbShape;
   public ThemeProvider themeProvider;

   /**
    * Creates a slider style config.
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public SliderStyleConfig() {
      this.trackStrokeWeight = 1.5F;
      this.trackStrokeWeightHover = 2.5F;
      this.trackThickness = 8.0F;
      this.thumbStrokeWeight = 2.0F;
      this.thumbStrokeWeightHover = 3.0F;
      this.thumbSize = 24.0F;
      this.showValueText = true;
      this.svgColorMode = SvgColorMode.USE_RENDER_STYLE;
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
