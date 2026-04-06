package com.cpz.processing.controls.controls.label.style;

/**
 * Enumeration of horizontal align values.
 *
 * Responsibilities:
 * - Resolve visual values from immutable state and theme data.
 * - Keep interaction rules outside the rendering layer.
 *
 * Behavior:
 * - Does not process raw input or mutate the backing model.
 *
 * Notes:
 * - This type belongs to the visual styling pipeline.
 */
public enum HorizontalAlign {
   START,
   CENTER,
   END;

   // $FF: synthetic method
   private static HorizontalAlign[] $values() {
      return new HorizontalAlign[]{START, CENTER, END};
   }
}
