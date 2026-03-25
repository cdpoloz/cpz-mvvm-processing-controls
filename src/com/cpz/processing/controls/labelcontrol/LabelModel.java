package com.cpz.processing.controls.labelcontrol;

import com.cpz.processing.controls.common.Enableable;

/**
 * @author CPZ
 */

public final class LabelModel implements Enableable {

    private String text = "";
    private boolean enabled = true;

    public void setText(String text) {
        this.text = text != null ? text : "";
    }

    public String getText() {
        return text;
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
