package com.cpz.processing.controls.controls.toggle.viewmodel;

import com.cpz.processing.controls.controls.toggle.model.ToggleModel;
import com.cpz.processing.controls.core.viewmodel.AbstractInteractiveControlViewModel;

public class ToggleViewModel extends AbstractInteractiveControlViewModel {
   public ToggleViewModel(ToggleModel var1) {
      super(var1);
      this.setTotalStates(var1.getTotalStates());
   }

   public int getState() {
      return ((ToggleModel)this.model).getState();
   }

   public int getPrevState() {
      return ((ToggleModel)this.model).getPrevState();
   }

   public boolean hasChanged() {
      return ((ToggleModel)this.model).getState() != ((ToggleModel)this.model).getPrevState();
   }

   public boolean isFirstState() {
      return ((ToggleModel)this.model).getState() == 0;
   }

   public void setTotalStates(int var1) {
      int var2 = Math.max(1, var1);
      ((ToggleModel)this.model).setTotalStates(var2);
      this.clampState(var2);
   }

   public int getTotalStates() {
      return ((ToggleModel)this.model).getTotalStates();
   }

   protected void activate() {
      int var1 = Math.max(1, ((ToggleModel)this.model).getTotalStates());
      int var2 = ((ToggleModel)this.model).getState();
      ((ToggleModel)this.model).setPrevState(var2);
      ((ToggleModel)this.model).setState((var2 + 1) % var1);
   }

   private void clampState(int var1) {
      if (((ToggleModel)this.model).getState() >= var1) {
         ((ToggleModel)this.model).setPrevState(((ToggleModel)this.model).getState());
         ((ToggleModel)this.model).setState(var1 - 1);
      }

   }
}
