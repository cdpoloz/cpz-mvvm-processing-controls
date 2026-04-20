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

/**
 * Style component for slider style.
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
public final class SliderStyle {
   private final SliderStyleConfig config;
   private final SliderRenderer renderer;
   private final ThemeProvider themeProvider;

   /**
    * Creates a slider style.
    *
    * @param config parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public SliderStyle(SliderStyleConfig config) {
      this(config, new SliderRenderer());
   }

   /**
    * Creates a slider style.
    *
    * @param config parameter used by this operation
    * @param renderer parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public SliderStyle(SliderStyleConfig config, SliderRenderer renderer) {
      if (config == null) {
         throw new IllegalArgumentException("config must not be null");
      } else {
         this.config = config;
         this.renderer = renderer == null ? new SliderRenderer() : renderer;
         this.themeProvider = config.themeProvider != null ? config.themeProvider : new ThemeManager();
      }
   }

   /**
    * Renders the current frame.
    *
    * @param sketch parameter used by this operation
    * @param state parameter used by this operation
    * @param snapshot parameter used by this operation
    * @param sliderGeometry parameter used by this operation
    *
    * Behavior:
    * - Uses already available state and does not define business rules.
    */
   public void render(PApplet sketch, SliderViewState state, ThemeSnapshot snapshot, SliderGeometry sliderGeometry) {
      ThemeTokens tokens = snapshot.tokens;
      boolean shouldActivate = state.pressed() || state.dragging();
      int color = this.resolveColorOverride(tokens.surfaceVariant, this.config.trackOverride, this.config.trackColor);
      int color2 = InteractiveStyleHelper.resolveFillColor(color, this.resolveInteractiveColor(color, tokens.hoverOverlay, this.config.trackHoverOverride, this.config.trackHoverColor), this.resolveInteractiveColor(color, tokens.pressedOverlay, this.config.trackPressedOverride, this.config.trackPressedColor), state.hovered(), shouldActivate);
      int color3 = this.resolveColorOverride(tokens.primary, this.config.progressOverride, this.config.activeTrackColor);
      int color4 = InteractiveStyleHelper.resolveFillColor(color3, this.resolveInteractiveColor(color3, tokens.hoverOverlay, this.config.progressHoverOverride, this.config.activeTrackHoverColor), this.resolveInteractiveColor(color3, tokens.pressedOverlay, this.config.progressPressedOverride, this.config.activeTrackPressedColor), state.hovered(), shouldActivate);
      int color5 = this.resolveColorOverride(tokens.primary, this.config.thumbOverride, this.config.thumbColor);
      int color6 = InteractiveStyleHelper.resolveFillColor(color5, this.resolveInteractiveColor(color5, tokens.hoverOverlay, this.config.thumbHoverOverride, this.config.thumbHoverColor), this.resolveInteractiveColor(color5, tokens.pressedOverlay, this.config.thumbPressedOverride, this.config.thumbPressedColor), state.hovered(), shouldActivate);
      int disabledAlpha = this.config.disabledAlpha != null ? this.config.disabledAlpha : tokens.disabledAlpha;
      int color7 = this.resolveColorOverride(tokens.border, this.config.trackStrokeOverride, this.config.trackStrokeColor);
      int color8 = this.resolveColorOverride(tokens.border, this.config.thumbStrokeOverride, this.config.thumbStrokeColor);
      int color9 = this.resolveColorOverride(tokens.onSurface, this.config.textOverride, this.config.textColor);
      SliderRenderStyle renderStyle = new SliderRenderStyle(InteractiveStyleHelper.applyDisabledAlpha(color2, state.enabled(), disabledAlpha), InteractiveStyleHelper.resolveStrokeColor(color7, state.enabled(), disabledAlpha), InteractiveStyleHelper.resolveStrokeWeight(this.config.trackStrokeWeight, this.config.trackStrokeWeightHover, state.hovered()), this.config.trackThickness, InteractiveStyleHelper.applyDisabledAlpha(color4, state.enabled(), disabledAlpha), InteractiveStyleHelper.applyDisabledAlpha(color6, state.enabled(), disabledAlpha), InteractiveStyleHelper.resolveStrokeColor(color8, state.enabled(), disabledAlpha), InteractiveStyleHelper.resolveStrokeWeight(this.config.thumbStrokeWeight, this.config.thumbStrokeWeightHover, state.hovered()), this.config.thumbSize, InteractiveStyleHelper.resolveStrokeColor(color9, state.enabled(), disabledAlpha), this.config.svgColorMode, this.config.thumbShape, this.config.showValueText);
      this.renderer.render(sketch, sliderGeometry, state, renderStyle);
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
