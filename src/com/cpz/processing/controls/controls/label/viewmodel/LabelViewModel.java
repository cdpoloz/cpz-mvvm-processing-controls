package com.cpz.processing.controls.controls.label.viewmodel;

import com.cpz.processing.controls.controls.label.model.LabelModel;
import com.cpz.processing.controls.core.viewmodel.AbstractControlViewModel;

public final class LabelViewModel extends AbstractControlViewModel {
   public LabelViewModel(LabelModel var1) {
      super(var1);
   }

   public String getText() {
      return ((LabelModel)this.model).getText();
   }

   public void setText(String var1) {
      ((LabelModel)this.model).setText(var1);
   }
}
