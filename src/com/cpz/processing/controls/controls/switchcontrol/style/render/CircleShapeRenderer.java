package com.cpz.processing.controls.controls.switchcontrol.style.render;

import com.cpz.processing.controls.controls.switchcontrol.style.SwitchRenderStyle;
import processing.core.PApplet;

public final class CircleShapeRenderer implements ShapeRenderer {
   public void render(PApplet var1, float var2, float var3, float var4, float var5, SwitchRenderStyle var6) {
      var1.pushStyle();
      var1.stroke(var6.strokeColor());
      var1.strokeWeight(var6.strokeWeight());
      var1.fill(var6.fillColor());
      float var7 = Math.min(var4, var5);
      var1.circle(var2, var3, var7);
      var1.popStyle();
   }
}
