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
 *
 * @author CPZ
 */
public class LayoutConfig {
   private float normalizedX;
   private float normalizedY;
   private Anchor anchor;

   /**
    * Creates a layout config.
    *
    * @param x parameter used by this operation
    * @param y parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public LayoutConfig(float x, float y) {
      this.anchor = Anchor.TOP_LEFT;
      this.normalizedX = x;
      this.normalizedY = y;
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
    * @param anchor new anchor
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setAnchor(Anchor anchor) {
      this.anchor = anchor == null ? Anchor.TOP_LEFT : anchor;
   }
}
