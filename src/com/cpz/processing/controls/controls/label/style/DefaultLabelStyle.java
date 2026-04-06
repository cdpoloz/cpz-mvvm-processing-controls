package com.cpz.processing.controls.controls.label.style;

import com.cpz.processing.controls.controls.label.config.LabelStyleConfig;
import com.cpz.processing.controls.controls.label.state.LabelViewState;
import com.cpz.processing.controls.controls.label.style.render.DefaultTextRenderer;
import com.cpz.processing.controls.controls.label.style.render.LabelRenderer;
import com.cpz.processing.controls.core.style.InteractiveStyleHelper;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import processing.core.PApplet;

public final class DefaultLabelStyle implements LabelStyle {
   private final LabelStyleConfig config;
   private final LabelRenderer renderer;

   public DefaultLabelStyle(LabelStyleConfig var1) {
      this(var1, new DefaultTextRenderer());
   }

   public DefaultLabelStyle(LabelStyleConfig var1, LabelRenderer var2) {
      this.config = var1;
      this.renderer = var2;
   }

   public void render(PApplet var1, LabelViewState var2) {
      ThemeTokens var3 = this.resolveThemeProvider().getTheme().tokens();
      int var4 = this.config.textColor != null ? this.config.textColor : var3.onSurface;
      int var5 = this.config.disabledAlpha != null ? this.config.disabledAlpha : var3.disabledAlpha;
      LabelRenderStyle var6 = new LabelRenderStyle(var2.text(), InteractiveStyleHelper.applyDisabledAlpha(var4, var2.enabled(), var5), this.resolveTypography());
      this.renderer.render(var1, var2.x(), var2.y(), 0.0F, 0.0F, var6);
   }

   public LabelTypography resolveTypography() {
      return new LabelTypography(this.config.font, this.config.textSize, this.config.lineSpacingMultiplier, this.config.alignX, this.config.alignY);
   }

   private ThemeProvider resolveThemeProvider() {
      return this.config != null && this.config.themeProvider != null ? this.config.themeProvider : ThemeManager::getDefaultTheme;
   }
}
