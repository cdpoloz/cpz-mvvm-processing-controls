package com.cpz.processing.controls.controls.label.style.render;

import com.cpz.processing.controls.controls.label.style.HorizontalAlign;
import com.cpz.processing.controls.controls.label.style.VerticalAlign;

/**
 * Renderer for label align mapper.
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
public final class LabelAlignMapper {
   private LabelAlignMapper() {
   }

   /**
    * Performs map horizontal.
    *
    * @param var0 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int mapHorizontal(HorizontalAlign var0) {
      HorizontalAlign var1 = var0 == null ? HorizontalAlign.START : var0;
      switch (var1) {
         case CENTER:
            return 3;
         case END:
            return 39;
         case START:
         default:
            return 37;
      }
   }

   /**
    * Performs map vertical.
    *
    * @param var0 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int mapVertical(VerticalAlign var0) {
      VerticalAlign var1 = var0 == null ? VerticalAlign.BASELINE : var0;
      switch (var1) {
         case TOP:
            return 101;
         case CENTER:
            return 3;
         case BOTTOM:
            return 102;
         case BASELINE:
         default:
            return 0;
      }
   }

   /**
    * Performs from processing horizontal.
    *
    * @param var0 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static HorizontalAlign fromProcessingHorizontal(int var0) {
      switch (var0) {
         case 3:
            return HorizontalAlign.CENTER;
         case 37:
         default:
            return HorizontalAlign.START;
         case 39:
            return HorizontalAlign.END;
      }
   }

   /**
    * Performs from processing vertical.
    *
    * @param var0 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static VerticalAlign fromProcessingVertical(int var0) {
      switch (var0) {
         case 0:
         default:
            return VerticalAlign.BASELINE;
         case 3:
            return VerticalAlign.CENTER;
         case 101:
            return VerticalAlign.TOP;
         case 102:
            return VerticalAlign.BOTTOM;
      }
   }
}
