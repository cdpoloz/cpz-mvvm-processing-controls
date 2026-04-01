package com.cpz.processing.controls.numericfieldcontrol.style.render;

import com.cpz.processing.controls.numericfieldcontrol.style.NumericFieldRenderStyle;
import com.cpz.processing.controls.numericfieldcontrol.view.NumericFieldViewState;
import processing.core.PApplet;
import processing.core.PConstants;

public final class DefaultNumericFieldRenderer {

    public void render(PApplet sketch, NumericFieldViewState state, NumericFieldRenderStyle style) {
        float left = state.x() - (state.width() * 0.5f);
        float top = state.y() - (state.height() * 0.5f);

        sketch.pushStyle();
        sketch.rectMode(PConstants.CORNER);
        sketch.fill(style.backgroundColor());
        sketch.stroke(style.borderColor());
        sketch.rect(left, top, state.width(), state.height(), 6f);

        if (style.font() != null) {
            sketch.textFont(style.font(), style.textSize());
        } else {
            sketch.textSize(style.textSize());
        }
        sketch.textAlign(PConstants.LEFT, PConstants.CENTER);

        if (state.selectionStart() != state.selectionEnd()) {
            float selectionWidth = state.selectionEndX() - state.selectionStartX();
            sketch.noStroke();
            sketch.fill(style.selectionColor());
            sketch.rect(state.selectionStartX(), top + 4f, selectionWidth, state.height() - 8f, 4f);
        }

        sketch.fill(style.textColor());
        sketch.text(state.textBeforeSelection(), state.textX(), state.y());
        if (state.selectionStart() != state.selectionEnd()) {
            sketch.fill(style.selectionTextColor());
            sketch.text(state.selectedText(), state.selectionStartX(), state.y());
        }
        sketch.fill(style.textColor());
        sketch.text(state.textAfterSelection(), state.selectionEndX(), state.y());

        if (state.showCursor()) {
            float cursorTop = state.y() - (state.height() * 0.28f);
            float cursorBottom = state.y() + (state.height() * 0.28f);
            sketch.stroke(style.cursorColor());
            sketch.line(state.cursorX(), cursorTop, state.cursorX(), cursorBottom);
        }
        sketch.popStyle();
    }
}
