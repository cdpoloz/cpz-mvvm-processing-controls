package com.cpz.processing.controls.checkboxcontrol.style.render;

import com.cpz.processing.controls.checkboxcontrol.style.CheckboxRenderStyle;
import com.cpz.processing.controls.checkboxcontrol.style.interfaces.CheckboxRenderer;
import processing.core.PApplet;

public final class DefaultCheckboxRenderer implements CheckboxRenderer {

    @Override
    public void render(PApplet p, float x, float y, float width, float height, CheckboxRenderStyle style) {
        float left = x - (width * 0.5f);
        float top = y - (height * 0.5f);

        p.pushStyle();
        p.fill(style.boxFillColor());
        p.stroke(style.borderColor());
        p.strokeWeight(style.borderWidth());
        p.rect(left, top, width, height, style.cornerRadius());

        if (style.showCheck()) {
            float inset = Math.min(width, height) * style.checkInset();
            float x1 = left + inset;
            float y1 = top + (height * 0.55f);
            float x2 = left + (width * 0.45f);
            float y2 = top + height - inset;
            float x3 = left + width - inset;
            float y3 = top + inset;
            p.stroke(style.checkColor());
            p.strokeWeight(style.checkStrokeWeight());
            p.noFill();
            p.line(x1, y1, x2, y2);
            p.line(x2, y2, x3, y3);
        }

        p.popStyle();
    }
}
