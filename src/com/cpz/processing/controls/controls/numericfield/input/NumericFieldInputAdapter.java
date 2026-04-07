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
 */
public final class NumericFieldInputAdapter {
   private final NumericFieldView view;
   private final NumericFieldViewModel viewModel;
   private final FocusManager focusManager;

   /**
    * Creates a numeric field input adapter.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public NumericFieldInputAdapter(NumericFieldView var1, NumericFieldViewModel var2, FocusManager var3) {
      this.view = var1;
      this.viewModel = var2;
      this.focusManager = var3;
      this.focusManager.register(var2);
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
      this.viewModel.onPointerMove(this.viewModel.isVisible() && this.view.contains(var1, var2));
   }

   /**
    * Handles mouse press.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public boolean handleMousePress(float var1, float var2) {
      if (this.viewModel.isVisible() && this.viewModel.isEnabled()) {
         boolean var3 = this.view.contains(var1, var2);
         this.viewModel.onPointerPress(var3);
         if (!var3) {
            return false;
         } else {
            this.focusManager.requestFocus(this.viewModel);
            this.view.handleMousePress(var1);
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
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseDrag(float var1, float var2) {
      if (this.viewModel.isVisible() && this.viewModel.isEnabled()) {
         this.viewModel.onPointerMove(this.view.contains(var1, var2));
         this.view.handleMouseDrag(var1);
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
      this.view.handleMouseRelease();
      this.viewModel.onPointerRelease(this.view.contains(var1, var2));
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

   /**
    * Handles pointer event.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handlePointerEvent(PointerEvent var1) {
      if (var1 != null) {
         switch (var1.getType()) {
            case MOVE:
               this.handleMouseMove(var1.getX(), var1.getY());
               break;
            case PRESS:
               this.handleMousePress(var1.getX(), var1.getY());
               break;
            case DRAG:
               this.handleMouseDrag(var1.getX(), var1.getY());
               break;
            case RELEASE:
               this.handleMouseRelease(var1.getX(), var1.getY());
               break;
            case WHEEL:
               this.handleMouseWheel(var1.getWheelDelta(), var1.isShiftDown(), var1.isControlDown());
         }
      }
   }
}
