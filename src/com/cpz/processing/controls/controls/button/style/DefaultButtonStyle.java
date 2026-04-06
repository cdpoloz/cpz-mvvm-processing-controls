package com.cpz.processing.controls.controls.button.style;

import com.cpz.processing.controls.controls.button.config.ButtonStyleConfig;
import com.cpz.processing.controls.controls.button.state.ButtonViewState;
import com.cpz.processing.controls.controls.button.style.render.ButtonRenderer;
import com.cpz.processing.controls.controls.button.style.render.DefaultButtonRenderer;
import com.cpz.processing.controls.core.style.InteractiveStyleHelper;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import processing.core.PApplet;

/**
 * Style component for default button style.
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
public final class DefaultButtonStyle implements ButtonStyle {
   private final ButtonStyleConfig config;
   private final ButtonRenderer renderer;
   private final ThemeProvider themeProvider;

   /**
    * Creates a default button style.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultButtonStyle(ButtonStyleConfig var1) {
      this(var1, (ButtonRenderer)(var1 != null && var1.renderer != null ? var1.renderer : new DefaultButtonRenderer()));
   }

   /**
    * Creates a default button style.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultButtonStyle(ButtonStyleConfig var1, ButtonRenderer var2) {
      this.config = var1;
      this.renderer = var2;
      this.themeProvider = var1 != null && var1.themeProvider != null ? var1.themeProvider : new ThemeManager();
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
   public void render(PApplet var1, ButtonViewState var2, ThemeSnapshot var3) {
      ThemeTokens var4 = var3.tokens;
      int var5 = this.resolveColorOverride(var4.primary, this.config.fillOverride, this.config.baseColor);
      int var6 = this.resolveInteractiveOverride(var5, var4.hoverOverlay, this.config.hoverBlendWithWhite != null ? var1.lerpColor(var5, var1.color(255), this.config.hoverBlendWithWhite) : null);
      int var7 = this.resolveInteractiveOverride(var5, var4.pressedOverlay, this.config.pressedBlendWithBlack != null ? var1.lerpColor(var5, var1.color(0), this.config.pressedBlendWithBlack) : null);
      int var8 = InteractiveStyleHelper.resolveFillColor(var5, var6, var7, var2.hovered(), var2.pressed());
      int var9 = this.config.disabledAlpha != null ? this.config.disabledAlpha : var4.disabledAlpha;
      int var10 = this.resolveColorOverride(var4.border, this.config.strokeOverride, this.config.strokeColor);
      int var11 = this.resolveColorOverride(var4.onPrimary, this.config.textOverride, this.config.textColor);
      ButtonRenderStyle var12 = new ButtonRenderStyle(InteractiveStyleHelper.applyDisabledAlpha(var8, var2.enabled(), var9), InteractiveStyleHelper.resolveStrokeColor(var10, var2.enabled(), var9), InteractiveStyleHelper.resolveStrokeWeight(this.config.strokeWeight, this.config.strokeWeightHover, var2.hovered()), InteractiveStyleHelper.applyDisabledAlpha(var11, var2.enabled(), var9), this.config.cornerRadius, var2.showText(), var2.text());
      this.renderer.render(var1, var2.x(), var2.y(), var2.width(), var2.height(), var12);
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
