package com.cpz.processing.controls.core.overlay.tooltip.style;

import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.overlay.tooltip.state.TooltipViewState;
import com.cpz.processing.controls.core.overlay.tooltip.style.render.DefaultTooltipRenderer;
import com.cpz.processing.controls.core.style.InteractiveStyleHelper;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import com.cpz.processing.controls.core.util.Colors;
import processing.core.PApplet;

public final class DefaultTooltipStyle {
   private final TooltipStyleConfig config;
   private final DefaultTooltipRenderer renderer;
   private final ThemeProvider themeProvider;

   public DefaultTooltipStyle(TooltipStyleConfig var1) {
      this(var1, new DefaultTooltipRenderer());
   }

   public DefaultTooltipStyle(TooltipStyleConfig var1, DefaultTooltipRenderer var2) {
      this.config = var1 == null ? new TooltipStyleConfig() : var1;
      this.renderer = var2 == null ? new DefaultTooltipRenderer() : var2;
      this.themeProvider = this.config.themeProvider != null ? this.config.themeProvider : new ThemeManager();
   }

   public void render(PApplet var1, TooltipViewState var2, ThemeSnapshot var3) {
      this.renderer.render(var1, var2, this.resolveRenderStyle(var3));
   }

   public TooltipRenderStyle resolveRenderStyle() {
      return this.resolveRenderStyle(this.getThemeSnapshot());
   }

   public TooltipRenderStyle resolveRenderStyle(ThemeSnapshot var1) {
      ThemeTokens var2 = var1.tokens;
      int var3 = this.config.backgroundOverride != null ? this.config.backgroundOverride : InteractiveStyleHelper.applyOverlay(var2.surfaceVariant, Colors.argb(220, 0, 0, 0));
      int var4 = this.config.textOverride != null ? this.config.textOverride : var2.onSurface;
      int var5 = this.config.borderOverride != null ? this.config.borderOverride : var2.border;
      return new TooltipRenderStyle(var3, var4, var5, 1.0F, this.config.textSize, this.config.textPadding, this.config.cornerRadius, this.config.minHeight, this.config.font);
   }

   public float getTextPadding() {
      return this.config.textPadding;
   }

   public ThemeSnapshot getThemeSnapshot() {
      return this.themeProvider.getSnapshot();
   }
}
