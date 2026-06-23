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
 *
 * @author CPZ
 */
public final class TextFieldViewModel extends AbstractControlViewModel implements KeyboardInputTarget {
   private static final char NO_PENDING_ACCENT = '\0';
   private final ClipboardService clipboardService = new ClipboardService();
   private int cursorIndex;
   private int selectionStart;
   private int selectionEnd;
   private int selectionAnchor;
   private boolean selecting;
   private boolean focused;
   private char pendingAccent = NO_PENDING_ACCENT;
   private Consumer<String> onTextChanged;

   /**
    * Creates a text field view model.
    *
    * @param model parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public TextFieldViewModel(TextFieldModel model) {
      super(model);
      this.cursorIndex = model.getText().length();
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
    * @param text new text
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setText(String text) {
      this.clearPendingAccent();
      ((TextFieldModel)this.model).setText(text);
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
    * @param index new cursor index
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setCursorIndex(int index) {
      this.clearPendingAccent();
      this.cursorIndex = this.clampIndex(index);
      this.clearSelection();
   }

   /**
    * Updates cursor index without selection reset.
    *
    * @param index new cursor index without selection reset
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setCursorIndexWithoutSelectionReset(int index) {
      this.clearPendingAccent();
      this.cursorIndex = this.clampIndex(index);
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
    * @param focused new focused
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setFocused(boolean focused) {
      this.focused = focused && this.isEnabled() && this.isVisible();
      if (!this.focused) {
         this.clearPendingAccent();
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
      this.clearPendingAccent();
      this.cursorIndex = this.clampIndex(this.cursorIndex);
      this.clearSelection();
      this.selecting = false;
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
      if (!this.canEdit() || Character.isISOControl(key)) {
         return;
      }

      if (this.pendingAccent != NO_PENDING_ACCENT) {
         if (this.isAccentKey(key)) {
            char accent = this.pendingAccent;
            this.pendingAccent = key;
            this.insertTextInternal(String.valueOf(accent));
            return;
         }

         if (key == ' ') {
            char accent = this.pendingAccent;
            this.clearPendingAccent();
            this.insertTextInternal(String.valueOf(accent));
            return;
         }

         char composed = this.composeAccent(this.pendingAccent, key);
         if (composed != NO_PENDING_ACCENT) {
            this.clearPendingAccent();
            this.insertTextInternal(String.valueOf(composed));
            return;
         }

         String text = String.valueOf(this.pendingAccent) + key;
         this.clearPendingAccent();
         this.insertTextInternal(text);
      } else if (this.isAccentKey(key)) {
         this.pendingAccent = key;
      } else {
         this.insertTextInternal(String.valueOf(key));
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
      this.clearPendingAccent();
      this.insertTextInternal(text);
   }

   private void insertTextInternal(String text) {
      if (this.canEdit() && text != null && !text.isEmpty()) {
         if (this.hasSelection()) {
            this.deleteSelectionInternal();
         }

         String text2 = ((TextFieldModel)this.model).getText();
         String text3 = text2.substring(0, this.cursorIndex) + text + text2.substring(this.cursorIndex);
         ((TextFieldModel)this.model).setText(text3);
         this.cursorIndex += text.length();
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
         this.clearPendingAccent();
         if (this.hasSelection()) {
            this.deleteSelection();
         } else if (this.cursorIndex != 0) {
            String text = ((TextFieldModel)this.model).getText();
            String path = text.substring(0, this.cursorIndex - 1);
            String path2 = path + text.substring(this.cursorIndex);
            ((TextFieldModel)this.model).setText(path2);
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
         this.clearPendingAccent();
         if (this.hasSelection()) {
            this.deleteSelection();
         } else {
            String text = ((TextFieldModel)this.model).getText();
            if (this.cursorIndex < text.length()) {
               String path = text.substring(0, this.cursorIndex);
               String path2 = path + text.substring(this.cursorIndex + 1);
               ((TextFieldModel)this.model).setText(path2);
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
         this.clearPendingAccent();
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
         this.clearPendingAccent();
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
         this.clearPendingAccent();
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
         this.clearPendingAccent();
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
         this.clearPendingAccent();
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
         this.clearPendingAccent();
         this.ensureSelectionAnchor();
         this.cursorIndex = Math.min(((TextFieldModel)this.model).getText().length(), this.cursorIndex + 1);
         this.updateSelectionFromAnchor();
      }
   }

   /**
    * Performs move cursor home with selection.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void moveCursorHomeWithSelection() {
      if (this.focused) {
         this.clearPendingAccent();
         this.ensureSelectionAnchor();
         this.cursorIndex = 0;
         this.updateSelectionFromAnchor();
      }
   }

   /**
    * Performs move cursor end with selection.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void moveCursorEndWithSelection() {
      if (this.focused) {
         this.clearPendingAccent();
         this.ensureSelectionAnchor();
         this.cursorIndex = ((TextFieldModel)this.model).getText().length();
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
         this.clearPendingAccent();
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
         this.clearPendingAccent();
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
         String text4 = ((TextFieldModel)this.model).getText();
         return text4.substring(this.getSelectionMin(), this.getSelectionMax());
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
         this.clearPendingAccent();
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
         this.clearPendingAccent();
         this.insertText(this.clipboardService.paste());
      }
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

   protected void onAvailabilityChanged() {
      if (!this.isEnabled() || !this.isVisible()) {
         this.focused = false;
         this.clearPendingAccent();
      }

      this.cursorIndex = this.clampIndex(this.cursorIndex);
      this.selectionStart = this.clampIndex(this.selectionStart);
      this.selectionEnd = this.clampIndex(this.selectionEnd);
      this.selectionAnchor = this.clampIndex(this.selectionAnchor);
      this.selecting = this.selecting && this.focused;
      if (!this.focused) {
         this.clearPendingAccent();
         this.clearSelection();
      }

   }

   private boolean isAccentKey(char key) {
      return key == '\u00B4' || key == '`' || key == '^' || key == '\u00A8' || key == '~';
   }

   private char composeAccent(char accent, char key) {
      switch (accent) {
         case '\u00B4':
            return this.composeAcute(key);
         case '`':
            return this.composeGrave(key);
         case '^':
            return this.composeCircumflex(key);
         case '\u00A8':
            return this.composeDiaeresis(key);
         case '~':
            return this.composeTilde(key);
         default:
            return NO_PENDING_ACCENT;
      }
   }

   private char composeAcute(char key) {
      switch (key) {
         case 'a':
            return '\u00E1';
         case 'e':
            return '\u00E9';
         case 'i':
            return '\u00ED';
         case 'o':
            return '\u00F3';
         case 'u':
            return '\u00FA';
         case 'A':
            return '\u00C1';
         case 'E':
            return '\u00C9';
         case 'I':
            return '\u00CD';
         case 'O':
            return '\u00D3';
         case 'U':
            return '\u00DA';
         default:
            return NO_PENDING_ACCENT;
      }
   }

   private char composeGrave(char key) {
      switch (key) {
         case 'a':
            return '\u00E0';
         case 'e':
            return '\u00E8';
         case 'i':
            return '\u00EC';
         case 'o':
            return '\u00F2';
         case 'u':
            return '\u00F9';
         case 'A':
            return '\u00C0';
         case 'E':
            return '\u00C8';
         case 'I':
            return '\u00CC';
         case 'O':
            return '\u00D2';
         case 'U':
            return '\u00D9';
         default:
            return NO_PENDING_ACCENT;
      }
   }

   private char composeCircumflex(char key) {
      switch (key) {
         case 'a':
            return '\u00E2';
         case 'e':
            return '\u00EA';
         case 'i':
            return '\u00EE';
         case 'o':
            return '\u00F4';
         case 'u':
            return '\u00FB';
         case 'A':
            return '\u00C2';
         case 'E':
            return '\u00CA';
         case 'I':
            return '\u00CE';
         case 'O':
            return '\u00D4';
         case 'U':
            return '\u00DB';
         default:
            return NO_PENDING_ACCENT;
      }
   }

   private char composeDiaeresis(char key) {
      switch (key) {
         case 'a':
            return '\u00E4';
         case 'e':
            return '\u00EB';
         case 'i':
            return '\u00EF';
         case 'o':
            return '\u00F6';
         case 'u':
            return '\u00FC';
         case 'A':
            return '\u00C4';
         case 'E':
            return '\u00CB';
         case 'I':
            return '\u00CF';
         case 'O':
            return '\u00D6';
         case 'U':
            return '\u00DC';
         default:
            return NO_PENDING_ACCENT;
      }
   }

   private char composeTilde(char key) {
      switch (key) {
         case 'a':
            return '\u00E3';
         case 'o':
            return '\u00F5';
         case 'n':
            return '\u00F1';
         case 'A':
            return '\u00C3';
         case 'O':
            return '\u00D5';
         case 'N':
            return '\u00D1';
         default:
            return NO_PENDING_ACCENT;
      }
   }

   private void clearPendingAccent() {
      this.pendingAccent = NO_PENDING_ACCENT;
   }

   private void deleteSelectionInternal() {
      int value = this.getSelectionMin();
      int value2 = this.getSelectionMax();
      String text = ((TextFieldModel)this.model).getText();
      TextFieldModel model = (TextFieldModel)this.model;
      String path = text.substring(0, value);
      model.setText(path + text.substring(value2));
      this.cursorIndex = value;
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

   private int clampIndex(int index) {
      return Math.max(0, Math.min(((TextFieldModel)this.model).getText().length(), index));
   }

   private void notifyTextChanged() {
      if (this.onTextChanged != null) {
         this.onTextChanged.accept(((TextFieldModel)this.model).getText());
      }
   }
}
