package com.cpz.processing.controls.controls.label.model;

import com.cpz.processing.controls.core.model.Enableable;

public final class LabelModel implements Enableable {
   private String text = "";
   private boolean enabled = true;

   public void setText(String var1) {
      this.text = var1 != null ? var1 : "";
   }

   public String getText() {
      return this.text;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }
}
