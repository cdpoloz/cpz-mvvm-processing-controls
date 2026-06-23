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
 *
 * @author CPZ
 */
public final class DefaultNumericFieldRenderer {
   /**
    * Renders the current frame.
    *
    * @param sketch parameter used by this operation
    * @param state parameter used by this operation
    * @param renderStyle parameter used by this operation
    *
    * Behavior:
    * - Uses already available state and does not define business rules.
    */
   public void render(PApplet sketch, NumericFieldViewState state, NumericFieldRenderStyle renderStyle) {
      float value = state.x() - state.width() * 0.5F;
      float value2 = state.y() - state.height() * 0.5F;
      sketch.pushStyle();
      sketch.rectMode(0);
      sketch.fill(renderStyle.backgroundColor());
      sketch.stroke(renderStyle.borderColor());
      sketch.rect(value, value2, state.width(), state.height(), 6.0F);
      if (renderStyle.font() != null) {
         sketch.textFont(renderStyle.font(), renderStyle.textSize());
      } else {
         sketch.textSize(renderStyle.textSize());
      }

      sketch.textAlign(37, 3);
      if (state.selectionStart() != state.selectionEnd()) {
         float value3 = state.selectionEndX() - state.selectionStartX();
         sketch.noStroke();
         sketch.fill(renderStyle.selectionColor());
         sketch.rect(state.selectionStartX(), value2 + 4.0F, value3, state.height() - 8.0F, 4.0F);
      }

      sketch.fill(renderStyle.textColor());
      sketch.text(state.textBeforeSelection(), state.textX(), state.y());
      if (state.selectionStart() != state.selectionEnd()) {
         sketch.fill(renderStyle.selectionTextColor());
         sketch.text(state.selectedText(), state.selectionStartX(), state.y());
      }

      sketch.fill(renderStyle.textColor());
      sketch.text(state.textAfterSelection(), state.selectionEndX(), state.y());
      if (state.showCursor()) {
         float value4 = state.y() - state.height() * 0.28F;
         float value5 = state.y() + state.height() * 0.28F;
         sketch.stroke(renderStyle.cursorColor());
         sketch.line(state.cursorX(), value4, state.cursorX(), value5);
      }

      sketch.popStyle();
   }
}
