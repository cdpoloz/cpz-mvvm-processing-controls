package com.cpz.processing.controls.controls.radiogroup.style;

import com.cpz.processing.controls.controls.radiogroup.config.RadioGroupStyleConfig;
import com.cpz.processing.controls.controls.radiogroup.state.RadioGroupItemViewState;
import com.cpz.processing.controls.controls.radiogroup.state.RadioGroupViewState;
import com.cpz.processing.controls.controls.radiogroup.style.render.DefaultRadioGroupRenderer;
import com.cpz.processing.controls.core.style.InteractiveStyleHelper;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import com.cpz.processing.controls.core.util.Colors;
import java.util.ArrayList;
import processing.core.PApplet;

public final class RadioGroupStyle {
   private final RadioGroupStyleConfig config;
   private final DefaultRadioGroupRenderer renderer;

   public RadioGroupStyle(RadioGroupStyleConfig var1) {
      this(var1, new DefaultRadioGroupRenderer());
   }

   public RadioGroupStyle(RadioGroupStyleConfig var1, DefaultRadioGroupRenderer var2) {
      this.config = var1 == null ? new RadioGroupStyleConfig() : var1;
      this.renderer = var2 == null ? new DefaultRadioGroupRenderer() : var2;
   }

   public void render(PApplet var1, RadioGroupViewState var2) {
      this.renderer.render(var1, var2, this.resolveRenderStyle(var2));
   }

   public RadioGroupRenderStyle resolveRenderStyle(RadioGroupViewState var1) {
      ThemeTokens var2 = ThemeManager.getTheme().tokens();
      ArrayList var3 = new ArrayList(var1.items().size());
      int var4 = this.config.disabledAlpha != null ? this.config.disabledAlpha : var2.disabledAlpha;

      for(RadioGroupItemViewState var6 : var1.items()) {
         int var7 = this.resolveColor(var2.onSurface, this.config.textOverride);
         int var8 = this.resolveColor(var2.border, this.config.indicatorOverride);
         int var9 = this.resolveBackground(var2, var6);
         int var10 = var6.selected() ? this.resolveColor(var2.primary, this.config.selectedDotOverride) : Colors.argb(0, 0, 0, 0);
         var3.add(new RadioGroupItemRenderStyle(InteractiveStyleHelper.applyDisabledAlpha(var7, var1.enabled(), var4), InteractiveStyleHelper.applyDisabledAlpha(var8, var1.enabled(), var4), InteractiveStyleHelper.applyDisabledAlpha(var9, var1.enabled(), var4), InteractiveStyleHelper.applyDisabledAlpha(var10, var1.enabled(), var4), InteractiveStyleHelper.applyDisabledAlpha(var9, var1.enabled(), var4)));
      }

      return new RadioGroupRenderStyle(var3, this.config.indicatorOuterDiameter, this.config.indicatorInnerDiameter, this.config.strokeWeight, this.config.textSize, this.config.cornerRadius, this.config.font);
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

   public float getItemHeight() {
      return this.config.itemHeight;
   }

   public float getItemSpacing() {
      return this.config.itemSpacing;
   }

   public float getMinimumItemHeight() {
      return this.config.minimumItemHeight;
   }

   public float getIndicatorOffsetX() {
      return this.config.indicatorOffsetX;
   }

   public float getTextOffsetX() {
      return this.config.textOffsetX;
   }
}
