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
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public CheckboxModel(boolean var1) {
      this(ControlCode.auto("checkbox"), var1);
   }

   public CheckboxModel(String var1, boolean var2) {
      this.code = Objects.requireNonNull(var1, "code");
      this.checked = var2;
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
    * @param var1 new checked
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setChecked(boolean var1) {
      this.checked = var1;
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
