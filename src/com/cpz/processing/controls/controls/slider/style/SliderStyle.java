package com.cpz.processing.controls.controls.slider.style;

import com.cpz.processing.controls.controls.slider.config.SliderStyleConfig;
import com.cpz.processing.controls.controls.slider.state.SliderViewState;
import com.cpz.processing.controls.controls.slider.style.render.SliderRenderer;
import com.cpz.processing.controls.controls.slider.view.SliderGeometry;
import com.cpz.processing.controls.core.style.InteractiveStyleHelper;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import processing.core.PApplet;

public final class SliderStyle {
   private final SliderStyleConfig config;
   private final SliderRenderer renderer;

   public SliderStyle(SliderStyleConfig var1) {
      this(var1, new SliderRenderer());
   }

   public SliderStyle(SliderStyleConfig var1, SliderRenderer var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("config must not be null");
      } else {
         this.config = var1;
         this.renderer = var2 == null ? new SliderRenderer() : var2;
      }
   }

   public void render(PApplet var1, SliderViewState var2, SliderGeometry var3) {
      ThemeTokens var4 = ThemeManager.getTheme().tokens();
      boolean var5 = var2.pressed() || var2.dragging();
      int var6 = this.resolveColorOverride(var4.surfaceVariant, this.config.trackOverride, this.config.trackColor);
      int var7 = InteractiveStyleHelper.resolveFillColor(var6, this.resolveInteractiveColor(var6, var4.hoverOverlay, this.config.trackHoverOverride, this.config.trackHoverColor), this.resolveInteractiveColor(var6, var4.pressedOverlay, this.config.trackPressedOverride, this.config.trackPressedColor), var2.hovered(), var5);
      int var8 = this.resolveColorOverride(var4.primary, this.config.progressOverride, this.config.activeTrackColor);
      int var9 = InteractiveStyleHelper.resolveFillColor(var8, this.resolveInteractiveColor(var8, var4.hoverOverlay, this.config.progressHoverOverride, this.config.activeTrackHoverColor), this.resolveInteractiveColor(var8, var4.pressedOverlay, this.config.progressPressedOverride, this.config.activeTrackPressedColor), var2.hovered(), var5);
      int var10 = this.resolveColorOverride(var4.primary, this.config.thumbOverride, this.config.thumbColor);
      int var11 = InteractiveStyleHelper.resolveFillColor(var10, this.resolveInteractiveColor(var10, var4.hoverOverlay, this.config.thumbHoverOverride, this.config.thumbHoverColor), this.resolveInteractiveColor(var10, var4.pressedOverlay, this.config.thumbPressedOverride, this.config.thumbPressedColor), var2.hovered(), var5);
      int var12 = this.config.disabledAlpha != null ? this.config.disabledAlpha : var4.disabledAlpha;
      int var13 = this.resolveColorOverride(var4.border, this.config.trackStrokeOverride, this.config.trackStrokeColor);
      int var14 = this.resolveColorOverride(var4.border, this.config.thumbStrokeOverride, this.config.thumbStrokeColor);
      int var15 = this.resolveColorOverride(var4.onSurface, this.config.textOverride, this.config.textColor);
      SliderRenderStyle var16 = new SliderRenderStyle(InteractiveStyleHelper.applyDisabledAlpha(var7, var2.enabled(), var12), InteractiveStyleHelper.resolveStrokeColor(var13, var2.enabled(), var12), InteractiveStyleHelper.resolveStrokeWeight(this.config.trackStrokeWeight, this.config.trackStrokeWeightHover, var2.hovered()), this.config.trackThickness, InteractiveStyleHelper.applyDisabledAlpha(var9, var2.enabled(), var12), InteractiveStyleHelper.applyDisabledAlpha(var11, var2.enabled(), var12), InteractiveStyleHelper.resolveStrokeColor(var14, var2.enabled(), var12), InteractiveStyleHelper.resolveStrokeWeight(this.config.thumbStrokeWeight, this.config.thumbStrokeWeightHover, var2.hovered()), this.config.thumbSize, InteractiveStyleHelper.resolveStrokeColor(var15, var2.enabled(), var12), this.config.svgColorMode, this.config.thumbShape, this.config.showValueText);
      this.renderer.render(var1, var3, var2, var16);
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
}
