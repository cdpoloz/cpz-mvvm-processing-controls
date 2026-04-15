package com.cpz.processing.controls.controls.dropdown.model;

import com.cpz.processing.controls.core.model.Enableable;
import com.cpz.processing.controls.core.util.ControlCode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Model for drop down model.
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
public final class DropDownModel implements Enableable {
   private final String code;
   private List<String> items;
   private int selectedIndex;
   private boolean enabled;

   /**
    * Creates a drop down model.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DropDownModel(List<String> var1) {
      this(ControlCode.auto("dropdown"), var1, -1);
   }

   /**
    * Creates a drop down model.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DropDownModel(List<String> var1, int var2) {
      this(ControlCode.auto("dropdown"), var1, var2);
   }

   public DropDownModel(String var1, List<String> var2) {
      this(var1, var2, -1);
   }

   public DropDownModel(String var1, List<String> var2, int var3) {
      this.code = Objects.requireNonNull(var1, "code");
      this.items = sanitizeItems(var2);
      this.selectedIndex = normalizeSelectedIndex(var3, this.items.size());
      this.enabled = true;
   }

   public String getCode() {
      return this.code;
   }

   /**
    * Returns items.
    *
    * @return current items
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public List<String> getItems() {
      return Collections.unmodifiableList(this.items);
   }

   /**
    * Updates items.
    *
    * @param var1 new items
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setItems(List<String> var1) {
      this.items = sanitizeItems(var1);
      this.selectedIndex = normalizeSelectedIndex(this.selectedIndex, this.items.size());
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
      this.selectedIndex = normalizeSelectedIndex(var1, this.items.size());
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

   private static List<String> sanitizeItems(List<String> var0) {
      if (var0 != null && !var0.isEmpty()) {
         ArrayList<String> var1 = new ArrayList<>(var0.size());

         for(String var3 : var0) {
            var1.add(var3 == null ? "" : var3);
         }

         return Collections.unmodifiableList(var1);
      } else {
         return List.of();
      }
   }

   private static int normalizeSelectedIndex(int var0, int var1) {
      if (var1 <= 0) {
         return -1;
      } else {
         return var0 < 0 ? -1 : Math.min(var0, var1 - 1);
      }
   }
}
