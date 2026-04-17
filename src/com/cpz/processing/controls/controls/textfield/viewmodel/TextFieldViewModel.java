package com.cpz.processing.controls.controls.textfield.viewmodel;

import com.cpz.processing.controls.controls.textfield.model.TextFieldModel;
import com.cpz.processing.controls.core.input.ClipboardService;
import com.cpz.processing.controls.core.input.KeyboardInputTarget;
import com.cpz.processing.controls.core.viewmodel.AbstractControlViewModel;

import java.util.function.Consumer;

/**
 * ViewModel for text field view model.
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
public final class TextFieldViewModel extends AbstractControlViewModel implements KeyboardInputTarget {
   private final ClipboardService clipboardService = new ClipboardService();
   private int cursorIndex;
   private int selectionStart;
   private int selectionEnd;
   private int selectionAnchor;
   private boolean selecting;
   private boolean focused;
   private Consumer<String> onTextChanged;

   /**
    * Creates a text field view model.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public TextFieldViewModel(TextFieldModel var1) {
      super(var1);
      this.cursorIndex = var1.getText().length();
      this.selectionStart = this.cursorIndex;
      this.selectionEnd = this.cursorIndex;
      this.selectionAnchor = this.cursorIndex;
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
      return ((TextFieldModel)this.model).getText();
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
      ((TextFieldModel)this.model).setText(var1);
      this.cursorIndex = this.clampIndex(this.cursorIndex);
      this.clearSelection();
      this.notifyTextChanged();
   }

   /**
    * Returns cursor index.
    *
    * @return current cursor index
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getCursorIndex() {
      return this.cursorIndex;
   }

   /**
    * Updates cursor index.
    *
    * @param var1 new cursor index
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setCursorIndex(int var1) {
      this.cursorIndex = this.clampIndex(var1);
      this.clearSelection();
   }

   /**
    * Updates cursor index without selection reset.
    *
    * @param var1 new cursor index without selection reset
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setCursorIndexWithoutSelectionReset(int var1) {
      this.cursorIndex = this.clampIndex(var1);
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
      this.focused = var1 && this.isEnabled() && this.isVisible();
      if (!this.focused) {
         this.cursorIndex = this.clampIndex(this.cursorIndex);
         this.clearSelection();
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
    * Handles focus gained.
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onFocusGained() {
      this.focused = this.isEnabled() && this.isVisible();
      this.cursorIndex = this.clampIndex(this.cursorIndex);
      this.clearSelection();
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
      this.cursorIndex = this.clampIndex(this.cursorIndex);
      this.clearSelection();
      this.selecting = false;
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
         if (this.hasSelection()) {
            this.deleteSelectionInternal();
         }

         String var2 = ((TextFieldModel)this.model).getText();
         String var3 = var2.substring(0, this.cursorIndex) + var1 + var2.substring(this.cursorIndex);
         ((TextFieldModel)this.model).setText(var3);
         this.cursorIndex += var1.length();
         this.clearSelection();
         this.notifyTextChanged();
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
         } else if (this.cursorIndex != 0) {
            String var1 = ((TextFieldModel)this.model).getText();
            String var10000 = var1.substring(0, this.cursorIndex - 1);
            String var2 = var10000 + var1.substring(this.cursorIndex);
            ((TextFieldModel)this.model).setText(var2);
            --this.cursorIndex;
            this.clearSelection();
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
         } else {
            String var1 = ((TextFieldModel)this.model).getText();
            if (this.cursorIndex < var1.length()) {
               String var10000 = var1.substring(0, this.cursorIndex);
               String var2 = var10000 + var1.substring(this.cursorIndex + 1);
               ((TextFieldModel)this.model).setText(var2);
               this.clearSelection();
               this.notifyTextChanged();
            }
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
         this.cursorIndex = this.hasSelection() ? this.getSelectionMin() : Math.max(0, this.cursorIndex - 1);
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
         this.cursorIndex = this.hasSelection() ? this.getSelectionMax() : Math.min(((TextFieldModel)this.model).getText().length(), this.cursorIndex + 1);
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
         this.cursorIndex = 0;
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
         this.cursorIndex = ((TextFieldModel)this.model).getText().length();
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
         this.cursorIndex = Math.max(0, this.cursorIndex - 1);
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
         this.cursorIndex = Math.min(((TextFieldModel)this.model).getText().length(), this.cursorIndex + 1);
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
         this.selectionEnd = ((TextFieldModel)this.model).getText().length();
         this.cursorIndex = this.selectionEnd;
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
      if (!this.hasSelection()) {
         return "";
      } else {
         String var1 = ((TextFieldModel)this.model).getText();
         return var1.substring(this.getSelectionMin(), this.getSelectionMax());
      }
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

   private void notifyTextChanged() {
      if (this.onTextChanged != null) {
         this.onTextChanged.accept(((TextFieldModel)this.model).getText());
      }
   }
}
