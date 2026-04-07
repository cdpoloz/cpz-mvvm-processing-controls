package com.cpz.processing.controls.controls.toggle.input;

import com.cpz.processing.controls.core.input.PointerInputAdapter;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.input.PointerInputTarget;
import com.cpz.processing.controls.core.input.PointerInteractable;

/**
 * Input component for toggle input adapter.
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
 */
public final class ToggleInputAdapter {
   private final PointerInputAdapter delegate;

   /**
    * Creates a toggle input adapter.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public ToggleInputAdapter(PointerInteractable var1, PointerInputTarget var2) {
      this.delegate = new PointerInputAdapter(var1, var2);
   }

   /**
    * Handles mouse move.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseMove(float var1, float var2) {
      this.delegate.handleMouseMove(var1, var2);
   }

   /**
    * Handles mouse press.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMousePress(float var1, float var2) {
      this.delegate.handleMousePress(var1, var2);
   }

   /**
    * Handles mouse release.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseRelease(float var1, float var2) {
      this.delegate.handleMouseRelease(var1, var2);
   }

   /**
    * Handles pointer event.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handlePointerEvent(PointerEvent var1) {
      this.delegate.handlePointerEvent(var1);
   }
}
