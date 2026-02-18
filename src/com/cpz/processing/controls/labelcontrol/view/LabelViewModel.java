package com.cpz.processing.controls.labelcontrol.view;

import com.cpz.processing.controls.labelcontrol.LabelModel;

/**
 * @author CPZ
 */
public final class LabelViewModel {

    private final LabelModel model;
    private boolean enabled = true;

    public LabelViewModel(LabelModel model) {
        this.model = model;
    }

    public String getText() {
        return model.getText();
    }

    public void setText(String text) {
        model.setText(text);
    }

    public boolean isDisplay() {
        return model.isDisplay();
    }

    public void setDisplay(boolean display) {
        model.setDisplay(display);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
