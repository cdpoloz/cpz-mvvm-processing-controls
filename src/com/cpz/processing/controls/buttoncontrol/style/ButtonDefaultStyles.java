package com.cpz.processing.controls.buttoncontrol.style;

import com.cpz.processing.controls.common.theme.Theme;
import com.cpz.processing.controls.common.theme.ThemeManager;
import com.cpz.processing.controls.util.Colors;

/**
 * @author CPZ
 */
public final class ButtonDefaultStyles {

    private ButtonDefaultStyles() {
    }

    public static ButtonStyle primary() {
        Theme theme = ThemeManager.getTheme();
        ButtonStyleConfig config = new ButtonStyleConfig();
        config.baseColor = theme.primaryColor;
        config.textColor = Colors.gray(255);
        config.strokeColor = Colors.gray(255);
        config.strokeWeight = 2f;
        config.strokeWeightHover = 3f;
        config.cornerRadius = 10f;
        config.disabledAlpha = theme.disabledAlpha;
        config.hoverBlendWithWhite = theme.hoverModifier;
        config.pressedBlendWithBlack = theme.pressedModifier;
        return new DefaultButtonStyle(config);
    }
}
