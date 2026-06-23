package com.cpz.processing.controls.controls.slider.model;

/**
 * Enumeration of slider orientation values.
 *
 * Responsibilities:
 * - Store durable control state.
 * - Remain independent from rendering and input code.
 *
 * Behavior:
 * - Keeps state mutation independent from rendering concerns.
 *
 * Notes:
 * - This type belongs to the MVVM Model layer.
 *
 * @author CPZ
 */
public enum SliderOrientation {
   HORIZONTAL,
   VERTICAL;

   // $FF: synthetic method
   private static SliderOrientation[] $values() {
      return new SliderOrientation[]{HORIZONTAL, VERTICAL};
   }
}
