package com.cpz.processing.controls.controls.switchcontrol.viewmodel;

import com.cpz.processing.controls.controls.switchcontrol.model.SwitchModel;
import com.cpz.processing.controls.core.viewmodel.AbstractInteractiveControlViewModel;

public class SwitchViewModel extends AbstractInteractiveControlViewModel {
   public SwitchViewModel(SwitchModel var1) {
      super(var1);
      this.setTotalStates(var1.getTotalStates());
   }

   public int getState() {
      return ((SwitchModel)this.model).getState();
   }

   public int getPrevState() {
      return ((SwitchModel)this.model).getPrevState();
   }

   public boolean hasChanged() {
      return ((SwitchModel)this.model).getState() != ((SwitchModel)this.model).getPrevState();
   }

   public boolean isFirstState() {
      return ((SwitchModel)this.model).getState() == 0;
   }

   public void setTotalStates(int var1) {
      int var2 = Math.max(1, var1);
      ((SwitchModel)this.model).setTotalStates(var2);
      this.clampState(var2);
   }

   public int getTotalStates() {
      return ((SwitchModel)this.model).getTotalStates();
   }

   protected void activate() {
      int var1 = Math.max(1, ((SwitchModel)this.model).getTotalStates());
      int var2 = ((SwitchModel)this.model).getState();
      ((SwitchModel)this.model).setPrevState(var2);
      ((SwitchModel)this.model).setState((var2 + 1) % var1);
   }

   private void clampState(int var1) {
      if (((SwitchModel)this.model).getState() >= var1) {
         ((SwitchModel)this.model).setPrevState(((SwitchModel)this.model).getState());
         ((SwitchModel)this.model).setState(var1 - 1);
      }

   }
}
