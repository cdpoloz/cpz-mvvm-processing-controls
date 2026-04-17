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
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public NumericFieldViewModel(NumericFieldModel var1) {
      super(var1);
      this.textBuffer = this.formatValue(var1.getValue());
      this.cursorPosition = this.textBuffer.length();
      this.selectionStart = this.cursorPosition;
      this.selectionEnd = this.cursorPosition;
      this.selectionAnchor = this.cursorPosition;
      this.lastSyncedValue = var1.getValue();
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
    * @param var1 new cursor position
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setCursorPosition(int var1) {
      this.cursorPosition = this.clampIndex(var1);
      this.clearSelection();
   }

   /**
    * Updates cursor position without selection reset.
    *
    * @param var1 new cursor position without selection reset
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setCursorPositionWithoutSelectionReset(int var1) {
      this.cursorPosition = this.clampIndex(var1);
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
    * @param var1 new selection start
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setSelectionStart(int var1) {
      this.selectionStart = this.clampIndex(var1);
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
    * @param var1 new selection end
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setSelectionEnd(int var1) {
      this.selectionEnd = this.clampIndex(var1);
   }

   /**
    * Updates selection anchor.
    *
    * @param var1 new selection anchor
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setSelectionAnchor(int var1) {
      this.selectionAnchor = this.clampIndex(var1);
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
    * @param var1 new selecting
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setSelecting(boolean var1) {
      this.selecting = var1 && this.focused && this.isEnabled() && this.isVisible();
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
    * @param var1 new focused
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setFocused(boolean var1) {
      this.focused = var1 && this.isEditableContext();
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
    * @param var1 new on value changed
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setOnValueChanged(Consumer<BigDecimal> var1) {
      this.onValueChanged = var1;
   }

   /**
    * Updates on text changed.
    *
    * @param var1 new on text changed
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setOnTextChanged(Consumer<String> var1) {
      this.onTextChanged = var1;
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
    * @param var1 new text
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setText(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("text must not be null");
      } else if (!this.isCandidateTextAllowed(var1)) {
         throw new IllegalArgumentException("text must use only digits, an optional leading '-', and a single optional '.': " + var1);
      } else {
         this.textBuffer = var1;
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
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onKeyTyped(char var1) {
      if (!Character.isISOControl(var1)) {
         this.insertText(String.valueOf(var1));
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
      if (this.canEdit() && var1 != null && !var1.isEmpty()) {
         if (this.isInsertAllowed(var1)) {
            if (this.hasSelection()) {
               this.deleteSelectionInternal();
            }

            String var2 = this.textBuffer.substring(0, this.cursorPosition) + var1 + this.textBuffer.substring(this.cursorPosition);
            if (this.isCandidateTextAllowed(var2)) {
               this.textBuffer = var2;
               this.cursorPosition += var1.length();
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
            String var10001 = this.textBuffer.substring(0, this.cursorPosition - 1);
            this.textBuffer = var10001 + this.textBuffer.substring(this.cursorPosition);
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
            String var10001 = this.textBuffer.substring(0, this.cursorPosition);
            this.textBuffer = var10001 + this.textBuffer.substring(this.cursorPosition + 1);
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
    * @param var1 new value
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setValue(BigDecimal var1) {
      BigDecimal var2 = ((NumericFieldModel)this.model).getValue();
      ((NumericFieldModel)this.model).setValue(this.clampAndNormalize(var1 == null ? this.fallbackForEmptyCommit() : var1));
      this.notifyValueChangedIfNeeded(var2, ((NumericFieldModel)this.model).getValue());
      this.textBuffer = this.formatValue(((NumericFieldModel)this.model).getValue());
      this.editing = false;
      this.lastSyncedValue = ((NumericFieldModel)this.model).getValue();
      this.cursorPosition = this.textBuffer.length();
      this.clearSelection();
      this.notifyTextChanged();
   }

   protected void activate() {
   }

   private void setCommittedValue(BigDecimal var1) {
      BigDecimal var2 = ((NumericFieldModel)this.model).getValue();
      String var4 = this.textBuffer;
      BigDecimal var3 = this.clampAndNormalize(var1 == null ? this.fallbackForEmptyCommit() : var1);
      ((NumericFieldModel)this.model).setValue(var3);
      this.notifyValueChangedIfNeeded(var2, ((NumericFieldModel)this.model).getValue());
      this.textBuffer = this.formatValue(((NumericFieldModel)this.model).getValue());
      this.editing = false;
      this.lastSyncedValue = ((NumericFieldModel)this.model).getValue();
      this.cursorPosition = this.textBuffer.length();
      this.clearSelection();
      if (!Objects.equals(var4, this.textBuffer)) {
         this.notifyTextChanged();
      }
   }

   private void updateModelFromBufferIfParsable() {
      if (!this.isIntermediateValidState(this.textBuffer)) {
         BigDecimal var1 = this.tryParse(this.textBuffer);
         if (var1 != null) {
            BigDecimal var2 = ((NumericFieldModel)this.model).getValue();
            ((NumericFieldModel)this.model).setValue(this.clamp(var1));
            this.lastSyncedValue = ((NumericFieldModel)this.model).getValue();
            this.notifyValueChangedIfNeeded(var2, ((NumericFieldModel)this.model).getValue());
         }
      }
   }

   private void syncFromModelIfNotEditing() {
      if (!this.editing) {
         String var1 = this.formatValue(((NumericFieldModel)this.model).getValue());
         this.textBuffer = var1;
         if (this.lastSyncedValue == null || ((NumericFieldModel)this.model).getValue().compareTo(this.lastSyncedValue) != 0) {
            this.lastSyncedValue = ((NumericFieldModel)this.model).getValue();
            this.cursorPosition = Math.min(this.cursorPosition, this.textBuffer.length());
            this.clearSelection();
         }

      }
   }

   private boolean isInsertAllowed(String var1) {
      int var2 = this.cursorPosition;
      String var3 = this.hasSelection() ? this.textBuffer.substring(0, this.getSelectionMin()) + this.textBuffer.substring(this.getSelectionMax()) : this.textBuffer;
      if (this.hasSelection()) {
         var2 = this.getSelectionMin();
      }

      for(int var4 = 0; var4 < var1.length(); ++var4) {
         char var5 = var1.charAt(var4);
         if (!this.isValidInsertion(var5, var2, var3)) {
            return false;
         }

         var3 = var3.substring(0, var2) + var5 + var3.substring(var2);
         ++var2;
      }

      return true;
   }

   private boolean isCharacterAllowed(char var1) {
      if (Character.isDigit(var1)) {
         return true;
      } else if (var1 == '-' && ((NumericFieldModel)this.model).isAllowNegative()) {
         return true;
      } else {
         return var1 == '.' && ((NumericFieldModel)this.model).isAllowDecimal();
      }
   }

   private boolean isCandidateTextAllowed(String var1) {
      if (var1 == null) {
         return false;
      } else {
         int var2 = 0;
         int var3 = 0;

         for(int var4 = 0; var4 < var1.length(); ++var4) {
            char var5 = var1.charAt(var4);
            if (!Character.isDigit(var5)) {
               if (var5 == '.') {
                  ++var2;
                  if (!((NumericFieldModel)this.model).isAllowDecimal() || var2 > 1) {
                     return false;
                  }
               } else {
                  if (var5 != '-') {
                     return false;
                  }

                  ++var3;
                  if (!((NumericFieldModel)this.model).isAllowNegative() || var3 > 1 || var4 != 0) {
                     return false;
                  }
               }
            }
         }

         return true;
      }
   }

   private boolean isIntermediateValidState(String var1) {
      return Objects.equals(var1, "") || Objects.equals(var1, "-") || Objects.equals(var1, ".") || Objects.equals(var1, "-.") || Objects.equals(var1, "0.") || Objects.equals(var1, "-0.");
   }

   private BigDecimal tryParse(String var1) {
      if (var1 == null) {
         return null;
      } else {
         String var2 = var1.trim();
         if (this.isIntermediateValidState(var2)) {
            return null;
         } else {
            try {
               BigDecimal var3 = new BigDecimal(var2);
               if (!((NumericFieldModel)this.model).isAllowDecimal()) {
                  var3 = var3.setScale(0, RoundingMode.DOWN);
               }

               return var3;
            } catch (NumberFormatException var4) {
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
      String var1 = this.textBuffer == null ? "" : this.textBuffer.trim();
      BigDecimal var2;
      if (var1.isEmpty()) {
         var2 = this.fallbackForEmptyCommit();
      } else if (!"-".equals(var1) && !".".equals(var1) && !"-.".equals(var1)) {
         BigDecimal var3 = this.tryParse(var1);
         var2 = var3 == null ? this.fallbackForInvalidIntermediateCommit() : var3;
      } else {
         var2 = this.fallbackForInvalidIntermediateCommit();
      }

      this.setCommittedValue(var2);
   }

   private BigDecimal clampAndNormalize(BigDecimal var1) {
      return this.clamp(var1).setScale(((NumericFieldModel)this.model).getScale(), RoundingMode.HALF_UP);
   }

   private BigDecimal clamp(BigDecimal var1) {
      BigDecimal var2 = var1;
      if (((NumericFieldModel)this.model).getMin() != null && var1.compareTo(((NumericFieldModel)this.model).getMin()) < 0) {
         var2 = ((NumericFieldModel)this.model).getMin();
      }

      if (((NumericFieldModel)this.model).getMax() != null && var2.compareTo(((NumericFieldModel)this.model).getMax()) > 0) {
         var2 = ((NumericFieldModel)this.model).getMax();
      }

      return var2;
   }

   private String formatValue(BigDecimal var1) {
      return var1.setScale(((NumericFieldModel)this.model).getScale(), RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
   }

   private boolean isValidInsertion(char var1, int var2, String var3) {
      if (!this.isCharacterAllowed(var1)) {
         return false;
      } else if (var1 != '-') {
         if (var1 == '.') {
            return var3.indexOf(46) < 0;
         } else {
            return true;
         }
      } else {
         return var2 == 0 && var3.indexOf(45) < 0;
      }
   }

   private void notifyValueChangedIfNeeded(BigDecimal var1, BigDecimal var2) {
      if (var1 != null && var2 != null) {
         if (var1.compareTo(var2) != 0) {
            if (this.onValueChanged != null) {
               this.onValueChanged.accept(var2);
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
      int var1 = this.getSelectionMin();
      int var2 = this.getSelectionMax();
      String var10001 = this.textBuffer.substring(0, var1);
      this.textBuffer = var10001 + this.textBuffer.substring(var2);
      this.cursorPosition = var1;
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

   private int clampIndex(int var1) {
      return Math.max(0, Math.min(this.textBuffer.length(), var1));
   }

   private boolean canEdit() {
      return this.focused && this.isEditableContext();
   }

   private boolean isEditableContext() {
      return this.isEnabled() && this.isVisible();
   }

}
