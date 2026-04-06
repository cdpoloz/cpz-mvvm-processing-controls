package com.cpz.processing.controls.controls.textfield.input;

import com.cpz.processing.controls.controls.textfield.view.TextFieldView;
import com.cpz.processing.controls.controls.textfield.viewmodel.TextFieldViewModel;
import com.cpz.processing.controls.core.focus.FocusManager;

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
 */
public final class TextFieldInputAdapter {
   private final TextFieldView view;
   private final TextFieldViewModel viewModel;
   private final FocusManager focusManager;

   /**
    * Creates a text field input adapter.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public TextFieldInputAdapter(TextFieldView var1, TextFieldViewModel var2, FocusManager var3) {
      this.view = var1;
      this.viewModel = var2;
      this.focusManager = var3;
      this.focusManager.register(var2);
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
         if (!this.view.contains(var1, var2)) {
            return false;
         } else {
            this.focusManager.requestFocus(this.viewModel);
            this.view.handleMousePress(var1);
            return true;
         }
      } else {
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
         this.view.handleMouseDrag(var1);
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
}
