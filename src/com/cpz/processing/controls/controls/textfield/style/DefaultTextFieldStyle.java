package com.cpz.processing.controls.controls.textfield.style;

import com.cpz.processing.controls.controls.textfield.config.TextFieldStyleConfig;
import com.cpz.processing.controls.controls.textfield.state.TextFieldViewState;
import com.cpz.processing.controls.controls.textfield.style.render.DefaultTextFieldRenderer;
import com.cpz.processing.controls.controls.textfield.style.render.TextFieldRenderer;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import processing.core.PApplet;

/**
 * Style component for default text field style.
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
public final class DefaultTextFieldStyle implements TextFieldStyle {
   private final TextFieldStyleConfig config;
   private final TextFieldRenderer renderer;
   private final ThemeProvider themeProvider;

   /**
    * Creates a default text field style.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultTextFieldStyle(TextFieldStyleConfig var1) {
      this(var1, new DefaultTextFieldRenderer());
   }

   /**
    * Creates a default text field style.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultTextFieldStyle(TextFieldStyleConfig var1, TextFieldRenderer var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("config must not be null");
      } else {
         this.config = var1;
         this.renderer = (TextFieldRenderer)(var2 == null ? new DefaultTextFieldRenderer() : var2);
         this.themeProvider = var1.themeProvider != null ? var1.themeProvider : new ThemeManager();
      }
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
   public void render(PApplet var1, TextFieldViewState var2, ThemeSnapshot var3) {
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
   public TextFieldRenderStyle resolveRenderStyle(TextFieldViewState var1, ThemeSnapshot var2) {
      ThemeTokens var3 = var2.tokens;
      int var4 = this.resolveColorOverride(var3.surface, this.config.backgroundOverride, this.config.backgroundColor);
      int var5 = this.resolveColorOverride(var3.border, this.config.borderOverride, this.config.borderColor);
      int var6 = this.resolveColorOverride(var3.onSurface, this.config.textOverride, this.config.textColor);
      int var7 = this.resolveColorOverride(var3.cursor, this.config.cursorOverride, this.config.cursorColor);
      int var8 = this.resolveColorOverride(var3.selection, this.config.selectionOverride, this.config.selectionColor);
      int var9 = this.resolveSelectionTextColor(var3.onSurface);
      return new TextFieldRenderStyle(var4, var1.focused() ? this.blend(var5, var7, 0.35F) : var5, var6, var1.focused() ? var7 : var5, var8, var9, this.config.textSize, this.config.font);
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
