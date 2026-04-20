package com.cpz.processing.controls.controls.radiogroup.model;

import com.cpz.processing.controls.core.model.Enableable;
import com.cpz.processing.controls.core.util.ControlCode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
 *
 * @author CPZ
 */
public final class RadioGroupModel implements Enableable {
   private final String code;
   private List<String> options;
   private int selectedIndex;
   private boolean enabled;

   /**
    * Creates a radio group model.
    *
    * @param list parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public RadioGroupModel(List<String> list) {
      this(ControlCode.auto("radiogroup"), list, -1);
   }

   /**
    * Creates a radio group model.
    *
    * @param list parameter used by this operation
    * @param value parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public RadioGroupModel(List<String> list, int value) {
      this(ControlCode.auto("radiogroup"), list, value);
   }

   public RadioGroupModel(String text, List<String> list) {
      this(text, list, -1);
   }

   public RadioGroupModel(String text, List<String> list, int value) {
      this.code = Objects.requireNonNull(text, "code");
      this.enabled = true;
      this.options = this.sanitizeOptions(list);
      this.selectedIndex = this.normalizeIndex(value, this.options.size());
   }

   public String getCode() {
      return this.code;
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
    * @param list new options
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setOptions(List<String> list) {
      this.options = this.sanitizeOptions(list);
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
    * @param index new selected index
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setSelectedIndex(int index) {
      this.selectedIndex = this.normalizeIndex(index, this.options.size());
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

   private List<String> sanitizeOptions(List<String> list) {
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

   private int normalizeIndex(int zIndex, int zIndex2) {
      if (zIndex2 <= 0) {
         return -1;
      } else {
         return zIndex >= -1 && zIndex < zIndex2 ? zIndex : -1;
      }
   }
}
