package com.cpz.processing.controls.controls.button.config;

import com.cpz.processing.controls.controls.button.style.render.ButtonRenderer;
import com.cpz.processing.controls.core.theme.ThemeProvider;

/**
 * Configuration holder for button style config.
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
public final class ButtonStyleConfig {
   public Integer fillOverride;
   public Integer textOverride;
   public Integer strokeOverride;
   public Integer baseColor;
   public Integer textColor;
   public Integer strokeColor;
   public float strokeWeight;
   public float strokeWeightHover;
   public float cornerRadius;
   public Integer disabledAlpha;
   public Float hoverBlendWithWhite;
   public Float pressedBlendWithBlack;
   public ButtonRenderer renderer;
   public ThemeProvider themeProvider;

   /**
    * Updates renderer.
    *
    * @param renderer new renderer
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setRenderer(ButtonRenderer renderer) {
      this.renderer = renderer;
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
