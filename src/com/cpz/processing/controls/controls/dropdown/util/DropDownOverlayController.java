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
import java.util.Objects;

public final class DropDownOverlayController {
   private final DropDownView view;
   private final DropDownViewModel viewModel;
   private final FocusManager focusManager;
   private final OverlayManager overlayManager;
   private final InputManager inputManager;
   private final int zIndex;
   private final InputLayer inputLayer;
   private final OverlayEntry overlayEntry;
   private TransferHandler transferHandler;
   private boolean registered;

   public DropDownOverlayController(DropDownView var1, DropDownViewModel var2, FocusManager var3, OverlayManager var4, InputManager var5, int var6) {
      this.view = var1;
      this.viewModel = var2;
      this.focusManager = var3;
      this.overlayManager = var4;
      this.inputManager = var5;
      this.zIndex = var6;
      this.inputLayer = new OverlayInputLayer(var6);
      Objects.requireNonNull(var1);
      this.overlayEntry = new OverlayEntry(var6, var1::draw, this.inputLayer, this::closeOverlay, var2);
   }

   public void setTransferHandler(TransferHandler var1) {
      this.transferHandler = var1;
   }

   public void syncRegistration() {
      if (this.viewModel.isExpanded()) {
         this.register();
      } else {
         this.unregister();
      }

   }

   public void dispose() {
      this.unregister();
   }

   public DropDownView getView() {
      return this.view;
   }

   public DropDownViewModel getViewModel() {
      return this.viewModel;
   }

   public int getZIndex() {
      return this.zIndex;
   }

   public void closeOverlay() {
      this.viewModel.close();
      this.view.handleMouseMove(-1.0F, -1.0F);
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

   private final class OverlayInputLayer extends DefaultInputLayer {
      private OverlayInputLayer(int var2) {
         Objects.requireNonNull(DropDownOverlayController.this);
         super(var2);
      }

      public boolean isPointerCaptureEnabled() {
         return DropDownOverlayController.this.viewModel.isExpanded();
      }

      public boolean isKeyboardCaptureEnabled() {
         return false;
      }

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
                  if (DropDownOverlayController.this.transferHandler != null && DropDownOverlayController.this.transferHandler.handleTransfer(DropDownOverlayController.this, var1)) {
                     return true;
                  }

                  DropDownOverlayController.this.view.handleMousePress(var1.getX(), var1.getY(), DropDownOverlayController.this.focusManager);
                  return true;
               case RELEASE:
                  DropDownOverlayController.this.view.handleMouseRelease(var1.getX(), var1.getY());
                  return true;
               default:
                  return false;
            }
         }
      }

      public boolean handleKeyboardEvent(KeyboardEvent var1) {
         return false;
      }
   }

   public interface TransferHandler {
      boolean handleTransfer(DropDownOverlayController var1, PointerEvent var2);
   }
}
