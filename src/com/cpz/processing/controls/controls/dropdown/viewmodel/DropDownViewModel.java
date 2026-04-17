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
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DropDownViewModel(DropDownModel var1) {
      super(var1);
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
    * @param var1 new items
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setItems(List<String> var1) {
      int var2 = ((DropDownModel)this.model).getSelectedIndex();
      ((DropDownModel)this.model).setItems(var1);
      if (((DropDownModel)this.model).getItems().isEmpty()) {
         this.close();
      }

      this.notifySelectionIfChanged(var2);
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
      int var1 = ((DropDownModel)this.model).getSelectedIndex();
      List var2 = ((DropDownModel)this.model).getItems();
      return var1 >= 0 && var1 < var2.size() ? (String)var2.get(var1) : "";
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
    * @param var1 new hovered
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setHovered(boolean var1) {
      this.hovered = var1 && this.isInteractive();
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
    * @param var1 new pressed
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setPressed(boolean var1) {
      this.pressed = var1 && this.isInteractive();
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
    * @param var1 new focused
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setFocused(boolean var1) {
      this.focused = var1 && this.isInteractive();
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
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void selectIndex(int var1) {
      int var2 = ((DropDownModel)this.model).getSelectedIndex();
      ((DropDownModel)this.model).setSelectedIndex(var1);
      this.notifySelectionIfChanged(var2);
   }

   public void setOnSelectionChanged(Consumer<Integer> var1) {
      this.onSelectionChanged = var1;
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

   private void notifySelectionIfChanged(int var1) {
      int var2 = ((DropDownModel)this.model).getSelectedIndex();
      if (var1 != var2 && this.onSelectionChanged != null) {
         this.onSelectionChanged.accept(var2);
      }
   }
}
