package com.cpz.processing.controls.tooltipoverlay;

import processing.core.PApplet;
import processing.core.PConstants;

public final class DefaultTooltipRenderer {

    public void render(PApplet sketch, TooltipViewState state, TooltipRenderStyle style) {
        float left = state.x() - (state.width() * 0.5f);
        float top = state.y() - (state.height() * 0.5f);

        sketch.pushStyle();
        sketch.rectMode(PConstants.CORNER);
        sketch.stroke(style.strokeColor());
        sketch.strokeWeight(style.strokeWeight());
        sketch.fill(style.backgroundColor());
        sketch.rect(left, top, state.width(), state.height(), style.cornerRadius());
        if (style.font() != null) {
            sketch.textFont(style.font(), style.textSize());
        } else {
            sketch.textSize(style.textSize());
        }
        sketch.fill(style.textColor());
        sketch.textAlign(PConstants.LEFT, PConstants.CENTER);
        sketch.text(state.text(), left + style.textPadding(), state.y());
        sketch.popStyle();
    }
}
