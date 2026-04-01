package com.cpz.processing.controls.controls.toggle.style.render;

import com.cpz.processing.controls.controls.toggle.style.ToggleRenderStyle;
import processing.core.PApplet;

public final class CircleShapeRenderer implements ToggleShapeRenderer {
   public void render(PApplet var1, float var2, float var3, float var4, float var5, ToggleRenderStyle var6) {
      var1.pushStyle();
      var1.stroke(var6.strokeColor());
      var1.strokeWeight(var6.strokeWeight());
      var1.fill(var6.fillColor());
      float var7 = Math.min(var4, var5);
      var1.circle(var2, var3, var7);
      var1.popStyle();
   }
}
