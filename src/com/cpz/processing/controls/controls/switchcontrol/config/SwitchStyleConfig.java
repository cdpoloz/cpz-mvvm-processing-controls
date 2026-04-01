package com.cpz.processing.controls.controls.switchcontrol.config;

import com.cpz.processing.controls.controls.switchcontrol.style.render.ShapeRenderer;

public final class SwitchStyleConfig {
   public ShapeRenderer shape;
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

   public void setShapeRenderer(ShapeRenderer var1) {
      this.shape = var1;
   }
}
