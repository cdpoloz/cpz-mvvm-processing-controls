package com.cpz.processing.controls.textfieldcontrol.model;

import com.cpz.processing.controls.common.Enableable;

public final class TextFieldModel implements Enableable {

    private String text = "";
    private boolean enabled = true;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text must not be null");
        }
        this.text = text;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
