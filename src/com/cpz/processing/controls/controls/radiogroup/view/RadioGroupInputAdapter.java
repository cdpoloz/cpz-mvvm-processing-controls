package com.cpz.processing.controls.controls.radiogroup.view;

import com.cpz.processing.controls.controls.radiogroup.viewmodel.RadioGroupViewModel;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.input.PointerInputAdapter;

public final class RadioGroupInputAdapter {
   private final RadioGroupView view;
   private final RadioGroupViewModel viewModel;
   private final FocusManager focusManager;
   private final PointerInputAdapter delegate;

   public RadioGroupInputAdapter(RadioGroupView var1, RadioGroupViewModel var2, FocusManager var3) {
      this.view = var1;
      this.viewModel = var2;
      this.focusManager = var3;
      this.delegate = new PointerInputAdapter(var1, var2);
      this.focusManager.register(var2);
   }

   public void handleMouseMove(float var1, float var2) {
      this.delegate.handleMouseMove(var1, var2);
      this.viewModel.setHoveredIndex(this.view.getOptionIndexAt(var1, var2));
   }

   public void handleMousePress(float var1, float var2) {
      this.delegate.handleMousePress(var1, var2);
      int var3 = this.view.getOptionIndexAt(var1, var2);
      this.viewModel.onOptionPressed(var3);
      this.viewModel.setHoveredIndex(var3);
      if (var3 >= 0) {
         this.focusManager.requestFocus(this.viewModel);
      }

   }

   public void handleMouseRelease(float var1, float var2) {
      this.delegate.handleMouseRelease(var1, var2);
      int var3 = this.view.getOptionIndexAt(var1, var2);
      this.viewModel.onOptionReleased(var3);
      this.viewModel.setHoveredIndex(var3);
   }
}
