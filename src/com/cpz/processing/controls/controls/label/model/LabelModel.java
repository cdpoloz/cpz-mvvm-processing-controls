package com.cpz.processing.controls.controls.label.model;

import com.cpz.processing.controls.core.model.Enableable;
import com.cpz.processing.controls.core.util.ControlCode;

import java.util.Objects;

/**
 * Model for label model.
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
public final class LabelModel implements Enableable {
   private final String code;
   private String text = "";
   private boolean enabled = true;

   public LabelModel() {
      this(ControlCode.auto("label"));
   }

   public LabelModel(String var1) {
      this.code = Objects.requireNonNull(var1, "code");
   }

   public String getCode() {
      return this.code;
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
      this.text = var1 != null ? var1 : "";
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
}
