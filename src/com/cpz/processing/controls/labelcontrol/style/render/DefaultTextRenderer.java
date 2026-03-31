package com.cpz.processing.controls.labelcontrol.style.render;

import com.cpz.processing.controls.labelcontrol.style.LabelRenderStyle;
import com.cpz.processing.controls.labelcontrol.style.interfaces.LabelRenderer;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class DefaultTextRenderer implements LabelRenderer {

    @Override
    public void render(PApplet sketch, float x, float y, float width, float height, LabelRenderStyle style) {
        sketch.pushStyle();
        if (style.typography().font() != null) {
            sketch.textFont(style.typography().font());
        }
        sketch.textSize(style.typography().textSize());
        sketch.textAlign(style.typography().textAlignHorizontal(), style.typography().textAlignVertical());
        sketch.fill(style.textColor());

        String text = style.text();
        if (text == null) {
            text = "";
        }
        String[] lines = text.split("\n", -1);
        float lineHeight = (sketch.textAscent() + sketch.textDescent()) * style.typography().lineSpacingMultiplier();
        for (int i = 0; i < lines.length; i++) {
            sketch.text(lines[i], x, y + (i * lineHeight));
        }
        sketch.popStyle();
    }
}
