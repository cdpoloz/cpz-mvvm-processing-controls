package com.cpz.processing.controls.core.overlay.tooltip.viewmodel;

import com.cpz.processing.controls.core.overlay.tooltip.model.TooltipModel;
import com.cpz.processing.controls.core.viewmodel.AbstractControlViewModel;

public final class TooltipViewModel extends AbstractControlViewModel {
   public TooltipViewModel(TooltipModel var1) {
      super(var1);
   }

   public String getText() {
      return ((TooltipModel)this.model).getText();
   }

   public void setText(String var1) {
      ((TooltipModel)this.model).setText(var1);
   }
}
