package com.cpz.processing.controls.labelcontrol.style;

import com.cpz.processing.controls.common.style.InteractiveStyleHelper;
import com.cpz.processing.controls.labelcontrol.style.interfaces.LabelRenderer;
import com.cpz.processing.controls.labelcontrol.style.interfaces.LabelStyle;
import com.cpz.processing.controls.labelcontrol.style.render.DefaultTextRenderer;
import com.cpz.processing.controls.labelcontrol.view.LabelViewState;
import processing.core.PApplet;

public final class DefaultLabelStyle implements LabelStyle {

    private final LabelStyleConfig config;
    private final LabelRenderer renderer;

    public DefaultLabelStyle(LabelStyleConfig config) {
        this(config, new DefaultTextRenderer());
    }

    public DefaultLabelStyle(LabelStyleConfig config, LabelRenderer renderer) {
        this.config = config;
        this.renderer = renderer;
    }

    @Override
    public void render(PApplet sketch, LabelViewState state) {
        LabelRenderStyle style = new LabelRenderStyle(
                state.text(),
                InteractiveStyleHelper.applyDisabledAlpha(config.textColor, state.enabled(), config.disabledAlpha),
                resolveTypography()
        );
        renderer.render(sketch, state.x(), state.y(), 0f, 0f, style);
    }

    @Override
    public LabelTypography resolveTypography() {
        return new LabelTypography(
                config.font,
                config.textSize,
                config.lineSpacingMultiplier,
                config.alignX,
                config.alignY
        );
    }
}
