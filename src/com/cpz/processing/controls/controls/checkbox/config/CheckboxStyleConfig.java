package com.cpz.processing.controls.controls.checkbox.config;

import com.cpz.processing.controls.controls.checkbox.style.render.CheckboxRenderer;
import com.cpz.processing.controls.core.theme.ThemeProvider;

/**
 * Configuration holder for checkbox style config.
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
 */
public final class CheckboxStyleConfig {
   public Integer checkedFillOverride;
   public Integer uncheckedFillOverride;
   public Integer hoverFillOverride;
   public Integer pressedFillOverride;
   public Integer checkOverride;
   public Integer strokeOverride;
   public Integer boxColor;
   public Integer boxHoverColor;
   public Integer boxPressedColor;
   public Integer checkColor;
   public Integer borderColor;
   public float borderWidth;
   public float borderWidthHover;
   public float cornerRadius;
   public Integer disabledAlpha;
   public float checkInset;
   public CheckboxRenderer renderer;
   public ThemeProvider themeProvider;

   /**
    * Updates renderer.
    *
    * @param var1 new renderer
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setRenderer(CheckboxRenderer var1) {
      this.renderer = var1;
   }

   /**
    * Updates theme provider.
    *
    * @param var1 new theme provider
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setThemeProvider(ThemeProvider var1) {
      this.themeProvider = var1;
   }
}
