package com.cpz.processing.controls.controls.slider.input;

import com.cpz.processing.controls.controls.slider.view.SliderView;
import com.cpz.processing.controls.controls.slider.viewmodel.SliderViewModel;
import com.cpz.processing.controls.core.input.PointerInteractable;

public final class SliderInputAdapter {
   private final PointerInteractable view;
   private final SliderView sliderView;
   private final SliderViewModel viewModel;

   public SliderInputAdapter(SliderView var1, SliderViewModel var2) {
      this.view = var1;
      this.sliderView = var1;
      this.viewModel = var2;
   }

   public void handleMouseMove(float var1, float var2) {
      this.viewModel.onPointerMoved(this.viewModel.isVisible() && this.view.contains(var1, var2));
   }

   public void handleMousePress(float var1, float var2) {
      if (this.viewModel.isVisible() && this.viewModel.isEnabled()) {
         if (this.view.contains(var1, var2)) {
            this.viewModel.onPointerPressed(this.sliderView.toNormalizedValue(var1, var2));
         } else {
            this.viewModel.onPointerMoved(false);
         }

      } else {
         this.viewModel.onPointerMoved(false);
      }
   }

   public void handleMouseDrag(float var1, float var2) {
      if (this.viewModel.isVisible()) {
         this.viewModel.onPointerMoved(this.view.contains(var1, var2));
         this.viewModel.onPointerDragged(this.sliderView.toNormalizedValue(var1, var2));
      }
   }

   public void handleMouseRelease(float var1, float var2) {
      this.viewModel.onPointerReleased();
      this.handleMouseMove(var1, var2);
   }

   public void handleMouseWheel(float var1, boolean var2, boolean var3) {
      this.viewModel.onMouseWheel(var1, var2, var3);
   }
}
