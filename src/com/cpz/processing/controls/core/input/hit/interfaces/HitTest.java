package com.cpz.processing.controls.core.input.hit.interfaces;

/**
 * Input component for hit test.
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
public interface HitTest {
   boolean contains(float var1, float var2);

   default void onLayout(float var1, float var2, float var3, float var4) {
   }
}
