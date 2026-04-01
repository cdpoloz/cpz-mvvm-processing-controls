package com.cpz.processing.controls.core.overlay.tooltip.style;

import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.overlay.tooltip.state.TooltipViewState;
import com.cpz.processing.controls.core.overlay.tooltip.style.render.DefaultTooltipRenderer;
import com.cpz.processing.controls.core.style.InteractiveStyleHelper;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import com.cpz.processing.controls.core.util.Colors;
import processing.core.PApplet;

public final class DefaultTooltipStyle {
   private final TooltipStyleConfig config;
   private final DefaultTooltipRenderer renderer;

   public DefaultTooltipStyle(TooltipStyleConfig var1) {
      this(var1, new DefaultTooltipRenderer());
   }

   public DefaultTooltipStyle(TooltipStyleConfig var1, DefaultTooltipRenderer var2) {
      this.config = var1 == null ? new TooltipStyleConfig() : var1;
      this.renderer = var2 == null ? new DefaultTooltipRenderer() : var2;
   }

   public void render(PApplet var1, TooltipViewState var2) {
      this.renderer.render(var1, var2, this.resolveRenderStyle());
   }

   public TooltipRenderStyle resolveRenderStyle() {
      ThemeTokens var1 = ThemeManager.getTheme().tokens();
      int var2 = this.config.backgroundOverride != null ? this.config.backgroundOverride : InteractiveStyleHelper.applyOverlay(var1.surfaceVariant, Colors.argb(220, 0, 0, 0));
      int var3 = this.config.textOverride != null ? this.config.textOverride : var1.onSurface;
      int var4 = this.config.borderOverride != null ? this.config.borderOverride : var1.border;
      return new TooltipRenderStyle(var2, var3, var4, 1.0F, this.config.textSize, this.config.textPadding, this.config.cornerRadius, this.config.minHeight, this.config.font);
   }

   public float getTextPadding() {
      return this.config.textPadding;
   }
}
