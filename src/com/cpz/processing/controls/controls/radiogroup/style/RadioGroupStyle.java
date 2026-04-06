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
 */
public final class RadioGroupStyle {
   private final RadioGroupStyleConfig config;
   private final DefaultRadioGroupRenderer renderer;
   private final ThemeProvider themeProvider;

   /**
    * Creates a radio group style.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public RadioGroupStyle(RadioGroupStyleConfig var1) {
      this(var1, new DefaultRadioGroupRenderer());
   }

   /**
    * Creates a radio group style.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public RadioGroupStyle(RadioGroupStyleConfig var1, DefaultRadioGroupRenderer var2) {
      this.config = var1 == null ? new RadioGroupStyleConfig() : var1;
      this.renderer = var2 == null ? new DefaultRadioGroupRenderer() : var2;
      this.themeProvider = this.config.themeProvider != null ? this.config.themeProvider : new ThemeManager();
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
   public void render(PApplet var1, RadioGroupViewState var2, ThemeSnapshot var3) {
      this.renderer.render(var1, var2, this.resolveRenderStyle(var2, var3));
   }

   /**
    * Resolves render style.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @return resolved render style
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public RadioGroupRenderStyle resolveRenderStyle(RadioGroupViewState var1, ThemeSnapshot var2) {
      ThemeTokens var3 = var2.tokens;
      ArrayList var4 = new ArrayList(var1.items().size());
      int var5 = this.config.disabledAlpha != null ? this.config.disabledAlpha : var3.disabledAlpha;

      for(RadioGroupItemViewState var7 : var1.items()) {
         int var8 = this.resolveColor(var3.onSurface, this.config.textOverride);
         int var9 = this.resolveColor(var3.border, this.config.indicatorOverride);
         int var10 = this.resolveBackground(var3, var7);
         int var11 = var7.selected() ? this.resolveColor(var3.primary, this.config.selectedDotOverride) : Colors.argb(0, 0, 0, 0);
         var4.add(new RadioGroupItemRenderStyle(InteractiveStyleHelper.applyDisabledAlpha(var8, var1.enabled(), var5), InteractiveStyleHelper.applyDisabledAlpha(var9, var1.enabled(), var5), InteractiveStyleHelper.applyDisabledAlpha(var10, var1.enabled(), var5), InteractiveStyleHelper.applyDisabledAlpha(var11, var1.enabled(), var5), InteractiveStyleHelper.applyDisabledAlpha(var10, var1.enabled(), var5)));
      }

      return new RadioGroupRenderStyle(var4, this.config.indicatorOuterDiameter, this.config.indicatorInnerDiameter, this.config.strokeWeight, this.config.textSize, this.config.cornerRadius, this.config.font);
   }

   private int resolveBackground(ThemeTokens var1, RadioGroupItemViewState var2) {
      int var3 = Colors.argb(0, var1.surface >>> 16 & 255, var1.surface >>> 8 & 255, var1.surface & 255);
      int var4 = this.resolveColor(var3, this.config.backgroundOverride);
      if (var2.pressed()) {
         return this.config.pressedBackgroundOverride != null ? this.config.pressedBackgroundOverride : InteractiveStyleHelper.applyOverlay(var1.surface, var1.pressedOverlay);
      } else if (var2.focused()) {
         return InteractiveStyleHelper.applyOverlay(var1.surface, Colors.alpha(28, var1.primary));
      } else if (var2.hovered()) {
         return this.config.hoveredBackgroundOverride != null ? this.config.hoveredBackgroundOverride : InteractiveStyleHelper.applyOverlay(var1.surface, var1.hoverOverlay);
      } else {
         return var4;
      }
   }

   private int resolveColor(int var1, Integer var2) {
      return var2 != null ? var2 : var1;
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
