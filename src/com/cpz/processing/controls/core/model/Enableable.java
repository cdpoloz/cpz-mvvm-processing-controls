package com.cpz.processing.controls.core.model;

/**
 * Model for enableable.
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
public interface Enableable {
   /**
    * Returns whether the element is enabled.
    *
    * @return {@code true} when interaction is allowed
    */
   boolean isEnabled();

   /**
    * Updates enabled state.
    *
    * @param enabled new enabled flag
    */
   void setEnabled(boolean enabled);
}
