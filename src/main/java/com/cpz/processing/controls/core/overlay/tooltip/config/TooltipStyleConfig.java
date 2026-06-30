package com.cpz.processing.controls.core.overlay.tooltip.config;

import com.cpz.processing.controls.core.theme.ThemeProvider;
import processing.core.PFont;

/**
 * Configuration holder for tooltip style config.
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
public final class TooltipStyleConfig {
   public Integer backgroundOverride;
   public Integer textOverride;
   public Integer borderOverride;
   public float textSize = 14.0F;
   public float textPadding = 10.0F;
   public float offset = 10.0F;
   public float cornerRadius = 8.0F;
   public float minHeight = 32.0F;
   public float strokeWeight = 1.0F;
   public PFont font;
   public ThemeProvider themeProvider;

   public TooltipStyleConfig() {
   }

   public TooltipStyleConfig(TooltipStyleConfig source) {
      this.copyFrom(source);
   }

   public TooltipStyleConfig copy() {
      return new TooltipStyleConfig(this);
   }

   public TooltipStyleConfig copyFrom(TooltipStyleConfig source) {
      if (source == null) {
         return this.reset();
      }

      this.backgroundOverride = source.backgroundOverride;
      this.textOverride = source.textOverride;
      this.borderOverride = source.borderOverride;
      this.textSize = source.textSize;
      this.textPadding = source.textPadding;
      this.offset = source.offset;
      this.cornerRadius = source.cornerRadius;
      this.minHeight = source.minHeight;
      this.strokeWeight = source.strokeWeight;
      this.font = source.font;
      this.themeProvider = source.themeProvider;
      return this;
   }

   public TooltipStyleConfig reset() {
      this.backgroundOverride = null;
      this.textOverride = null;
      this.borderOverride = null;
      this.textSize = 14.0F;
      this.textPadding = 10.0F;
      this.offset = 10.0F;
      this.cornerRadius = 8.0F;
      this.minHeight = 32.0F;
      this.strokeWeight = 1.0F;
      this.font = null;
      this.themeProvider = null;
      return this;
   }

   public TooltipStyleConfig setBackgroundColor(int argb) {
      this.backgroundOverride = argb;
      return this;
   }

   public TooltipStyleConfig setTextColor(int argb) {
      this.textOverride = argb;
      return this;
   }

   public TooltipStyleConfig setBorderColor(int argb) {
      this.borderOverride = argb;
      return this;
   }

   public TooltipStyleConfig setFont(PFont font) {
      this.font = font;
      return this;
   }

   public TooltipStyleConfig setTextSize(float textSize) {
      this.textSize = textSize;
      return this;
   }

   public TooltipStyleConfig setTextPadding(float textPadding) {
      this.textPadding = Math.max(0.0F, textPadding);
      return this;
   }

   public TooltipStyleConfig setPadding(float padding) {
      return this.setTextPadding(padding);
   }

   public TooltipStyleConfig setOffset(float offset) {
      this.offset = Math.max(0.0F, offset);
      return this;
   }

   public TooltipStyleConfig setCornerRadius(float cornerRadius) {
      this.cornerRadius = Math.max(0.0F, cornerRadius);
      return this;
   }

   public TooltipStyleConfig setStrokeWeight(float strokeWeight) {
      this.strokeWeight = Math.max(0.0F, strokeWeight);
      return this;
   }

   public TooltipStyleConfig setMinHeight(float minHeight) {
      this.minHeight = Math.max(0.0F, minHeight);
      return this;
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
