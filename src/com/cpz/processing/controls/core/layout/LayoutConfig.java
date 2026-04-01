package com.cpz.processing.controls.core.layout;

public class LayoutConfig {
   private float normalizedX;
   private float normalizedY;
   private Anchor anchor;

   public LayoutConfig(float var1, float var2) {
      this.anchor = Anchor.TOP_LEFT;
      this.normalizedX = var1;
      this.normalizedY = var2;
   }

   public float getNormalizedX() {
      return this.normalizedX;
   }

   public float getNormalizedY() {
      return this.normalizedY;
   }

   public Anchor getAnchor() {
      return this.anchor;
   }

   public void setAnchor(Anchor var1) {
      this.anchor = var1 == null ? Anchor.TOP_LEFT : var1;
   }
}
