package com.cpz.processing.controls.buttoncontrol.model;

/**
 * @author CPZ
 */
public final class ButtonModel {

    private String text;
    private boolean enabled;

    public ButtonModel(String text) {
        this.text = text;
        this.enabled = true;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
