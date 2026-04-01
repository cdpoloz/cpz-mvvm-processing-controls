package com.cpz.processing.controls.numericfieldcontrol.viewmodel;

import com.cpz.processing.controls.common.input.ClipboardService;
import com.cpz.processing.controls.common.input.KeyboardInputTarget;
import com.cpz.processing.controls.common.viewmodel.AbstractInteractiveControlViewModel;
import com.cpz.processing.controls.numericfieldcontrol.model.NumericFieldModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.function.Consumer;

public final class NumericFieldViewModel extends AbstractInteractiveControlViewModel<NumericFieldModel> implements KeyboardInputTarget {

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

    public NumericFieldViewModel(NumericFieldModel model) {
        super(model);
        textBuffer = formatValue(model.getValue());
        cursorPosition = textBuffer.length();
        selectionStart = cursorPosition;
        selectionEnd = cursorPosition;
        selectionAnchor = cursorPosition;
        lastSyncedValue = model.getValue();
    }

    public String getText() {
        syncFromModelIfNotEditing();
        return textBuffer;
    }

    public BigDecimal getValue() {
        syncFromModelIfNotEditing();
        return model.getValue();
    }

    public boolean isEditing() {
        return editing;
    }

    public int getCursorPosition() {
        syncSelectionBounds();
        return cursorPosition;
    }

    public void setCursorPosition(int cursorPosition) {
        this.cursorPosition = clampIndex(cursorPosition);
        clearSelection();
    }

    public void setCursorPositionWithoutSelectionReset(int cursorPosition) {
        this.cursorPosition = clampIndex(cursorPosition);
    }

    public int getSelectionStart() {
        syncSelectionBounds();
        return selectionStart;
    }

    public void setSelectionStart(int selectionStart) {
        this.selectionStart = clampIndex(selectionStart);
    }

    public int getSelectionEnd() {
        syncSelectionBounds();
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

    public int getSelectionMin() {
        return Math.min(selectionStart, selectionEnd);
    }

    public int getSelectionMax() {
        return Math.max(selectionStart, selectionEnd);
    }

    public boolean hasSelection() {
        return selectionStart != selectionEnd;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused && isEditableContext();
        if (!this.focused) {
            commit();
            selecting = false;
        }
    }

    public boolean isShowCursor() {
        return focused;
    }

    public void setOnValueChanged(Consumer<BigDecimal> listener) {
        this.onValueChanged = listener;
    }

    @Override
    public void onFocusGained() {
        focused = isEditableContext();
        syncFromModelIfNotEditing();
        selecting = false;
    }

    @Override
    public void onFocusLost() {
        focused = false;
        commit();
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
        if (!isInsertAllowed(text)) {
            return;
        }
        if (hasSelection()) {
            deleteSelectionInternal();
        }
        String nextText = textBuffer.substring(0, cursorPosition) + text + textBuffer.substring(cursorPosition);
        if (!isCandidateTextAllowed(nextText)) {
            return;
        }
        textBuffer = nextText;
        cursorPosition += text.length();
        clearSelection();
        editing = true;
        updateModelFromBufferIfParsable();
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
        if (cursorPosition == 0) {
            return;
        }
        textBuffer = textBuffer.substring(0, cursorPosition - 1) + textBuffer.substring(cursorPosition);
        cursorPosition--;
        clearSelection();
        editing = true;
        updateModelFromBufferIfParsable();
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
        if (cursorPosition >= textBuffer.length()) {
            return;
        }
        textBuffer = textBuffer.substring(0, cursorPosition) + textBuffer.substring(cursorPosition + 1);
        clearSelection();
        editing = true;
        updateModelFromBufferIfParsable();
    }

    @Override
    public void moveCursorLeft() {
        if (!focused) {
            return;
        }
        cursorPosition = hasSelection() ? getSelectionMin() : Math.max(0, cursorPosition - 1);
        clearSelection();
    }

    @Override
    public void moveCursorRight() {
        if (!focused) {
            return;
        }
        cursorPosition = hasSelection() ? getSelectionMax() : Math.min(textBuffer.length(), cursorPosition + 1);
        clearSelection();
    }

    @Override
    public void moveCursorLeftWithSelection() {
        if (!focused) {
            return;
        }
        ensureSelectionAnchor();
        cursorPosition = Math.max(0, cursorPosition - 1);
        updateSelectionFromAnchor();
    }

    @Override
    public void moveCursorRightWithSelection() {
        if (!focused) {
            return;
        }
        ensureSelectionAnchor();
        cursorPosition = Math.min(textBuffer.length(), cursorPosition + 1);
        updateSelectionFromAnchor();
    }

    @Override
    public void selectAll() {
        if (!focused) {
            return;
        }
        selectionAnchor = 0;
        selectionStart = 0;
        selectionEnd = textBuffer.length();
        cursorPosition = textBuffer.length();
    }

    @Override
    public void deleteSelection() {
        if (!canEdit() || !hasSelection()) {
            return;
        }
        deleteSelectionInternal();
        editing = true;
        updateModelFromBufferIfParsable();
    }

    @Override
    public String getSelectedText() {
        if (!hasSelection()) {
            return "";
        }
        return textBuffer.substring(getSelectionMin(), getSelectionMax());
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
    public void increment(boolean shiftDown, boolean ctrlDown) {
        applyStep(resolveAdjustedStep(shiftDown, ctrlDown));
    }

    @Override
    public void decrement(boolean shiftDown, boolean ctrlDown) {
        applyStep(resolveAdjustedStep(shiftDown, ctrlDown).negate());
    }

    public void onMouseWheel(float delta, boolean shiftDown, boolean ctrlDown) {
        if (!focused || !isInteractive() || delta == 0f) {
            return;
        }
        if (delta < 0f) {
            increment(shiftDown, ctrlDown);
        } else {
            decrement(shiftDown, ctrlDown);
        }
    }

    public void setValue(BigDecimal value) {
        BigDecimal previousValue = model.getValue();
        model.setValue(clampAndNormalize(value == null ? fallbackForEmptyCommit() : value));
        notifyValueChangedIfNeeded(previousValue, model.getValue());
        syncFromModelIfNotEditing();
    }

    @Override
    protected void activate() {
        // NumericField does not trigger an action on release.
    }

    private void applyStep(BigDecimal delta) {
        if (!focused || !isInteractive()) {
            return;
        }
        if (editing) {
            commitValue();
        }
        BigDecimal nextValue = model.getValue().add(delta);
        setCommittedValue(nextValue);
    }

    private void setCommittedValue(BigDecimal rawValue) {
        BigDecimal previousValue = model.getValue();
        BigDecimal normalized = clampAndNormalize(rawValue == null ? fallbackForEmptyCommit() : rawValue);
        model.setValue(normalized);
        notifyValueChangedIfNeeded(previousValue, model.getValue());
        textBuffer = formatValue(model.getValue());
        editing = false;
        lastSyncedValue = model.getValue();
        cursorPosition = textBuffer.length();
        clearSelection();
    }

    private void updateModelFromBufferIfParsable() {
        if (isIntermediateValidState(textBuffer)) {
            return;
        }
        BigDecimal parsed = tryParse(textBuffer);
        if (parsed == null) {
            return;
        }
        BigDecimal previousValue = model.getValue();
        model.setValue(clamp(parsed));
        lastSyncedValue = model.getValue();
        notifyValueChangedIfNeeded(previousValue, model.getValue());
    }

    private void syncFromModelIfNotEditing() {
        if (editing) {
            return;
        }
        String formattedValue = formatValue(model.getValue());
        textBuffer = formattedValue;
        if (lastSyncedValue == null || model.getValue().compareTo(lastSyncedValue) != 0) {
            lastSyncedValue = model.getValue();
            cursorPosition = Math.min(cursorPosition, textBuffer.length());
            clearSelection();
        }
    }

    private boolean isInsertAllowed(String insertedText) {
        int insertionPosition = cursorPosition;
        String candidate = hasSelection()
                ? textBuffer.substring(0, getSelectionMin()) + textBuffer.substring(getSelectionMax())
                : textBuffer;
        if (hasSelection()) {
            insertionPosition = getSelectionMin();
        }
        for (int i = 0; i < insertedText.length(); i++) {
            char character = insertedText.charAt(i);
            if (!isValidInsertion(character, insertionPosition, candidate)) {
                return false;
            }
            candidate = candidate.substring(0, insertionPosition) + character + candidate.substring(insertionPosition);
            insertionPosition++;
        }
        return true;
    }

    private boolean isCharacterAllowed(char character) {
        if (Character.isDigit(character)) {
            return true;
        }
        if (character == '-' && model.isAllowNegative()) {
            return true;
        }
        return character == '.' && model.isAllowDecimal();
    }

    private boolean isCandidateTextAllowed(String candidateText) {
        if (candidateText == null) {
            return false;
        }
        int decimalCount = 0;
        int minusCount = 0;
        for (int i = 0; i < candidateText.length(); i++) {
            char character = candidateText.charAt(i);
            if (Character.isDigit(character)) {
                continue;
            }
            if (character == '.') {
                decimalCount++;
                if (!model.isAllowDecimal() || decimalCount > 1) {
                    return false;
                }
                continue;
            }
            if (character == '-') {
                minusCount++;
                if (!model.isAllowNegative() || minusCount > 1 || i != 0) {
                    return false;
                }
                continue;
            }
            return false;
        }
        return true;
    }

    private boolean isIntermediateValidState(String text) {
        return Objects.equals(text, "")
                || Objects.equals(text, "-")
                || Objects.equals(text, ".")
                || Objects.equals(text, "-.")
                || Objects.equals(text, "0.")
                || Objects.equals(text, "-0.");
    }

    private BigDecimal tryParse(String candidateText) {
        if (candidateText == null) {
            return null;
        }
        String trimmed = candidateText.trim();
        if (isIntermediateValidState(trimmed)) {
            return null;
        }
        try {
            BigDecimal parsed = new BigDecimal(trimmed);
            if (!model.isAllowDecimal()) {
                parsed = parsed.setScale(0, RoundingMode.DOWN);
            }
            return parsed;
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private BigDecimal fallbackForEmptyCommit() {
        if (model.getMin() != null) {
            return clampAndNormalize(model.getMin());
        }
        return clampAndNormalize(BigDecimal.ZERO);
    }

    private BigDecimal fallbackForInvalidIntermediateCommit() {
        return clampAndNormalize(lastSyncedValue == null ? model.getValue() : lastSyncedValue);
    }

    private void commitValue() {
        String candidate = textBuffer == null ? "" : textBuffer.trim();
        BigDecimal committedValue;
        if (candidate.isEmpty()) {
            committedValue = fallbackForEmptyCommit();
        } else if ("-".equals(candidate) || ".".equals(candidate) || "-.".equals(candidate)) {
            committedValue = fallbackForInvalidIntermediateCommit();
        } else {
            BigDecimal parsed = tryParse(candidate);
            committedValue = parsed == null ? fallbackForInvalidIntermediateCommit() : parsed;
        }
        setCommittedValue(committedValue);
    }

    private BigDecimal resolveAdjustedStep(boolean shiftDown, boolean ctrlDown) {
        BigDecimal baseStep = model.getStep();
        if (shiftDown) {
            return baseStep.multiply(BigDecimal.TEN);
        }
        if (ctrlDown) {
            return baseStep.divide(BigDecimal.TEN, Math.max(model.getScale() + 1, 1), RoundingMode.HALF_UP);
        }
        return baseStep;
    }

    private BigDecimal clampAndNormalize(BigDecimal value) {
        return clamp(value).setScale(model.getScale(), RoundingMode.HALF_UP);
    }

    private BigDecimal clamp(BigDecimal value) {
        BigDecimal candidate = value;
        if (candidate.compareTo(model.getMin()) < 0) {
            candidate = model.getMin();
        }
        if (candidate.compareTo(model.getMax()) > 0) {
            candidate = model.getMax();
        }
        return candidate;
    }

    private String formatValue(BigDecimal value) {
        return value.setScale(model.getScale(), RoundingMode.HALF_UP).toPlainString();
    }

    private boolean isValidInsertion(char character, int position, String candidate) {
        if (!isCharacterAllowed(character)) {
            return false;
        }
        if (character == '-') {
            return position == 0 && candidate.indexOf('-') < 0;
        }
        if (character == '.') {
            return candidate.indexOf('.') < 0;
        }
        return true;
    }

    private void notifyValueChangedIfNeeded(BigDecimal previousValue, BigDecimal nextValue) {
        if (previousValue == null || nextValue == null) {
            return;
        }
        if (previousValue.compareTo(nextValue) == 0) {
            return;
        }
        if (onValueChanged != null) {
            onValueChanged.accept(nextValue);
        }
    }

    private void deleteSelectionInternal() {
        int selectionMin = getSelectionMin();
        int selectionMax = getSelectionMax();
        textBuffer = textBuffer.substring(0, selectionMin) + textBuffer.substring(selectionMax);
        cursorPosition = selectionMin;
        clearSelection();
    }

    private void clearSelection() {
        selectionStart = cursorPosition;
        selectionEnd = cursorPosition;
        selectionAnchor = cursorPosition;
    }

    private void ensureSelectionAnchor() {
        if (!hasSelection()) {
            selectionAnchor = cursorPosition;
        }
    }

    private void updateSelectionFromAnchor() {
        selectionStart = selectionAnchor;
        selectionEnd = cursorPosition;
    }

    private void syncSelectionBounds() {
        cursorPosition = clampIndex(cursorPosition);
        selectionStart = clampIndex(selectionStart);
        selectionEnd = clampIndex(selectionEnd);
        selectionAnchor = clampIndex(selectionAnchor);
    }

    private int clampIndex(int requestedIndex) {
        return Math.max(0, Math.min(textBuffer.length(), requestedIndex));
    }

    private boolean canEdit() {
        return focused && isEditableContext();
    }

    private boolean isEditableContext() {
        return isEnabled() && isVisible();
    }

    @Override
    public void commit() {
        commitValue();
    }
}
