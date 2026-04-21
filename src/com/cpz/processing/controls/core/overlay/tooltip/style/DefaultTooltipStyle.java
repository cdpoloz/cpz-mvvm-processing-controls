package com.cpz.processing.controls.core.overlay.tooltip.style;

import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.overlay.tooltip.state.TooltipViewState;
import com.cpz.processing.controls.core.overlay.tooltip.style.render.DefaultTooltipRenderer;
import com.cpz.processing.controls.core.style.InteractiveStyleHelper;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import com.cpz.utils.color.Colors;
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
 *
 * @author CPZ
 */
public final class DefaultTooltipStyle {
   private final TooltipStyleConfig config;
   private final DefaultTooltipRenderer renderer;
   private final ThemeProvider themeProvider;

   /**
    * Creates a default tooltip style.
    *
    * @param config parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultTooltipStyle(TooltipStyleConfig config) {
      this(config, new DefaultTooltipRenderer());
   }

   /**
    * Creates a default tooltip style.
    *
    * @param config parameter used by this operation
    * @param renderer parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultTooltipStyle(TooltipStyleConfig config, DefaultTooltipRenderer renderer) {
      this.config = config == null ? new TooltipStyleConfig() : config;
      this.renderer = renderer == null ? new DefaultTooltipRenderer() : renderer;
      this.themeProvider = this.config.themeProvider != null ? this.config.themeProvider : new ThemeManager();
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
   public void render(PApplet sketch, TooltipViewState state, ThemeSnapshot snapshot) {
      this.renderer.render(sketch, state, this.resolveRenderStyle(snapshot));
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
    * @param snapshot parameter used by this operation
    * @return resolved render style
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public TooltipRenderStyle resolveRenderStyle(ThemeSnapshot snapshot) {
      ThemeTokens tokens = snapshot.tokens;
      int backgroundColor = this.config.backgroundOverride != null ? this.config.backgroundOverride : InteractiveStyleHelper.applyOverlay(tokens.surfaceVariant, Colors.argb(220, 0, 0, 0));
      int index = this.config.textOverride != null ? this.config.textOverride : tokens.onSurface;
      int color = this.config.borderOverride != null ? this.config.borderOverride : tokens.border;
      return new TooltipRenderStyle(backgroundColor, index, color, 1.0F, this.config.textSize, this.config.textPadding, this.config.cornerRadius, this.config.minHeight, this.config.font);
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
