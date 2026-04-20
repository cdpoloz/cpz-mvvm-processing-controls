package com.cpz.processing.controls.controls.dropdown.viewmodel;

import com.cpz.processing.controls.controls.dropdown.model.DropDownModel;
import com.cpz.processing.controls.core.focus.Focusable;
import com.cpz.processing.controls.core.viewmodel.AbstractControlViewModel;
import java.util.List;
import java.util.function.Consumer;

/**
 * ViewModel for drop down view model.
 *
 * Responsibilities:
 * - Expose control state to the view layer.
 * - Coordinate interaction and synchronize with the backing model.
 *
 * Behavior:
 * - Does not perform drawing directly.
 *
 * Notes:
 * - This type belongs to the MVVM ViewModel layer.
 *
 * @author CPZ
 */
public final class DropDownViewModel extends AbstractControlViewModel implements Focusable {
   private boolean hovered;
   private boolean pressed;
   private boolean focused;
   private boolean expanded;
   private Consumer<Integer> onSelectionChanged;

   /**
    * Creates a drop down view model.
    *
    * @param model parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DropDownViewModel(DropDownModel model) {
      super(model);
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
      return ((DropDownModel)this.model).getItems();
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
      int value = ((DropDownModel)this.model).getSelectedIndex();
      ((DropDownModel)this.model).setItems(list);
      if (((DropDownModel)this.model).getItems().isEmpty()) {
         this.close();
      }

      this.notifySelectionIfChanged(value);
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
      return ((DropDownModel)this.model).getSelectedIndex();
   }

   /**
    * Returns selected text.
    *
    * @return current selected text
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public String getSelectedText() {
      int index2 = ((DropDownModel)this.model).getSelectedIndex();
      List list2 = ((DropDownModel)this.model).getItems();
      return index2 >= 0 && index2 < list2.size() ? (String)list2.get(index2) : "";
   }

   /**
    * Returns whether hovered.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isHovered() {
      return this.hovered;
   }

   /**
    * Updates hovered.
    *
    * @param inside new hovered
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setHovered(boolean inside) {
      this.hovered = inside && this.isInteractive();
   }

   /**
    * Returns whether pressed.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isPressed() {
      return this.pressed;
   }

   /**
    * Updates pressed.
    *
    * @param pressed new pressed
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setPressed(boolean pressed) {
      this.pressed = pressed && this.isInteractive();
   }

   /**
    * Returns whether focused.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isFocused() {
      return this.focused;
   }

   /**
    * Updates focused.
    *
    * @param focused new focused
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setFocused(boolean focused) {
      this.focused = focused && this.isInteractive();
      if (!this.focused) {
         this.close();
      }

   }

   /**
    * Returns whether expanded.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isExpanded() {
      return this.expanded;
   }

   public void open() {
      if (this.isInteractive() && !((DropDownModel)this.model).getItems().isEmpty()) {
         this.expanded = true;
      } else {
         this.expanded = false;
      }
   }

   /**
    * Performs toggle expanded.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void toggleExpanded() {
      if (this.expanded) {
         this.close();
      } else {
         this.open();
      }
   }

   /**
    * Performs close.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void close() {
      this.expanded = false;
      this.pressed = false;
   }

   /**
    * Performs select index.
    *
    * @param index parameter used by this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void selectIndex(int index) {
      int value = ((DropDownModel)this.model).getSelectedIndex();
      ((DropDownModel)this.model).setSelectedIndex(index);
      this.notifySelectionIfChanged(value);
   }

   public void setOnSelectionChanged(Consumer<Integer> consumer) {
      this.onSelectionChanged = consumer;
   }

   /**
    * Handles focus gained.
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onFocusGained() {
      this.focused = this.isInteractive();
   }

   /**
    * Handles focus lost.
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onFocusLost() {
      this.focused = false;
      this.close();
   }

   protected void onAvailabilityChanged() {
      if (!this.isInteractive()) {
         this.hovered = false;
         this.pressed = false;
         this.focused = false;
         this.expanded = false;
      }

   }

   private boolean isInteractive() {
      return this.isVisible() && this.isEnabled();
   }

   private void notifySelectionIfChanged(int value) {
      int value2 = ((DropDownModel)this.model).getSelectedIndex();
      if (value != value2 && this.onSelectionChanged != null) {
         this.onSelectionChanged.accept(value2);
      }
   }
}
