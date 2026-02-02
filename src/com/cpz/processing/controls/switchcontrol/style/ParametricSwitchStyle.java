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
        if (cfg.colorEstados == null || cfg.colorEstados.length == 0)
            throw new IllegalArgumentException("Debe definirse al menos un color");
        this.cfg = cfg;
    }

    @Override
    public void draw(PApplet p, SwitchViewState s) {
        cfg.shape.draw(p, s, cfg);
    }
}
