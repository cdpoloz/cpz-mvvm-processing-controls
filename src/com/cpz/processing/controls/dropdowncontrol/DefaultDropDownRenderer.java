package com.cpz.processing.controls.dropdowncontrol;

import processing.core.PApplet;
import processing.core.PConstants;

public final class DefaultDropDownRenderer {

    public void render(PApplet sketch, DropDownViewState state, DropDownRenderStyle style) {
        float left = state.x() - (state.width() * 0.5f);
        float top = state.y() - (state.height() * 0.5f);
        float right = left + state.width();

        sketch.pushStyle();
        sketch.rectMode(PConstants.CORNER);
        applyTypography(sketch, style);

        sketch.stroke(style.strokeColor());
        sketch.strokeWeight(style.strokeWeight());
        sketch.fill(style.baseFillColor());
        sketch.rect(left, top, state.width(), state.height(), style.cornerRadius());

        sketch.fill(style.textColor());
        sketch.textAlign(PConstants.LEFT, PConstants.CENTER);
        sketch.text(state.selectedText(), left + style.textPadding(), state.y());

        drawArrow(sketch, right - style.arrowPadding(), state.y(), style.arrowColor(), state.expanded());

        if (state.expanded()) {
            drawExpandedList(sketch, state, style, left, top);
        }

        sketch.popStyle();
    }

    private void drawExpandedList(PApplet sketch,
                                  DropDownViewState state,
                                  DropDownRenderStyle style,
                                  float left,
                                  float top) {
        int visibleItems = Math.min(state.items().size(), style.maxVisibleItems());
        if (visibleItems <= 0) {
            return;
        }

        float listTop = top + state.height();
        float listHeight = visibleItems * style.itemHeight();

        sketch.stroke(style.strokeColor());
        sketch.strokeWeight(style.strokeWeight());
        sketch.fill(style.listFillColor());
        sketch.rect(left, listTop, state.width(), listHeight, style.listCornerRadius());

        sketch.textAlign(PConstants.LEFT, PConstants.CENTER);
        for (int index = 0; index < visibleItems; index++) {
            float itemTop = listTop + (index * style.itemHeight());
            boolean hoveredItem = index == state.hoveredIndex();
            boolean selectedItem = index == state.selectedIndex();

            if (selectedItem || hoveredItem) {
                sketch.noStroke();
                sketch.fill(selectedItem ? style.itemSelectedColor() : style.itemHoverColor());
                sketch.rect(left + 1f, itemTop + 1f, state.width() - 2f, style.itemHeight() - 2f, 4f);
            }

            sketch.fill(style.textColor());
            sketch.text(state.items().get(index), left + style.textPadding(), itemTop + (style.itemHeight() * 0.5f));
        }
    }

    private void drawArrow(PApplet sketch, float centerX, float centerY, int color, boolean expanded) {
        float arrowWidth = 10f;
        float arrowHeight = 6f;
        sketch.noStroke();
        sketch.fill(color);
        sketch.beginShape();
        if (expanded) {
            sketch.vertex(centerX - (arrowWidth * 0.5f), centerY + (arrowHeight * 0.5f));
            sketch.vertex(centerX + (arrowWidth * 0.5f), centerY + (arrowHeight * 0.5f));
            sketch.vertex(centerX, centerY - (arrowHeight * 0.5f));
        } else {
            sketch.vertex(centerX - (arrowWidth * 0.5f), centerY - (arrowHeight * 0.5f));
            sketch.vertex(centerX + (arrowWidth * 0.5f), centerY - (arrowHeight * 0.5f));
            sketch.vertex(centerX, centerY + (arrowHeight * 0.5f));
        }
        sketch.endShape(PConstants.CLOSE);
    }

    private void applyTypography(PApplet sketch, DropDownRenderStyle style) {
        if (style.font() != null) {
            sketch.textFont(style.font(), style.textSize());
        } else {
            sketch.textSize(style.textSize());
        }
    }
}
