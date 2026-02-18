package com.cpz.processing.controls.labelcontrol.style.render;

import com.cpz.processing.controls.labelcontrol.style.LabelStyleConfig;
import com.cpz.processing.controls.labelcontrol.style.interfaces.LabelStyle;
import com.cpz.processing.controls.labelcontrol.view.LabelViewState;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class DefaultTextRenderer implements LabelStyle {

    private final LabelStyleConfig config;

    public DefaultTextRenderer(LabelStyleConfig config) {
        this.config = config;
    }

    public LabelStyleConfig getConfig() {
        return config;
    }

    @Override
    public void applyTextStyle(PApplet sketch) {
        if (config.font != null) {
            sketch.textFont(config.font);
        }
        sketch.textSize(config.textSize);
        sketch.textAlign(config.alignX, config.alignY);
    }

    @Override
    public float getLineSpacingMultiplier() {
        return config.lineSpacingMultiplier;
    }

    @Override
    public void draw(PApplet sketch, LabelViewState state) {
        sketch.pushStyle();
        applyTextStyle(sketch);
        if (!state.enabled()) {
            sketch.fill(config.textColor, config.disabledAlpha);
        } else {
            sketch.fill(config.textColor);
        }

        String text = state.text();
        if (text == null) {
            text = "";
        }
        String[] lines = text.split("\n", -1);
        float lineHeight = (sketch.textAscent() + sketch.textDescent()) * config.lineSpacingMultiplier;
        float x = state.x();
        float y = state.y();
        for (int i = 0; i < lines.length; i++) {
            sketch.text(lines[i], x, y + (i * lineHeight));
        }
        sketch.popStyle();
    }
}
