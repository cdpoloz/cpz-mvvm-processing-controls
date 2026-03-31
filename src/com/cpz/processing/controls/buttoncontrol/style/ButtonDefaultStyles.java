package com.cpz.processing.controls.buttoncontrol.style;

/**
 * @author CPZ
 */
public final class ButtonDefaultStyles {

    private ButtonDefaultStyles() {
    }

    public static ButtonStyle primary() {
        ButtonStyleConfig config = new ButtonStyleConfig();
        config.strokeWeight = 2f;
        config.strokeWeightHover = 3f;
        config.cornerRadius = 10f;
        return new DefaultButtonStyle(config);
    }
}
