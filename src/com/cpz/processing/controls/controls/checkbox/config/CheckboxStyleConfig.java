package com.cpz.processing.controls.controls.checkbox.config;

import com.cpz.processing.controls.controls.checkbox.style.render.CheckboxRenderer;

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

   public void setRenderer(CheckboxRenderer var1) {
      this.renderer = var1;
   }
}
