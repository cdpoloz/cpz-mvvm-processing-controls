package com.cpz.processing.controls.controls.numericfield.style;

import com.cpz.processing.controls.controls.numericfield.config.NumericFieldStyleConfig;
import com.cpz.processing.controls.controls.numericfield.state.NumericFieldViewState;
import com.cpz.processing.controls.controls.numericfield.style.render.DefaultNumericFieldRenderer;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import processing.core.PApplet;

/**
 * Style component for numeric field style.
 *
 * Responsibilities:
 * - Resolve visual values from immutable state and theme data.
 * - Keep interaction rules outside the rendering layer.
 *
 * Behavior:
 * - Does not process raw input or mutate the backing model.
 *
 * Notes:
 * - This type belongs to the visual styling pipeline.
 *
 * @author CPZ
 */
public final class NumericFieldStyle {
   private final NumericFieldStyleConfig config;
   private final DefaultNumericFieldRenderer renderer;
   private final ThemeProvider themeProvider;

   /**
    * Creates a numeric field style.
    *
    * @param config parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public NumericFieldStyle(NumericFieldStyleConfig config) {
      this(config, new DefaultNumericFieldRenderer());
   }

   /**
    * Creates a numeric field style.
    *
    * @param config parameter used by this operation
    * @param renderer parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public NumericFieldStyle(NumericFieldStyleConfig config, DefaultNumericFieldRenderer renderer) {
      this.config = config == null ? new NumericFieldStyleConfig() : config;
      this.renderer = renderer == null ? new DefaultNumericFieldRenderer() : renderer;
      this.themeProvider = this.config.themeProvider != null ? this.config.themeProvider : new ThemeManager();
   }

   /**
    * Renders the current frame.
    *
    * @param sketch parameter used by this operation
    * @param state parameter used by this operation
    * @param snapshot parameter used by this operation
    *
    * Behavior:
    * - Uses already available state and does not define business rules.
    */
   public void render(PApplet sketch, NumericFieldViewState state, ThemeSnapshot snapshot) {
      this.renderer.render(sketch, state, this.resolveRenderStyle(state, snapshot));
   }

   /**
    * Resolves render style.
    *
    * @param state parameter used by this operation
    * @param snapshot parameter used by this operation
    * @return resolved render style
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public NumericFieldRenderStyle resolveRenderStyle(NumericFieldViewState state, ThemeSnapshot snapshot) {
      ThemeTokens tokens = snapshot.tokens;
      int color4 = this.resolveColorOverride(tokens.surface, this.config.backgroundOverride, this.config.backgroundColor);
      int borderColor2 = this.resolveColorOverride(tokens.border, this.config.borderOverride, this.config.borderColor);
      int color5 = this.resolveColorOverride(tokens.onSurface, this.config.textOverride, this.config.textColor);
      int color6 = this.resolveColorOverride(tokens.cursor, this.config.cursorOverride, this.config.cursorColor);
      int color7 = this.resolveColorOverride(tokens.selection, this.config.selectionOverride, this.config.selectionColor);
      int color8 = this.resolveSelectionTextColor(tokens.onSurface);
      return new NumericFieldRenderStyle(color4, state.focused() ? this.blend(borderColor2, color6, 0.35F) : borderColor2, color5, state.focused() ? color6 : borderColor2, color7, color8, this.config.textSize, this.config.font);
   }

   private int resolveSelectionTextColor(int color) {
      if (this.config.selectionTextOverride != null) {
         return this.config.selectionTextOverride;
      } else {
         return this.config.selectionTextColor != null ? this.config.selectionTextColor : color;
      }
   }

   private int resolveColorOverride(int color, Integer color2, Integer color3) {
      if (color2 != null) {
         return color2;
      } else {
         return color3 != null ? color3 : color;
      }
   }

   private int blend(int value, int value2, float x) {
      float value3 = Math.max(0.0F, Math.min(1.0F, x));
      int value4 = this.blendChannel(value >>> 24 & 255, value2 >>> 24 & 255, value3);
      int value5 = this.blendChannel(value >>> 16 & 255, value2 >>> 16 & 255, value3);
      int value6 = this.blendChannel(value >>> 8 & 255, value2 >>> 8 & 255, value3);
      int value7 = this.blendChannel(value & 255, value2 & 255, value3);
      return (value4 & 255) << 24 | (value5 & 255) << 16 | (value6 & 255) << 8 | value7 & 255;
   }

   private int blendChannel(int value, int value2, float x) {
      return Math.round((float)value + (float)(value2 - value) * x);
   }

   /**
    * Returns theme snapshot.
    *
    * @return current theme snapshot
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public ThemeSnapshot getThemeSnapshot() {
      return this.themeProvider.getSnapshot();
   }
}
