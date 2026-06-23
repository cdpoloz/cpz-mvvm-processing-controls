package com.cpz.processing.controls.core.overlay.tooltip.style.render;

import com.cpz.processing.controls.core.overlay.tooltip.state.TooltipViewState;
import com.cpz.processing.controls.core.overlay.tooltip.style.TooltipRenderStyle;
import processing.core.PApplet;

/**
 * Renderer for default tooltip renderer.
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
public final class DefaultTooltipRenderer {
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
   public void render(PApplet sketch, TooltipViewState state, TooltipRenderStyle renderStyle) {
      float value = state.x() - state.width() * 0.5F;
      float value2 = state.y() - state.height() * 0.5F;
      sketch.pushStyle();
      sketch.rectMode(0);
      sketch.stroke(renderStyle.strokeColor());
      sketch.strokeWeight(renderStyle.strokeWeight());
      sketch.fill(renderStyle.backgroundColor());
      sketch.rect(value, value2, state.width(), state.height(), renderStyle.cornerRadius());
      if (renderStyle.font() != null) {
         sketch.textFont(renderStyle.font(), renderStyle.textSize());
      } else {
         sketch.textSize(renderStyle.textSize());
      }

      sketch.fill(renderStyle.textColor());
      sketch.textAlign(37, 3);
      sketch.text(state.text(), value + renderStyle.textPadding(), state.y());
      sketch.popStyle();
   }
}
