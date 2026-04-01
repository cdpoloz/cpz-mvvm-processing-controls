package com.cpz.processing.controls.controls.textfield.input;

import com.cpz.processing.controls.controls.textfield.view.TextFieldView;
import com.cpz.processing.controls.controls.textfield.viewmodel.TextFieldViewModel;
import com.cpz.processing.controls.core.focus.FocusManager;

public final class TextFieldInputAdapter {
   private final TextFieldView view;
   private final TextFieldViewModel viewModel;
   private final FocusManager focusManager;

   public TextFieldInputAdapter(TextFieldView var1, TextFieldViewModel var2, FocusManager var3) {
      this.view = var1;
      this.viewModel = var2;
      this.focusManager = var3;
      this.focusManager.register(var2);
   }

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

   public void handleMouseDrag(float var1, float var2) {
      if (this.viewModel.isVisible() && this.viewModel.isEnabled()) {
         this.view.handleMouseDrag(var1);
      }
   }

   public void handleMouseRelease() {
      this.view.handleMouseRelease();
   }
}
