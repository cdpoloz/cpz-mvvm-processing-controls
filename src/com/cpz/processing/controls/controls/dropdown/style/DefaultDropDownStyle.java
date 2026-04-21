package com.cpz.processing.controls.controls.dropdown.style;

import com.cpz.processing.controls.controls.dropdown.config.DropDownStyleConfig;
import com.cpz.processing.controls.controls.dropdown.state.DropDownViewState;
import com.cpz.processing.controls.controls.dropdown.style.render.DefaultDropDownRenderer;
import com.cpz.processing.controls.core.style.InteractiveStyleHelper;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import com.cpz.utils.color.Colors;
import processing.core.PApplet;

/**
 * Style component for default drop down style.
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
public final class DefaultDropDownStyle {
   private final DropDownStyleConfig config;
   private final DefaultDropDownRenderer renderer;
   private final ThemeProvider themeProvider;

   /**
    * Creates a default drop down style.
    *
    * @param config parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultDropDownStyle(DropDownStyleConfig config) {
      this(config, new DefaultDropDownRenderer());
   }

   /**
    * Creates a default drop down style.
    *
    * @param config parameter used by this operation
    * @param renderer parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultDropDownStyle(DropDownStyleConfig config, DefaultDropDownRenderer renderer) {
      if (config == null) {
         throw new IllegalArgumentException("config must not be null");
      } else {
         this.config = config;
         this.renderer = renderer == null ? new DefaultDropDownRenderer() : renderer;
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
   public void render(PApplet sketch, DropDownViewState state, ThemeSnapshot snapshot) {
      this.renderer.render(sketch, state, this.resolveRenderStyle(state, snapshot));
   }

   /**
    * Returns corner radius.
    *
    * @return current corner radius
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getCornerRadius() {
      return this.config.cornerRadius;
   }

   /**
    * Returns list corner radius.
    *
    * @return current list corner radius
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getListCornerRadius() {
      return this.config.listCornerRadius;
   }

   /**
    * Returns stroke weight.
    *
    * @return current stroke weight
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getStrokeWeight() {
      return this.config.strokeWeight;
   }

   /**
    * Returns focused stroke weight.
    *
    * @return current focused stroke weight
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getFocusedStrokeWeight() {
      return this.config.focusedStrokeWeight;
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
    * Returns text padding.
    *
    * @return current text padding
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getTextPadding() {
      return this.config.textPadding;
   }

   /**
    * Returns arrow padding.
    *
    * @return current arrow padding
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getArrowPadding() {
      return this.config.arrowPadding;
   }

   /**
    * Returns max visible items.
    *
    * @return current max visible items
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getMaxVisibleItems() {
      return Math.max(1, this.config.maxVisibleItems);
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
   public DropDownRenderStyle resolveRenderStyle(DropDownViewState state, ThemeSnapshot snapshot) {
      ThemeTokens tokens = snapshot.tokens;
      int color3 = this.resolveColor(tokens.surface, this.config.baseFillOverride);
      int color4 = this.resolveColor(tokens.surfaceVariant, this.config.listFillOverride);
      int color5 = this.resolveColor(tokens.onSurface, this.config.textOverride);
      int borderColor = this.resolveColor(tokens.border, this.config.borderOverride);
      int color6 = this.resolveColor(tokens.primary, this.config.focusedBorderOverride);
      int color7 = this.resolveColor(tokens.hoverOverlay, this.config.hoverItemOverlayOverride);
      int color8 = this.resolveColor(Colors.alpha(36, tokens.primary), this.config.selectedItemOverlayOverride);
      int index = this.resolveBaseFill(color3, color7, tokens.pressedOverlay, state);
      int index2 = state.focused() ? this.blend(borderColor, color6, 0.35F) : borderColor;
      float value8 = !state.hovered() && !state.focused() ? this.config.strokeWeight : this.config.focusedStrokeWeight;
      int color9 = InteractiveStyleHelper.applyOverlay(color4, color7);
      int color10 = InteractiveStyleHelper.applyOverlay(color4, color8);
      int disabledAlpha = this.config.disabledAlpha != null ? this.config.disabledAlpha : tokens.disabledAlpha;
      return new DropDownRenderStyle(InteractiveStyleHelper.applyDisabledAlpha(index, state.enabled(), disabledAlpha), InteractiveStyleHelper.applyDisabledAlpha(color4, state.enabled(), disabledAlpha), InteractiveStyleHelper.applyDisabledAlpha(color9, state.enabled(), disabledAlpha), InteractiveStyleHelper.applyDisabledAlpha(color10, state.enabled(), disabledAlpha), InteractiveStyleHelper.applyDisabledAlpha(index2, state.enabled(), disabledAlpha), value8, InteractiveStyleHelper.applyDisabledAlpha(color5, state.enabled(), disabledAlpha), InteractiveStyleHelper.applyDisabledAlpha(color5, state.enabled(), disabledAlpha), state.cornerRadius(), state.listCornerRadius(), this.config.textSize, state.itemHeight(), state.textPadding(), state.arrowPadding(), Math.max(1, state.maxVisibleItems()), this.config.font);
   }

   private int resolveBaseFill(int value, int value2, int value3, DropDownViewState state) {
      return InteractiveStyleHelper.resolveFillColorWithOverlays(value, value2, value3, state.hovered(), state.pressed());
   }

   private int resolveColor(int color, Integer color2) {
      return color2 != null ? color2 : color;
   }

   private int blend(int value, int value2, float x) {
      float value3 = Math.max(0.0F, Math.min(1.0F, x));
      int value4 = this.blendChannel(value >>> 24 & 255, value2 >>> 24 & 255, value3);
      int value5 = this.blendChannel(value >>> 16 & 255, value2 >>> 16 & 255, value3);
      int value6 = this.blendChannel(value >>> 8 & 255, value2 >>> 8 & 255, value3);
      int value7 = this.blendChannel(value & 255, value2 & 255, value3);
      return Colors.argb(value4, value5, value6, value7);
   }

   private int blendChannel(int value, int value2, float x) {
      return Math.round((float)value + (float)(value2 - value) * x);
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
