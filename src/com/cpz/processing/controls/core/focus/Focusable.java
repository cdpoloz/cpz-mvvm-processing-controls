package com.cpz.processing.controls.core.focus;

import com.cpz.processing.controls.core.model.Enableable;
import com.cpz.processing.controls.core.view.Visible;

/**
 * Contract for focusable.
 *
 * Responsibilities:
 * - Define a focused public contract.
 * - Keep implementations replaceable.
 *
 * Behavior:
 * - Declares the contract without prescribing implementation details.
 *
 * Notes:
 * - This type is part of the public project surface.
 *
 * @author CPZ
 */
public interface Focusable extends Visible, Enableable {
   /**
    * Returns whether the target is focused.
    *
    * @return {@code true} when keyboard focus currently belongs to this target
    */
   boolean isFocused();

   /**
    * Updates the focus flag.
    *
    * @param focused new focus state
    */
   void setFocused(boolean focused);

   /**
    * Hook invoked when focus is granted.
    */
   default void onFocusGained() {
   }

   /**
    * Hook invoked when focus is removed.
    */
   default void onFocusLost() {
   }
}
