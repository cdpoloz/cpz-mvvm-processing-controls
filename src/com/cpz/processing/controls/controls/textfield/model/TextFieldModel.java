package com.cpz.processing.controls.controls.textfield.model;

import com.cpz.processing.controls.core.model.Enableable;

public final class TextFieldModel implements Enableable {
   private String text = "";
   private boolean enabled = true;

   public String getText() {
      return this.text;
   }

   public void setText(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("text must not be null");
      } else {
         this.text = var1;
      }
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }
}
