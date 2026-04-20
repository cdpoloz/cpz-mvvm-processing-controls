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
 *
 * @author CPZ
 */
public final class PointerInputAdapter {
   private final PointerInteractable view;
   private final PointerInputTarget viewModel;

   /**
    * Creates a pointer input adapter.
    *
    * @param interactable parameter used by this operation
    * @param target parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public PointerInputAdapter(PointerInteractable interactable, PointerInputTarget target) {
      this.view = interactable;
      this.viewModel = target;
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
      if (!this.viewModel.isVisible()) {
         this.viewModel.onPointerMove(false);
      } else {
         this.viewModel.onPointerMove(this.view.contains(mouseX, mouseY));
      }
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
      if (this.viewModel.isVisible() && this.viewModel.isEnabled()) {
         this.viewModel.onPointerPress(this.view.contains(mouseX, mouseY));
      } else {
         this.viewModel.onPointerPress(false);
      }
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
      if (!this.viewModel.isVisible()) {
         this.viewModel.onPointerRelease(false);
      } else {
         this.viewModel.onPointerRelease(this.view.contains(mouseX, mouseY));
      }
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
      if (event != null) {
         switch (event.getType()) {
            case MOVE:
            case DRAG:
               this.handleMouseMove(event.getX(), event.getY());
               break;
            case PRESS:
               this.handleMousePress(event.getX(), event.getY());
               break;
            case RELEASE:
               this.handleMouseRelease(event.getX(), event.getY());
         }
      }
   }
}
