package com.cpz.processing.controls.core.overlay.tooltip.model;

import com.cpz.processing.controls.core.model.Enableable;

public final class TooltipModel implements Enableable {
   private String text;
   private boolean enabled = true;

   public TooltipModel(String var1) {
      this.text = var1 == null ? "" : var1;
   }

   public String getText() {
      return this.text;
   }

   public void setText(String var1) {
      this.text = var1 == null ? "" : var1;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }
}
