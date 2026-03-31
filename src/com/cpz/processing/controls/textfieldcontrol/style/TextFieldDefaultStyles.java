package com.cpz.processing.controls.textfieldcontrol.style;

import com.cpz.processing.controls.common.theme.Theme;
import com.cpz.processing.controls.common.theme.ThemeManager;
import com.cpz.processing.controls.util.Colors;

public final class TextFieldDefaultStyles {

    private TextFieldDefaultStyles() {
    }

    public static DefaultTextFieldStyle standard() {
        Theme theme = ThemeManager.getTheme();
        TextFieldStyleConfig config = new TextFieldStyleConfig();
        config.backgroundColor = Colors.gray(250);
        config.borderColor = Colors.gray(120);
        config.textColor = theme.textColor;
        config.cursorColor = theme.primaryColor;
        config.selectionColor = Colors.alpha(140, theme.primaryColor);
        config.selectionTextColor = config.textColor;
        config.textSize = 16f;
        config.font = null;
        return new DefaultTextFieldStyle(config);
    }
}
