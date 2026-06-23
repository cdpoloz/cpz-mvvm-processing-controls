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
 *
 * @author CPZ
 */
public final class RadioGroupViewModel extends AbstractControlViewModel implements PointerInputTarget, KeyboardInputTarget {
   private int hoveredIndex = -1;
   private int pressedIndex = -1;
   private boolean focused;
   private int activeIndex = -1;
   private Consumer<Integer> onOptionSelected;

   /**
    * Creates a radio group view model.
    *
    * @param model parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public RadioGroupViewModel(RadioGroupModel model) {
      super(model);
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
   public List<String> getOptions() {
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
    * @param index new selected index
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setSelectedIndex(int index) {
      int value = ((RadioGroupModel)this.model).getSelectedIndex();
      ((RadioGroupModel)this.model).setSelectedIndex(index);
      this.syncActiveIndex();
      this.hoveredIndex = this.normalizeOptionIndex(this.hoveredIndex);
      this.pressedIndex = this.normalizeOptionIndex(this.pressedIndex);
      this.notifySelectionIfChanged(value);
   }

   public void setOptions(List<String> list) {
      int value = ((RadioGroupModel)this.model).getSelectedIndex();
      ((RadioGroupModel)this.model).setOptions(list);
      this.syncActiveIndex();
      this.hoveredIndex = this.normalizeOptionIndex(this.hoveredIndex);
      this.pressedIndex = this.normalizeOptionIndex(this.pressedIndex);
      this.notifySelectionIfChanged(value);
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
    * @param index new hovered index
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setHoveredIndex(int index) {
      this.hoveredIndex = this.isInteractive() ? this.normalizeOptionIndex(index) : -1;
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
    * @param index new pressed index
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setPressedIndex(int index) {
      this.pressedIndex = this.isInteractive() ? this.normalizeOptionIndex(index) : -1;
   }

   /**
    * Handles option pressed.
    *
    * @param value parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onOptionPressed(int value) {
      if (!this.isInteractive()) {
         this.pressedIndex = -1;
      } else {
         this.pressedIndex = this.normalizeOptionIndex(value);
      }
   }

   /**
    * Handles option released.
    *
    * @param value parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onOptionReleased(int value) {
      if (!this.isInteractive()) {
         this.pressedIndex = -1;
      } else {
         int value2 = this.normalizeOptionIndex(value);
         if (value2 >= 0 && value2 == this.pressedIndex) {
            this.setSelectedIndex(value2);
         }

         this.pressedIndex = -1;
      }
   }

   /**
    * Updates on option selected.
    *
    * @param consumer new on option selected
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setOnOptionSelected(Consumer<Integer> consumer) {
      this.onOptionSelected = consumer;
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
    * @param focused new focused
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setFocused(boolean focused) {
      this.focused = focused && this.isInteractive();
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
    * @param inside parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onPointerMove(boolean inside) {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      } else {
         if (!inside) {
            this.hoveredIndex = -1;
         }

      }
   }

   /**
    * Handles pointer press.
    *
    * @param coarseStep coarse navigation modifier
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onPointerPress(boolean enabled) {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      } else {
         if (!enabled) {
            this.pressedIndex = -1;
         }

      }
   }

   /**
    * Handles pointer release.
    *
    * @param inside parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onPointerRelease(boolean inside) {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      } else {
         if (!inside) {
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
    * @param key parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onKeyTyped(char key) {
      if (key == ' ') {
         this.selectCurrentOption();
      }

   }

   /**
    * Performs insert text.
    *
    * @param text parameter used by this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void insertText(String text) {
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
      int index2 = ((RadioGroupModel)this.model).getSelectedIndex();
      return index2 >= 0 && index2 < ((RadioGroupModel)this.model).getOptions().size() ? (String)((RadioGroupModel)this.model).getOptions().get(index2) : "";
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
    * @param coarseStep coarse navigation modifier
    * @param fineStep fine navigation modifier
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void increment(boolean coarseStep, boolean fineStep) {
      this.navigatePrevious();
   }

   /**
    * Performs decrement.
    *
    * @param coarseStep coarse navigation modifier
    * @param fineStep fine navigation modifier
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void decrement(boolean coarseStep, boolean fineStep) {
      this.navigateNext();
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
      int value = ((RadioGroupModel)this.model).getSelectedIndex();
      if (value >= 0) {
         this.activeIndex = value;
      } else {
         this.activeIndex = ((RadioGroupModel)this.model).getOptions().isEmpty() ? -1 : 0;
      }
   }

   private void resetInteractionState() {
      this.hoveredIndex = -1;
      this.pressedIndex = -1;
   }

   private int normalizeOptionIndex(int zIndex) {
      return zIndex >= 0 && zIndex < ((RadioGroupModel)this.model).getOptions().size() ? zIndex : -1;
   }

   private boolean isInteractive() {
      return this.isEnabled() && this.isVisible();
   }

   private void notifySelectionIfChanged(int value) {
      int value2 = ((RadioGroupModel)this.model).getSelectedIndex();
      if (value != value2 && this.onOptionSelected != null) {
         this.onOptionSelected.accept(value2);
      }
   }
}
