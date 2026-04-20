package com.cpz.processing.controls.core.input;

/**
 * Input component for pointer interactable.
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
public interface PointerInteractable {
   /**
    * Tests whether the given sketch-space point is inside the view bounds.
    *
    * @param x x coordinate in sketch space
    * @param y y coordinate in sketch space
    * @return {@code true} when the point intersects the control
    */
   boolean contains(float x, float y);
}
