package com.cpz.processing.controls.core.overlay.tooltip.model;

import com.cpz.processing.controls.core.model.Enableable;

/**
 * Model for tooltip model.
 *
 * Responsibilities:
 * - Store durable control state.
 * - Remain independent from rendering and input code.
 *
 * Behavior:
 * - Keeps state mutation independent from rendering concerns.
 *
 * Notes:
 * - This type belongs to the MVVM Model layer.
 *
 * @author CPZ
 */
public final class TooltipModel implements Enableable {
   private String text;
   private boolean enabled = true;

   /**
    * Creates a tooltip model.
    *
    * @param text parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public TooltipModel(String text) {
      this.text = text == null ? "" : text;
   }

   /**
    * Returns text.
    *
    * @return current text
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public String getText() {
      return this.text;
   }

   /**
    * Updates text.
    *
    * @param text new text
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setText(String text) {
      this.text = text == null ? "" : text;
   }

   /**
    * Returns whether enabled.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isEnabled() {
      return this.enabled;
   }

   /**
    * Updates enabled.
    *
    * @param enabled new enabled
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }
}
