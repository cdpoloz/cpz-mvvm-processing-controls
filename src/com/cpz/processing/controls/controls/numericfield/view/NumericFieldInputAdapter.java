package com.cpz.processing.controls.controls.numericfield.view;

import com.cpz.processing.controls.controls.numericfield.viewmodel.NumericFieldViewModel;
import com.cpz.processing.controls.core.focus.FocusManager;

public final class NumericFieldInputAdapter {
   private final NumericFieldView view;
   private final NumericFieldViewModel viewModel;
   private final FocusManager focusManager;

   public NumericFieldInputAdapter(NumericFieldView var1, NumericFieldViewModel var2, FocusManager var3) {
      this.view = var1;
      this.viewModel = var2;
      this.focusManager = var3;
      this.focusManager.register(var2);
   }

   public void handleMouseMove(float var1, float var2) {
      this.viewModel.onPointerMove(this.viewModel.isVisible() && this.view.contains(var1, var2));
   }

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

   public void handleMouseDrag(float var1, float var2) {
      if (this.viewModel.isVisible() && this.viewModel.isEnabled()) {
         this.viewModel.onPointerMove(this.view.contains(var1, var2));
         this.view.handleMouseDrag(var1);
      }
   }

   public void handleMouseRelease(float var1, float var2) {
      this.view.handleMouseRelease();
      this.viewModel.onPointerRelease(this.view.contains(var1, var2));
   }

   public void handleMouseWheel(float var1, boolean var2, boolean var3) {
      this.viewModel.onMouseWheel(var1, var2, var3);
   }
}
