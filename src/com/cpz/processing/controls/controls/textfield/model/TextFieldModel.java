package com.cpz.processing.controls.controls.textfield.model;

import com.cpz.processing.controls.core.model.Enableable;
import com.cpz.processing.controls.core.util.ControlCode;

import java.util.Objects;

/**
 * Model for text field model.
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
public final class TextFieldModel implements Enableable {
   private final String code;
   private String text = "";
   private boolean enabled = true;

   public TextFieldModel() {
      this(ControlCode.auto("textfield"));
   }

   public TextFieldModel(String var1) {
      this.code = Objects.requireNonNull(var1, "code");
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
      if (var1 == null) {
         throw new IllegalArgumentException("text must not be null");
      } else {
         this.text = var1;
      }
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
