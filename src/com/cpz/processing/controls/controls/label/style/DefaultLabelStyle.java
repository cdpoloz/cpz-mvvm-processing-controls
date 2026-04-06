package com.cpz.processing.controls.controls.label.style;

import com.cpz.processing.controls.controls.label.config.LabelStyleConfig;
import com.cpz.processing.controls.controls.label.state.LabelViewState;
import com.cpz.processing.controls.controls.label.style.render.DefaultTextRenderer;
import com.cpz.processing.controls.controls.label.style.render.LabelRenderer;
import com.cpz.processing.controls.core.style.InteractiveStyleHelper;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import processing.core.PApplet;

/**
 * Style component for default label style.
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
public final class DefaultLabelStyle implements LabelStyle {
   private final LabelStyleConfig config;
   private final LabelRenderer renderer;
   private final ThemeProvider themeProvider;

   /**
    * Creates a default label style.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultLabelStyle(LabelStyleConfig var1) {
      this(var1, new DefaultTextRenderer());
   }

   /**
    * Creates a default label style.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultLabelStyle(LabelStyleConfig var1, LabelRenderer var2) {
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
   public void render(PApplet var1, LabelViewState var2, ThemeSnapshot var3) {
      ThemeTokens var4 = var3.tokens;
      int var5 = this.config.textColor != null ? this.config.textColor : var4.onSurface;
      int var6 = this.config.disabledAlpha != null ? this.config.disabledAlpha : var4.disabledAlpha;
      LabelRenderStyle var7 = new LabelRenderStyle(var2.text(), InteractiveStyleHelper.applyDisabledAlpha(var5, var2.enabled(), var6), this.resolveTypography());
      this.renderer.render(var1, var2.x(), var2.y(), 0.0F, 0.0F, var7);
   }

   /**
    * Resolves typography.
    *
    * @return resolved typography
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public LabelTypography resolveTypography() {
      return new LabelTypography(this.config.font, this.config.textSize, this.config.lineSpacingMultiplier, this.config.alignX, this.config.alignY);
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
