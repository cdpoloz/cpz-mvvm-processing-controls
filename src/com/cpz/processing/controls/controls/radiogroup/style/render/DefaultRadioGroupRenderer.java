package com.cpz.processing.controls.controls.radiogroup.style.render;

import com.cpz.processing.controls.controls.radiogroup.state.RadioGroupItemViewState;
import com.cpz.processing.controls.controls.radiogroup.state.RadioGroupViewState;
import com.cpz.processing.controls.controls.radiogroup.style.RadioGroupItemRenderStyle;
import com.cpz.processing.controls.controls.radiogroup.style.RadioGroupRenderStyle;
import processing.core.PApplet;

/**
 * Renderer for default radio group renderer.
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
public final class DefaultRadioGroupRenderer {
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
   public void render(PApplet sketch, RadioGroupViewState state, RadioGroupRenderStyle renderStyle) {
      sketch.pushStyle();
      if (renderStyle.font() != null) {
         sketch.textFont(renderStyle.font(), renderStyle.textSize());
      } else {
         sketch.textSize(renderStyle.textSize());
      }

      sketch.textAlign(37, 3);

      for(int value = 0; value < state.items().size(); ++value) {
         RadioGroupItemViewState state2 = (RadioGroupItemViewState)state.items().get(value);
         RadioGroupItemRenderStyle renderStyle2 = (RadioGroupItemRenderStyle)renderStyle.itemStyles().get(value);
         this.drawItem(sketch, state2, renderStyle2, renderStyle);
      }

      sketch.popStyle();
   }

   private void drawItem(PApplet sketch, RadioGroupItemViewState state, RadioGroupItemRenderStyle renderStyle, RadioGroupRenderStyle renderStyle2) {
      float value = state.x() - state.width() * 0.5F;
      float value2 = state.y() - state.height() * 0.5F;
      if ((renderStyle.backgroundColor() >>> 24 & 255) > 0) {
         sketch.noStroke();
         sketch.fill(renderStyle.backgroundColor());
         sketch.rect(value, value2, state.width(), state.height(), renderStyle2.cornerRadius());
      }

      sketch.stroke(renderStyle.indicatorStrokeColor());
      sketch.strokeWeight(renderStyle2.strokeWeight());
      sketch.fill(renderStyle.indicatorFillColor());
      sketch.circle(state.indicatorCenterX(), state.indicatorCenterY(), renderStyle2.indicatorOuterDiameter());
      if (state.selected()) {
         sketch.noStroke();
         sketch.fill(renderStyle.indicatorDotColor());
         sketch.circle(state.indicatorCenterX(), state.indicatorCenterY(), renderStyle2.indicatorInnerDiameter());
      }

      sketch.fill(renderStyle.textColor());
      sketch.text(state.text(), state.textX(), state.y());
   }
}
