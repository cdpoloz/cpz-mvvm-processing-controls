package com.cpz.processing.controls.controls.checkbox.viewmodel;

import com.cpz.processing.controls.controls.checkbox.model.CheckboxModel;
import com.cpz.processing.controls.core.viewmodel.AbstractInteractiveControlViewModel;

public final class CheckboxViewModel extends AbstractInteractiveControlViewModel {
   public CheckboxViewModel(CheckboxModel var1) {
      super(var1);
   }

   public boolean isChecked() {
      return ((CheckboxModel)this.model).isChecked();
   }

   public void setChecked(boolean var1) {
      ((CheckboxModel)this.model).setChecked(var1);
   }

   protected void activate() {
      ((CheckboxModel)this.model).setChecked(!((CheckboxModel)this.model).isChecked());
   }
}
