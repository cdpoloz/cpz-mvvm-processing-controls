package com.cpz.processing.controls.controls.checkbox.model;

import com.cpz.processing.controls.core.model.Enableable;

public final class CheckboxModel implements Enableable {
   private boolean checked;
   private boolean enabled = true;

   public CheckboxModel() {
   }

   public CheckboxModel(boolean var1) {
      this.checked = var1;
   }

   public boolean isChecked() {
      return this.checked;
   }

   public void setChecked(boolean var1) {
      this.checked = var1;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }
}
