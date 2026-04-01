package com.cpz.processing.controls.controls.radiogroup.viewmodel;

import com.cpz.processing.controls.controls.radiogroup.model.RadioGroupModel;
import com.cpz.processing.controls.core.input.KeyboardInputTarget;
import com.cpz.processing.controls.core.input.PointerInputTarget;
import com.cpz.processing.controls.core.viewmodel.AbstractControlViewModel;
import java.util.List;
import java.util.function.Consumer;

public final class RadioGroupViewModel extends AbstractControlViewModel implements PointerInputTarget, KeyboardInputTarget {
   private int hoveredIndex = -1;
   private int pressedIndex = -1;
   private boolean focused;
   private int activeIndex = -1;
   private Consumer onOptionSelected;

   public RadioGroupViewModel(RadioGroupModel var1) {
      super(var1);
      this.syncActiveIndex();
   }

   public List getOptions() {
      return ((RadioGroupModel)this.model).getOptions();
   }

   public int getSelectedIndex() {
      return ((RadioGroupModel)this.model).getSelectedIndex();
   }

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

   public int getHoveredIndex() {
      return this.hoveredIndex;
   }

   public void setHoveredIndex(int var1) {
      this.hoveredIndex = this.isInteractive() ? this.normalizeOptionIndex(var1) : -1;
   }

   public int getPressedIndex() {
      return this.pressedIndex;
   }

   public void setPressedIndex(int var1) {
      this.pressedIndex = this.isInteractive() ? this.normalizeOptionIndex(var1) : -1;
   }

   public void onOptionPressed(int var1) {
      if (!this.isInteractive()) {
         this.pressedIndex = -1;
      } else {
         this.pressedIndex = this.normalizeOptionIndex(var1);
      }
   }

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

   public void setOnOptionSelected(Consumer var1) {
      this.onOptionSelected = var1;
   }

   public int getActiveIndex() {
      return this.activeIndex;
   }

   public boolean isFocused() {
      return this.focused;
   }

   public void setFocused(boolean var1) {
      this.focused = var1 && this.isInteractive();
      if (!this.focused) {
         this.pressedIndex = -1;
      } else {
         this.syncActiveIndex();
      }

   }

   public void onFocusGained() {
      this.focused = this.isInteractive();
      this.syncActiveIndex();
   }

   public void onFocusLost() {
      this.focused = false;
      this.pressedIndex = -1;
   }

   public void onPointerMove(boolean var1) {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      } else {
         if (!var1) {
            this.hoveredIndex = -1;
         }

      }
   }

   public void onPointerPress(boolean var1) {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      } else {
         if (!var1) {
            this.pressedIndex = -1;
         }

      }
   }

   public void onPointerRelease(boolean var1) {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      } else {
         if (!var1) {
            this.pressedIndex = -1;
         }

      }
   }

   public void navigatePrevious() {
      if (this.focused && !((RadioGroupModel)this.model).getOptions().isEmpty()) {
         this.activeIndex = this.activeIndex < 0 ? ((RadioGroupModel)this.model).getOptions().size() - 1 : Math.max(0, this.activeIndex - 1);
         this.hoveredIndex = -1;
      }
   }

   public void navigateNext() {
      if (this.focused && !((RadioGroupModel)this.model).getOptions().isEmpty()) {
         this.activeIndex = this.activeIndex < 0 ? 0 : Math.min(((RadioGroupModel)this.model).getOptions().size() - 1, this.activeIndex + 1);
         this.hoveredIndex = -1;
      }
   }

   public void selectPressedOption() {
      this.onOptionReleased(this.pressedIndex);
   }

   public void selectCurrentOption() {
      if (this.activeIndex >= 0) {
         this.setSelectedIndex(this.activeIndex);
      }

   }

   public void onKeyTyped(char var1) {
      if (var1 == ' ') {
         this.selectCurrentOption();
      }

   }

   public void insertText(String var1) {
   }

   public void backspace() {
   }

   public void deleteForward() {
   }

   public void moveCursorLeft() {
      this.navigatePrevious();
   }

   public void moveCursorRight() {
      this.navigateNext();
   }

   public void moveCursorLeftWithSelection() {
      this.navigatePrevious();
   }

   public void moveCursorRightWithSelection() {
      this.navigateNext();
   }

   public void selectAll() {
   }

   public void deleteSelection() {
   }

   public String getSelectedText() {
      int var1 = ((RadioGroupModel)this.model).getSelectedIndex();
      return var1 >= 0 && var1 < ((RadioGroupModel)this.model).getOptions().size() ? (String)((RadioGroupModel)this.model).getOptions().get(var1) : "";
   }

   public void copySelection() {
   }

   public void cutSelection() {
   }

   public void pasteFromClipboard() {
   }

   public void commit() {
      this.selectCurrentOption();
   }

   public void increment(boolean var1, boolean var2) {
      this.navigateNext();
   }

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
