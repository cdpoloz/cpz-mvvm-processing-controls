package com.cpz.processing.controls.controls.numericfield.style.render;

import com.cpz.processing.controls.controls.numericfield.state.NumericFieldViewState;
import com.cpz.processing.controls.controls.numericfield.style.NumericFieldRenderStyle;
import processing.core.PApplet;

/**
 * Renderer for default numeric field renderer.
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
public final class DefaultNumericFieldRenderer {
   /**
    * Renders the current frame.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    *
    * Behavior:
    * - Uses already available state and does not define business rules.
    */
   public void render(PApplet var1, NumericFieldViewState var2, NumericFieldRenderStyle var3) {
      float var4 = var2.x() - var2.width() * 0.5F;
      float var5 = var2.y() - var2.height() * 0.5F;
      var1.pushStyle();
      var1.rectMode(0);
      var1.fill(var3.backgroundColor());
      var1.stroke(var3.borderColor());
      var1.rect(var4, var5, var2.width(), var2.height(), 6.0F);
      if (var3.font() != null) {
         var1.textFont(var3.font(), var3.textSize());
      } else {
         var1.textSize(var3.textSize());
      }

      var1.textAlign(37, 3);
      if (var2.selectionStart() != var2.selectionEnd()) {
         float var6 = var2.selectionEndX() - var2.selectionStartX();
         var1.noStroke();
         var1.fill(var3.selectionColor());
         var1.rect(var2.selectionStartX(), var5 + 4.0F, var6, var2.height() - 8.0F, 4.0F);
      }

      var1.fill(var3.textColor());
      var1.text(var2.textBeforeSelection(), var2.textX(), var2.y());
      if (var2.selectionStart() != var2.selectionEnd()) {
         var1.fill(var3.selectionTextColor());
         var1.text(var2.selectedText(), var2.selectionStartX(), var2.y());
      }

      var1.fill(var3.textColor());
      var1.text(var2.textAfterSelection(), var2.selectionEndX(), var2.y());
      if (var2.showCursor()) {
         float var8 = var2.y() - var2.height() * 0.28F;
         float var7 = var2.y() + var2.height() * 0.28F;
         var1.stroke(var3.cursorColor());
         var1.line(var2.cursorX(), var8, var2.cursorX(), var7);
      }

      var1.popStyle();
   }
}
