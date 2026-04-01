package com.cpz.processing.controls.controls.button.model;

import com.cpz.processing.controls.core.model.Enableable;

public final class ButtonModel implements Enableable {
   private String text;
   private boolean enabled;

   public ButtonModel(String var1) {
      this.text = this.normalizeText(var1);
      this.enabled = true;
   }

   public String getText() {
      return this.text;
   }

   public void setText(String var1) {
      this.text = this.normalizeText(var1);
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   private String normalizeText(String var1) {
      return var1 == null ? "" : var1;
   }
}
