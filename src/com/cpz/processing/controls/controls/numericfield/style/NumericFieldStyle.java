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
 */
public final class NumericFieldStyle {
   private final NumericFieldStyleConfig config;
   private final DefaultNumericFieldRenderer renderer;
   private final ThemeProvider themeProvider;

   /**
    * Creates a numeric field style.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public NumericFieldStyle(NumericFieldStyleConfig var1) {
      this(var1, new DefaultNumericFieldRenderer());
   }

   /**
    * Creates a numeric field style.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public NumericFieldStyle(NumericFieldStyleConfig var1, DefaultNumericFieldRenderer var2) {
      this.config = var1 == null ? new NumericFieldStyleConfig() : var1;
      this.renderer = var2 == null ? new DefaultNumericFieldRenderer() : var2;
      this.themeProvider = this.config.themeProvider != null ? this.config.themeProvider : new ThemeManager();
   }

   /**
    * Renders the current frame.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    *
    * Behavior:
    * - Uses already available state and does not define business rules.
    */
   public void render(PApplet var1, NumericFieldViewState var2, ThemeSnapshot var3) {
      this.renderer.render(var1, var2, this.resolveRenderStyle(var2, var3));
   }

   /**
    * Resolves render style.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @return resolved render style
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public NumericFieldRenderStyle resolveRenderStyle(NumericFieldViewState var1, ThemeSnapshot var2) {
      ThemeTokens var3 = var2.tokens;
      int var4 = this.resolveColorOverride(var3.surface, this.config.backgroundOverride, this.config.backgroundColor);
      int var5 = this.resolveColorOverride(var3.border, this.config.borderOverride, this.config.borderColor);
      int var6 = this.resolveColorOverride(var3.onSurface, this.config.textOverride, this.config.textColor);
      int var7 = this.resolveColorOverride(var3.cursor, this.config.cursorOverride, this.config.cursorColor);
      int var8 = this.resolveColorOverride(var3.selection, this.config.selectionOverride, this.config.selectionColor);
      int var9 = this.resolveSelectionTextColor(var3.onSurface);
      return new NumericFieldRenderStyle(var4, var1.focused() ? this.blend(var5, var7, 0.35F) : var5, var6, var1.focused() ? var7 : var5, var8, var9, this.config.textSize, this.config.font);
   }

   private int resolveSelectionTextColor(int var1) {
      if (this.config.selectionTextOverride != null) {
         return this.config.selectionTextOverride;
      } else {
         return this.config.selectionTextColor != null ? this.config.selectionTextColor : var1;
      }
   }

   private int resolveColorOverride(int var1, Integer var2, Integer var3) {
      if (var2 != null) {
         return var2;
      } else {
         return var3 != null ? var3 : var1;
      }
   }

   private int blend(int var1, int var2, float var3) {
      float var4 = Math.max(0.0F, Math.min(1.0F, var3));
      int var5 = this.blendChannel(var1 >>> 24 & 255, var2 >>> 24 & 255, var4);
      int var6 = this.blendChannel(var1 >>> 16 & 255, var2 >>> 16 & 255, var4);
      int var7 = this.blendChannel(var1 >>> 8 & 255, var2 >>> 8 & 255, var4);
      int var8 = this.blendChannel(var1 & 255, var2 & 255, var4);
      return (var5 & 255) << 24 | (var6 & 255) << 16 | (var7 & 255) << 8 | var8 & 255;
   }

   private int blendChannel(int var1, int var2, float var3) {
      return Math.round((float)var1 + (float)(var2 - var1) * var3);
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
