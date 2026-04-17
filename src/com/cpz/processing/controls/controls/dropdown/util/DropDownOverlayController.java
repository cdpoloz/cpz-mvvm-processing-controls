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
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    * @param var4 parameter used by this operation
    * @param var5 parameter used by this operation
    * @param var6 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DropDownOverlayController(DropDownView var1, DropDownViewModel var2, FocusManager var3, OverlayManager var4, InputManager var5, int var6) {
      this.view = var1;
      this.viewModel = var2;
      this.focusManager = var3;
      this.overlayManager = var4;
      this.inputManager = var5;
      this.zIndex = var6;
      this.inputLayer = new OverlayInputLayer(var6);
      Objects.requireNonNull(var1);
      this.overlayEntry = new OverlayEntry(var6, var1::draw, this.inputLayer, this::closeOverlay);
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

   private boolean routePressToSibling(PointerEvent var1) {
      for(DropDownOverlayController var3 : CONTROLLERS) {
         if (var3 != this && var3.viewModel.isVisible() && var3.viewModel.isEnabled() && var3.view.contains(var1.getX(), var1.getY())) {
            this.closeOverlay();
            var3.handleTransferredPress(var1.getX(), var1.getY());
            return true;
         }
      }

      return false;
   }

   private void handleTransferredPress(float var1, float var2) {
      this.view.handleMousePress(var1, var2, this.focusManager);
      this.syncRegistration();
   }

   private final class OverlayInputLayer extends DefaultInputLayer {
      private OverlayInputLayer(int var2) {
         Objects.requireNonNull(DropDownOverlayController.this);
         super(var2);
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
       * @param var1 parameter used by this operation
       * @return result of this operation
       *
       * Behavior:
       * - Applies the public interaction flow exposed by this type.
       */
      public boolean handlePointerEvent(PointerEvent var1) {
         boolean var2 = DropDownOverlayController.this.viewModel.isExpanded();
         if (!var2) {
            return false;
         } else {
            switch (var1.getType()) {
               case MOVE:
                  DropDownOverlayController.this.view.handleMouseMove(var1.getX(), var1.getY());
                  return true;
               case PRESS:
                  if (DropDownOverlayController.this.routePressToSibling(var1)) {
                     return true;
                  }

                  boolean var3 = DropDownOverlayController.this.view.handleMousePress(var1.getX(), var1.getY(), DropDownOverlayController.this.focusManager);
                  if (!var3) {
                     DropDownOverlayController.this.closeOverlay();
                     return true;
                  }

                  if (!DropDownOverlayController.this.viewModel.isExpanded() && DropDownOverlayController.this.focusManager != null && DropDownOverlayController.this.focusManager.isFocused(DropDownOverlayController.this.viewModel)) {
                     DropDownOverlayController.this.focusManager.clearFocus();
                  }

                  DropDownOverlayController.this.syncRegistration();
                  return true;
               case RELEASE:
                  DropDownOverlayController.this.view.handleMouseRelease(var1.getX(), var1.getY());
                  return true;
               default:
                  return false;
            }
         }
      }

      /**
       * Handles keyboard event.
       *
       * @param var1 parameter used by this operation
       * @return result of this operation
       *
       * Behavior:
       * - Applies the public interaction flow exposed by this type.
       */
      public boolean handleKeyboardEvent(KeyboardEvent var1) {
         return false;
      }
   }
}
