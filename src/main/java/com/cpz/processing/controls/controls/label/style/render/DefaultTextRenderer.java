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
 *
 * @author CPZ
 */
public final class DefaultTextRenderer implements LabelRenderer {
   /**
    * Renders the current frame.
    *
    * @param sketch parameter used by this operation
    * @param x parameter used by this operation
    * @param y parameter used by this operation
    * @param width parameter used by this operation
    * @param height parameter used by this operation
    * @param renderStyle parameter used by this operation
    *
    * Behavior:
    * - Uses already available state and does not define business rules.
    */
   public void render(PApplet sketch, float x, float y, float width, float height, LabelRenderStyle renderStyle) {
      sketch.pushStyle();
      if (renderStyle.typography().font() != null) {
         sketch.textFont(renderStyle.typography().font());
      }

      sketch.textSize(renderStyle.typography().textSize());
      sketch.fill(renderStyle.textColor());
      String text = renderStyle.text();
      if (text == null) {
         text = "";
      }

      String[] text2 = text.split("\n", -1);
      float value = sketch.textAscent();
      float value2 = sketch.textDescent();
      float value3 = (value + value2) * renderStyle.typography().lineSpacingMultiplier();
      if (width > 0.0F && height > 0.0F) {
         sketch.textAlign(LabelAlignMapper.mapHorizontal(renderStyle.typography().textAlignHorizontal()), 0);
         float value4 = (float)text2.length * value3;
         float value5;
         switch (renderStyle.typography().textAlignVertical()) {
            case TOP:
               value5 = y;
               break;
            case CENTER:
               value5 = y + (height - value4) * 0.5F;
               break;
            case BOTTOM:
               value5 = y + height - value4;
               break;
            case BASELINE:
            default:
               value5 = y - value;
         }

         float value6;
         switch (renderStyle.typography().textAlignHorizontal()) {
            case CENTER:
               value6 = x + width * 0.5F;
               break;
            case END:
               value6 = x + width;
               break;
            case START:
            default:
               value6 = x;
         }

         for(int value7 = 0; value7 < text2.length; ++value7) {
            float value8 = value5 + value + (float)value7 * value3;
            sketch.text(text2[value7], value6, value8);
         }
      } else {
         sketch.textAlign(LabelAlignMapper.mapHorizontal(renderStyle.typography().textAlignHorizontal()), LabelAlignMapper.mapVertical(renderStyle.typography().textAlignVertical()));

         for(int value9 = 0; value9 < text2.length; ++value9) {
            sketch.text(text2[value9], x, y + (float)value9 * value3);
         }
      }

      sketch.popStyle();
   }
}
