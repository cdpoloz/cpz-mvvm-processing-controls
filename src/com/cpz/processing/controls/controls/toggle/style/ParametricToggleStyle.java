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
 *
 * @author CPZ
 */
public final class ParametricToggleStyle implements ToggleStyle {
   private final ToggleStyleConfig cfg;
   private final ThemeProvider themeProvider;

   /**
    * Creates a parametric toggle style.
    *
    * @param config parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public ParametricToggleStyle(ToggleStyleConfig config) {
      if (config == null) {
         throw new IllegalArgumentException("Config null");
      } else if (config.shape == null) {
         throw new IllegalArgumentException("ToggleShapeRenderer requerido");
      } else {
         this.cfg = config;
         this.themeProvider = config.themeProvider != null ? config.themeProvider : new ThemeManager();
      }
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
   public void render(PApplet sketch, ToggleViewState state, ThemeSnapshot snapshot) {
      ThemeTokens tokens = snapshot.tokens;
      int value = Math.max(0, state.stateIndex());
      int color = value == 0 ? this.resolveStateColor(tokens.surfaceVariant, this.cfg.offFillOverride, 0) : this.resolveStateColor(tokens.primary, this.cfg.onFillOverride, value);
      int color2 = InteractiveStyleHelper.resolveFillColor(color, this.resolveInteractiveColor(color, tokens.hoverOverlay, this.cfg.hoverFillOverride, this.cfg.hoverBlendWithWhite, sketch, true), this.resolveInteractiveColor(color, tokens.pressedOverlay, this.cfg.pressedFillOverride, this.cfg.pressedBlendWithBlack, sketch, false), state.hovered(), state.pressed());
      int disabledAlpha = this.cfg.disabledAlpha != null ? this.cfg.disabledAlpha : tokens.disabledAlpha;
      int color3 = this.resolveColorOverride(tokens.border, this.cfg.strokeOverride, this.cfg.strokeColor);
      ToggleRenderStyle renderStyle = new ToggleRenderStyle(InteractiveStyleHelper.applyDisabledAlpha(color2, state.enabled(), disabledAlpha), InteractiveStyleHelper.resolveStrokeColor(color3, state.enabled(), disabledAlpha), InteractiveStyleHelper.resolveStrokeWeight(this.cfg.strokeWidth, this.cfg.strokeWidthHover, state.hovered()));
      this.cfg.shape.render(sketch, state.x(), state.y(), state.width(), state.height(), renderStyle);
   }

   private int resolveStateColor(int color, Integer color2, int color3) {
      if (this.cfg.stateColors != null && color3 < this.cfg.stateColors.length && this.cfg.stateColors[color3] != null) {
         return this.cfg.stateColors[color3];
      } else {
         return color2 != null ? color2 : color;
      }
   }

   private int resolveInteractiveColor(int color, int color2, Integer color3, Float value, PApplet sketch, boolean enabled) {
      if (color3 != null) {
         return color3;
      } else {
         return value != null ? sketch.lerpColor(color, sketch.color(enabled ? 255 : 0), value) : InteractiveStyleHelper.applyOverlay(color, color2);
      }
   }

   private int resolveColorOverride(int color, Integer color2, Integer color3) {
      if (color2 != null) {
         return color2;
      } else {
         return color3 != null ? color3 : color;
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
