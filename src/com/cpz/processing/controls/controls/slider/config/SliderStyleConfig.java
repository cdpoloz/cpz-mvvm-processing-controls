package com.cpz.processing.controls.controls.slider.config;

import com.cpz.processing.controls.controls.slider.style.SvgColorMode;
import processing.core.PShape;

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

   public SliderStyleConfig() {
      this.svgColorMode = SvgColorMode.USE_RENDER_STYLE;
   }
}
