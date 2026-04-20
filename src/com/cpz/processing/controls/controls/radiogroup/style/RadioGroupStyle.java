package com.cpz.processing.controls.controls.radiogroup.style;

import com.cpz.processing.controls.controls.radiogroup.config.RadioGroupStyleConfig;
import com.cpz.processing.controls.controls.radiogroup.state.RadioGroupItemViewState;
import com.cpz.processing.controls.controls.radiogroup.state.RadioGroupViewState;
import com.cpz.processing.controls.controls.radiogroup.style.render.DefaultRadioGroupRenderer;
import com.cpz.processing.controls.core.style.InteractiveStyleHelper;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import com.cpz.processing.controls.core.util.Colors;
import java.util.ArrayList;
import processing.core.PApplet;

/**
 * Style component for radio group style.
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
public final class RadioGroupStyle {
   private final RadioGroupStyleConfig config;
   private final DefaultRadioGroupRenderer renderer;
   private final ThemeProvider themeProvider;

   /**
    * Creates a radio group style.
    *
    * @param config parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public RadioGroupStyle(RadioGroupStyleConfig config) {
      this(config, new DefaultRadioGroupRenderer());
   }

   /**
    * Creates a radio group style.
    *
    * @param config parameter used by this operation
    * @param renderer parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public RadioGroupStyle(RadioGroupStyleConfig config, DefaultRadioGroupRenderer renderer) {
      this.config = config == null ? new RadioGroupStyleConfig() : config;
      this.renderer = renderer == null ? new DefaultRadioGroupRenderer() : renderer;
      this.themeProvider = this.config.themeProvider != null ? this.config.themeProvider : new ThemeManager();
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
   public void render(PApplet sketch, RadioGroupViewState state, ThemeSnapshot snapshot) {
      this.renderer.render(sketch, state, this.resolveRenderStyle(state, snapshot));
   }

   /**
    * Resolves render style.
    *
    * @param state parameter used by this operation
    * @param snapshot parameter used by this operation
    * @return resolved render style
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public RadioGroupRenderStyle resolveRenderStyle(RadioGroupViewState state, ThemeSnapshot snapshot) {
      ThemeTokens tokens = snapshot.tokens;
      ArrayList itemStyles = new ArrayList(state.items().size());
      int disabledAlpha = this.config.disabledAlpha != null ? this.config.disabledAlpha : tokens.disabledAlpha;

      for(RadioGroupItemViewState itemState : state.items()) {
         int color3 = this.resolveColor(tokens.onSurface, this.config.textOverride);
         int borderColor = this.resolveColor(tokens.border, this.config.indicatorOverride);
         int backgroundColor = this.resolveBackground(tokens, itemState);
         int selectedColor = itemState.selected() ? this.resolveColor(tokens.primary, this.config.selectedDotOverride) : Colors.argb(0, 0, 0, 0);
         itemStyles.add(new RadioGroupItemRenderStyle(InteractiveStyleHelper.applyDisabledAlpha(color3, state.enabled(), disabledAlpha), InteractiveStyleHelper.applyDisabledAlpha(borderColor, state.enabled(), disabledAlpha), InteractiveStyleHelper.applyDisabledAlpha(backgroundColor, state.enabled(), disabledAlpha), InteractiveStyleHelper.applyDisabledAlpha(selectedColor, state.enabled(), disabledAlpha), InteractiveStyleHelper.applyDisabledAlpha(backgroundColor, state.enabled(), disabledAlpha)));
      }

      return new RadioGroupRenderStyle(itemStyles, this.config.indicatorOuterDiameter, this.config.indicatorInnerDiameter, this.config.strokeWeight, this.config.textSize, this.config.cornerRadius, this.config.font);
   }

   private int resolveBackground(ThemeTokens tokens, RadioGroupItemViewState state) {
      int color = Colors.argb(0, tokens.surface >>> 16 & 255, tokens.surface >>> 8 & 255, tokens.surface & 255);
      int color2 = this.resolveColor(color, this.config.backgroundOverride);
      if (state.pressed()) {
         return this.config.pressedBackgroundOverride != null ? this.config.pressedBackgroundOverride : InteractiveStyleHelper.applyOverlay(tokens.surface, tokens.pressedOverlay);
      } else if (state.focused()) {
         return InteractiveStyleHelper.applyOverlay(tokens.surface, Colors.alpha(28, tokens.primary));
      } else if (state.hovered()) {
         return this.config.hoveredBackgroundOverride != null ? this.config.hoveredBackgroundOverride : InteractiveStyleHelper.applyOverlay(tokens.surface, tokens.hoverOverlay);
      } else {
         return color2;
      }
   }

   private int resolveColor(int color, Integer color2) {
      return color2 != null ? color2 : color;
   }

   /**
    * Returns item height.
    *
    * @return current item height
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getItemHeight() {
      return this.config.itemHeight;
   }

   /**
    * Returns item spacing.
    *
    * @return current item spacing
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getItemSpacing() {
      return this.config.itemSpacing;
   }

   /**
    * Returns minimum item height.
    *
    * @return current minimum item height
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getMinimumItemHeight() {
      return this.config.minimumItemHeight;
   }

   /**
    * Returns indicator offset x.
    *
    * @return current indicator offset x
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getIndicatorOffsetX() {
      return this.config.indicatorOffsetX;
   }

   /**
    * Returns text offset x.
    *
    * @return current text offset x
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getTextOffsetX() {
      return this.config.textOffsetX;
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
