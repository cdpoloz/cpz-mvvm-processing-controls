package com.cpz.processing.controls.textfieldcontrol.viewmodel;

import com.cpz.processing.controls.common.input.KeyboardInputTarget;
import com.cpz.processing.controls.common.viewmodel.AbstractControlViewModel;
import com.cpz.processing.controls.textfieldcontrol.model.TextFieldModel;

public final class TextFieldViewModel extends AbstractControlViewModel<TextFieldModel> implements KeyboardInputTarget {

    private final com.cpz.processing.controls.common.input.ClipboardService clipboardService =
            new com.cpz.processing.controls.common.input.ClipboardService();
    private int cursorIndex;
    private int selectionStart;
    private int selectionEnd;
    private int selectionAnchor;
    private boolean selecting;
    private boolean focused;

    public TextFieldViewModel(TextFieldModel model) {
        super(model);
        cursorIndex = model.getText().length();
        selectionStart = cursorIndex;
        selectionEnd = cursorIndex;
        selectionAnchor = cursorIndex;
    }

    public String getText() {
        return model.getText();
    }

    public void setText(String text) {
        model.setText(text);
        cursorIndex = clampIndex(cursorIndex);
        clearSelection();
    }

    public int getCursorIndex() {
        return cursorIndex;
    }

    public void setCursorIndex(int cursorIndex) {
        this.cursorIndex = clampIndex(cursorIndex);
        clearSelection();
    }

    public void setCursorIndexWithoutSelectionReset(int cursorIndex) {
        this.cursorIndex = clampIndex(cursorIndex);
    }

    public int getSelectionStart() {
        return selectionStart;
    }

    public void setSelectionStart(int selectionStart) {
        this.selectionStart = clampIndex(selectionStart);
    }

    public int getSelectionEnd() {
        return selectionEnd;
    }

    public void setSelectionEnd(int selectionEnd) {
        this.selectionEnd = clampIndex(selectionEnd);
    }

    public void setSelectionAnchor(int selectionAnchor) {
        this.selectionAnchor = clampIndex(selectionAnchor);
    }

    public boolean isSelecting() {
        return selecting;
    }

    public void setSelecting(boolean selecting) {
        this.selecting = selecting && focused && isEnabled() && isVisible();
    }

    public boolean hasSelection() {
        return selectionStart != selectionEnd;
    }

    public int getSelectionMin() {
        return Math.min(selectionStart, selectionEnd);
    }

    public int getSelectionMax() {
        return Math.max(selectionStart, selectionEnd);
    }

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused && isEnabled() && isVisible();
        if (!this.focused) {
            cursorIndex = clampIndex(cursorIndex);
            clearSelection();
            selecting = false;
        }
    }

    public boolean isShowCursor() {
        return focused;
    }

    @Override
    public void onFocusGained() {
        focused = isEnabled() && isVisible();
        cursorIndex = clampIndex(cursorIndex);
        clearSelection();
        selecting = false;
    }

    @Override
    public void onFocusLost() {
        focused = false;
        cursorIndex = clampIndex(cursorIndex);
        clearSelection();
        selecting = false;
    }

    @Override
    public void onKeyTyped(char character) {
        if (!Character.isISOControl(character)) {
            insertText(String.valueOf(character));
        }
    }

    @Override
    public void insertText(String text) {
        if (!canEdit() || text == null || text.isEmpty()) {
            return;
        }
        if (hasSelection()) {
            deleteSelectionInternal();
        }
        String currentText = model.getText();
        String nextText = currentText.substring(0, cursorIndex) + text + currentText.substring(cursorIndex);
        model.setText(nextText);
        cursorIndex += text.length();
        clearSelection();
    }

    @Override
    public void backspace() {
        if (!canEdit()) {
            return;
        }
        if (hasSelection()) {
            deleteSelection();
            return;
        }
        if (cursorIndex == 0) {
            return;
        }
        String text = model.getText();
        String nextText = text.substring(0, cursorIndex - 1) + text.substring(cursorIndex);
        model.setText(nextText);
        cursorIndex--;
        clearSelection();
    }

    @Override
    public void deleteForward() {
        if (!canEdit()) {
            return;
        }
        if (hasSelection()) {
            deleteSelection();
            return;
        }
        String text = model.getText();
        if (cursorIndex >= text.length()) {
            return;
        }
        String nextText = text.substring(0, cursorIndex) + text.substring(cursorIndex + 1);
        model.setText(nextText);
        clearSelection();
    }

    @Override
    public void moveCursorLeft() {
        if (!focused) {
            return;
        }
        cursorIndex = hasSelection() ? getSelectionMin() : Math.max(0, cursorIndex - 1);
        clearSelection();
    }

    @Override
    public void moveCursorRight() {
        if (!focused) {
            return;
        }
        cursorIndex = hasSelection() ? getSelectionMax() : Math.min(model.getText().length(), cursorIndex + 1);
        clearSelection();
    }

    @Override
    public void moveCursorLeftWithSelection() {
        if (!focused) {
            return;
        }
        ensureSelectionAnchor();
        cursorIndex = Math.max(0, cursorIndex - 1);
        updateSelectionFromAnchor();
    }

    @Override
    public void moveCursorRightWithSelection() {
        if (!focused) {
            return;
        }
        ensureSelectionAnchor();
        cursorIndex = Math.min(model.getText().length(), cursorIndex + 1);
        updateSelectionFromAnchor();
    }

    @Override
    public void selectAll() {
        if (!focused) {
            return;
        }
        selectionAnchor = 0;
        selectionStart = 0;
        selectionEnd = model.getText().length();
        cursorIndex = selectionEnd;
    }

    @Override
    public void deleteSelection() {
        if (!canEdit() || !hasSelection()) {
            return;
        }
        deleteSelectionInternal();
    }

    @Override
    public String getSelectedText() {
        if (!hasSelection()) {
            return "";
        }
        String text = model.getText();
        return text.substring(getSelectionMin(), getSelectionMax());
    }

    @Override
    public void copySelection() {
        clipboardService.copy(getSelectedText());
    }

    @Override
    public void cutSelection() {
        if (!canEdit()) {
            return;
        }
        clipboardService.copy(getSelectedText());
        deleteSelection();
    }

    @Override
    public void pasteFromClipboard() {
        if (!canEdit()) {
            return;
        }
        insertText(clipboardService.paste());
    }

    @Override
    protected void onAvailabilityChanged() {
        if (!isEnabled() || !isVisible()) {
            focused = false;
        }
        cursorIndex = clampIndex(cursorIndex);
        selectionStart = clampIndex(selectionStart);
        selectionEnd = clampIndex(selectionEnd);
        selectionAnchor = clampIndex(selectionAnchor);
        selecting = selecting && focused;
        if (!focused) {
            clearSelection();
        }
    }

    private void deleteSelectionInternal() {
        int selectionMin = getSelectionMin();
        int selectionMax = getSelectionMax();
        String text = model.getText();
        model.setText(text.substring(0, selectionMin) + text.substring(selectionMax));
        cursorIndex = selectionMin;
        clearSelection();
    }

    private void clearSelection() {
        selectionStart = cursorIndex;
        selectionEnd = cursorIndex;
        selectionAnchor = cursorIndex;
    }

    private void ensureSelectionAnchor() {
        if (!hasSelection()) {
            selectionAnchor = cursorIndex;
        }
    }

    private void updateSelectionFromAnchor() {
        selectionStart = selectionAnchor;
        selectionEnd = cursorIndex;
    }

    private boolean canEdit() {
        return focused && isEnabled() && isVisible();
    }

    private int clampIndex(int requestedIndex) {
        return Math.max(0, Math.min(model.getText().length(), requestedIndex));
    }
}
