package com.cpz.processing.controls.labelcontrol.style;

import com.cpz.processing.controls.common.theme.ThemeManager;
import com.cpz.processing.controls.common.theme.ThemeTokens;
import com.cpz.processing.controls.labelcontrol.style.interfaces.LabelStyle;
import processing.core.PConstants;

/**
 * @author CPZ
 */
public final class LabelDefaultStyles {

    private LabelDefaultStyles() {
    }

    public static LabelStyle defaultText() {
        ThemeTokens tokens = ThemeManager.getTheme().tokens();
        LabelStyleConfig config = new LabelStyleConfig();
        config.font = null;
        config.textSize = 12f;
        config.textColor = tokens.onSurface;
        config.lineSpacingMultiplier = 1.0f;
        config.alignX = PConstants.LEFT;
        config.alignY = PConstants.BASELINE;
        config.disabledAlpha = tokens.disabledAlpha;
        return new DefaultLabelStyle(config);
    }
}
