package com.cpz.processing.controls.controls.textfield.input;

import com.cpz.processing.controls.controls.textfield.view.TextFieldView;
import com.cpz.processing.controls.controls.textfield.viewmodel.TextFieldViewModel;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.input.PointerEvent;

/**
 * Input component for text field input adapter.
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
public final class TextFieldInputAdapter {
   private final TextFieldView view;
   private final TextFieldViewModel viewModel;
   private final FocusManager focusManager;

   /**
    * Creates a text field input adapter.
    *
    * @param view parameter used by this operation
    * @param viewModel parameter used by this operation
    * @param focusManager parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public TextFieldInputAdapter(TextFieldView view, TextFieldViewModel viewModel, FocusManager focusManager) {
      this.view = view;
      this.viewModel = viewModel;
      this.focusManager = focusManager;
      this.focusManager.register(viewModel);
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
         if (!this.view.contains(x, y)) {
            return false;
         } else {
            this.focusManager.requestFocus(this.viewModel);
            this.view.handleMousePress(x);
            return true;
         }
      } else {
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
         this.view.handleMouseDrag(mouseX);
      }
   }

   /**
    * Handles mouse release.
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseRelease() {
      this.view.handleMouseRelease();
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
            case PRESS:
               this.handleMousePress(event.getX(), event.getY());
               break;
            case DRAG:
               this.handleMouseDrag(event.getX(), event.getY());
               break;
            case RELEASE:
               this.handleMouseRelease();
         }
      }
   }
}
