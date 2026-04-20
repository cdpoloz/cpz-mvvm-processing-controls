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
 *
 * @author CPZ
 */
public final class DropDownModel implements Enableable {
   private final String code;
   private List<String> items;
   private int selectedIndex;
   private boolean enabled;

   /**
    * Creates a drop down model.
    *
    * @param list parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DropDownModel(List<String> list) {
      this(ControlCode.auto("dropdown"), list, -1);
   }

   /**
    * Creates a drop down model.
    *
    * @param list parameter used by this operation
    * @param value parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DropDownModel(List<String> list, int value) {
      this(ControlCode.auto("dropdown"), list, value);
   }

   public DropDownModel(String text, List<String> list) {
      this(text, list, -1);
   }

   public DropDownModel(String text, List<String> list, int value) {
      this.code = Objects.requireNonNull(text, "code");
      this.items = sanitizeItems(list);
      this.selectedIndex = normalizeSelectedIndex(value, this.items.size());
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
    * @param list new items
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setItems(List<String> list) {
      this.items = sanitizeItems(list);
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
    * @param index new selected index
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setSelectedIndex(int index) {
      this.selectedIndex = normalizeSelectedIndex(index, this.items.size());
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

   private static List<String> sanitizeItems(List<String> list) {
      if (list != null && !list.isEmpty()) {
         ArrayList<String> arrayList = new ArrayList<>(list.size());

         for(String text : list) {
            arrayList.add(text == null ? "" : text);
         }

         return Collections.unmodifiableList(arrayList);
      } else {
         return List.of();
      }
   }

   private static int normalizeSelectedIndex(int zIndex, int zIndex2) {
      if (zIndex2 <= 0) {
         return -1;
      } else {
         return zIndex < 0 ? -1 : Math.min(zIndex, zIndex2 - 1);
      }
   }
}
