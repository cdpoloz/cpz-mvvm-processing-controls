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
 * Coordinates drop-down overlay registration and overlay input capture.
 *
 * <p>The controller keeps the expanded list registered with
 * {@link OverlayManager} and registers a matching high-priority input layer
 * with {@link InputManager} while the drop-down is expanded. The caller supplies
 * geometry synchronization callbacks so a parent-aware drop-down can update its
 * global overlay anchor without the parent knowing about this controller.</p>
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
    private final Runnable syncGlobalGeometry;
    private final Runnable syncPresentationGeometry;
    private final InputLayer inputLayer;
    private final OverlayEntry overlayEntry;
    private boolean registered;

   /**
    * Creates a controller for one drop-down overlay.
    *
    * @param view view that renders and hit-tests the base and expanded list
    * @param viewModel interaction state for the drop-down
    * @param focusManager focus manager used by the facade
    * @param overlayManager host-owned overlay manager
    * @param inputManager host-owned input manager
    * @param zIndex overlay and input-layer priority
    * @param syncGlobalGeometry callback that synchronizes the view to global
    *                           overlay coordinates
    * @param syncPresentationGeometry callback that restores the collapsed-field
    *                                 presentation coordinates
    */
   public DropDownOverlayController(DropDownView view, DropDownViewModel viewModel, FocusManager focusManager, OverlayManager overlayManager, InputManager inputManager, int zIndex, Runnable syncGlobalGeometry, Runnable syncPresentationGeometry) {
      this.view = view;
      this.viewModel = viewModel;
      this.focusManager = focusManager;
      this.overlayManager = overlayManager;
      this.inputManager = inputManager;
      this.zIndex = zIndex;
      this.syncGlobalGeometry = syncGlobalGeometry;
      this.syncPresentationGeometry = syncPresentationGeometry;
      this.inputLayer = new OverlayInputLayer(zIndex);
      Objects.requireNonNull(view);
      this.overlayEntry = new OverlayEntry(zIndex, view::draw, this.inputLayer, this::closeOverlay);
      CONTROLLERS.add(this);
   }

   /**
    * Registers or unregisters overlay resources according to expanded state.
    */
   public void syncRegistration() {
      if (this.viewModel.isExpanded()) {
         this.register();
      } else {
         this.unregister();
      }

   }

   /**
    * Unregisters overlay resources and removes this controller from sibling
    * transfer coordination.
    */
   public void dispose() {
      this.unregister();
      CONTROLLERS.remove(this);
   }

   /**
    * Closes the expanded list and unregisters its overlay resources.
    */
   public void closeOverlay() {
      this.viewModel.close();
      this.view.clearHoverState();
      this.unregister();
      this.syncPresentation();
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
         if (dropDownOverlayController != this && dropDownOverlayController.containsGlobalBase(event.getX(), event.getY())) {
            this.closeOverlay();
            dropDownOverlayController.handleTransferredPress(event.getX(), event.getY());
            return true;
         }
      }

      return false;
   }

   private void handleTransferredPress(float x, float y) {
      this.syncGlobal();
      this.view.handleMousePress(x, y, this.focusManager);
      this.syncRegistration();
      this.syncPresentation();
   }

   private boolean containsGlobalBase(float x, float y) {
      if (!this.viewModel.isVisible() || !this.viewModel.isEnabled()) {
         return false;
      }
      this.syncGlobal();
      boolean contains = this.view.containsBaseBounds(x, y);
      this.syncPresentation();
      return contains;
   }

   private void syncGlobal() {
      if (this.syncGlobalGeometry != null) {
         this.syncGlobalGeometry.run();
      }
   }

   private void syncPresentation() {
      if (this.syncPresentationGeometry != null) {
         this.syncPresentationGeometry.run();
      }
   }

   private final class OverlayInputLayer extends DefaultInputLayer {
      private OverlayInputLayer(int zIndex) {
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
                  if (!DropDownOverlayController.this.view.contains(event.getX(), event.getY())
                          && DropDownOverlayController.this.routePressToSibling(event)) {
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
