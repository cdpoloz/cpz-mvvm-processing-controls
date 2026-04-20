package com.cpz.processing.controls.controls.slider.model;

/**
 * Enumeration of snap mode values.
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
public enum SnapMode {
   ALWAYS,
   ON_RELEASE;

   // $FF: synthetic method
   private static SnapMode[] $values() {
      return new SnapMode[]{ALWAYS, ON_RELEASE};
   }
}
