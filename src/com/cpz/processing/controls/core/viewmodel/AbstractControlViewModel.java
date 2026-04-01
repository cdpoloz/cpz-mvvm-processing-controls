package com.cpz.processing.controls.core.viewmodel;

import com.cpz.processing.controls.core.model.Enableable;
import com.cpz.processing.controls.core.view.Visible;

public abstract class AbstractControlViewModel implements Visible, Enableable {
   protected final Enableable model;
   private boolean visible = true;

   protected AbstractControlViewModel(Enableable var1) {
      this.model = var1;
   }

   public final boolean isVisible() {
      return this.visible;
   }

   public final void setVisible(boolean var1) {
      this.visible = var1;
      this.onAvailabilityChanged();
   }

   public boolean isEnabled() {
      return this.model.isEnabled();
   }

   public void setEnabled(boolean var1) {
      this.model.setEnabled(var1);
      this.onAvailabilityChanged();
   }

   protected void onAvailabilityChanged() {
   }
}
