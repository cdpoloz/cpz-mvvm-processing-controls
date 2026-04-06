package com.cpz.processing.controls.controls.textfield.style;

import com.cpz.processing.controls.controls.textfield.config.TextFieldStyleConfig;
import com.cpz.processing.controls.controls.textfield.state.TextFieldViewState;
import com.cpz.processing.controls.controls.textfield.style.render.DefaultTextFieldRenderer;
import com.cpz.processing.controls.controls.textfield.style.render.TextFieldRenderer;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import processing.core.PApplet;

public final class DefaultTextFieldStyle implements TextFieldStyle {
   private final TextFieldStyleConfig config;
   private final TextFieldRenderer renderer;

   public DefaultTextFieldStyle(TextFieldStyleConfig var1) {
      this(var1, new DefaultTextFieldRenderer());
   }

   public DefaultTextFieldStyle(TextFieldStyleConfig var1, TextFieldRenderer var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("config must not be null");
      } else {
         this.config = var1;
         this.renderer = (TextFieldRenderer)(var2 == null ? new DefaultTextFieldRenderer() : var2);
      }
   }

   public void render(PApplet var1, TextFieldViewState var2) {
      this.renderer.render(var1, var2, this.resolveRenderStyle(var2));
   }

   public TextFieldRenderStyle resolveRenderStyle(TextFieldViewState var1) {
      ThemeTokens var2 = this.resolveThemeProvider().getTheme().tokens();
      int var3 = this.resolveColorOverride(var2.surface, this.config.backgroundOverride, this.config.backgroundColor);
      int var4 = this.resolveColorOverride(var2.border, this.config.borderOverride, this.config.borderColor);
      int var5 = this.resolveColorOverride(var2.onSurface, this.config.textOverride, this.config.textColor);
      int var6 = this.resolveColorOverride(var2.cursor, this.config.cursorOverride, this.config.cursorColor);
      int var7 = this.resolveColorOverride(var2.selection, this.config.selectionOverride, this.config.selectionColor);
      int var8 = this.resolveSelectionTextColor(var2.onSurface);
      return new TextFieldRenderStyle(var3, var1.focused() ? this.blend(var4, var6, 0.35F) : var4, var5, var1.focused() ? var6 : var4, var7, var8, this.config.textSize, this.config.font);
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

   private ThemeProvider resolveThemeProvider() {
      return this.config.themeProvider != null ? this.config.themeProvider : ThemeManager::getDefaultTheme;
   }
}
