package com.cpz.processing.controls.core.overlay.tooltip.style.render;

import com.cpz.processing.controls.core.overlay.tooltip.state.TooltipViewState;
import com.cpz.processing.controls.core.overlay.tooltip.style.TooltipRenderStyle;
import processing.core.PApplet;

public final class DefaultTooltipRenderer {
   public void render(PApplet var1, TooltipViewState var2, TooltipRenderStyle var3) {
      float var4 = var2.x() - var2.width() * 0.5F;
      float var5 = var2.y() - var2.height() * 0.5F;
      var1.pushStyle();
      var1.rectMode(0);
      var1.stroke(var3.strokeColor());
      var1.strokeWeight(var3.strokeWeight());
      var1.fill(var3.backgroundColor());
      var1.rect(var4, var5, var2.width(), var2.height(), var3.cornerRadius());
      if (var3.font() != null) {
         var1.textFont(var3.font(), var3.textSize());
      } else {
         var1.textSize(var3.textSize());
      }

      var1.fill(var3.textColor());
      var1.textAlign(37, 3);
      var1.text(var2.text(), var4 + var3.textPadding(), var2.y());
      var1.popStyle();
   }
}
