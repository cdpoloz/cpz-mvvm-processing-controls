package com.cpz.processing.controls.checkboxcontrol.style;

import com.cpz.processing.controls.checkboxcontrol.style.interfaces.CheckboxRenderer;
import com.cpz.processing.controls.checkboxcontrol.style.render.DefaultCheckboxRenderer;
import com.cpz.processing.controls.checkboxcontrol.view.CheckboxViewState;
import com.cpz.processing.controls.common.style.InteractiveStyleHelper;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class DefaultCheckboxStyle implements CheckboxStyle {

    private final CheckboxStyleConfig config;
    private final CheckboxRenderer renderer;

    public DefaultCheckboxStyle(CheckboxStyleConfig config) {
        this(config, config != null && config.renderer != null ? config.renderer : new DefaultCheckboxRenderer());
    }

    public DefaultCheckboxStyle(CheckboxStyleConfig config, CheckboxRenderer renderer) {
        this.config = config;
        this.renderer = renderer;
    }

    @Override
    public void render(PApplet p, CheckboxViewState state) {
        int boxColor = InteractiveStyleHelper.resolveFillColor(
                config.boxColor,
                config.boxHoverColor,
                config.boxPressedColor,
                state.hovered(),
                state.pressed()
        );
        CheckboxRenderStyle style = new CheckboxRenderStyle(
                InteractiveStyleHelper.applyDisabledAlpha(boxColor, state.enabled(), config.disabledAlpha),
                InteractiveStyleHelper.resolveStrokeColor(config.borderColor, state.enabled(), config.disabledAlpha),
                InteractiveStyleHelper.resolveStrokeWeight(config.borderWidth, config.borderWidthHover, state.hovered()),
                config.cornerRadius,
                state.checked(),
                InteractiveStyleHelper.resolveStrokeColor(config.checkColor, state.enabled(), config.disabledAlpha),
                config.checkInset,
                Math.max(2.5f, state.width() * 0.12f)
        );
        renderer.render(p, state.x(), state.y(), state.width(), state.height(), style);
    }
}
