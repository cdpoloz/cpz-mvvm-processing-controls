package com.cpz.processing.controls.labelcontrol.style;

import com.cpz.processing.controls.labelcontrol.style.interfaces.LabelStyle;
import com.cpz.processing.controls.labelcontrol.style.render.DefaultTextRenderer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class LabelDefaultStyles {

    private LabelDefaultStyles() {
    }

    @Contract(" -> new")
    public static @NotNull LabelStyle defaultText() {
        LabelStyleConfig config = new LabelStyleConfig();
        config.font = null;
        config.textSize = 12f;
        config.textColor = 0;
        config.lineSpacingMultiplier = 1.0f;
        config.alignX = PApplet.LEFT;
        config.alignY = PApplet.BASELINE;
        return new DefaultTextRenderer(config);
    }
}
