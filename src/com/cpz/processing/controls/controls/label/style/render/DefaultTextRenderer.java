package com.cpz.processing.controls.controls.label.style.render;

import com.cpz.processing.controls.controls.label.style.LabelRenderStyle;
import processing.core.PApplet;

public final class DefaultTextRenderer implements LabelRenderer {
   public void render(PApplet var1, float var2, float var3, float var4, float var5, LabelRenderStyle var6) {
      var1.pushStyle();
      if (var6.typography().font() != null) {
         var1.textFont(var6.typography().font());
      }

      var1.textSize(var6.typography().textSize());
      var1.textAlign(LabelAlignMapper.mapHorizontal(var6.typography().textAlignHorizontal()), LabelAlignMapper.mapVertical(var6.typography().textAlignVertical()));
      var1.fill(var6.textColor());
      String var7 = var6.text();
      if (var7 == null) {
         var7 = "";
      }

      String[] var8 = var7.split("\n", -1);
      float var9 = (var1.textAscent() + var1.textDescent()) * var6.typography().lineSpacingMultiplier();

      for(int var10 = 0; var10 < var8.length; ++var10) {
         var1.text(var8[var10], var2, var3 + (float)var10 * var9);
      }

      var1.popStyle();
   }
}
