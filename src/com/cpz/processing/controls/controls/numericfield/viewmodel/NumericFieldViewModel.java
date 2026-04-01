package com.cpz.processing.controls.controls.numericfield.viewmodel;

import com.cpz.processing.controls.controls.numericfield.model.NumericFieldModel;
import com.cpz.processing.controls.core.input.ClipboardService;
import com.cpz.processing.controls.core.input.KeyboardInputTarget;
import com.cpz.processing.controls.core.viewmodel.AbstractInteractiveControlViewModel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.function.Consumer;

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
   private Consumer<BigDecimal> onValueChanged;

   public NumericFieldViewModel(NumericFieldModel var1) {
      super(var1);
      this.textBuffer = this.formatValue(var1.getValue());
      this.cursorPosition = this.textBuffer.length();
      this.selectionStart = this.cursorPosition;
      this.selectionEnd = this.cursorPosition;
      this.selectionAnchor = this.cursorPosition;
      this.lastSyncedValue = var1.getValue();
   }

   public String getText() {
      this.syncFromModelIfNotEditing();
      return this.textBuffer;
   }

   public BigDecimal getValue() {
      this.syncFromModelIfNotEditing();
      return ((NumericFieldModel)this.model).getValue();
   }

   public boolean isEditing() {
      return this.editing;
   }

   public int getCursorPosition() {
      this.syncSelectionBounds();
      return this.cursorPosition;
   }

   public void setCursorPosition(int var1) {
      this.cursorPosition = this.clampIndex(var1);
      this.clearSelection();
   }

   public void setCursorPositionWithoutSelectionReset(int var1) {
      this.cursorPosition = this.clampIndex(var1);
   }

   public int getSelectionStart() {
      this.syncSelectionBounds();
      return this.selectionStart;
   }

   public void setSelectionStart(int var1) {
      this.selectionStart = this.clampIndex(var1);
   }

   public int getSelectionEnd() {
      this.syncSelectionBounds();
      return this.selectionEnd;
   }

   public void setSelectionEnd(int var1) {
      this.selectionEnd = this.clampIndex(var1);
   }

   public void setSelectionAnchor(int var1) {
      this.selectionAnchor = this.clampIndex(var1);
   }

   public boolean isSelecting() {
      return this.selecting;
   }

   public void setSelecting(boolean var1) {
      this.selecting = var1 && this.focused && this.isEnabled() && this.isVisible();
   }

   public int getSelectionMin() {
      return Math.min(this.selectionStart, this.selectionEnd);
   }

   public int getSelectionMax() {
      return Math.max(this.selectionStart, this.selectionEnd);
   }

   public boolean hasSelection() {
      return this.selectionStart != this.selectionEnd;
   }

   public boolean isFocused() {
      return this.focused;
   }

   public void setFocused(boolean var1) {
      this.focused = var1 && this.isEditableContext();
      if (!this.focused) {
         this.commit();
         this.selecting = false;
      }

   }

   public boolean isShowCursor() {
      return this.focused;
   }

   public void setOnValueChanged(Consumer<BigDecimal> var1) {
      this.onValueChanged = var1;
   }

   public void onFocusGained() {
      this.focused = this.isEditableContext();
      this.syncFromModelIfNotEditing();
      this.selecting = false;
   }

   public void onFocusLost() {
      this.focused = false;
      this.commit();
      this.selecting = false;
   }

   public void onKeyTyped(char var1) {
      if (!Character.isISOControl(var1)) {
         this.insertText(String.valueOf(var1));
      }

   }

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
            }
         }
      }
   }

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
         }
      }
   }

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
         }
      }
   }

   public void moveCursorLeft() {
      if (this.focused) {
         this.cursorPosition = this.hasSelection() ? this.getSelectionMin() : Math.max(0, this.cursorPosition - 1);
         this.clearSelection();
      }
   }

   public void moveCursorRight() {
      if (this.focused) {
         this.cursorPosition = this.hasSelection() ? this.getSelectionMax() : Math.min(this.textBuffer.length(), this.cursorPosition + 1);
         this.clearSelection();
      }
   }

   public void moveCursorLeftWithSelection() {
      if (this.focused) {
         this.ensureSelectionAnchor();
         this.cursorPosition = Math.max(0, this.cursorPosition - 1);
         this.updateSelectionFromAnchor();
      }
   }

   public void moveCursorRightWithSelection() {
      if (this.focused) {
         this.ensureSelectionAnchor();
         this.cursorPosition = Math.min(this.textBuffer.length(), this.cursorPosition + 1);
         this.updateSelectionFromAnchor();
      }
   }

   public void selectAll() {
      if (this.focused) {
         this.selectionAnchor = 0;
         this.selectionStart = 0;
         this.selectionEnd = this.textBuffer.length();
         this.cursorPosition = this.textBuffer.length();
      }
   }

   public void deleteSelection() {
      if (this.canEdit() && this.hasSelection()) {
         this.deleteSelectionInternal();
         this.editing = true;
         this.updateModelFromBufferIfParsable();
      }
   }

   public String getSelectedText() {
      return !this.hasSelection() ? "" : this.textBuffer.substring(this.getSelectionMin(), this.getSelectionMax());
   }

   public void copySelection() {
      this.clipboardService.copy(this.getSelectedText());
   }

   public void cutSelection() {
      if (this.canEdit()) {
         this.clipboardService.copy(this.getSelectedText());
         this.deleteSelection();
      }
   }

   public void pasteFromClipboard() {
      if (this.canEdit()) {
         this.insertText(this.clipboardService.paste());
      }
   }

   public void increment(boolean var1, boolean var2) {
      this.applyStep(this.resolveAdjustedStep(var1, var2));
   }

   public void decrement(boolean var1, boolean var2) {
      this.applyStep(this.resolveAdjustedStep(var1, var2).negate());
   }

   public void onMouseWheel(float var1, boolean var2, boolean var3) {
      if (this.focused && this.isInteractive() && var1 != 0.0F) {
         if (var1 < 0.0F) {
            this.increment(var2, var3);
         } else {
            this.decrement(var2, var3);
         }

      }
   }

   public void setValue(BigDecimal var1) {
      BigDecimal var2 = ((NumericFieldModel)this.model).getValue();
      ((NumericFieldModel)this.model).setValue(this.clampAndNormalize(var1 == null ? this.fallbackForEmptyCommit() : var1));
      this.notifyValueChangedIfNeeded(var2, ((NumericFieldModel)this.model).getValue());
      this.syncFromModelIfNotEditing();
   }

   protected void activate() {
   }

   private void applyStep(BigDecimal var1) {
      if (this.focused && this.isInteractive()) {
         if (this.editing) {
            this.commitValue();
         }

         BigDecimal var2 = ((NumericFieldModel)this.model).getValue().add(var1);
         this.setCommittedValue(var2);
      }
   }

   private void setCommittedValue(BigDecimal var1) {
      BigDecimal var2 = ((NumericFieldModel)this.model).getValue();
      BigDecimal var3 = this.clampAndNormalize(var1 == null ? this.fallbackForEmptyCommit() : var1);
      ((NumericFieldModel)this.model).setValue(var3);
      this.notifyValueChangedIfNeeded(var2, ((NumericFieldModel)this.model).getValue());
      this.textBuffer = this.formatValue(((NumericFieldModel)this.model).getValue());
      this.editing = false;
      this.lastSyncedValue = ((NumericFieldModel)this.model).getValue();
      this.cursorPosition = this.textBuffer.length();
      this.clearSelection();
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

   private BigDecimal resolveAdjustedStep(boolean var1, boolean var2) {
      BigDecimal var3 = ((NumericFieldModel)this.model).getStep();
      if (var1) {
         return var3.multiply(BigDecimal.TEN);
      } else {
         return var2 ? var3.divide(BigDecimal.TEN, Math.max(((NumericFieldModel)this.model).getScale() + 1, 1), RoundingMode.HALF_UP) : var3;
      }
   }

   private BigDecimal clampAndNormalize(BigDecimal var1) {
      return this.clamp(var1).setScale(((NumericFieldModel)this.model).getScale(), RoundingMode.HALF_UP);
   }

   private BigDecimal clamp(BigDecimal var1) {
      BigDecimal var2 = var1;
      if (var1.compareTo(((NumericFieldModel)this.model).getMin()) < 0) {
         var2 = ((NumericFieldModel)this.model).getMin();
      }

      if (var2.compareTo(((NumericFieldModel)this.model).getMax()) > 0) {
         var2 = ((NumericFieldModel)this.model).getMax();
      }

      return var2;
   }

   private String formatValue(BigDecimal var1) {
      return var1.setScale(((NumericFieldModel)this.model).getScale(), RoundingMode.HALF_UP).toPlainString();
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

   public void commit() {
      this.commitValue();
   }
}
