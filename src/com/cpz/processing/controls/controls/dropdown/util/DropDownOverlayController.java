package com.cpz.processing.controls.controls.dropdown.util;

import com.cpz.processing.controls.controls.dropdown.view.DropDownView;
import com.cpz.processing.controls.controls.dropdown.viewmodel.DropDownViewModel;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.InputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Utility component for drop down overlay controller.
 *
 * Responsibilities:
 * - Expose a public architectural role.
 * - Keep responsibilities explicit in the API surface.
 *
 * Behavior:
 * - Keeps the public role isolated from unrelated concerns.
 *
 * Notes:
 * - This type is part of the public project surface.
 *
 * @author CPZ
 */
public final class DropDownOverlayController {
   private static final List<DropDownOverlayController> CONTROLLERS = new ArrayList<>();
   private final DropDownView view;
   private final DropDownViewModel viewModel;
   private final FocusManager focusManager;
   private final OverlayManager overlayManager;
   private final InputManager inputManager;
   private final int zIndex;
   private final InputLayer inputLayer;
   private final OverlayEntry overlayEntry;
   private boolean registered;

   /**
    * Creates a drop down overlay controller.
    *
    * @param view parameter used by this operation
    * @param viewModel parameter used by this operation
    * @param focusManager parameter used by this operation
    * @param overlayManager parameter used by this operation
    * @param inputManager parameter used by this operation
    * @param zIndex parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DropDownOverlayController(DropDownView view, DropDownViewModel viewModel, FocusManager focusManager, OverlayManager overlayManager, InputManager inputManager, int zIndex) {
      this.view = view;
      this.viewModel = viewModel;
      this.focusManager = focusManager;
      this.overlayManager = overlayManager;
      this.inputManager = inputManager;
      this.zIndex = zIndex;
      this.inputLayer = new OverlayInputLayer(zIndex);
      Objects.requireNonNull(view);
      this.overlayEntry = new OverlayEntry(zIndex, view::draw, this.inputLayer, this::closeOverlay);
      CONTROLLERS.add(this);
   }

   /**
    * Performs sync registration.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void syncRegistration() {
      if (this.viewModel.isExpanded()) {
         this.register();
      } else {
         this.unregister();
      }

   }

   /**
    * Performs dispose.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void dispose() {
      this.unregister();
      CONTROLLERS.remove(this);
   }

   /**
    * Performs close overlay.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void closeOverlay() {
      this.viewModel.close();
      this.view.clearHoverState();
      this.unregister();
      if (this.focusManager != null && this.focusManager.isFocused(this.viewModel)) {
         this.focusManager.clearFocus();
      }
   }

   private void register() {
      if (!this.registered) {
         this.overlayManager.register(this.overlayEntry);
         this.inputManager.registerLayer(this.inputLayer);
         this.registered = true;
      }
   }

   private void unregister() {
      if (this.registered) {
         this.overlayManager.unregister(this.overlayEntry);
         this.inputManager.unregisterLayer(this.inputLayer);
         this.registered = false;
      }
   }

   private boolean routePressToSibling(PointerEvent event) {
      for(DropDownOverlayController dropDownOverlayController : CONTROLLERS) {
         if (dropDownOverlayController != this && dropDownOverlayController.viewModel.isVisible() && dropDownOverlayController.viewModel.isEnabled() && dropDownOverlayController.view.contains(event.getX(), event.getY())) {
            this.closeOverlay();
            dropDownOverlayController.handleTransferredPress(event.getX(), event.getY());
            return true;
         }
      }

      return false;
   }

   private void handleTransferredPress(float x, float y) {
      this.view.handleMousePress(x, y, this.focusManager);
      this.syncRegistration();
   }

   private final class OverlayInputLayer extends DefaultInputLayer {
      private OverlayInputLayer(int zIndex) {
         Objects.requireNonNull(DropDownOverlayController.this);
         super(zIndex);
      }

      /**
       * Returns whether pointer capture enabled.
       *
       * @return whether the current condition is satisfied
       *
       * Behavior:
       * - Returns the current value without applying side effects.
       */
      public boolean isPointerCaptureEnabled() {
         return DropDownOverlayController.this.viewModel.isExpanded();
      }

      /**
       * Returns whether keyboard capture enabled.
       *
       * @return whether the current condition is satisfied
       *
       * Behavior:
       * - Returns the current value without applying side effects.
       */
      public boolean isKeyboardCaptureEnabled() {
         return false;
      }

      /**
       * Handles pointer event.
       *
       * @param event parameter used by this operation
       * @return result of this operation
       *
       * Behavior:
       * - Applies the public interaction flow exposed by this type.
       */
      public boolean handlePointerEvent(PointerEvent event) {
         boolean active = DropDownOverlayController.this.viewModel.isExpanded();
         if (!active) {
            return false;
         } else {
            switch (event.getType()) {
               case MOVE:
                  DropDownOverlayController.this.view.handleMouseMove(event.getX(), event.getY());
                  return true;
               case PRESS:
                  if (DropDownOverlayController.this.routePressToSibling(event)) {
                     return true;
                  }

                  boolean inside = DropDownOverlayController.this.view.handleMousePress(event.getX(), event.getY(), DropDownOverlayController.this.focusManager);
                  if (!inside) {
                     DropDownOverlayController.this.closeOverlay();
                     return true;
                  }

                  if (!DropDownOverlayController.this.viewModel.isExpanded() && DropDownOverlayController.this.focusManager != null && DropDownOverlayController.this.focusManager.isFocused(DropDownOverlayController.this.viewModel)) {
                     DropDownOverlayController.this.focusManager.clearFocus();
                  }

                  DropDownOverlayController.this.syncRegistration();
                  return true;
               case RELEASE:
                  DropDownOverlayController.this.view.handleMouseRelease(event.getX(), event.getY());
                  return true;
               default:
                  return false;
            }
         }
      }

      /**
       * Handles keyboard event.
       *
       * @param event parameter used by this operation
       * @return result of this operation
       *
       * Behavior:
       * - Applies the public interaction flow exposed by this type.
       */
      public boolean handleKeyboardEvent(KeyboardEvent event) {
         return false;
      }
   }
}
