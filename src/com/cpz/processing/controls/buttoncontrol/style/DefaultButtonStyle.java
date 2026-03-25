package com.cpz.processing.controls.buttoncontrol.style;

import com.cpz.processing.controls.buttoncontrol.view.ButtonViewState;
import com.cpz.processing.controls.common.style.InteractiveStyleHelper;
import processing.core.PApplet;
import processing.core.PConstants;

/**
 * @author CPZ
 */
public final class DefaultButtonStyle implements ButtonStyle {

    private final ButtonStyleConfig config;

    public DefaultButtonStyle(ButtonStyleConfig config) {
        this.config = config;
    }

    @Override
    public void render(PApplet p, ButtonViewState state) {
        p.pushStyle();

        int background = InteractiveStyleHelper.resolveFillColor(
                p,
                config.baseColor,
                config.hoverBlendWithWhite,
                config.pressedBlendWithBlack,
                state.hovered(),
                state.pressed()
        );

        p.noStroke();
        p.fill(InteractiveStyleHelper.applyDisabledAlpha(background, state.enabled(), config.disabledAlpha));

        float left = state.x() - (state.width() * 0.5f);
        float top = state.y() - (state.height() * 0.5f);
        p.rect(left, top, state.width(), state.height(), config.cornerRadius);

        p.fill(InteractiveStyleHelper.applyDisabledAlpha(config.textColor, state.enabled(), config.disabledAlpha));
        p.textAlign(PConstants.CENTER, PConstants.CENTER);
        String text = state.text();
        p.text(text, state.x(), state.y());

        p.popStyle();
    }
}
