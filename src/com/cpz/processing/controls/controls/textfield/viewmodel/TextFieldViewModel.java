package com.cpz.processing.controls.controls.textfield.viewmodel;

import com.cpz.processing.controls.controls.textfield.model.TextFieldModel;
import com.cpz.processing.controls.core.input.ClipboardService;
import com.cpz.processing.controls.core.input.KeyboardInputTarget;
import com.cpz.processing.controls.core.viewmodel.AbstractControlViewModel;

public final class TextFieldViewModel extends AbstractControlViewModel implements KeyboardInputTarget {
   private final ClipboardService clipboardService = new ClipboardService();
   private int cursorIndex;
   private int selectionStart;
   private int selectionEnd;
   private int selectionAnchor;
   private boolean selecting;
   private boolean focused;

   public TextFieldViewModel(TextFieldModel var1) {
      super(var1);
      this.cursorIndex = var1.getText().length();
      this.selectionStart = this.cursorIndex;
      this.selectionEnd = this.cursorIndex;
      this.selectionAnchor = this.cursorIndex;
   }

   public String getText() {
      return ((TextFieldModel)this.model).getText();
   }

   public void setText(String var1) {
      ((TextFieldModel)this.model).setText(var1);
      this.cursorIndex = this.clampIndex(this.cursorIndex);
      this.clearSelection();
   }

   public int getCursorIndex() {
      return this.cursorIndex;
   }

   public void setCursorIndex(int var1) {
      this.cursorIndex = this.clampIndex(var1);
      this.clearSelection();
   }

   public void setCursorIndexWithoutSelectionReset(int var1) {
      this.cursorIndex = this.clampIndex(var1);
   }

   public int getSelectionStart() {
      return this.selectionStart;
   }

   public void setSelectionStart(int var1) {
      this.selectionStart = this.clampIndex(var1);
   }

   public int getSelectionEnd() {
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

   public boolean hasSelection() {
      return this.selectionStart != this.selectionEnd;
   }

   public int getSelectionMin() {
      return Math.min(this.selectionStart, this.selectionEnd);
   }

   public int getSelectionMax() {
      return Math.max(this.selectionStart, this.selectionEnd);
   }

   public boolean isFocused() {
      return this.focused;
   }

   public void setFocused(boolean var1) {
      this.focused = var1 && this.isEnabled() && this.isVisible();
      if (!this.focused) {
         this.cursorIndex = this.clampIndex(this.cursorIndex);
         this.clearSelection();
         this.selecting = false;
      }

   }

   public boolean isShowCursor() {
      return this.focused;
   }

   public void onFocusGained() {
      this.focused = this.isEnabled() && this.isVisible();
      this.cursorIndex = this.clampIndex(this.cursorIndex);
      this.clearSelection();
      this.selecting = false;
   }

   public void onFocusLost() {
      this.focused = false;
      this.cursorIndex = this.clampIndex(this.cursorIndex);
      this.clearSelection();
      this.selecting = false;
   }

   public void onKeyTyped(char var1) {
      if (!Character.isISOControl(var1)) {
         this.insertText(String.valueOf(var1));
      }

   }

   public void insertText(String var1) {
      if (this.canEdit() && var1 != null && !var1.isEmpty()) {
         if (this.hasSelection()) {
            this.deleteSelectionInternal();
         }

         String var2 = ((TextFieldModel)this.model).getText();
         String var3 = var2.substring(0, this.cursorIndex) + var1 + var2.substring(this.cursorIndex);
         ((TextFieldModel)this.model).setText(var3);
         this.cursorIndex += var1.length();
         this.clearSelection();
      }
   }

   public void backspace() {
      if (this.canEdit()) {
         if (this.hasSelection()) {
            this.deleteSelection();
         } else if (this.cursorIndex != 0) {
            String var1 = ((TextFieldModel)this.model).getText();
            String var10000 = var1.substring(0, this.cursorIndex - 1);
            String var2 = var10000 + var1.substring(this.cursorIndex);
            ((TextFieldModel)this.model).setText(var2);
            --this.cursorIndex;
            this.clearSelection();
         }
      }
   }

   public void deleteForward() {
      if (this.canEdit()) {
         if (this.hasSelection()) {
            this.deleteSelection();
         } else {
            String var1 = ((TextFieldModel)this.model).getText();
            if (this.cursorIndex < var1.length()) {
               String var10000 = var1.substring(0, this.cursorIndex);
               String var2 = var10000 + var1.substring(this.cursorIndex + 1);
               ((TextFieldModel)this.model).setText(var2);
               this.clearSelection();
            }
         }
      }
   }

   public void moveCursorLeft() {
      if (this.focused) {
         this.cursorIndex = this.hasSelection() ? this.getSelectionMin() : Math.max(0, this.cursorIndex - 1);
         this.clearSelection();
      }
   }

   public void moveCursorRight() {
      if (this.focused) {
         this.cursorIndex = this.hasSelection() ? this.getSelectionMax() : Math.min(((TextFieldModel)this.model).getText().length(), this.cursorIndex + 1);
         this.clearSelection();
      }
   }

   public void moveCursorLeftWithSelection() {
      if (this.focused) {
         this.ensureSelectionAnchor();
         this.cursorIndex = Math.max(0, this.cursorIndex - 1);
         this.updateSelectionFromAnchor();
      }
   }

   public void moveCursorRightWithSelection() {
      if (this.focused) {
         this.ensureSelectionAnchor();
         this.cursorIndex = Math.min(((TextFieldModel)this.model).getText().length(), this.cursorIndex + 1);
         this.updateSelectionFromAnchor();
      }
   }

   public void selectAll() {
      if (this.focused) {
         this.selectionAnchor = 0;
         this.selectionStart = 0;
         this.selectionEnd = ((TextFieldModel)this.model).getText().length();
         this.cursorIndex = this.selectionEnd;
      }
   }

   public void deleteSelection() {
      if (this.canEdit() && this.hasSelection()) {
         this.deleteSelectionInternal();
      }
   }

   public String getSelectedText() {
      if (!this.hasSelection()) {
         return "";
      } else {
         String var1 = ((TextFieldModel)this.model).getText();
         return var1.substring(this.getSelectionMin(), this.getSelectionMax());
      }
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

   protected void onAvailabilityChanged() {
      if (!this.isEnabled() || !this.isVisible()) {
         this.focused = false;
      }

      this.cursorIndex = this.clampIndex(this.cursorIndex);
      this.selectionStart = this.clampIndex(this.selectionStart);
      this.selectionEnd = this.clampIndex(this.selectionEnd);
      this.selectionAnchor = this.clampIndex(this.selectionAnchor);
      this.selecting = this.selecting && this.focused;
      if (!this.focused) {
         this.clearSelection();
      }

   }

   private void deleteSelectionInternal() {
      int var1 = this.getSelectionMin();
      int var2 = this.getSelectionMax();
      String var3 = ((TextFieldModel)this.model).getText();
      TextFieldModel var10000 = (TextFieldModel)this.model;
      String var10001 = var3.substring(0, var1);
      var10000.setText(var10001 + var3.substring(var2));
      this.cursorIndex = var1;
      this.clearSelection();
   }

   private void clearSelection() {
      this.selectionStart = this.cursorIndex;
      this.selectionEnd = this.cursorIndex;
      this.selectionAnchor = this.cursorIndex;
   }

   private void ensureSelectionAnchor() {
      if (!this.hasSelection()) {
         this.selectionAnchor = this.cursorIndex;
      }

   }

   private void updateSelectionFromAnchor() {
      this.selectionStart = this.selectionAnchor;
      this.selectionEnd = this.cursorIndex;
   }

   private boolean canEdit() {
      return this.focused && this.isEnabled() && this.isVisible();
   }

   private int clampIndex(int var1) {
      return Math.max(0, Math.min(((TextFieldModel)this.model).getText().length(), var1));
   }
}
