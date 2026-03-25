package com.cpz.processing.controls.switchcontrol.style;

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
        cfg.shape.render(p, s, cfg);
    }
}
