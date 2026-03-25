package com.cpz.processing.controls.switchcontrol.style;

import com.cpz.processing.controls.common.theme.Theme;
import com.cpz.processing.controls.common.theme.ThemeManager;
import com.cpz.processing.controls.switchcontrol.style.interfaces.SwitchStyle;
import com.cpz.processing.controls.switchcontrol.style.render.CircleShapeRenderer;
import com.cpz.processing.controls.util.Colors;

/**
 * @author CPZ
 */
public final class SwitchDefaultStyles {

    private SwitchDefaultStyles() {}

    public static SwitchStyle circular() {
        Theme theme = ThemeManager.getTheme();
        SwitchStyleConfig cfg = new SwitchStyleConfig();
        cfg.shape = new CircleShapeRenderer();
        cfg.stateColors = new int[] {
                Colors.gray(40),
                theme.primaryColor
        };
        cfg.strokeColor = Colors.gray(255);
        cfg.strokeWidth = 2f;
        cfg.strokeWidthHover = 3f;
        cfg.disabledAlpha = theme.disabledAlpha;
        cfg.cornerRadius = 0;
        return new ParametricSwitchStyle(cfg);
    }
}
