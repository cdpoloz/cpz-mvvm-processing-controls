package com.cpz.processing.controls.controls.radiogroup.model;

import com.cpz.processing.controls.core.model.Enableable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model for radio group model.
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
public final class RadioGroupModel implements Enableable {
   private List<String> options;
   private int selectedIndex;
   private boolean enabled;

   /**
    * Creates a radio group model.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public RadioGroupModel(List<String> var1) {
      this(var1, -1);
   }

   /**
    * Creates a radio group model.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public RadioGroupModel(List<String> var1, int var2) {
      this.enabled = true;
      this.options = this.sanitizeOptions(var1);
      this.selectedIndex = this.normalizeIndex(var2, this.options.size());
   }

   /**
    * Returns options.
    *
    * @return current options
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public List<String> getOptions() {
      return Collections.unmodifiableList(this.options);
   }

   /**
    * Updates options.
    *
    * @param var1 new options
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setOptions(List<String> var1) {
      this.options = this.sanitizeOptions(var1);
      this.selectedIndex = this.normalizeIndex(this.selectedIndex, this.options.size());
   }

   /**
    * Returns selected index.
    *
    * @return current selected index
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getSelectedIndex() {
      return this.selectedIndex;
   }

   /**
    * Updates selected index.
    *
    * @param var1 new selected index
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setSelectedIndex(int var1) {
      this.selectedIndex = this.normalizeIndex(var1, this.options.size());
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

   private List<String> sanitizeOptions(List<String> var1) {
      if (var1 != null && !var1.isEmpty()) {
         ArrayList<String> var2 = new ArrayList<>(var1.size());

         for(String var4 : var1) {
            var2.add(var4 == null ? "" : var4);
         }

         return Collections.unmodifiableList(var2);
      } else {
         return List.of();
      }
   }

   private int normalizeIndex(int var1, int var2) {
      if (var2 <= 0) {
         return -1;
      } else {
         return var1 >= -1 && var1 < var2 ? var1 : -1;
      }
   }
}
