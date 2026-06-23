package com.cpz.processing.controls.controls.dropdown.style.render;

import com.cpz.processing.controls.controls.dropdown.state.DropDownViewState;
import com.cpz.processing.controls.controls.dropdown.style.DropDownRenderStyle;
import processing.core.PApplet;

/**
 * Renderer for default drop down renderer.
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
public final class DefaultDropDownRenderer {
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
   public void render(PApplet sketch, DropDownViewState state, DropDownRenderStyle renderStyle) {
      float value = state.x() - state.width() * 0.5F;
      float value2 = state.y() - state.height() * 0.5F;
      float value3 = value + state.width();
      sketch.pushStyle();
      sketch.rectMode(0);
      this.applyTypography(sketch, renderStyle);
      sketch.stroke(renderStyle.strokeColor());
      sketch.strokeWeight(renderStyle.strokeWeight());
      sketch.fill(renderStyle.baseFillColor());
      sketch.rect(value, value2, state.width(), state.height(), renderStyle.cornerRadius());
      sketch.fill(renderStyle.textColor());
      sketch.textAlign(37, 3);
      sketch.text(state.selectedText(), value + renderStyle.textPadding(), state.y());
      this.drawArrow(sketch, value3 - renderStyle.arrowPadding(), state.y(), renderStyle.arrowColor(), state.expanded());
      if (state.expanded()) {
         this.drawExpandedList(sketch, state, renderStyle, value, value2);
      }

      sketch.popStyle();
   }

   private void drawExpandedList(PApplet sketch, DropDownViewState state, DropDownRenderStyle renderStyle, float x, float y) {
      int value = Math.min(state.items().size(), renderStyle.maxVisibleItems());
      if (value > 0) {
         float value2 = y + state.height();
         float value3 = (float)value * renderStyle.itemHeight();
         sketch.stroke(renderStyle.strokeColor());
         sketch.strokeWeight(renderStyle.strokeWeight());
         sketch.fill(renderStyle.listFillColor());
         sketch.rect(x, value2, state.width(), value3, renderStyle.listCornerRadius());
         sketch.textAlign(37, 3);

         for(int value4 = 0; value4 < value; ++value4) {
            float value5 = value2 + (float)value4 * renderStyle.itemHeight();
            boolean active = value4 == state.hoveredIndex();
            boolean active2 = value4 == state.selectedIndex();
            if (active2 || active) {
               sketch.noStroke();
               sketch.fill(active2 ? renderStyle.itemSelectedColor() : renderStyle.itemHoverColor());
               sketch.rect(x + 1.0F, value5 + 1.0F, state.width() - 2.0F, renderStyle.itemHeight() - 2.0F, 4.0F);
            }

            sketch.fill(renderStyle.textColor());
            sketch.text((String)state.items().get(value4), x + renderStyle.textPadding(), value5 + renderStyle.itemHeight() * 0.5F);
         }

      }
   }

   private void drawArrow(PApplet sketch, float x, float y, int value, boolean enabled) {
      float value2 = 10.0F;
      float value3 = 6.0F;
      sketch.noStroke();
      sketch.fill(value);
      sketch.beginShape();
      if (enabled) {
         sketch.vertex(x - value2 * 0.5F, y + value3 * 0.5F);
         sketch.vertex(x + value2 * 0.5F, y + value3 * 0.5F);
         sketch.vertex(x, y - value3 * 0.5F);
      } else {
         sketch.vertex(x - value2 * 0.5F, y - value3 * 0.5F);
         sketch.vertex(x + value2 * 0.5F, y - value3 * 0.5F);
         sketch.vertex(x, y + value3 * 0.5F);
      }

      sketch.endShape(2);
   }

   private void applyTypography(PApplet sketch, DropDownRenderStyle renderStyle) {
      if (renderStyle.font() != null) {
         sketch.textFont(renderStyle.font(), renderStyle.textSize());
      } else {
         sketch.textSize(renderStyle.textSize());
      }

   }
}
