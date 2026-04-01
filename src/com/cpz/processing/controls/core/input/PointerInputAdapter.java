package com.cpz.processing.controls.core.input;

public final class PointerInputAdapter {
   private final PointerInteractable view;
   private final PointerInputTarget viewModel;

   public PointerInputAdapter(PointerInteractable var1, PointerInputTarget var2) {
      this.view = var1;
      this.viewModel = var2;
   }

   public void handleMouseMove(float var1, float var2) {
      if (!this.viewModel.isVisible()) {
         this.viewModel.onPointerMove(false);
      } else {
         this.viewModel.onPointerMove(this.view.contains(var1, var2));
      }
   }

   public void handleMousePress(float var1, float var2) {
      if (this.viewModel.isVisible() && this.viewModel.isEnabled()) {
         this.viewModel.onPointerPress(this.view.contains(var1, var2));
      } else {
         this.viewModel.onPointerPress(false);
      }
   }

   public void handleMouseRelease(float var1, float var2) {
      if (!this.viewModel.isVisible()) {
         this.viewModel.onPointerRelease(false);
      } else {
         this.viewModel.onPointerRelease(this.view.contains(var1, var2));
      }
   }
}
