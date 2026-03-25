package com.cpz.processing.controls.buttoncontrol.model;

import com.cpz.processing.controls.common.Enableable;

/**
 * @author CPZ
 */
public final class ButtonModel implements Enableable {

    private String text;
    private boolean enabled;

    public ButtonModel(String text) {
        this.text = normalizeText(text);
        this.enabled = true;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = normalizeText(text);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private String normalizeText(String text) {
        return text == null ? "" : text;
    }
}
