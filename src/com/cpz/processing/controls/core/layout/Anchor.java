package com.cpz.processing.controls.core.layout;

/**
 * Enumeration of anchor values.
 *
 * Responsibilities:
 * - Represent layout data or coordinate resolution.
 * - Keep placement logic outside control behavior.
 *
 * Behavior:
 * - Provides symbolic values only.
 *
 * Notes:
 * - This type is part of the public project surface.
 *
 * @author CPZ
 */
public enum Anchor {
   TOP_LEFT,
   TOP_CENTER,
   TOP_RIGHT,
   CENTER,
   BOTTOM_LEFT,
   BOTTOM_CENTER,
   BOTTOM_RIGHT;

   // $FF: synthetic method
   private static Anchor[] $values() {
      return new Anchor[]{TOP_LEFT, TOP_CENTER, TOP_RIGHT, CENTER, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT};
   }
}
