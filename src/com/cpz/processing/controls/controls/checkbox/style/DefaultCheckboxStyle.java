package com.cpz.processing.controls.controls.checkbox.style;

import com.cpz.processing.controls.controls.checkbox.config.CheckboxStyleConfig;
import com.cpz.processing.controls.controls.checkbox.state.CheckboxViewState;
import com.cpz.processing.controls.controls.checkbox.style.render.CheckboxRenderer;
import com.cpz.processing.controls.controls.checkbox.style.render.DefaultCheckboxRenderer;
import com.cpz.processing.controls.core.style.InteractiveStyleHelper;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import processing.core.PApplet;

public final class DefaultCheckboxStyle implements CheckboxStyle {
   private final CheckboxStyleConfig config;
   private final CheckboxRenderer renderer;

   public DefaultCheckboxStyle(CheckboxStyleConfig var1) {
      this(var1, (CheckboxRenderer)(var1 != null && var1.renderer != null ? var1.renderer : new DefaultCheckboxRenderer()));
   }

   public DefaultCheckboxStyle(CheckboxStyleConfig var1, CheckboxRenderer var2) {
      this.config = var1;
      this.renderer = var2;
   }

   public void render(PApplet var1, CheckboxViewState var2) {
      ThemeTokens var3 = this.resolveThemeProvider().getTheme().tokens();
      int var4 = var2.checked() ? this.resolveBaseFill(var3.primary, this.config.checkedFillOverride) : this.resolveBaseFill(var3.surface, this.config.uncheckedFillOverride);
      int var5 = InteractiveStyleHelper.resolveFillColor(var4, this.resolveInteractiveColor(var4, var3.hoverOverlay, this.config.hoverFillOverride, this.config.boxHoverColor), this.resolveInteractiveColor(var4, var3.pressedOverlay, this.config.pressedFillOverride, this.config.boxPressedColor), var2.hovered(), var2.pressed());
      int var6 = this.config.disabledAlpha != null ? this.config.disabledAlpha : var3.disabledAlpha;
      int var7 = this.resolveColorOverride(var3.border, this.config.strokeOverride, this.config.borderColor);
      int var8 = this.resolveColorOverride(var3.onPrimary, this.config.checkOverride, this.config.checkColor);
      CheckboxRenderStyle var9 = new CheckboxRenderStyle(InteractiveStyleHelper.applyDisabledAlpha(var5, var2.enabled(), var6), InteractiveStyleHelper.resolveStrokeColor(var7, var2.enabled(), var6), InteractiveStyleHelper.resolveStrokeWeight(this.config.borderWidth, this.config.borderWidthHover, var2.hovered()), this.config.cornerRadius, var2.checked(), InteractiveStyleHelper.resolveStrokeColor(var8, var2.enabled(), var6), this.config.checkInset, Math.max(2.5F, var2.width() * 0.12F));
      this.renderer.render(var1, var2.x(), var2.y(), var2.width(), var2.height(), var9);
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

   private ThemeProvider resolveThemeProvider() {
      return this.config != null && this.config.themeProvider != null ? this.config.themeProvider : ThemeManager::getDefaultTheme;
   }
}
