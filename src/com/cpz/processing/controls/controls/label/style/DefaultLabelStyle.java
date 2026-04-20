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
 *
 * @author CPZ
 */
public final class DefaultLabelStyle implements LabelStyle {
   private final LabelStyleConfig config;
   private final LabelRenderer renderer;
   private final ThemeProvider themeProvider;

   /**
    * Creates a default label style.
    *
    * @param config parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultLabelStyle(LabelStyleConfig config) {
      this(config, new DefaultTextRenderer());
   }

   /**
    * Creates a default label style.
    *
    * @param config parameter used by this operation
    * @param renderer parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DefaultLabelStyle(LabelStyleConfig config, LabelRenderer renderer) {
      this.config = config;
      this.renderer = renderer;
      this.themeProvider = config != null && config.themeProvider != null ? config.themeProvider : new ThemeManager();
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
   public void render(PApplet sketch, LabelViewState state, ThemeSnapshot snapshot) {
      ThemeTokens tokens = snapshot.tokens;
      int color = this.config.textColor != null ? this.config.textColor : tokens.onSurface;
      int disabledAlpha = this.config.disabledAlpha != null ? this.config.disabledAlpha : tokens.disabledAlpha;
      LabelRenderStyle renderStyle = new LabelRenderStyle(state.text(), InteractiveStyleHelper.applyDisabledAlpha(color, state.enabled(), disabledAlpha), this.resolveTypography());
      this.renderer.render(sketch, state.x(), state.y(), state.width(), state.height(), renderStyle);
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
