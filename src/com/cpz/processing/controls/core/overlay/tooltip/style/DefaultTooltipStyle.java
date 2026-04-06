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

/**
 * Style component for default tooltip style.
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
public final class DefaultTooltipStyle {
   private final TooltipStyleConfig config;
   private final DefaultTooltipRenderer renderer;
   private final ThemeProvider themeProvider;

   /**
    * Creates a default tooltip style.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultTooltipStyle(TooltipStyleConfig var1) {
      this(var1, new DefaultTooltipRenderer());
   }

   /**
    * Creates a default tooltip style.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultTooltipStyle(TooltipStyleConfig var1, DefaultTooltipRenderer var2) {
      this.config = var1 == null ? new TooltipStyleConfig() : var1;
      this.renderer = var2 == null ? new DefaultTooltipRenderer() : var2;
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
   public void render(PApplet var1, TooltipViewState var2, ThemeSnapshot var3) {
      this.renderer.render(var1, var2, this.resolveRenderStyle(var3));
   }

   /**
    * Resolves render style.
    *
    * @return resolved render style
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public TooltipRenderStyle resolveRenderStyle() {
      return this.resolveRenderStyle(this.getThemeSnapshot());
   }

   /**
    * Resolves render style.
    *
    * @param var1 parameter used by this operation
    * @return resolved render style
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public TooltipRenderStyle resolveRenderStyle(ThemeSnapshot var1) {
      ThemeTokens var2 = var1.tokens;
      int var3 = this.config.backgroundOverride != null ? this.config.backgroundOverride : InteractiveStyleHelper.applyOverlay(var2.surfaceVariant, Colors.argb(220, 0, 0, 0));
      int var4 = this.config.textOverride != null ? this.config.textOverride : var2.onSurface;
      int var5 = this.config.borderOverride != null ? this.config.borderOverride : var2.border;
      return new TooltipRenderStyle(var3, var4, var5, 1.0F, this.config.textSize, this.config.textPadding, this.config.cornerRadius, this.config.minHeight, this.config.font);
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
