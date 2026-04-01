package com.cpz.processing.controls.tooltipoverlay;

import com.cpz.processing.controls.common.Enableable;

public final class TooltipModel implements Enableable {

    private String text;
    private boolean enabled = true;

    public TooltipModel(String text) {
        this.text = text == null ? "" : text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text == null ? "" : text;
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
