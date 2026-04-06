package com.cpz.processing.controls.controls.slider.input;

import com.cpz.processing.controls.controls.slider.view.SliderView;
import com.cpz.processing.controls.controls.slider.viewmodel.SliderViewModel;
import com.cpz.processing.controls.core.input.PointerInteractable;

/**
 * Input component for slider input adapter.
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
public final class SliderInputAdapter {
   private final PointerInteractable view;
   private final SliderView sliderView;
   private final SliderViewModel viewModel;

   /**
    * Creates a slider input adapter.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public SliderInputAdapter(SliderView var1, SliderViewModel var2) {
      this.view = var1;
      this.sliderView = var1;
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
      this.viewModel.onPointerMoved(this.viewModel.isVisible() && this.view.contains(var1, var2));
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
         if (this.view.contains(var1, var2)) {
            this.viewModel.onPointerPressed(this.sliderView.toNormalizedValue(var1, var2));
         } else {
            this.viewModel.onPointerMoved(false);
         }

      } else {
         this.viewModel.onPointerMoved(false);
      }
   }

   /**
    * Handles mouse drag.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseDrag(float var1, float var2) {
      if (this.viewModel.isVisible()) {
         this.viewModel.onPointerMoved(this.view.contains(var1, var2));
         this.viewModel.onPointerDragged(this.sliderView.toNormalizedValue(var1, var2));
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
      this.viewModel.onPointerReleased();
      this.handleMouseMove(var1, var2);
   }

   /**
    * Handles mouse wheel.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseWheel(float var1, boolean var2, boolean var3) {
      this.viewModel.onMouseWheel(var1, var2, var3);
   }
}
