package com.cpz.processing.controls.controls.checkbox.style;

import com.cpz.processing.controls.controls.checkbox.config.CheckboxStyleConfig;
import com.cpz.processing.controls.controls.checkbox.state.CheckboxViewState;
import com.cpz.processing.controls.controls.checkbox.style.render.CheckboxRenderer;
import com.cpz.processing.controls.controls.checkbox.style.render.DefaultCheckboxRenderer;
import com.cpz.processing.controls.core.style.InteractiveStyleHelper;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import processing.core.PApplet;

/**
 * Style component for default checkbox style.
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
public final class DefaultCheckboxStyle implements CheckboxStyle {
   private final CheckboxStyleConfig config;
   private final CheckboxRenderer renderer;
   private final ThemeProvider themeProvider;

   /**
    * Creates a default checkbox style.
    *
    * @param config parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultCheckboxStyle(CheckboxStyleConfig config) {
      this(config, (CheckboxRenderer)(config != null && config.renderer != null ? config.renderer : new DefaultCheckboxRenderer()));
   }

   /**
    * Creates a default checkbox style.
    *
    * @param config parameter used by this operation
    * @param renderer parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultCheckboxStyle(CheckboxStyleConfig config, CheckboxRenderer renderer) {
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
   public void render(PApplet sketch, CheckboxViewState state, ThemeSnapshot snapshot) {
      ThemeTokens tokens = snapshot.tokens;
      int value = state.checked() ? this.resolveBaseFill(tokens.primary, this.config.checkedFillOverride) : this.resolveBaseFill(tokens.surface, this.config.uncheckedFillOverride);
      int color = InteractiveStyleHelper.resolveFillColor(value, this.resolveInteractiveColor(value, tokens.hoverOverlay, this.config.hoverFillOverride, this.config.boxHoverColor), this.resolveInteractiveColor(value, tokens.pressedOverlay, this.config.pressedFillOverride, this.config.boxPressedColor), state.hovered(), state.pressed());
      int disabledAlpha = this.config.disabledAlpha != null ? this.config.disabledAlpha : tokens.disabledAlpha;
      int color2 = this.resolveColorOverride(tokens.border, this.config.strokeOverride, this.config.borderColor);
      int color3 = this.resolveColorOverride(tokens.onPrimary, this.config.checkOverride, this.config.checkColor);
      CheckboxRenderStyle renderStyle = new CheckboxRenderStyle(InteractiveStyleHelper.applyDisabledAlpha(color, state.enabled(), disabledAlpha), InteractiveStyleHelper.resolveStrokeColor(color2, state.enabled(), disabledAlpha), InteractiveStyleHelper.resolveStrokeWeight(this.config.borderWidth, this.config.borderWidthHover, state.hovered()), this.config.cornerRadius, state.checked(), InteractiveStyleHelper.resolveStrokeColor(color3, state.enabled(), disabledAlpha), this.config.checkInset, Math.max(2.5F, state.width() * 0.12F));
      this.renderer.render(sketch, state.x(), state.y(), state.width(), state.height(), renderStyle);
   }

   private int resolveBaseFill(int value, Integer value2) {
      if (value2 != null) {
         return value2;
      } else {
         return this.config.boxColor != null ? this.config.boxColor : value;
      }
   }

   private int resolveInteractiveColor(int color, int color2, Integer color3, Integer color4) {
      if (color3 != null) {
         return color3;
      } else {
         return color4 != null ? color4 : InteractiveStyleHelper.applyOverlay(color, color2);
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
