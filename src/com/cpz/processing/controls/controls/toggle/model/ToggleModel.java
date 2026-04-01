package com.cpz.processing.controls.controls.toggle.model;

import com.cpz.processing.controls.core.model.Enableable;

public final class ToggleModel implements Enableable {
   private int state;
   private int prevState;
   private int totalStates = 2;
   private boolean enabled = true;

   public void setTotalStates(int var1) {
      this.totalStates = var1;
   }

   public int getState() {
      return this.state;
   }

   public void setState(int var1) {
      this.state = var1;
   }

   public int getPrevState() {
      return this.prevState;
   }

   public void setPrevState(int var1) {
      this.prevState = var1;
   }

   public int getTotalStates() {
      return this.totalStates;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }
}
