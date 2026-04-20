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
 *
 * @author CPZ
 */
public final class ButtonModel implements Enableable {
   private final String code;

   private String text;
   private boolean enabled;

   /**
    * Creates a button model.
    *
    * @param text parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public ButtonModel(String text) {
      this(ControlCode.auto("button"), text);
   }

   public ButtonModel(String text, String text2) {
      this.code = Objects.requireNonNull(text, "code");
      this.text = this.normalizeText(text2);
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
    * @param text new text
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setText(String text) {
      this.text = this.normalizeText(text);
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

   private String normalizeText(String text) {
      return text == null ? "" : text;
   }
}
