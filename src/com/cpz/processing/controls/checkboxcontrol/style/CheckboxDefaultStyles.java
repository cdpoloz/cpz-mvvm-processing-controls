package com.cpz.processing.controls.checkboxcontrol.style;

/**
 * @author CPZ
 */
public final class CheckboxDefaultStyles {

    private CheckboxDefaultStyles() {
    }

    public static CheckboxStyle standard() {
        CheckboxStyleConfig config = new CheckboxStyleConfig();
        config.borderWidth = 2f;
        config.borderWidthHover = 3f;
        config.cornerRadius = 5f;
        config.checkInset = 0.24f;
        return new DefaultCheckboxStyle(config);
    }
}
