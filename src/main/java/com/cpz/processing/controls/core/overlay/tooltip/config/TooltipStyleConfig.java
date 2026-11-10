package com.cpz.processing.controls.core.overlay.tooltip.config;

import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.geometry.MeasureMode;
import com.cpz.processing.controls.core.util.FontLoader;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import processing.core.PFont;
import processing.core.PApplet;
import java.util.Objects;

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
   public float minWidth = 40.0F;
   public FontLoader.FontResolver fontResolver;
   private ControlMeasure textSizeMeasure;
   private ControlMeasure textPaddingMeasure;
   private ControlMeasure offsetMeasure;
   private ControlMeasure cornerRadiusMeasure;
   private ControlMeasure minHeightMeasure;
   private ControlMeasure strokeWeightMeasure;
   private ControlMeasure minWidthMeasure;
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
      this.minWidth = source.minWidth;
      this.fontResolver = source.fontResolver;
      this.textSizeMeasure = source.textSizeMeasure;
      this.textPaddingMeasure = source.textPaddingMeasure;
      this.offsetMeasure = source.offsetMeasure;
      this.cornerRadiusMeasure = source.cornerRadiusMeasure;
      this.minHeightMeasure = source.minHeightMeasure;
      this.strokeWeightMeasure = source.strokeWeightMeasure;
      this.minWidthMeasure = source.minWidthMeasure;
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
      this.minWidth = 40.0F;
      this.fontResolver = null;
      this.textSizeMeasure = null;
      this.textPaddingMeasure = null;
      this.offsetMeasure = null;
      this.cornerRadiusMeasure = null;
      this.minHeightMeasure = null;
      this.strokeWeightMeasure = null;
      this.minWidthMeasure = null;
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
      this.fontResolver = null;
      return this;
   }

   public TooltipStyleConfig setTextSize(float textSize) {
      this.textSizeMeasure = null;
      this.textSize = textSize;
      return this;
   }

   public TooltipStyleConfig setTextPadding(float textPadding) {
      this.textPaddingMeasure = null;
      this.textPadding = Math.max(0.0F, textPadding);
      return this;
   }

   public TooltipStyleConfig setPadding(float padding) {
      return this.setTextPadding(padding);
   }

   public TooltipStyleConfig setOffset(float offset) {
      this.offsetMeasure = null;
      this.offset = Math.max(0.0F, offset);
      return this;
   }

   public TooltipStyleConfig setCornerRadius(float cornerRadius) {
      this.cornerRadiusMeasure = null;
      this.cornerRadius = Math.max(0.0F, cornerRadius);
      return this;
   }

   public TooltipStyleConfig setStrokeWeight(float strokeWeight) {
      this.strokeWeightMeasure = null;
      this.strokeWeight = Math.max(0.0F, strokeWeight);
      return this;
   }

   public TooltipStyleConfig setMinWidth(float minWidth) {
      this.minWidthMeasure = null;
      this.minWidth = Math.max(0.0F, minWidth);
      return this;
   }

   public TooltipStyleConfig setMinHeight(float minHeight) {
      this.minHeightMeasure = null;
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
   /**
    * Sets an explicit measure; relative values use sketch height.
    * @param measure absolute pixels or relative factor
    * @return this config
    */
   public TooltipStyleConfig setTextSize(ControlMeasure measure) {
      Objects.requireNonNull(measure, "measure");
      if (measure.mode() == MeasureMode.ABSOLUTE) {
          return this.setTextSize(measure.value());
      }
      this.textSizeMeasure = measure;
      return this;
   }

   public ControlMeasure getTextSizeMeasure() {
      return this.textSizeMeasure == null ? ControlMeasure.absolute(this.textSize) : this.textSizeMeasure;
   }

   /**
    * Sets an explicit measure; relative values use sketch height.
    * @param measure absolute pixels or relative factor
    * @return this config
    */
   public TooltipStyleConfig setTextPadding(ControlMeasure measure) {
      Objects.requireNonNull(measure, "measure");
      if (measure.mode() == MeasureMode.ABSOLUTE) {
          return this.setTextPadding(measure.value());
      }
      this.textPaddingMeasure = measure;
      return this;
   }

   public ControlMeasure getTextPaddingMeasure() {
      return this.textPaddingMeasure == null ? ControlMeasure.absolute(this.textPadding) : this.textPaddingMeasure;
   }

   /**
    * Sets an explicit measure; relative values use sketch height.
    * @param measure absolute pixels or relative factor
    * @return this config
    */
   public TooltipStyleConfig setOffset(ControlMeasure measure) {
      Objects.requireNonNull(measure, "measure");
      if (measure.mode() == MeasureMode.ABSOLUTE) {
          return this.setOffset(measure.value());
      }
      this.offsetMeasure = measure;
      return this;
   }

   public ControlMeasure getOffsetMeasure() {
      return this.offsetMeasure == null ? ControlMeasure.absolute(this.offset) : this.offsetMeasure;
   }

   /**
    * Sets an explicit measure; relative values use sketch height.
    * @param measure absolute pixels or relative factor
    * @return this config
    */
   public TooltipStyleConfig setCornerRadius(ControlMeasure measure) {
      Objects.requireNonNull(measure, "measure");
      if (measure.mode() == MeasureMode.ABSOLUTE) {
          return this.setCornerRadius(measure.value());
      }
      this.cornerRadiusMeasure = measure;
      return this;
   }

   public ControlMeasure getCornerRadiusMeasure() {
      return this.cornerRadiusMeasure == null ? ControlMeasure.absolute(this.cornerRadius) : this.cornerRadiusMeasure;
   }

   /**
    * Sets an explicit measure; relative values use sketch height.
    * @param measure absolute pixels or relative factor
    * @return this config
    */
   public TooltipStyleConfig setMinHeight(ControlMeasure measure) {
      Objects.requireNonNull(measure, "measure");
      if (measure.mode() == MeasureMode.ABSOLUTE) {
          return this.setMinHeight(measure.value());
      }
      this.minHeightMeasure = measure;
      return this;
   }

   public ControlMeasure getMinHeightMeasure() {
      return this.minHeightMeasure == null ? ControlMeasure.absolute(this.minHeight) : this.minHeightMeasure;
   }

   /**
    * Sets an explicit measure; relative values use sketch height.
    * @param measure absolute pixels or relative factor
    * @return this config
    */
   public TooltipStyleConfig setStrokeWeight(ControlMeasure measure) {
      Objects.requireNonNull(measure, "measure");
      if (measure.mode() == MeasureMode.ABSOLUTE) {
          return this.setStrokeWeight(measure.value());
      }
      this.strokeWeightMeasure = measure;
      return this;
   }

   public ControlMeasure getStrokeWeightMeasure() {
      return this.strokeWeightMeasure == null ? ControlMeasure.absolute(this.strokeWeight) : this.strokeWeightMeasure;
   }

   /**
    * Sets an explicit measure; relative values use sketch height.
    * @param measure absolute pixels or relative factor
    * @return this config
    */
   public TooltipStyleConfig setMinWidth(ControlMeasure measure) {
      Objects.requireNonNull(measure, "measure");
      if (measure.mode() == MeasureMode.ABSOLUTE) {
          return this.setMinWidth(measure.value());
      }
      this.minWidthMeasure = measure;
      return this;
   }

   public ControlMeasure getMinWidthMeasure() {
      return this.minWidthMeasure == null ? ControlMeasure.absolute(this.minWidth) : this.minWidthMeasure;
   }

   public TooltipStyleConfig setPadding(ControlMeasure padding) {
      return this.setTextPadding(padding);
   }

   /**
    * Resolves an independent pixel config without overwriting retained measures.
    * @param sketch overlay surface
    * @return resolved pixel config
    */
   public TooltipStyleConfig resolve(PApplet sketch) {
      TooltipStyleConfig result = new TooltipStyleConfig(this);
      result.setTextSize(Math.max(1.0F, this.getTextSizeMeasure().resolve(sketch.height)));
      result.setTextPadding(Math.max(0.0F, this.getTextPaddingMeasure().resolve(sketch.height)));
      result.setOffset(Math.max(0.0F, this.getOffsetMeasure().resolve(sketch.height)));
      result.setCornerRadius(Math.max(0.0F, this.getCornerRadiusMeasure().resolve(sketch.height)));
      result.setMinHeight(Math.max(0.0F, this.getMinHeightMeasure().resolve(sketch.height)));
      result.setStrokeWeight(Math.max(0.0F, this.getStrokeWeightMeasure().resolve(sketch.height)));
      result.setMinWidth(Math.max(0.0F, this.getMinWidthMeasure().resolve(sketch.height)));
      result.font = FontLoader.resolve(this.fontResolver, sketch, result.textSize, this.font);
      result.fontResolver = null;
      return result;
   }
}
