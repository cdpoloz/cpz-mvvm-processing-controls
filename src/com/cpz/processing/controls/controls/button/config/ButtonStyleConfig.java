package com.cpz.processing.controls.controls.button.config;

import com.cpz.processing.controls.controls.button.style.render.ButtonRenderer;

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

   public void setRenderer(ButtonRenderer var1) {
      this.renderer = var1;
   }
}
