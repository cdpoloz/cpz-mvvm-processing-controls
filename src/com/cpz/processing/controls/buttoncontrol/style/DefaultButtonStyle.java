package com.cpz.processing.controls.buttoncontrol.style;

import com.cpz.processing.controls.buttoncontrol.view.ButtonViewState;
import processing.core.PApplet;
import processing.core.PConstants;

/**
 * @author CPZ
 */
public final class DefaultButtonStyle implements ButtonStyle {

    private final int baseColor;
    private final int textColor;
    private final float cornerRadius;
    private final int disabledAlpha;

    public DefaultButtonStyle() {
        this.baseColor = 0xFF4A90E2;
        this.textColor = 0xFFFFFFFF;
        this.cornerRadius = 10f;
        this.disabledAlpha = 110;
    }

    @Override
    public void render(PApplet p, ButtonViewState state) {
        p.pushStyle();

        int lighter = p.lerpColor(baseColor, p.color(255), 0.18f);
        int darker = p.lerpColor(baseColor, p.color(0), 0.20f);
        int background = baseColor;
        if (state.pressed()) {
            background = darker;
        } else if (state.hovered()) {
            background = lighter;
        }

        p.noStroke();
        if (!state.enabled()) {
            p.fill(background, disabledAlpha);
        } else {
            p.fill(background);
        }

        float left = state.x() - (state.width() * 0.5f);
        float top = state.y() - (state.height() * 0.5f);
        p.rect(left, top, state.width(), state.height(), cornerRadius);

        p.fill(textColor);
        if (!state.enabled()) {
            p.fill(textColor, disabledAlpha);
        }
        p.textAlign(PConstants.CENTER, PConstants.CENTER);
        String text = state.text();
        if (text == null) {
            text = "";
        }
        p.text(text, state.x(), state.y());

        p.popStyle();
    }
}
