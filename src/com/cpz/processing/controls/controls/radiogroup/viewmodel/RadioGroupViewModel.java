package com.cpz.processing.controls.controls.radiogroup.viewmodel;

import com.cpz.processing.controls.controls.radiogroup.model.RadioGroupModel;
import com.cpz.processing.controls.core.input.KeyboardInputTarget;
import com.cpz.processing.controls.core.input.PointerInputTarget;
import com.cpz.processing.controls.core.viewmodel.AbstractControlViewModel;
import java.util.List;
import java.util.function.Consumer;

/**
 * ViewModel for radio group view model.
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
public final class RadioGroupViewModel extends AbstractControlViewModel implements PointerInputTarget, KeyboardInputTarget {
   private int hoveredIndex = -1;
   private int pressedIndex = -1;
   private boolean focused;
   private int activeIndex = -1;
   private Consumer onOptionSelected;

   /**
    * Creates a radio group view model.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public RadioGroupViewModel(RadioGroupModel var1) {
      super(var1);
      this.syncActiveIndex();
   }

   /**
    * Returns options.
    *
    * @return current options
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public List getOptions() {
      return ((RadioGroupModel)this.model).getOptions();
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
      return ((RadioGroupModel)this.model).getSelectedIndex();
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
      if (this.isInteractive() && var1 >= 0 && var1 < ((RadioGroupModel)this.model).getOptions().size()) {
         int var2 = ((RadioGroupModel)this.model).getSelectedIndex();
         ((RadioGroupModel)this.model).setSelectedIndex(var1);
         this.activeIndex = var1;
         if (var2 != ((RadioGroupModel)this.model).getSelectedIndex() && this.onOptionSelected != null) {
            this.onOptionSelected.accept(((RadioGroupModel)this.model).getSelectedIndex());
         }

      }
   }

   /**
    * Returns hovered index.
    *
    * @return current hovered index
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getHoveredIndex() {
      return this.hoveredIndex;
   }

   /**
    * Updates hovered index.
    *
    * @param var1 new hovered index
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setHoveredIndex(int var1) {
      this.hoveredIndex = this.isInteractive() ? this.normalizeOptionIndex(var1) : -1;
   }

   /**
    * Returns pressed index.
    *
    * @return current pressed index
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getPressedIndex() {
      return this.pressedIndex;
   }

   /**
    * Updates pressed index.
    *
    * @param var1 new pressed index
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setPressedIndex(int var1) {
      this.pressedIndex = this.isInteractive() ? this.normalizeOptionIndex(var1) : -1;
   }

   /**
    * Handles option pressed.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onOptionPressed(int var1) {
      if (!this.isInteractive()) {
         this.pressedIndex = -1;
      } else {
         this.pressedIndex = this.normalizeOptionIndex(var1);
      }
   }

   /**
    * Handles option released.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onOptionReleased(int var1) {
      if (!this.isInteractive()) {
         this.pressedIndex = -1;
      } else {
         int var2 = this.normalizeOptionIndex(var1);
         if (var2 >= 0 && var2 == this.pressedIndex) {
            this.setSelectedIndex(var2);
         }

         this.pressedIndex = -1;
      }
   }

   /**
    * Updates on option selected.
    *
    * @param var1 new on option selected
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setOnOptionSelected(Consumer var1) {
      this.onOptionSelected = var1;
   }

   /**
    * Returns active index.
    *
    * @return current active index
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getActiveIndex() {
      return this.activeIndex;
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
         this.pressedIndex = -1;
      } else {
         this.syncActiveIndex();
      }

   }

   /**
    * Handles focus gained.
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onFocusGained() {
      this.focused = this.isInteractive();
      this.syncActiveIndex();
   }

   /**
    * Handles focus lost.
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onFocusLost() {
      this.focused = false;
      this.pressedIndex = -1;
   }

   /**
    * Handles pointer move.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onPointerMove(boolean var1) {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      } else {
         if (!var1) {
            this.hoveredIndex = -1;
         }

      }
   }

   /**
    * Handles pointer press.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onPointerPress(boolean var1) {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      } else {
         if (!var1) {
            this.pressedIndex = -1;
         }

      }
   }

   /**
    * Handles pointer release.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onPointerRelease(boolean var1) {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      } else {
         if (!var1) {
            this.pressedIndex = -1;
         }

      }
   }

   /**
    * Performs navigate previous.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void navigatePrevious() {
      if (this.focused && !((RadioGroupModel)this.model).getOptions().isEmpty()) {
         this.activeIndex = this.activeIndex < 0 ? ((RadioGroupModel)this.model).getOptions().size() - 1 : Math.max(0, this.activeIndex - 1);
         this.hoveredIndex = -1;
      }
   }

   /**
    * Performs navigate next.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void navigateNext() {
      if (this.focused && !((RadioGroupModel)this.model).getOptions().isEmpty()) {
         this.activeIndex = this.activeIndex < 0 ? 0 : Math.min(((RadioGroupModel)this.model).getOptions().size() - 1, this.activeIndex + 1);
         this.hoveredIndex = -1;
      }
   }

   /**
    * Performs select pressed option.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void selectPressedOption() {
      this.onOptionReleased(this.pressedIndex);
   }

   /**
    * Performs select current option.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void selectCurrentOption() {
      if (this.activeIndex >= 0) {
         this.setSelectedIndex(this.activeIndex);
      }

   }

   /**
    * Handles key typed.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onKeyTyped(char var1) {
      if (var1 == ' ') {
         this.selectCurrentOption();
      }

   }

   /**
    * Performs insert text.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void insertText(String var1) {
   }

   /**
    * Performs backspace.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void backspace() {
   }

   /**
    * Performs delete forward.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void deleteForward() {
   }

   /**
    * Performs move cursor left.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void moveCursorLeft() {
      this.navigatePrevious();
   }

   /**
    * Performs move cursor right.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void moveCursorRight() {
      this.navigateNext();
   }

   /**
    * Performs move cursor left with selection.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void moveCursorLeftWithSelection() {
      this.navigatePrevious();
   }

   /**
    * Performs move cursor right with selection.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void moveCursorRightWithSelection() {
      this.navigateNext();
   }

   /**
    * Performs select all.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void selectAll() {
   }

   /**
    * Performs delete selection.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void deleteSelection() {
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
      int var1 = ((RadioGroupModel)this.model).getSelectedIndex();
      return var1 >= 0 && var1 < ((RadioGroupModel)this.model).getOptions().size() ? (String)((RadioGroupModel)this.model).getOptions().get(var1) : "";
   }

   /**
    * Performs copy selection.
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public void copySelection() {
   }

   /**
    * Performs cut selection.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void cutSelection() {
   }

   /**
    * Performs paste from clipboard.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void pasteFromClipboard() {
   }

   /**
    * Commits the current public state.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void commit() {
      this.selectCurrentOption();
   }

   /**
    * Performs increment.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void increment(boolean var1, boolean var2) {
      this.navigateNext();
   }

   /**
    * Performs decrement.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void decrement(boolean var1, boolean var2) {
      this.navigatePrevious();
   }

   protected void onAvailabilityChanged() {
      if (!this.isInteractive()) {
         this.resetInteractionState();
         this.focused = false;
      } else {
         this.syncActiveIndex();
      }

   }

   private void syncActiveIndex() {
      int var1 = ((RadioGroupModel)this.model).getSelectedIndex();
      if (var1 >= 0) {
         this.activeIndex = var1;
      } else {
         this.activeIndex = ((RadioGroupModel)this.model).getOptions().isEmpty() ? -1 : 0;
      }
   }

   private void resetInteractionState() {
      this.hoveredIndex = -1;
      this.pressedIndex = -1;
   }

   private int normalizeOptionIndex(int var1) {
      return var1 >= 0 && var1 < ((RadioGroupModel)this.model).getOptions().size() ? var1 : -1;
   }

   private boolean isInteractive() {
      return this.isEnabled() && this.isVisible();
   }
}
