package com.cpz.processing.controls.controls.button.viewmodel;

import com.cpz.processing.controls.controls.button.model.ButtonModel;
import com.cpz.processing.controls.controls.button.util.ButtonListener;
import com.cpz.processing.controls.core.viewmodel.AbstractInteractiveControlViewModel;
import java.util.Objects;

public final class ButtonViewModel extends AbstractInteractiveControlViewModel {
   private ButtonListener clickListener;
   private boolean showText = true;

   public ButtonViewModel(ButtonModel var1) {
      super(var1);
   }

   public void setClickListener(ButtonListener var1) {
      this.clickListener = var1;
   }

   public String getText() {
      return ((ButtonModel)this.model).getText();
   }

   public void setText(String var1) {
      ((ButtonModel)this.model).setText(var1);
   }

   public boolean isShowText() {
      return this.showText;
   }

   public void setShowText(boolean var1) {
      this.showText = var1;
   }

   /** @deprecated */
   @Deprecated
   public void setOnClick(Runnable var1) {
      if (var1 == null) {
         this.clickListener = null;
      } else {
         Objects.requireNonNull(var1);
         this.clickListener = var1::run;
      }

   }

   protected void activate() {
      if (this.clickListener != null) {
         this.clickListener.onClick();
      }

   }
}
