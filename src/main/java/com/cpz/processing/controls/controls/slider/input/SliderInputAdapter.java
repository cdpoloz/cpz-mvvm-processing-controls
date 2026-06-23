package com.cpz.processing.controls.controls.slider.input;

import com.cpz.processing.controls.controls.slider.view.SliderView;
import com.cpz.processing.controls.controls.slider.viewmodel.SliderViewModel;
import com.cpz.processing.controls.core.input.PointerEvent;
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
 *
 * @author CPZ
 */
public final class SliderInputAdapter {
   private final PointerInteractable view;
   private final SliderView sliderView;
   private final SliderViewModel viewModel;

   /**
    * Creates a slider input adapter.
    *
    * @param view parameter used by this operation
    * @param viewModel parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public SliderInputAdapter(SliderView view, SliderViewModel viewModel) {
      this.view = view;
      this.sliderView = view;
      this.viewModel = viewModel;
   }

   /**
    * Handles mouse move.
    *
    * @param wheelDelta mouse wheel delta
    * @param mouseY parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseMove(float mouseX, float mouseY) {
      this.viewModel.onPointerMoved(this.viewModel.isVisible() && this.view.contains(mouseX, mouseY));
   }

   /**
    * Handles mouse press.
    *
    * @param wheelDelta mouse wheel delta
    * @param mouseY parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMousePress(float mouseX, float mouseY) {
      if (this.viewModel.isVisible() && this.viewModel.isEnabled()) {
         if (this.view.contains(mouseX, mouseY)) {
            this.viewModel.onPointerPressed(this.sliderView.toNormalizedValue(mouseX, mouseY));
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
    * @param wheelDelta mouse wheel delta
    * @param mouseY parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseDrag(float mouseX, float mouseY) {
      if (this.viewModel.isVisible()) {
         this.viewModel.onPointerMoved(this.view.contains(mouseX, mouseY));
         this.viewModel.onPointerDragged(this.sliderView.toNormalizedValue(mouseX, mouseY));
      }
   }

   /**
    * Handles mouse release.
    *
    * @param wheelDelta mouse wheel delta
    * @param mouseY parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseRelease(float mouseX, float mouseY) {
      this.viewModel.onPointerReleased();
      this.handleMouseMove(mouseX, mouseY);
   }

   /**
    * Handles mouse wheel.
    *
    * @param wheelDelta mouse wheel delta
    * @param coarseStep coarse-step modifier
    * @param fineStep fine-step modifier
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseWheel(float wheelDelta, boolean coarseStep, boolean fineStep) {
      this.viewModel.onMouseWheel(wheelDelta, coarseStep, fineStep);
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
               this.handleMouseMove(event.getX(), event.getY());
               break;
            case PRESS:
               this.handleMousePress(event.getX(), event.getY());
               break;
            case DRAG:
               this.handleMouseDrag(event.getX(), event.getY());
               break;
            case RELEASE:
               this.handleMouseRelease(event.getX(), event.getY());
               break;
            case WHEEL:
               this.handleMouseWheel(event.getWheelDelta(), event.isShiftDown(), event.isControlDown());
         }
      }
   }
}
