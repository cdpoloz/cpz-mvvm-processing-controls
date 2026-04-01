package com.cpz.processing.controls.controls.toggle.style;

import com.cpz.processing.controls.controls.toggle.config.ToggleStyleConfig;
import com.cpz.processing.controls.controls.toggle.state.ToggleViewState;
import com.cpz.processing.controls.core.style.InteractiveStyleHelper;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import processing.core.PApplet;

public final class ParametricToggleStyle implements ToggleStyle {
   private final ToggleStyleConfig cfg;

   public ParametricToggleStyle(ToggleStyleConfig var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Config null");
      } else if (var1.shape == null) {
         throw new IllegalArgumentException("ToggleShapeRenderer requerido");
      } else {
         this.cfg = var1;
      }
   }

   public void render(PApplet var1, ToggleViewState var2) {
      ThemeTokens var3 = ThemeManager.getTheme().tokens();
      int var4 = Math.max(0, var2.stateIndex());
      int var5 = var4 == 0 ? this.resolveStateColor(var3.surfaceVariant, this.cfg.offFillOverride, 0) : this.resolveStateColor(var3.primary, this.cfg.onFillOverride, var4);
      int var6 = InteractiveStyleHelper.resolveFillColor(var5, this.resolveInteractiveColor(var5, var3.hoverOverlay, this.cfg.hoverFillOverride, this.cfg.hoverBlendWithWhite, var1, true), this.resolveInteractiveColor(var5, var3.pressedOverlay, this.cfg.pressedFillOverride, this.cfg.pressedBlendWithBlack, var1, false), var2.hovered(), var2.pressed());
      int var7 = this.cfg.disabledAlpha != null ? this.cfg.disabledAlpha : var3.disabledAlpha;
      int var8 = this.resolveColorOverride(var3.border, this.cfg.strokeOverride, this.cfg.strokeColor);
      ToggleRenderStyle var9 = new ToggleRenderStyle(InteractiveStyleHelper.applyDisabledAlpha(var6, var2.enabled(), var7), InteractiveStyleHelper.resolveStrokeColor(var8, var2.enabled(), var7), InteractiveStyleHelper.resolveStrokeWeight(this.cfg.strokeWidth, this.cfg.strokeWidthHover, var2.hovered()));
      this.cfg.shape.render(var1, var2.x(), var2.y(), var2.width(), var2.height(), var9);
   }

   private int resolveStateColor(int var1, Integer var2, int var3) {
      if (this.cfg.stateColors != null && var3 < this.cfg.stateColors.length && this.cfg.stateColors[var3] != null) {
         return this.cfg.stateColors[var3];
      } else {
         return var2 != null ? var2 : var1;
      }
   }

   private int resolveInteractiveColor(int var1, int var2, Integer var3, Float var4, PApplet var5, boolean var6) {
      if (var3 != null) {
         return var3;
      } else {
         return var4 != null ? var5.lerpColor(var1, var5.color(var6 ? 255 : 0), var4) : InteractiveStyleHelper.applyOverlay(var1, var2);
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
