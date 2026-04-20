package com.cpz.processing.controls.controls.checkbox.model;

import com.cpz.processing.controls.core.model.Enableable;
import com.cpz.processing.controls.core.util.ControlCode;

import java.util.Objects;

/**
 * Model for checkbox model.
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
public final class CheckboxModel implements Enableable {
   private final String code;
   private boolean checked;
   private boolean enabled = true;

   /**
    * Creates a checkbox model.
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public CheckboxModel() {
      this(ControlCode.auto("checkbox"), false);
   }

   /**
    * Creates a checkbox model.
    *
    * @param enabled parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public CheckboxModel(boolean enabled) {
      this(ControlCode.auto("checkbox"), enabled);
   }

   public CheckboxModel(String text, boolean enabled) {
      this.code = Objects.requireNonNull(text, "code");
      this.checked = enabled;
   }

   public String getCode() {
      return this.code;
   }

   /**
    * Returns whether checked.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isChecked() {
      return this.checked;
   }

   /**
    * Updates checked.
    *
    * @param enabled new checked
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setChecked(boolean enabled) {
      this.checked = enabled;
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
