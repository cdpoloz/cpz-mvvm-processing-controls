package com.cpz.processing.controls.controls.button.model;

import com.cpz.processing.controls.core.util.ControlCode;
import com.cpz.processing.controls.core.model.Enableable;

import java.util.Objects;

/**
 * Model for button model.
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
 */
public final class ButtonModel implements Enableable {
   private final String code;

   private String text;
   private boolean enabled;

   /**
    * Creates a button model.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public ButtonModel(String var1) {
      this(ControlCode.auto("button"), var1);
   }

   public ButtonModel(String var1, String var2) {
      this.code = Objects.requireNonNull(var1, "code");
      this.text = this.normalizeText(var2);
      this.enabled = true;
   }

   public String getCode() {
      return this.code;
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
    * @param var1 new text
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setText(String var1) {
      this.text = this.normalizeText(var1);
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
    * @param var1 new enabled
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   private String normalizeText(String var1) {
      return var1 == null ? "" : var1;
   }
}
