package com.cpz.processing.controls.controls.toggle.style;

import com.cpz.processing.controls.controls.toggle.config.ToggleStyleConfig;
import com.cpz.processing.controls.controls.toggle.state.ToggleViewState;
import com.cpz.processing.controls.core.style.InteractiveStyleHelper;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import processing.core.PApplet;

/**
 * Style component for parametric toggle style.
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
public final class ParametricToggleStyle implements ToggleStyle {
   private final ToggleStyleConfig cfg;
   private final ThemeProvider themeProvider;

   /**
    * Creates a parametric toggle style.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public ParametricToggleStyle(ToggleStyleConfig var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Config null");
      } else if (var1.shape == null) {
         throw new IllegalArgumentException("ToggleShapeRenderer requerido");
      } else {
         this.cfg = var1;
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
   public void render(PApplet var1, ToggleViewState var2, ThemeSnapshot var3) {
      ThemeTokens var4 = var3.tokens;
      int var5 = Math.max(0, var2.stateIndex());
      int var6 = var5 == 0 ? this.resolveStateColor(var4.surfaceVariant, this.cfg.offFillOverride, 0) : this.resolveStateColor(var4.primary, this.cfg.onFillOverride, var5);
      int var7 = InteractiveStyleHelper.resolveFillColor(var6, this.resolveInteractiveColor(var6, var4.hoverOverlay, this.cfg.hoverFillOverride, this.cfg.hoverBlendWithWhite, var1, true), this.resolveInteractiveColor(var6, var4.pressedOverlay, this.cfg.pressedFillOverride, this.cfg.pressedBlendWithBlack, var1, false), var2.hovered(), var2.pressed());
      int var8 = this.cfg.disabledAlpha != null ? this.cfg.disabledAlpha : var4.disabledAlpha;
      int var9 = this.resolveColorOverride(var4.border, this.cfg.strokeOverride, this.cfg.strokeColor);
      ToggleRenderStyle var10 = new ToggleRenderStyle(InteractiveStyleHelper.applyDisabledAlpha(var7, var2.enabled(), var8), InteractiveStyleHelper.resolveStrokeColor(var9, var2.enabled(), var8), InteractiveStyleHelper.resolveStrokeWeight(this.cfg.strokeWidth, this.cfg.strokeWidthHover, var2.hovered()));
      this.cfg.shape.render(var1, var2.x(), var2.y(), var2.width(), var2.height(), var10);
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
