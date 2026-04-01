package com.cpz.processing.controls.controls.radiogroup.style.render;

import com.cpz.processing.controls.controls.radiogroup.state.RadioGroupItemViewState;
import com.cpz.processing.controls.controls.radiogroup.state.RadioGroupViewState;
import com.cpz.processing.controls.controls.radiogroup.style.RadioGroupItemRenderStyle;
import com.cpz.processing.controls.controls.radiogroup.style.RadioGroupRenderStyle;
import processing.core.PApplet;

public final class DefaultRadioGroupRenderer {
   public void render(PApplet var1, RadioGroupViewState var2, RadioGroupRenderStyle var3) {
      var1.pushStyle();
      if (var3.font() != null) {
         var1.textFont(var3.font(), var3.textSize());
      } else {
         var1.textSize(var3.textSize());
      }

      var1.textAlign(37, 3);

      for(int var4 = 0; var4 < var2.items().size(); ++var4) {
         RadioGroupItemViewState var5 = (RadioGroupItemViewState)var2.items().get(var4);
         RadioGroupItemRenderStyle var6 = (RadioGroupItemRenderStyle)var3.itemStyles().get(var4);
         this.drawItem(var1, var5, var6, var3);
      }

      var1.popStyle();
   }

   private void drawItem(PApplet var1, RadioGroupItemViewState var2, RadioGroupItemRenderStyle var3, RadioGroupRenderStyle var4) {
      float var5 = var2.x() - var2.width() * 0.5F;
      float var6 = var2.y() - var2.height() * 0.5F;
      if ((var3.backgroundColor() >>> 24 & 255) > 0) {
         var1.noStroke();
         var1.fill(var3.backgroundColor());
         var1.rect(var5, var6, var2.width(), var2.height(), var4.cornerRadius());
      }

      var1.stroke(var3.indicatorStrokeColor());
      var1.strokeWeight(var4.strokeWeight());
      var1.fill(var3.indicatorFillColor());
      var1.circle(var2.indicatorCenterX(), var2.indicatorCenterY(), var4.indicatorOuterDiameter());
      if (var2.selected()) {
         var1.noStroke();
         var1.fill(var3.indicatorDotColor());
         var1.circle(var2.indicatorCenterX(), var2.indicatorCenterY(), var4.indicatorInnerDiameter());
      }

      var1.fill(var3.textColor());
      var1.text(var2.text(), var2.textX(), var2.y());
   }
}
