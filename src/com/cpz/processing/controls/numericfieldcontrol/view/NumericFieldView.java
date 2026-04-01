package com.cpz.processing.controls.numericfieldcontrol.view;

import com.cpz.processing.controls.common.ControlView;
import com.cpz.processing.controls.common.input.PointerInteractable;
import com.cpz.processing.controls.numericfieldcontrol.style.NumericFieldDefaultStyles;
import com.cpz.processing.controls.numericfieldcontrol.style.NumericFieldRenderStyle;
import com.cpz.processing.controls.numericfieldcontrol.style.NumericFieldStyle;
import com.cpz.processing.controls.numericfieldcontrol.viewmodel.NumericFieldViewModel;
import processing.core.PApplet;
import processing.core.PConstants;

public final class NumericFieldView implements ControlView, PointerInteractable {

    private static final float HORIZONTAL_PADDING = 10f;
    private static final long DOUBLE_CLICK_THRESHOLD = 250L;

    private final PApplet sketch;
    private final NumericFieldViewModel viewModel;
    private float x;
    private float y;
    private float width;
    private float height;
    private NumericFieldStyle style;
    private long lastClickTime;
    private int clickCount;

    public NumericFieldView(PApplet sketch,
                            NumericFieldViewModel viewModel,
                            float x,
                            float y,
                            float width,
                            float height) {
        this.sketch = sketch;
        this.viewModel = viewModel;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.style = NumericFieldDefaultStyles.standard();
    }

    @Override
    public void draw() {
        if (!viewModel.isVisible()) {
            return;
        }
        style.render(sketch, buildViewState());
    }

    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void setStyle(NumericFieldStyle style) {
        if (style != null) {
            this.style = style;
        }
    }

    @Override
    public boolean contains(float px, float py) {
        float halfWidth = width * 0.5f;
        float halfHeight = height * 0.5f;
        return px >= x - halfWidth
                && px <= x + halfWidth
                && py >= y - halfHeight
                && py <= y + halfHeight;
    }

    public int positionToCursorIndex(float mouseX) {
        String text = viewModel.getText();
        float localX = mouseX - getTextStartX();
        if (localX <= 0f || text.isEmpty()) {
            return 0;
        }

        float previousWidth = 0f;
        for (int i = 0; i < text.length(); i++) {
            float currentWidth = measureTextWidth(text.substring(0, i + 1));
            float midpoint = previousWidth + ((currentWidth - previousWidth) * 0.5f);
            if (localX < midpoint) {
                return i;
            }
            previousWidth = currentWidth;
        }
        return text.length();
    }

    public void handleMousePress(float mouseX) {
        int index = positionToCursorIndex(mouseX);
        updateClickCount();

        if (clickCount >= 3) {
            viewModel.selectAll();
            viewModel.setSelecting(false);
            clickCount = 0;
            return;
        }

        if (clickCount == 2) {
            selectTokenAt(index);
            viewModel.setSelecting(false);
            return;
        }

        viewModel.setSelectionAnchor(index);
        viewModel.setCursorPositionWithoutSelectionReset(index);
        viewModel.setSelectionStart(index);
        viewModel.setSelectionEnd(index);
        viewModel.setSelecting(true);
    }

    public void handleMouseDrag(float mouseX) {
        if (!viewModel.isSelecting()) {
            return;
        }
        int index = positionToCursorIndex(mouseX);
        viewModel.setSelectionEnd(index);
        viewModel.setCursorPositionWithoutSelectionReset(index);
    }

    public void handleMouseRelease() {
        viewModel.setSelecting(false);
    }

    private NumericFieldViewState buildViewState() {
        String text = viewModel.getText();
        float textX = getTextStartX();
        int selectionMin = viewModel.getSelectionMin();
        int selectionMax = viewModel.getSelectionMax();
        String textBeforeSelection = text.substring(0, selectionMin);
        String selectedText = text.substring(selectionMin, selectionMax);
        String textAfterSelection = text.substring(selectionMax);
        float selectionStartX = textX + measureTextWidth(textBeforeSelection);
        float selectionEndX = selectionStartX + measureTextWidth(selectedText);
        float cursorX = textX + measureTextWidth(text.substring(0, viewModel.getCursorPosition()));
        return new NumericFieldViewState(
                x,
                y,
                width,
                height,
                text,
                textBeforeSelection,
                selectedText,
                textAfterSelection,
                viewModel.getCursorPosition(),
                viewModel.getSelectionStart(),
                viewModel.getSelectionEnd(),
                viewModel.isFocused(),
                viewModel.isHovered(),
                viewModel.isPressed(),
                viewModel.isEditing(),
                viewModel.isShowCursor(),
                viewModel.isEnabled(),
                textX,
                cursorX,
                selectionStartX,
                selectionEndX
        );
    }

    private float getTextStartX() {
        return x - (width * 0.5f) + HORIZONTAL_PADDING;
    }

    private void updateClickCount() {
        long now = System.currentTimeMillis();
        if (now - lastClickTime < DOUBLE_CLICK_THRESHOLD) {
            clickCount++;
        } else {
            clickCount = 1;
        }
        lastClickTime = now;
    }

    private void selectTokenAt(int rawIndex) {
        String text = viewModel.getText();
        if (text.isEmpty()) {
            viewModel.setSelectionAnchor(0);
            viewModel.setSelectionStart(0);
            viewModel.setSelectionEnd(0);
            viewModel.setCursorPositionWithoutSelectionReset(0);
            return;
        }
        int index = Math.max(0, Math.min(rawIndex, text.length() - 1));
        int start = index;
        int end = index + 1;
        while (start > 0 && isTokenChar(text.charAt(start - 1))) {
            start--;
        }
        while (end < text.length() && isTokenChar(text.charAt(end))) {
            end++;
        }
        viewModel.setSelectionAnchor(start);
        viewModel.setSelectionStart(start);
        viewModel.setSelectionEnd(end);
        viewModel.setCursorPositionWithoutSelectionReset(end);
    }

    private boolean isTokenChar(char character) {
        return Character.isDigit(character) || character == '.' || character == '-';
    }

    private float measureTextWidth(String text) {
        NumericFieldRenderStyle renderStyle = style.resolveRenderStyle(buildMeasureState());
        sketch.pushStyle();
        if (renderStyle.font() != null) {
            sketch.textFont(renderStyle.font(), renderStyle.textSize());
        } else {
            sketch.textSize(renderStyle.textSize());
        }
        sketch.textAlign(PConstants.LEFT, PConstants.CENTER);
        float measuredWidth = sketch.textWidth(text == null ? "" : text);
        sketch.popStyle();
        return measuredWidth;
    }

    private NumericFieldViewState buildMeasureState() {
        float textStart = getTextStartX();
        return new NumericFieldViewState(
                x,
                y,
                width,
                height,
                viewModel.getText(),
                "",
                "",
                "",
                viewModel.getCursorPosition(),
                viewModel.getSelectionStart(),
                viewModel.getSelectionEnd(),
                viewModel.isFocused(),
                viewModel.isHovered(),
                viewModel.isPressed(),
                viewModel.isEditing(),
                viewModel.isShowCursor(),
                viewModel.isEnabled(),
                textStart,
                textStart,
                textStart,
                textStart
        );
    }
}
