package com.cpz.processing.controls.controls.slider.style;

import com.cpz.processing.controls.controls.slider.config.SliderStyleConfig;
import com.cpz.processing.controls.controls.slider.state.SliderViewState;
import com.cpz.processing.controls.controls.slider.style.render.SliderRenderer;
import com.cpz.processing.controls.controls.slider.view.SliderGeometry;
import com.cpz.processing.controls.core.style.InteractiveStyleHelper;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import processing.core.PApplet;

public final class SliderStyle {
   private final SliderStyleConfig config;
   private final SliderRenderer renderer;
   private final ThemeProvider themeProvider;

   public SliderStyle(SliderStyleConfig var1) {
      this(var1, new SliderRenderer());
   }

   public SliderStyle(SliderStyleConfig var1, SliderRenderer var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("config must not be null");
      } else {
         this.config = var1;
         this.renderer = var2 == null ? new SliderRenderer() : var2;
         this.themeProvider = var1.themeProvider != null ? var1.themeProvider : new ThemeManager();
      }
   }

   public void render(PApplet var1, SliderViewState var2, ThemeSnapshot var3, SliderGeometry var4) {
      ThemeTokens var5 = var3.tokens;
      boolean var6 = var2.pressed() || var2.dragging();
      int var7 = this.resolveColorOverride(var5.surfaceVariant, this.config.trackOverride, this.config.trackColor);
      int var8 = InteractiveStyleHelper.resolveFillColor(var7, this.resolveInteractiveColor(var7, var5.hoverOverlay, this.config.trackHoverOverride, this.config.trackHoverColor), this.resolveInteractiveColor(var7, var5.pressedOverlay, this.config.trackPressedOverride, this.config.trackPressedColor), var2.hovered(), var6);
      int var9 = this.resolveColorOverride(var5.primary, this.config.progressOverride, this.config.activeTrackColor);
      int var10 = InteractiveStyleHelper.resolveFillColor(var9, this.resolveInteractiveColor(var9, var5.hoverOverlay, this.config.progressHoverOverride, this.config.activeTrackHoverColor), this.resolveInteractiveColor(var9, var5.pressedOverlay, this.config.progressPressedOverride, this.config.activeTrackPressedColor), var2.hovered(), var6);
      int var11 = this.resolveColorOverride(var5.primary, this.config.thumbOverride, this.config.thumbColor);
      int var12 = InteractiveStyleHelper.resolveFillColor(var11, this.resolveInteractiveColor(var11, var5.hoverOverlay, this.config.thumbHoverOverride, this.config.thumbHoverColor), this.resolveInteractiveColor(var11, var5.pressedOverlay, this.config.thumbPressedOverride, this.config.thumbPressedColor), var2.hovered(), var6);
      int var13 = this.config.disabledAlpha != null ? this.config.disabledAlpha : var5.disabledAlpha;
      int var14 = this.resolveColorOverride(var5.border, this.config.trackStrokeOverride, this.config.trackStrokeColor);
      int var15 = this.resolveColorOverride(var5.border, this.config.thumbStrokeOverride, this.config.thumbStrokeColor);
      int var16 = this.resolveColorOverride(var5.onSurface, this.config.textOverride, this.config.textColor);
      SliderRenderStyle var17 = new SliderRenderStyle(InteractiveStyleHelper.applyDisabledAlpha(var8, var2.enabled(), var13), InteractiveStyleHelper.resolveStrokeColor(var14, var2.enabled(), var13), InteractiveStyleHelper.resolveStrokeWeight(this.config.trackStrokeWeight, this.config.trackStrokeWeightHover, var2.hovered()), this.config.trackThickness, InteractiveStyleHelper.applyDisabledAlpha(var10, var2.enabled(), var13), InteractiveStyleHelper.applyDisabledAlpha(var12, var2.enabled(), var13), InteractiveStyleHelper.resolveStrokeColor(var15, var2.enabled(), var13), InteractiveStyleHelper.resolveStrokeWeight(this.config.thumbStrokeWeight, this.config.thumbStrokeWeightHover, var2.hovered()), this.config.thumbSize, InteractiveStyleHelper.resolveStrokeColor(var16, var2.enabled(), var13), this.config.svgColorMode, this.config.thumbShape, this.config.showValueText);
      this.renderer.render(var1, var4, var2, var17);
   }

   private int resolveInteractiveColor(int var1, int var2, Integer var3, Integer var4) {
      if (var3 != null) {
         return var3;
      } else {
         return var4 != null ? var4 : InteractiveStyleHelper.applyOverlay(var1, var2);
      }
   }

   private int resolveColorOverride(int var1, Integer var2, Integer var3) {
      if (var2 != null) {
         return var2;
      } else {
         return var3 != null ? var3 : var1;
      }
   }

   public ThemeSnapshot getThemeSnapshot() {
      return this.themeProvider.getSnapshot();
   }
}
