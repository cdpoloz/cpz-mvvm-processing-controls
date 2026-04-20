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
 *
 * @author CPZ
 */
public interface HitTest {
   boolean contains(float x, float y);

   default void onLayout(float x, float y, float width, float height) {
   }
}
