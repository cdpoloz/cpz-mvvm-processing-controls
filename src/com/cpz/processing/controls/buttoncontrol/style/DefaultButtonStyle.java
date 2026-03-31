package com.cpz.processing.controls.buttoncontrol.style;

import com.cpz.processing.controls.buttoncontrol.style.interfaces.ButtonRenderer;
import com.cpz.processing.controls.buttoncontrol.style.render.DefaultButtonRenderer;
import com.cpz.processing.controls.buttoncontrol.view.ButtonViewState;
import com.cpz.processing.controls.common.style.InteractiveStyleHelper;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class DefaultButtonStyle implements ButtonStyle {

    private final ButtonStyleConfig config;
    private final ButtonRenderer renderer;

    public DefaultButtonStyle(ButtonStyleConfig config) {
        this(config, config != null && config.renderer != null ? config.renderer : new DefaultButtonRenderer());
    }

    public DefaultButtonStyle(ButtonStyleConfig config, ButtonRenderer renderer) {
        this.config = config;
        this.renderer = renderer;
    }

    @Override
    public void render(PApplet p, ButtonViewState state) {
        int background = InteractiveStyleHelper.resolveFillColor(
                p,
                config.baseColor,
                config.hoverBlendWithWhite,
                config.pressedBlendWithBlack,
                state.hovered(),
                state.pressed()
        );
        ButtonRenderStyle style = new ButtonRenderStyle(
                InteractiveStyleHelper.applyDisabledAlpha(background, state.enabled(), config.disabledAlpha),
                InteractiveStyleHelper.resolveStrokeColor(config.strokeColor, state.enabled(), config.disabledAlpha),
                InteractiveStyleHelper.resolveStrokeWeight(config.strokeWeight, config.strokeWeightHover, state.hovered()),
                InteractiveStyleHelper.applyDisabledAlpha(config.textColor, state.enabled(), config.disabledAlpha),
                config.cornerRadius,
                state.text()
        );
        renderer.render(p, state.x(), state.y(), state.width(), state.height(), style);
    }
}
