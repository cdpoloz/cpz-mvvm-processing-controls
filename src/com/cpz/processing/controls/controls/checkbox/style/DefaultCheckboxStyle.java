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
 */
public final class DefaultCheckboxStyle implements CheckboxStyle {
   private final CheckboxStyleConfig config;
   private final CheckboxRenderer renderer;
   private final ThemeProvider themeProvider;

   /**
    * Creates a default checkbox style.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultCheckboxStyle(CheckboxStyleConfig var1) {
      this(var1, (CheckboxRenderer)(var1 != null && var1.renderer != null ? var1.renderer : new DefaultCheckboxRenderer()));
   }

   /**
    * Creates a default checkbox style.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultCheckboxStyle(CheckboxStyleConfig var1, CheckboxRenderer var2) {
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
   public void render(PApplet var1, CheckboxViewState var2, ThemeSnapshot var3) {
      ThemeTokens var4 = var3.tokens;
      int var5 = var2.checked() ? this.resolveBaseFill(var4.primary, this.config.checkedFillOverride) : this.resolveBaseFill(var4.surface, this.config.uncheckedFillOverride);
      int var6 = InteractiveStyleHelper.resolveFillColor(var5, this.resolveInteractiveColor(var5, var4.hoverOverlay, this.config.hoverFillOverride, this.config.boxHoverColor), this.resolveInteractiveColor(var5, var4.pressedOverlay, this.config.pressedFillOverride, this.config.boxPressedColor), var2.hovered(), var2.pressed());
      int var7 = this.config.disabledAlpha != null ? this.config.disabledAlpha : var4.disabledAlpha;
      int var8 = this.resolveColorOverride(var4.border, this.config.strokeOverride, this.config.borderColor);
      int var9 = this.resolveColorOverride(var4.onPrimary, this.config.checkOverride, this.config.checkColor);
      CheckboxRenderStyle var10 = new CheckboxRenderStyle(InteractiveStyleHelper.applyDisabledAlpha(var6, var2.enabled(), var7), InteractiveStyleHelper.resolveStrokeColor(var8, var2.enabled(), var7), InteractiveStyleHelper.resolveStrokeWeight(this.config.borderWidth, this.config.borderWidthHover, var2.hovered()), this.config.cornerRadius, var2.checked(), InteractiveStyleHelper.resolveStrokeColor(var9, var2.enabled(), var7), this.config.checkInset, Math.max(2.5F, var2.width() * 0.12F));
      this.renderer.render(var1, var2.x(), var2.y(), var2.width(), var2.height(), var10);
   }

   private int resolveBaseFill(int var1, Integer var2) {
      if (var2 != null) {
         return var2;
      } else {
         return this.config.boxColor != null ? this.config.boxColor : var1;
      }
   }

   private int resolveInteractiveColor(int var1, int var2, Integer var3, Integer var4) {
      if (var3 != null) {
         return var3;
      } else {
         return var4 != null ? var4 : InteractiveStyleHelper.applyOverlay(var1, var2);
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
