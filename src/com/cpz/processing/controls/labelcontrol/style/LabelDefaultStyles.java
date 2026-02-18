package com.cpz.processing.controls.labelcontrol.style;

import com.cpz.processing.controls.labelcontrol.style.interfaces.LabelStyle;
import com.cpz.processing.controls.labelcontrol.style.render.DefaultTextRenderer;
import processing.core.PConstants;

/**
 * @author CPZ
 */
public final class LabelDefaultStyles {

    private LabelDefaultStyles() {
    }

    public static LabelStyle defaultText() {
        LabelStyleConfig config = new LabelStyleConfig();
        config.font = null;
        config.textSize = 12f;
        config.textColor = 0;
        config.lineSpacingMultiplier = 1.0f;
        config.alignX = PConstants.LEFT;
        config.alignY = PConstants.BASELINE;
        return new DefaultTextRenderer(config);
    }
}
