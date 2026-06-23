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
 *
 * @author CPZ
 */
public final class DefaultButtonStyle implements ButtonStyle {
   private final ButtonStyleConfig config;
   private final ButtonRenderer renderer;
   private final ThemeProvider themeProvider;

   /**
    * Creates a default button style.
    *
    * @param config parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultButtonStyle(ButtonStyleConfig config) {
      this(config, (ButtonRenderer)(config != null && config.renderer != null ? config.renderer : new DefaultButtonRenderer()));
   }

   /**
    * Creates a default button style.
    *
    * @param config parameter used by this operation
    * @param renderer parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultButtonStyle(ButtonStyleConfig config, ButtonRenderer renderer) {
      this.config = config;
      this.renderer = renderer;
      this.themeProvider = config != null && config.themeProvider != null ? config.themeProvider : new ThemeManager();
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
   public void render(PApplet sketch, ButtonViewState state, ThemeSnapshot snapshot) {
      ThemeTokens tokens = snapshot.tokens;
      int color = this.resolveColorOverride(tokens.primary, this.config.fillOverride, this.config.baseColor);
      int value = this.resolveInteractiveOverride(color, tokens.hoverOverlay, this.config.hoverBlendWithWhite != null ? sketch.lerpColor(color, sketch.color(255), this.config.hoverBlendWithWhite) : null);
      int value2 = this.resolveInteractiveOverride(color, tokens.pressedOverlay, this.config.pressedBlendWithBlack != null ? sketch.lerpColor(color, sketch.color(0), this.config.pressedBlendWithBlack) : null);
      int color2 = InteractiveStyleHelper.resolveFillColor(color, value, value2, state.hovered(), state.pressed());
      int disabledAlpha = this.config.disabledAlpha != null ? this.config.disabledAlpha : tokens.disabledAlpha;
      int color3 = this.resolveColorOverride(tokens.border, this.config.strokeOverride, this.config.strokeColor);
      int color4 = this.resolveColorOverride(tokens.onPrimary, this.config.textOverride, this.config.textColor);
      ButtonRenderStyle renderStyle = new ButtonRenderStyle(InteractiveStyleHelper.applyDisabledAlpha(color2, state.enabled(), disabledAlpha), InteractiveStyleHelper.resolveStrokeColor(color3, state.enabled(), disabledAlpha), InteractiveStyleHelper.resolveStrokeWeight(this.config.strokeWeight, this.config.strokeWeightHover, state.hovered()), InteractiveStyleHelper.applyDisabledAlpha(color4, state.enabled(), disabledAlpha), this.config.cornerRadius, state.showText(), state.text());
      this.renderer.render(sketch, state.x(), state.y(), state.width(), state.height(), renderStyle);
   }

   private int resolveColorOverride(int color, Integer color2, Integer color3) {
      if (color2 != null) {
         return color2;
      } else {
         return color3 != null ? color3 : color;
      }
   }

   private int resolveInteractiveOverride(int value, int value2, Integer value3) {
      return value3 != null ? value3 : InteractiveStyleHelper.applyOverlay(value, value2);
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
