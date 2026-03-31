package com.cpz.processing.controls.switchcontrol.style;

import com.cpz.processing.controls.switchcontrol.style.interfaces.SwitchStyle;
import com.cpz.processing.controls.switchcontrol.style.render.CircleShapeRenderer;

/**
 * @author CPZ
 */
public final class SwitchDefaultStyles {

    private SwitchDefaultStyles() {}

    public static SwitchStyle circular() {
        SwitchStyleConfig cfg = new SwitchStyleConfig();
        cfg.shape = new CircleShapeRenderer();
        cfg.strokeWidth = 2f;
        cfg.strokeWidthHover = 3f;
        return new ParametricSwitchStyle(cfg);
    }
}
