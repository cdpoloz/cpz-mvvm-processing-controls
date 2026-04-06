package com.cpz.processing.controls.core.input;

/**
 * Input component for pointer input adapter.
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
public final class PointerInputAdapter {
   private final PointerInteractable view;
   private final PointerInputTarget viewModel;

   /**
    * Creates a pointer input adapter.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public PointerInputAdapter(PointerInteractable var1, PointerInputTarget var2) {
      this.view = var1;
      this.viewModel = var2;
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
      if (!this.viewModel.isVisible()) {
         this.viewModel.onPointerMove(false);
      } else {
         this.viewModel.onPointerMove(this.view.contains(var1, var2));
      }
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
      if (this.viewModel.isVisible() && this.viewModel.isEnabled()) {
         this.viewModel.onPointerPress(this.view.contains(var1, var2));
      } else {
         this.viewModel.onPointerPress(false);
      }
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
      if (!this.viewModel.isVisible()) {
         this.viewModel.onPointerRelease(false);
      } else {
         this.viewModel.onPointerRelease(this.view.contains(var1, var2));
      }
   }
}
