package com.cpz.processing.controls.checkboxcontrol.style;

import com.cpz.processing.controls.checkboxcontrol.view.CheckboxViewState;
import com.cpz.processing.controls.common.style.InteractiveStyleHelper;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class DefaultCheckboxStyle implements CheckboxStyle {

    private final CheckboxStyleConfig config;

    public DefaultCheckboxStyle(CheckboxStyleConfig config) {
        this.config = config;
    }

    @Override
    public void render(PApplet p, CheckboxViewState state) {
        p.pushStyle();

        float left = state.x() - (state.width() * 0.5f);
        float top = state.y() - (state.height() * 0.5f);

        int boxColor = InteractiveStyleHelper.resolveFillColor(
                config.boxColor,
                config.boxHoverColor,
                config.boxPressedColor,
                state.hovered(),
                state.pressed()
        );
        p.fill(InteractiveStyleHelper.applyDisabledAlpha(boxColor, state.enabled(), config.disabledAlpha));
        p.stroke(InteractiveStyleHelper.resolveStrokeColor(config.borderColor, state.enabled(), config.disabledAlpha));
        p.strokeWeight(InteractiveStyleHelper.resolveStrokeWeight(config.borderWidth, config.borderWidthHover, state.hovered()));
        p.rect(left, top, state.width(), state.height(), config.cornerRadius);

        if (state.checked()) {
            float inset = Math.min(state.width(), state.height()) * config.checkInset;
            float x1 = left + inset;
            float y1 = top + (state.height() * 0.55f);
            float x2 = left + (state.width() * 0.45f);
            float y2 = top + state.height() - inset;
            float x3 = left + state.width() - inset;
            float y3 = top + inset;
            p.stroke(InteractiveStyleHelper.resolveStrokeColor(config.checkColor, state.enabled(), config.disabledAlpha));
            p.strokeWeight(Math.max(2.5f, state.width() * 0.12f));
            p.noFill();
            p.line(x1, y1, x2, y2);
            p.line(x2, y2, x3, y3);
        }

        p.popStyle();
    }
}
