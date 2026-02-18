package com.cpz.processing.controls.switchcontrol.style;

import com.cpz.processing.controls.switchcontrol.style.interfaces.SwitchStyle;
import com.cpz.processing.controls.switchcontrol.style.render.CircleShapeRenderer;
import com.cpz.processing.controls.util.Colors;

/**
 * @author CPZ
 */
public final class SwitchDefaultStyles {

    private SwitchDefaultStyles() {}

    public static SwitchStyle circular() {
        SwitchStyleConfig cfg = new SwitchStyleConfig();
        cfg.shape = new CircleShapeRenderer();
        cfg.colorEstados = new int[] {
                Colors.gray(40),
                Colors.gray(200)
        };
        cfg.colorStroke = Colors.gray(255);
        cfg.strokeWidth = 2f;
        cfg.strokeWidthHover = 3f;
        cfg.alfaDeshabilitado = 80;
        cfg.esquinaRadio = 0;
        return new ParametricSwitchStyle(cfg);
    }
}
