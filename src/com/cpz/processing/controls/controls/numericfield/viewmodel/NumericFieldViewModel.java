package com.cpz.processing.controls.controls.numericfield.viewmodel;

import com.cpz.processing.controls.controls.numericfield.model.NumericFieldModel;
import com.cpz.processing.controls.core.input.ClipboardService;
import com.cpz.processing.controls.core.input.KeyboardInputTarget;
import com.cpz.processing.controls.core.viewmodel.AbstractInteractiveControlViewModel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * ViewModel for numeric field view model.
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
public final class NumericFieldViewModel extends AbstractInteractiveControlViewModel implements KeyboardInputTarget {
   private final ClipboardService clipboardService = new ClipboardService();
   private String textBuffer;
   private int cursorPosition;
   private int selectionStart;
   private int selectionEnd;
   private int selectionAnchor;
   private boolean selecting;
   private boolean focused;
   private boolean editing;
   private BigDecimal lastSyncedValue;
   private Consumer<String> onTextChanged;
   private Consumer<BigDecimal> onValueChanged;

   /**
    * Creates a numeric field view model.
    *
    * @param model parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public NumericFieldViewModel(NumericFieldModel model) {
      super(model);
      this.textBuffer = this.formatValue(model.getValue());
      this.cursorPosition = this.textBuffer.length();
      this.selectionStart = this.cursorPosition;
      this.selectionEnd = this.cursorPosition;
      this.selectionAnchor = this.cursorPosition;
      this.lastSyncedValue = model.getValue();
   }

   /**
    * Returns text.
    *
    * @return current text
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public String getText() {
      this.syncFromModelIfNotEditing();
      return this.textBuffer;
   }

   /**
    * Returns value.
    *
    * @return current value
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public BigDecimal getValue() {
      this.syncFromModelIfNotEditing();
      return ((NumericFieldModel)this.model).getValue();
   }

   /**
    * Returns parsed value.
    *
    * @return current parsed value
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public BigDecimal getParsedValue() {
      return this.tryParseCurrentText();
   }

   /**
    * Returns whether valid.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isValid() {
      return this.tryParseCurrentText() != null;
   }

   /**
    * Returns whether editing.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isEditing() {
      return this.editing;
   }

   /**
    * Returns cursor position.
    *
    * @return current cursor position
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getCursorPosition() {
      this.syncSelectionBounds();
      return this.cursorPosition;
   }

   /**
    * Updates cursor position.
    *
    * @param value new cursor position
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setCursorPosition(int value) {
      this.cursorPosition = this.clampIndex(value);
      this.clearSelection();
   }

   /**
    * Updates cursor position without selection reset.
    *
    * @param value new cursor position without selection reset
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setCursorPositionWithoutSelectionReset(int value) {
      this.cursorPosition = this.clampIndex(value);
   }

   /**
    * Returns selection start.
    *
    * @return current selection start
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getSelectionStart() {
      this.syncSelectionBounds();
      return this.selectionStart;
   }

   /**
    * Updates selection start.
    *
    * @param value new selection start
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setSelectionStart(int value) {
      this.selectionStart = this.clampIndex(value);
   }

   /**
    * Returns selection end.
    *
    * @return current selection end
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getSelectionEnd() {
      this.syncSelectionBounds();
      return this.selectionEnd;
   }

   /**
    * Updates selection end.
    *
    * @param value new selection end
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setSelectionEnd(int value) {
      this.selectionEnd = this.clampIndex(value);
   }

   /**
    * Updates selection anchor.
    *
    * @param value new selection anchor
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setSelectionAnchor(int value) {
      this.selectionAnchor = this.clampIndex(value);
   }

   /**
    * Returns whether selecting.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isSelecting() {
      return this.selecting;
   }

   /**
    * Updates selecting.
    *
    * @param enabled new selecting
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setSelecting(boolean enabled) {
      this.selecting = enabled && this.focused && this.isEnabled() && this.isVisible();
   }

   /**
    * Returns selection min.
    *
    * @return current selection min
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getSelectionMin() {
      return Math.min(this.selectionStart, this.selectionEnd);
   }

   /**
    * Returns selection max.
    *
    * @return current selection max
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getSelectionMax() {
      return Math.max(this.selectionStart, this.selectionEnd);
   }

   /**
    * Returns whether selection.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean hasSelection() {
      return this.selectionStart != this.selectionEnd;
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
      this.focused = focused && this.isEditableContext();
      if (!this.focused) {
         this.commitValue();
         this.selecting = false;
      }

   }

   /**
    * Returns whether show cursor.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isShowCursor() {
      return this.focused;
   }

   /**
    * Updates on value changed.
    *
    * @param consumer new on value changed
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setOnValueChanged(Consumer<BigDecimal> consumer) {
      this.onValueChanged = consumer;
   }

   /**
    * Updates on text changed.
    *
    * @param consumer new on text changed
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setOnTextChanged(Consumer<String> consumer) {
      this.onTextChanged = consumer;
   }

   /**
    * Handles focus gained.
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onFocusGained() {
      this.focused = this.isEditableContext();
      this.syncFromModelIfNotEditing();
      this.selecting = false;
   }

   /**
    * Handles focus lost.
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onFocusLost() {
      this.focused = false;
      this.commitValue();
      this.selecting = false;
   }

   /**
    * Updates text.
    *
    * @param text new text
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setText(String text) {
      if (text == null) {
         throw new IllegalArgumentException("text must not be null");
      } else if (!this.isCandidateTextAllowed(text)) {
         throw new IllegalArgumentException("text must use only digits, an optional leading '-', and a single optional '.': " + text);
      } else {
         this.textBuffer = text;
         this.cursorPosition = this.textBuffer.length();
         this.clearSelection();
         this.editing = true;
         this.updateModelFromBufferIfParsable();
         this.notifyTextChanged();
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
      if (!Character.isISOControl(key)) {
         this.insertText(String.valueOf(key));
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
      if (this.canEdit() && text != null && !text.isEmpty()) {
         if (this.isInsertAllowed(text)) {
            if (this.hasSelection()) {
               this.deleteSelectionInternal();
            }

            String text2 = this.textBuffer.substring(0, this.cursorPosition) + text + this.textBuffer.substring(this.cursorPosition);
            if (this.isCandidateTextAllowed(text2)) {
               this.textBuffer = text2;
               this.cursorPosition += text.length();
               this.clearSelection();
               this.editing = true;
               this.updateModelFromBufferIfParsable();
               this.notifyTextChanged();
            }
         }
      }
   }

   /**
    * Performs backspace.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void backspace() {
      if (this.canEdit()) {
         if (this.hasSelection()) {
            this.deleteSelection();
         } else if (this.cursorPosition != 0) {
            String path = this.textBuffer.substring(0, this.cursorPosition - 1);
            this.textBuffer = path + this.textBuffer.substring(this.cursorPosition);
            --this.cursorPosition;
            this.clearSelection();
            this.editing = true;
            this.updateModelFromBufferIfParsable();
            this.notifyTextChanged();
         }
      }
   }

   /**
    * Performs delete forward.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void deleteForward() {
      if (this.canEdit()) {
         if (this.hasSelection()) {
            this.deleteSelection();
         } else if (this.cursorPosition < this.textBuffer.length()) {
            String path = this.textBuffer.substring(0, this.cursorPosition);
            this.textBuffer = path + this.textBuffer.substring(this.cursorPosition + 1);
            this.clearSelection();
            this.editing = true;
            this.updateModelFromBufferIfParsable();
            this.notifyTextChanged();
         }
      }
   }

   /**
    * Performs move cursor left.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void moveCursorLeft() {
      if (this.focused) {
         this.cursorPosition = this.hasSelection() ? this.getSelectionMin() : Math.max(0, this.cursorPosition - 1);
         this.clearSelection();
      }
   }

   /**
    * Performs move cursor right.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void moveCursorRight() {
      if (this.focused) {
         this.cursorPosition = this.hasSelection() ? this.getSelectionMax() : Math.min(this.textBuffer.length(), this.cursorPosition + 1);
         this.clearSelection();
      }
   }

   /**
    * Performs move cursor home.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void moveCursorHome() {
      if (this.focused) {
         this.cursorPosition = 0;
         this.clearSelection();
      }
   }

   /**
    * Performs move cursor end.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void moveCursorEnd() {
      if (this.focused) {
         this.cursorPosition = this.textBuffer.length();
         this.clearSelection();
      }
   }

   /**
    * Performs move cursor left with selection.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void moveCursorLeftWithSelection() {
      if (this.focused) {
         this.ensureSelectionAnchor();
         this.cursorPosition = Math.max(0, this.cursorPosition - 1);
         this.updateSelectionFromAnchor();
      }
   }

   /**
    * Performs move cursor right with selection.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void moveCursorRightWithSelection() {
      if (this.focused) {
         this.ensureSelectionAnchor();
         this.cursorPosition = Math.min(this.textBuffer.length(), this.cursorPosition + 1);
         this.updateSelectionFromAnchor();
      }
   }

   /**
    * Performs select all.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void selectAll() {
      if (this.focused) {
         this.selectionAnchor = 0;
         this.selectionStart = 0;
         this.selectionEnd = this.textBuffer.length();
         this.cursorPosition = this.textBuffer.length();
      }
   }

   /**
    * Performs delete selection.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void deleteSelection() {
      if (this.canEdit() && this.hasSelection()) {
         this.deleteSelectionInternal();
         this.editing = true;
         this.updateModelFromBufferIfParsable();
         this.notifyTextChanged();
      }
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
      return !this.hasSelection() ? "" : this.textBuffer.substring(this.getSelectionMin(), this.getSelectionMax());
   }

   /**
    * Performs copy selection.
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public void copySelection() {
      this.clipboardService.copy(this.getSelectedText());
   }

   /**
    * Performs cut selection.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void cutSelection() {
      if (this.canEdit()) {
         this.clipboardService.copy(this.getSelectedText());
         this.deleteSelection();
      }
   }

   /**
    * Performs paste from clipboard.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void pasteFromClipboard() {
      if (this.canEdit()) {
         this.insertText(this.clipboardService.paste());
      }
   }

   /**
    * Updates value.
    *
    * @param bigDecimal new value
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setValue(BigDecimal bigDecimal) {
      BigDecimal bigDecimal2 = ((NumericFieldModel)this.model).getValue();
      ((NumericFieldModel)this.model).setValue(this.clampAndNormalize(bigDecimal == null ? this.fallbackForEmptyCommit() : bigDecimal));
      this.notifyValueChangedIfNeeded(bigDecimal2, ((NumericFieldModel)this.model).getValue());
      this.textBuffer = this.formatValue(((NumericFieldModel)this.model).getValue());
      this.editing = false;
      this.lastSyncedValue = ((NumericFieldModel)this.model).getValue();
      this.cursorPosition = this.textBuffer.length();
      this.clearSelection();
      this.notifyTextChanged();
   }

   protected void activate() {
   }

   private void setCommittedValue(BigDecimal bigDecimal) {
      BigDecimal bigDecimal2 = ((NumericFieldModel)this.model).getValue();
      String text = this.textBuffer;
      BigDecimal bigDecimal3 = this.clampAndNormalize(bigDecimal == null ? this.fallbackForEmptyCommit() : bigDecimal);
      ((NumericFieldModel)this.model).setValue(bigDecimal3);
      this.notifyValueChangedIfNeeded(bigDecimal2, ((NumericFieldModel)this.model).getValue());
      this.textBuffer = this.formatValue(((NumericFieldModel)this.model).getValue());
      this.editing = false;
      this.lastSyncedValue = ((NumericFieldModel)this.model).getValue();
      this.cursorPosition = this.textBuffer.length();
      this.clearSelection();
      if (!Objects.equals(text, this.textBuffer)) {
         this.notifyTextChanged();
      }
   }

   private void updateModelFromBufferIfParsable() {
      if (!this.isIntermediateValidState(this.textBuffer)) {
         BigDecimal bigDecimal = this.tryParse(this.textBuffer);
         if (bigDecimal != null) {
            BigDecimal bigDecimal2 = ((NumericFieldModel)this.model).getValue();
            ((NumericFieldModel)this.model).setValue(this.clamp(bigDecimal));
            this.lastSyncedValue = ((NumericFieldModel)this.model).getValue();
            this.notifyValueChangedIfNeeded(bigDecimal2, ((NumericFieldModel)this.model).getValue());
         }
      }
   }

   private void syncFromModelIfNotEditing() {
      if (!this.editing) {
         String text = this.formatValue(((NumericFieldModel)this.model).getValue());
         this.textBuffer = text;
         if (this.lastSyncedValue == null || ((NumericFieldModel)this.model).getValue().compareTo(this.lastSyncedValue) != 0) {
            this.lastSyncedValue = ((NumericFieldModel)this.model).getValue();
            this.cursorPosition = Math.min(this.cursorPosition, this.textBuffer.length());
            this.clearSelection();
         }

      }
   }

   private boolean isInsertAllowed(String text) {
      int value = this.cursorPosition;
      String text2 = this.hasSelection() ? this.textBuffer.substring(0, this.getSelectionMin()) + this.textBuffer.substring(this.getSelectionMax()) : this.textBuffer;
      if (this.hasSelection()) {
         value = this.getSelectionMin();
      }

      for(int value2 = 0; value2 < text.length(); ++value2) {
         char key = text.charAt(value2);
         if (!this.isValidInsertion(key, value, text2)) {
            return false;
         }

         text2 = text2.substring(0, value) + key + text2.substring(value);
         ++value;
      }

      return true;
   }

   private boolean isCharacterAllowed(char key) {
      if (Character.isDigit(key)) {
         return true;
      } else if (key == '-' && ((NumericFieldModel)this.model).isAllowNegative()) {
         return true;
      } else {
         return key == '.' && ((NumericFieldModel)this.model).isAllowDecimal();
      }
   }

   private boolean isCandidateTextAllowed(String text) {
      if (text == null) {
         return false;
      } else {
         int value = 0;
         int value2 = 0;

         for(int value3 = 0; value3 < text.length(); ++value3) {
            char key = text.charAt(value3);
            if (!Character.isDigit(key)) {
               if (key == '.') {
                  ++value;
                  if (!((NumericFieldModel)this.model).isAllowDecimal() || value > 1) {
                     return false;
                  }
               } else {
                  if (key != '-') {
                     return false;
                  }

                  ++value2;
                  if (!((NumericFieldModel)this.model).isAllowNegative() || value2 > 1 || value3 != 0) {
                     return false;
                  }
               }
            }
         }

         return true;
      }
   }

   private boolean isIntermediateValidState(String text) {
      return Objects.equals(text, "") || Objects.equals(text, "-") || Objects.equals(text, ".") || Objects.equals(text, "-.") || Objects.equals(text, "0.") || Objects.equals(text, "-0.");
   }

   private BigDecimal tryParse(String text) {
      if (text == null) {
         return null;
      } else {
         String text2 = text.trim();
         if (this.isIntermediateValidState(text2)) {
            return null;
         } else {
            try {
               BigDecimal bigDecimal = new BigDecimal(text2);
               if (!((NumericFieldModel)this.model).isAllowDecimal()) {
                  bigDecimal = bigDecimal.setScale(0, RoundingMode.DOWN);
               }

               return bigDecimal;
            } catch (NumberFormatException numberFormatException) {
               return null;
            }
         }
      }
   }

   private BigDecimal fallbackForEmptyCommit() {
      return ((NumericFieldModel)this.model).getMin() != null ? this.clampAndNormalize(((NumericFieldModel)this.model).getMin()) : this.clampAndNormalize(BigDecimal.ZERO);
   }

   private BigDecimal fallbackForInvalidIntermediateCommit() {
      return this.clampAndNormalize(this.lastSyncedValue == null ? ((NumericFieldModel)this.model).getValue() : this.lastSyncedValue);
   }

   private void commitValue() {
      String text = this.textBuffer == null ? "" : this.textBuffer.trim();
      BigDecimal bigDecimal;
      if (text.isEmpty()) {
         bigDecimal = this.fallbackForEmptyCommit();
      } else if (!"-".equals(text) && !".".equals(text) && !"-.".equals(text)) {
         BigDecimal bigDecimal2 = this.tryParse(text);
         bigDecimal = bigDecimal2 == null ? this.fallbackForInvalidIntermediateCommit() : bigDecimal2;
      } else {
         bigDecimal = this.fallbackForInvalidIntermediateCommit();
      }

      this.setCommittedValue(bigDecimal);
   }

   private BigDecimal clampAndNormalize(BigDecimal bigDecimal) {
      return this.clamp(bigDecimal).setScale(((NumericFieldModel)this.model).getScale(), RoundingMode.HALF_UP);
   }

   private BigDecimal clamp(BigDecimal bigDecimal) {
      BigDecimal bigDecimal2 = bigDecimal;
      if (((NumericFieldModel)this.model).getMin() != null && bigDecimal.compareTo(((NumericFieldModel)this.model).getMin()) < 0) {
         bigDecimal2 = ((NumericFieldModel)this.model).getMin();
      }

      if (((NumericFieldModel)this.model).getMax() != null && bigDecimal2.compareTo(((NumericFieldModel)this.model).getMax()) > 0) {
         bigDecimal2 = ((NumericFieldModel)this.model).getMax();
      }

      return bigDecimal2;
   }

   private String formatValue(BigDecimal bigDecimal) {
      return bigDecimal.setScale(((NumericFieldModel)this.model).getScale(), RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
   }

   private boolean isValidInsertion(char key, int value, String text) {
      if (!this.isCharacterAllowed(key)) {
         return false;
      } else if (key != '-') {
         if (key == '.') {
            return text.indexOf(46) < 0;
         } else {
            return true;
         }
      } else {
         return value == 0 && text.indexOf(45) < 0;
      }
   }

   private void notifyValueChangedIfNeeded(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
      if (bigDecimal != null && bigDecimal2 != null) {
         if (bigDecimal.compareTo(bigDecimal2) != 0) {
            if (this.onValueChanged != null) {
               this.onValueChanged.accept(bigDecimal2);
            }

         }
      }
   }

   private void notifyTextChanged() {
      if (this.onTextChanged != null) {
         this.onTextChanged.accept(this.textBuffer);
      }
   }

   private BigDecimal tryParseCurrentText() {
      this.syncFromModelIfNotEditing();
      return this.tryParse(this.textBuffer == null ? "" : this.textBuffer.trim());
   }

   private void deleteSelectionInternal() {
      int value = this.getSelectionMin();
      int value2 = this.getSelectionMax();
      String path = this.textBuffer.substring(0, value);
      this.textBuffer = path + this.textBuffer.substring(value2);
      this.cursorPosition = value;
      this.clearSelection();
   }

   private void clearSelection() {
      this.selectionStart = this.cursorPosition;
      this.selectionEnd = this.cursorPosition;
      this.selectionAnchor = this.cursorPosition;
   }

   private void ensureSelectionAnchor() {
      if (!this.hasSelection()) {
         this.selectionAnchor = this.cursorPosition;
      }

   }

   private void updateSelectionFromAnchor() {
      this.selectionStart = this.selectionAnchor;
      this.selectionEnd = this.cursorPosition;
   }

   private void syncSelectionBounds() {
      this.cursorPosition = this.clampIndex(this.cursorPosition);
      this.selectionStart = this.clampIndex(this.selectionStart);
      this.selectionEnd = this.clampIndex(this.selectionEnd);
      this.selectionAnchor = this.clampIndex(this.selectionAnchor);
   }

   private int clampIndex(int index) {
      return Math.max(0, Math.min(this.textBuffer.length(), index));
   }

   private boolean canEdit() {
      return this.focused && this.isEditableContext();
   }

   private boolean isEditableContext() {
      return this.isEnabled() && this.isVisible();
   }

}
