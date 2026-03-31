package com.cpz.processing.controls.textfieldcontrol.style;

public final class TextFieldDefaultStyles {

    private TextFieldDefaultStyles() {
    }

    public static DefaultTextFieldStyle standard() {
        TextFieldStyleConfig config = new TextFieldStyleConfig();
        config.textSize = 16f;
        config.font = null;
        return new DefaultTextFieldStyle(config);
    }
}
