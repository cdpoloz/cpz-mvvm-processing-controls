package com.cpz.processing.controls.controls.dropdown.style.render;

import com.cpz.processing.controls.controls.dropdown.state.DropDownViewState;
import com.cpz.processing.controls.controls.dropdown.style.DropDownRenderStyle;
import processing.core.PApplet;

public final class DefaultDropDownRenderer {
   public void render(PApplet var1, DropDownViewState var2, DropDownRenderStyle var3) {
      float var4 = var2.x() - var2.width() * 0.5F;
      float var5 = var2.y() - var2.height() * 0.5F;
      float var6 = var4 + var2.width();
      var1.pushStyle();
      var1.rectMode(0);
      this.applyTypography(var1, var3);
      var1.stroke(var3.strokeColor());
      var1.strokeWeight(var3.strokeWeight());
      var1.fill(var3.baseFillColor());
      var1.rect(var4, var5, var2.width(), var2.height(), var3.cornerRadius());
      var1.fill(var3.textColor());
      var1.textAlign(37, 3);
      var1.text(var2.selectedText(), var4 + var3.textPadding(), var2.y());
      this.drawArrow(var1, var6 - var3.arrowPadding(), var2.y(), var3.arrowColor(), var2.expanded());
      if (var2.expanded()) {
         this.drawExpandedList(var1, var2, var3, var4, var5);
      }

      var1.popStyle();
   }

   private void drawExpandedList(PApplet var1, DropDownViewState var2, DropDownRenderStyle var3, float var4, float var5) {
      int var6 = Math.min(var2.items().size(), var3.maxVisibleItems());
      if (var6 > 0) {
         float var7 = var5 + var2.height();
         float var8 = (float)var6 * var3.itemHeight();
         var1.stroke(var3.strokeColor());
         var1.strokeWeight(var3.strokeWeight());
         var1.fill(var3.listFillColor());
         var1.rect(var4, var7, var2.width(), var8, var3.listCornerRadius());
         var1.textAlign(37, 3);

         for(int var9 = 0; var9 < var6; ++var9) {
            float var10 = var7 + (float)var9 * var3.itemHeight();
            boolean var11 = var9 == var2.hoveredIndex();
            boolean var12 = var9 == var2.selectedIndex();
            if (var12 || var11) {
               var1.noStroke();
               var1.fill(var12 ? var3.itemSelectedColor() : var3.itemHoverColor());
               var1.rect(var4 + 1.0F, var10 + 1.0F, var2.width() - 2.0F, var3.itemHeight() - 2.0F, 4.0F);
            }

            var1.fill(var3.textColor());
            var1.text((String)var2.items().get(var9), var4 + var3.textPadding(), var10 + var3.itemHeight() * 0.5F);
         }

      }
   }

   private void drawArrow(PApplet var1, float var2, float var3, int var4, boolean var5) {
      float var6 = 10.0F;
      float var7 = 6.0F;
      var1.noStroke();
      var1.fill(var4);
      var1.beginShape();
      if (var5) {
         var1.vertex(var2 - var6 * 0.5F, var3 + var7 * 0.5F);
         var1.vertex(var2 + var6 * 0.5F, var3 + var7 * 0.5F);
         var1.vertex(var2, var3 - var7 * 0.5F);
      } else {
         var1.vertex(var2 - var6 * 0.5F, var3 - var7 * 0.5F);
         var1.vertex(var2 + var6 * 0.5F, var3 - var7 * 0.5F);
         var1.vertex(var2, var3 + var7 * 0.5F);
      }

      var1.endShape(2);
   }

   private void applyTypography(PApplet var1, DropDownRenderStyle var2) {
      if (var2.font() != null) {
         var1.textFont(var2.font(), var2.textSize());
      } else {
         var1.textSize(var2.textSize());
      }

   }
}
