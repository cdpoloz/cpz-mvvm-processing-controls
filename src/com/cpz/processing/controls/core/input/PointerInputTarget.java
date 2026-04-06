package com.cpz.processing.controls.core.input;

import com.cpz.processing.controls.core.model.Enableable;
import com.cpz.processing.controls.core.view.Visible;

/**
 * Input component for pointer input target.
 *
 * Responsibilities:
 * - Translate public input flow into control operations.
 * - Keep raw event handling outside business state.
 *
 * Behavior:
 * - Declares the contract without prescribing implementation details.
 *
 * Notes:
 * - This type is part of the public project surface.
 */
public interface PointerInputTarget extends Visible, Enableable {
   /**
    * Updates hover state.
    *
    * @param var1 {@code true} when the pointer is currently inside the control
    */
   void onPointerMove(boolean var1);

   /**
    * Updates press state.
    *
    * @param var1 {@code true} when the pointer press happened inside the control
    */
   void onPointerPress(boolean var1);

   /**
    * Updates release state.
    *
    * @param var1 {@code true} when the pointer release happened inside the control
    */
   void onPointerRelease(boolean var1);
}
