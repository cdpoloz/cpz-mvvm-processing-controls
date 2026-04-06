package com.cpz.processing.controls.controls.button.style;

import com.cpz.processing.controls.controls.button.config.ButtonStyleConfig;
import com.cpz.processing.controls.controls.button.state.ButtonViewState;
import com.cpz.processing.controls.controls.button.style.render.ButtonRenderer;
import com.cpz.processing.controls.controls.button.style.render.DefaultButtonRenderer;
import com.cpz.processing.controls.core.style.InteractiveStyleHelper;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import processing.core.PApplet;

public final class DefaultButtonStyle implements ButtonStyle {
   private final ButtonStyleConfig config;
   private final ButtonRenderer renderer;

   public DefaultButtonStyle(ButtonStyleConfig var1) {
      this(var1, (ButtonRenderer)(var1 != null && var1.renderer != null ? var1.renderer : new DefaultButtonRenderer()));
   }

   public DefaultButtonStyle(ButtonStyleConfig var1, ButtonRenderer var2) {
      this.config = var1;
      this.renderer = var2;
   }

   public void render(PApplet var1, ButtonViewState var2) {
      ThemeTokens var3 = this.resolveThemeProvider().getTheme().tokens();
      int var4 = this.resolveColorOverride(var3.primary, this.config.fillOverride, this.config.baseColor);
      int var5 = this.resolveInteractiveOverride(var4, var3.hoverOverlay, this.config.hoverBlendWithWhite != null ? var1.lerpColor(var4, var1.color(255), this.config.hoverBlendWithWhite) : null);
      int var6 = this.resolveInteractiveOverride(var4, var3.pressedOverlay, this.config.pressedBlendWithBlack != null ? var1.lerpColor(var4, var1.color(0), this.config.pressedBlendWithBlack) : null);
      int var7 = InteractiveStyleHelper.resolveFillColor(var4, var5, var6, var2.hovered(), var2.pressed());
      int var8 = this.config.disabledAlpha != null ? this.config.disabledAlpha : var3.disabledAlpha;
      int var9 = this.resolveColorOverride(var3.border, this.config.strokeOverride, this.config.strokeColor);
      int var10 = this.resolveColorOverride(var3.onPrimary, this.config.textOverride, this.config.textColor);
      ButtonRenderStyle var11 = new ButtonRenderStyle(InteractiveStyleHelper.applyDisabledAlpha(var7, var2.enabled(), var8), InteractiveStyleHelper.resolveStrokeColor(var9, var2.enabled(), var8), InteractiveStyleHelper.resolveStrokeWeight(this.config.strokeWeight, this.config.strokeWeightHover, var2.hovered()), InteractiveStyleHelper.applyDisabledAlpha(var10, var2.enabled(), var8), this.config.cornerRadius, var2.showText(), var2.text());
      this.renderer.render(var1, var2.x(), var2.y(), var2.width(), var2.height(), var11);
   }

   private int resolveColorOverride(int var1, Integer var2, Integer var3) {
      if (var2 != null) {
         return var2;
      } else {
         return var3 != null ? var3 : var1;
      }
   }

   private int resolveInteractiveOverride(int var1, int var2, Integer var3) {
      return var3 != null ? var3 : InteractiveStyleHelper.applyOverlay(var1, var2);
   }

   private ThemeProvider resolveThemeProvider() {
      return this.config != null && this.config.themeProvider != null ? this.config.themeProvider : ThemeManager::getDefaultTheme;
   }
}
