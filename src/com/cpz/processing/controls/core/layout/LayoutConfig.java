package com.cpz.processing.controls.core.layout;

/**
 * Layout component for layout config.
 *
 * Responsibilities:
 * - Represent layout data or coordinate resolution.
 * - Keep placement logic outside control behavior.
 *
 * Behavior:
 * - Keeps the public role isolated from unrelated concerns.
 *
 * Notes:
 * - This type is part of the public project surface.
 */
public class LayoutConfig {
   private float normalizedX;
   private float normalizedY;
   private Anchor anchor;

   /**
    * Creates a layout config.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public LayoutConfig(float var1, float var2) {
      this.anchor = Anchor.TOP_LEFT;
      this.normalizedX = var1;
      this.normalizedY = var2;
   }

   /**
    * Returns normalized x.
    *
    * @return current normalized x
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getNormalizedX() {
      return this.normalizedX;
   }

   /**
    * Returns normalized y.
    *
    * @return current normalized y
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getNormalizedY() {
      return this.normalizedY;
   }

   /**
    * Returns anchor.
    *
    * @return current anchor
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public Anchor getAnchor() {
      return this.anchor;
   }

   /**
    * Updates anchor.
    *
    * @param var1 new anchor
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setAnchor(Anchor var1) {
      this.anchor = var1 == null ? Anchor.TOP_LEFT : var1;
   }
}
