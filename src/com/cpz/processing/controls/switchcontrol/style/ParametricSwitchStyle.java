package com.cpz.processing.controls.switchcontrol.style;

import com.cpz.processing.controls.common.style.InteractiveStyleHelper;
import com.cpz.processing.controls.switchcontrol.style.interfaces.SwitchStyle;
import com.cpz.processing.controls.switchcontrol.view.SwitchViewState;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class ParametricSwitchStyle implements SwitchStyle {

    private final SwitchStyleConfig cfg;

    public ParametricSwitchStyle(SwitchStyleConfig cfg) {
        if (cfg == null) throw new IllegalArgumentException("Config null");
        if (cfg.shape == null) throw new IllegalArgumentException("ShapeRenderer requerido");
        if (cfg.stateColors == null || cfg.stateColors.length == 0)
            throw new IllegalArgumentException("At least one color must be defined");
        this.cfg = cfg;
    }

    @Override
    public void render(PApplet p, SwitchViewState s) {
        int idx = Math.max(0, Math.min(s.stateIndex(), cfg.stateColors.length - 1));
        int baseFill = cfg.stateColors[idx];
        int interactiveFill = InteractiveStyleHelper.resolveFillColor(
                p,
                baseFill,
                cfg.hoverBlendWithWhite,
                cfg.pressedBlendWithBlack,
                s.hovered(),
                s.pressed()
        );
        SwitchRenderStyle style = new SwitchRenderStyle(
                InteractiveStyleHelper.applyDisabledAlpha(interactiveFill, s.enabled(), cfg.disabledAlpha),
                InteractiveStyleHelper.resolveStrokeColor(cfg.strokeColor, s.enabled(), cfg.disabledAlpha),
                InteractiveStyleHelper.resolveStrokeWeight(cfg.strokeWidth, cfg.strokeWidthHover, s.hovered())
        );
        cfg.shape.render(p, s.x(), s.y(), s.width(), s.height(), style);
    }
}
