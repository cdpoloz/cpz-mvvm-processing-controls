package com.cpz.processing.controls.checkboxcontrol.style;

import com.cpz.processing.controls.common.theme.Theme;
import com.cpz.processing.controls.common.theme.ThemeManager;
import com.cpz.processing.controls.util.Colors;

/**
 * @author CPZ
 */
public final class CheckboxDefaultStyles {

    private CheckboxDefaultStyles() {
    }

    public static CheckboxStyle standard() {
        Theme theme = ThemeManager.getTheme();
        CheckboxStyleConfig config = new CheckboxStyleConfig();
        config.boxColor = theme.secondaryColor;
        config.boxHoverColor = Colors.alpha(255, Colors.gray(255));
        config.boxPressedColor = Colors.gray(210);
        config.checkColor = theme.accentColor;
        config.borderColor = Colors.gray(70);
        config.borderWidth = 2f;
        config.borderWidthHover = 3f;
        config.cornerRadius = 5f;
        config.disabledAlpha = theme.disabledAlpha;
        config.checkInset = 0.24f;
        return new DefaultCheckboxStyle(config);
    }
}
