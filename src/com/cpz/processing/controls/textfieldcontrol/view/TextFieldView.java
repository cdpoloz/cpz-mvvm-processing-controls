package com.cpz.processing.controls.textfieldcontrol.view;

import com.cpz.processing.controls.common.ControlView;
import com.cpz.processing.controls.common.input.PointerInteractable;
import com.cpz.processing.controls.textfieldcontrol.style.TextFieldDefaultStyles;
import com.cpz.processing.controls.textfieldcontrol.style.TextFieldRenderStyle;
import com.cpz.processing.controls.textfieldcontrol.style.interfaces.TextFieldStyle;
import com.cpz.processing.controls.textfieldcontrol.viewmodel.TextFieldViewModel;
import processing.core.PApplet;
import processing.core.PConstants;

public final class TextFieldView implements ControlView, PointerInteractable {

    private static final float HORIZONTAL_PADDING = 10f;
    private static final long DOUBLE_CLICK_THRESHOLD = 250L;

    private final PApplet sketch;
    private final TextFieldViewModel viewModel;
    private float x;
    private float y;
    private float width;
    private float height;
    private TextFieldStyle style;
    private long lastClickTime;
    private int clickCount;

    public TextFieldView(PApplet sketch,
                         TextFieldViewModel viewModel,
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
        this.style = TextFieldDefaultStyles.standard();
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

    public void setStyle(TextFieldStyle style) {
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
            selectWordAt(index);
            viewModel.setSelecting(false);
            return;
        }

        viewModel.setSelectionAnchor(index);
        viewModel.setCursorIndexWithoutSelectionReset(index);
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
        viewModel.setCursorIndexWithoutSelectionReset(index);
    }

    public void handleMouseRelease() {
        viewModel.setSelecting(false);
    }

    private TextFieldViewState buildViewState() {
        String text = viewModel.getText();
        float textX = getTextStartX();
        int selectionMin = viewModel.getSelectionMin();
        int selectionMax = viewModel.getSelectionMax();
        String textBeforeSelection = text.substring(0, selectionMin);
        String selectedText = text.substring(selectionMin, selectionMax);
        String textAfterSelection = text.substring(selectionMax);
        float selectionStartX = textX + measureTextWidth(textBeforeSelection);
        float selectionEndX = selectionStartX + measureTextWidth(selectedText);
        float cursorX = textX + measureTextWidth(text.substring(0, viewModel.getCursorIndex()));
        return new TextFieldViewState(
                x,
                y,
                width,
                height,
                text,
                textBeforeSelection,
                selectedText,
                textAfterSelection,
                viewModel.getCursorIndex(),
                viewModel.getSelectionStart(),
                viewModel.getSelectionEnd(),
                viewModel.isFocused(),
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

    private void selectWordAt(int rawIndex) {
        String text = viewModel.getText();
        if (text.isEmpty()) {
            int index = 0;
            viewModel.setSelectionAnchor(index);
            viewModel.setSelectionStart(index);
            viewModel.setSelectionEnd(index);
            viewModel.setCursorIndexWithoutSelectionReset(index);
            return;
        }

        int index = Math.max(0, Math.min(rawIndex, text.length() - 1));
        int start = index;
        int end = index + 1;

        if (!Character.isWhitespace(text.charAt(index))) {
            while (start > 0 && !Character.isWhitespace(text.charAt(start - 1))) {
                start--;
            }
            while (end < text.length() && !Character.isWhitespace(text.charAt(end))) {
                end++;
            }
        }

        viewModel.setSelectionAnchor(start);
        viewModel.setSelectionStart(start);
        viewModel.setSelectionEnd(end);
        viewModel.setCursorIndexWithoutSelectionReset(end);
    }

    private float measureTextWidth(String text) {
        TextFieldRenderStyle renderStyle = style.resolveRenderStyle(buildMeasureState());
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

    private TextFieldViewState buildMeasureState() {
        return new TextFieldViewState(
                x,
                y,
                width,
                height,
                viewModel.getText(),
                "",
                "",
                "",
                viewModel.getCursorIndex(),
                viewModel.getSelectionStart(),
                viewModel.getSelectionEnd(),
                viewModel.isFocused(),
                viewModel.isShowCursor(),
                viewModel.isEnabled(),
                getTextStartX(),
                getTextStartX(),
                getTextStartX(),
                getTextStartX()
        );
    }
}
