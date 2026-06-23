package com.cpz.processing.controls.controls.button.input;

import com.cpz.processing.controls.core.input.PointerInputAdapter;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.input.PointerInputTarget;
import com.cpz.processing.controls.core.input.PointerInteractable;

/**
 * Input component for button input adapter.
 *
 * Responsibilities:
 * - Translate public input flow into control operations.
 * - Keep raw event handling outside business state.
 *
 * Behavior:
 * - Keeps the public role isolated from unrelated concerns.
 *
 * Notes:
 * - This type is part of the public project surface.
 *
 * @author CPZ
 */
public final class ButtonInputAdapter {
   private final PointerInputAdapter delegate;

   /**
    * Creates a button input adapter.
    *
    * @param interactable parameter used by this operation
    * @param target parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public ButtonInputAdapter(PointerInteractable interactable, PointerInputTarget target) {
      this.delegate = new PointerInputAdapter(interactable, target);
   }

   /**
    * Handles mouse move.
    *
    * @param mouseX parameter used by this operation
    * @param mouseY parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseMove(float mouseX, float mouseY) {
      this.delegate.handleMouseMove(mouseX, mouseY);
   }

   /**
    * Handles mouse press.
    *
    * @param mouseX parameter used by this operation
    * @param mouseY parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMousePress(float mouseX, float mouseY) {
      this.delegate.handleMousePress(mouseX, mouseY);
   }

   /**
    * Handles mouse release.
    *
    * @param mouseX parameter used by this operation
    * @param mouseY parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseRelease(float mouseX, float mouseY) {
      this.delegate.handleMouseRelease(mouseX, mouseY);
   }

   /**
    * Handles pointer event.
    *
    * @param event parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handlePointerEvent(PointerEvent event) {
      this.delegate.handlePointerEvent(event);
   }
}
