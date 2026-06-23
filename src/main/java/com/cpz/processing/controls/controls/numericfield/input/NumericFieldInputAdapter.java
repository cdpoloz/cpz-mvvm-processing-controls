package com.cpz.processing.controls.controls.numericfield.input;

import com.cpz.processing.controls.controls.numericfield.view.NumericFieldView;
import com.cpz.processing.controls.controls.numericfield.viewmodel.NumericFieldViewModel;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.input.PointerEvent;

/**
 * Input component for numeric field input adapter.
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
public final class NumericFieldInputAdapter {
   private final NumericFieldView view;
   private final NumericFieldViewModel viewModel;
   private final FocusManager focusManager;

   /**
    * Creates a numeric field input adapter.
    *
    * @param view parameter used by this operation
    * @param viewModel parameter used by this operation
    * @param focusManager parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public NumericFieldInputAdapter(NumericFieldView view, NumericFieldViewModel viewModel, FocusManager focusManager) {
      this.view = view;
      this.viewModel = viewModel;
      this.focusManager = focusManager;
      this.focusManager.register(viewModel);
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
      this.viewModel.onPointerMove(this.viewModel.isVisible() && this.view.contains(mouseX, mouseY));
   }

   /**
    * Handles mouse press.
    *
    * @param x parameter used by this operation
    * @param y parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public boolean handleMousePress(float x, float y) {
      if (this.viewModel.isVisible() && this.viewModel.isEnabled()) {
         boolean inside = this.view.contains(x, y);
         this.viewModel.onPointerPress(inside);
         if (!inside) {
            this.focusManager.clearFocus();
            return false;
         } else {
            this.focusManager.requestFocus(this.viewModel);
            this.view.handleMousePress(x);
            return true;
         }
      } else {
         this.viewModel.onPointerPress(false);
         return false;
      }
   }

   /**
    * Handles mouse drag.
    *
    * @param mouseX parameter used by this operation
    * @param mouseY parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseDrag(float mouseX, float mouseY) {
      if (this.viewModel.isVisible() && this.viewModel.isEnabled()) {
         this.viewModel.onPointerMove(this.view.contains(mouseX, mouseY));
         this.view.handleMouseDrag(mouseX);
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
      this.view.handleMouseRelease();
      this.viewModel.onPointerRelease(this.view.contains(mouseX, mouseY));
   }

   /**
    * Wheel-based value changes are intentionally not supported by NumericField in this iteration.
    * The no-op method is kept so existing sketches can continue calling the adapter uniformly.
    */
   public void handleMouseWheel(float mouseX, boolean enabled, boolean enabled2) {
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
               break;
         }
      }
   }
}
