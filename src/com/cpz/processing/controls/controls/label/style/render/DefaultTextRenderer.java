package com.cpz.processing.controls.controls.label.style.render;

import com.cpz.processing.controls.controls.label.style.LabelRenderStyle;
import processing.core.PApplet;

/**
 * Renderer for default text renderer.
 *
 * Responsibilities:
 * - Draw already resolved frame data.
 * - Keep rendering concerns separate from state decisions.
 *
 * Behavior:
 * - Uses already resolved state and does not decide behavior.
 *
 * Notes:
 * - This type belongs to the visual styling pipeline.
 */
public final class DefaultTextRenderer implements LabelRenderer {
   /**
    * Renders the current frame.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    * @param var4 parameter used by this operation
    * @param var5 parameter used by this operation
    * @param var6 parameter used by this operation
    *
    * Behavior:
    * - Uses already available state and does not define business rules.
    */
   public void render(PApplet var1, float var2, float var3, float var4, float var5, LabelRenderStyle var6) {
      var1.pushStyle();
      if (var6.typography().font() != null) {
         var1.textFont(var6.typography().font());
      }

      var1.textSize(var6.typography().textSize());
      var1.fill(var6.textColor());
      String var7 = var6.text();
      if (var7 == null) {
         var7 = "";
      }

      String[] var8 = var7.split("\n", -1);
      float var9 = var1.textAscent();
      float var10 = var1.textDescent();
      float var11 = (var9 + var10) * var6.typography().lineSpacingMultiplier();
      if (var4 > 0.0F && var5 > 0.0F) {
         var1.textAlign(LabelAlignMapper.mapHorizontal(var6.typography().textAlignHorizontal()), 0);
         float var12 = (float)var8.length * var11;
         float var13;
         switch (var6.typography().textAlignVertical()) {
            case TOP:
               var13 = var3;
               break;
            case CENTER:
               var13 = var3 + (var5 - var12) * 0.5F;
               break;
            case BOTTOM:
               var13 = var3 + var5 - var12;
               break;
            case BASELINE:
            default:
               var13 = var3 - var9;
         }

         float var14;
         switch (var6.typography().textAlignHorizontal()) {
            case CENTER:
               var14 = var2 + var4 * 0.5F;
               break;
            case END:
               var14 = var2 + var4;
               break;
            case START:
            default:
               var14 = var2;
         }

         for(int var15 = 0; var15 < var8.length; ++var15) {
            float var16 = var13 + var9 + (float)var15 * var11;
            var1.text(var8[var15], var14, var16);
         }
      } else {
         var1.textAlign(LabelAlignMapper.mapHorizontal(var6.typography().textAlignHorizontal()), LabelAlignMapper.mapVertical(var6.typography().textAlignVertical()));

         for(int var17 = 0; var17 < var8.length; ++var17) {
            var1.text(var8[var17], var2, var3 + (float)var17 * var11);
         }
      }

      var1.popStyle();
   }
}
