package com.cpz.processing.controls.controls.dropdown.style;

import com.cpz.processing.controls.controls.dropdown.config.DropDownStyleConfig;
import com.cpz.processing.controls.controls.dropdown.state.DropDownViewState;
import com.cpz.processing.controls.controls.dropdown.style.render.DefaultDropDownRenderer;
import com.cpz.processing.controls.core.style.InteractiveStyleHelper;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import com.cpz.processing.controls.core.util.Colors;
import processing.core.PApplet;

public final class DefaultDropDownStyle {
   private final DropDownStyleConfig config;
   private final DefaultDropDownRenderer renderer;
   private final ThemeProvider themeProvider;

   public DefaultDropDownStyle(DropDownStyleConfig var1) {
      this(var1, new DefaultDropDownRenderer());
   }

   public DefaultDropDownStyle(DropDownStyleConfig var1, DefaultDropDownRenderer var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("config must not be null");
      } else {
         this.config = var1;
         this.renderer = var2 == null ? new DefaultDropDownRenderer() : var2;
         this.themeProvider = var1.themeProvider != null ? var1.themeProvider : new ThemeManager();
      }
   }

   public void render(PApplet var1, DropDownViewState var2, ThemeSnapshot var3) {
      this.renderer.render(var1, var2, this.resolveRenderStyle(var2, var3));
   }

   public float getCornerRadius() {
      return this.config.cornerRadius;
   }

   public float getListCornerRadius() {
      return this.config.listCornerRadius;
   }

   public float getStrokeWeight() {
      return this.config.strokeWeight;
   }

   public float getFocusedStrokeWeight() {
      return this.config.focusedStrokeWeight;
   }

   public float getItemHeight() {
      return this.config.itemHeight;
   }

   public float getTextPadding() {
      return this.config.textPadding;
   }

   public float getArrowPadding() {
      return this.config.arrowPadding;
   }

   public int getMaxVisibleItems() {
      return Math.max(1, this.config.maxVisibleItems);
   }

   public DropDownRenderStyle resolveRenderStyle(DropDownViewState var1, ThemeSnapshot var2) {
      ThemeTokens var3 = var2.tokens;
      int var4 = this.resolveColor(var3.surface, this.config.baseFillOverride);
      int var5 = this.resolveColor(var3.surfaceVariant, this.config.listFillOverride);
      int var6 = this.resolveColor(var3.onSurface, this.config.textOverride);
      int var7 = this.resolveColor(var3.border, this.config.borderOverride);
      int var8 = this.resolveColor(var3.primary, this.config.focusedBorderOverride);
      int var9 = this.resolveColor(var3.hoverOverlay, this.config.hoverItemOverlayOverride);
      int var10 = this.resolveColor(Colors.alpha(36, var3.primary), this.config.selectedItemOverlayOverride);
      int var11 = this.resolveBaseFill(var4, var9, var3.pressedOverlay, var1);
      int var12 = var1.focused() ? this.blend(var7, var8, 0.35F) : var7;
      float var13 = !var1.hovered() && !var1.focused() ? this.config.strokeWeight : this.config.focusedStrokeWeight;
      int var14 = InteractiveStyleHelper.applyOverlay(var5, var9);
      int var15 = InteractiveStyleHelper.applyOverlay(var5, var10);
      int var16 = this.config.disabledAlpha != null ? this.config.disabledAlpha : var3.disabledAlpha;
      return new DropDownRenderStyle(InteractiveStyleHelper.applyDisabledAlpha(var11, var1.enabled(), var16), InteractiveStyleHelper.applyDisabledAlpha(var5, var1.enabled(), var16), InteractiveStyleHelper.applyDisabledAlpha(var14, var1.enabled(), var16), InteractiveStyleHelper.applyDisabledAlpha(var15, var1.enabled(), var16), InteractiveStyleHelper.applyDisabledAlpha(var12, var1.enabled(), var16), var13, InteractiveStyleHelper.applyDisabledAlpha(var6, var1.enabled(), var16), InteractiveStyleHelper.applyDisabledAlpha(var6, var1.enabled(), var16), var1.cornerRadius(), var1.listCornerRadius(), this.config.textSize, var1.itemHeight(), var1.textPadding(), var1.arrowPadding(), Math.max(1, var1.maxVisibleItems()), this.config.font);
   }

   private int resolveBaseFill(int var1, int var2, int var3, DropDownViewState var4) {
      return InteractiveStyleHelper.resolveFillColorWithOverlays(var1, var2, var3, var4.hovered(), var4.pressed());
   }

   private int resolveColor(int var1, Integer var2) {
      return var2 != null ? var2 : var1;
   }

   private int blend(int var1, int var2, float var3) {
      float var4 = Math.max(0.0F, Math.min(1.0F, var3));
      int var5 = this.blendChannel(var1 >>> 24 & 255, var2 >>> 24 & 255, var4);
      int var6 = this.blendChannel(var1 >>> 16 & 255, var2 >>> 16 & 255, var4);
      int var7 = this.blendChannel(var1 >>> 8 & 255, var2 >>> 8 & 255, var4);
      int var8 = this.blendChannel(var1 & 255, var2 & 255, var4);
      return Colors.argb(var5, var6, var7, var8);
   }

   private int blendChannel(int var1, int var2, float var3) {
      return Math.round((float)var1 + (float)(var2 - var1) * var3);
   }

   public ThemeSnapshot getThemeSnapshot() {
      return this.themeProvider.getSnapshot();
   }
}
