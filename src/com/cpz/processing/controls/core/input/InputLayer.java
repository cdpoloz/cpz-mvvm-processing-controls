package com.cpz.processing.controls.core.input;

/**
 * Input component for input layer.
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
public interface InputLayer {
   /**
    * Returns the dispatch priority.
    *
    * @return higher values are processed first
    */
   int getPriority();

   /**
    * Returns whether the layer participates in dispatch.
    *
    * @return {@code true} when the layer is active
    */
   boolean isActive();

   /**
    * Returns whether pointer events should be routed to this layer.
    *
    * @return {@code true} when pointer capture is enabled
    */
   boolean isPointerCaptureEnabled();

   /**
    * Returns whether keyboard events should be routed to this layer.
    *
    * @return {@code true} when keyboard capture is enabled
    */
   boolean isKeyboardCaptureEnabled();

   /**
    * Handles a pointer event.
    *
    * @param event pointer event being dispatched
    * @return {@code true} when the event is consumed
    */
   boolean handlePointerEvent(PointerEvent event);

   /**
    * Handles a keyboard event.
    *
    * @param event keyboard event being dispatched
    * @return {@code true} when the event is consumed
    */
   boolean handleKeyboardEvent(KeyboardEvent event);
}
