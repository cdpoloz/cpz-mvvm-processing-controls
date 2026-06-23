package com.cpz.processing.controls.controls.toggle.config;

import com.cpz.processing.controls.controls.toggle.style.render.ToggleShapeRenderer;
import com.cpz.processing.controls.core.theme.ThemeProvider;

/**
 * Configuration holder for toggle style config.
 *
 * Responsibilities:
 * - Collect configuration values for a public component.
 * - Keep initialization details outside the runtime pipeline.
 *
 * Behavior:
 * - Keeps the public role isolated from unrelated concerns.
 *
 * Notes:
 * - This type is part of the public project surface.
 *
 * @author CPZ
 */
public final class ToggleStyleConfig {
   public ToggleShapeRenderer shape;
   public Integer offFillOverride;
   public Integer onFillOverride;
   public Integer hoverFillOverride;
   public Integer pressedFillOverride;
   public Integer strokeOverride;
   public Integer[] stateColors;
   public Integer strokeColor;
   public float strokeWidth;
   public float strokeWidthHover;
   public Float hoverBlendWithWhite;
   public Float pressedBlendWithBlack;
   public Integer disabledAlpha;
   public ThemeProvider themeProvider;

   /**
    * Updates shape renderer.
    *
    * @param renderer new shape renderer
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setShapeRenderer(ToggleShapeRenderer renderer) {
      this.shape = renderer;
   }

   /**
    * Updates theme provider.
    *
    * @param themeProvider new theme provider
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setThemeProvider(ThemeProvider themeProvider) {
      this.themeProvider = themeProvider;
   }
}
