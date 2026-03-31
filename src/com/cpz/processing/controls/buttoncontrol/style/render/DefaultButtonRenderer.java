package com.cpz.processing.controls.buttoncontrol.style.render;

import com.cpz.processing.controls.buttoncontrol.style.ButtonRenderStyle;
import com.cpz.processing.controls.buttoncontrol.style.interfaces.ButtonRenderer;
import processing.core.PApplet;
import processing.core.PConstants;

public final class DefaultButtonRenderer implements ButtonRenderer {

    @Override
    public void render(PApplet p, float x, float y, float width, float height, ButtonRenderStyle style) {
        float left = x - (width * 0.5f);
        float top = y - (height * 0.5f);
        String text = style.text();
        if (text == null) {
            text = "";
        }

        p.pushStyle();
        p.fill(style.fillColor());
        p.stroke(style.strokeColor());
        p.strokeWeight(style.strokeWeight());
        p.rect(left, top, width, height, style.cornerRadius());

        if (style.showText()) {
            p.fill(style.textColor());
            p.textAlign(PConstants.CENTER, PConstants.CENTER);
            p.text(text, x, y);
        }
        p.popStyle();
    }
}
