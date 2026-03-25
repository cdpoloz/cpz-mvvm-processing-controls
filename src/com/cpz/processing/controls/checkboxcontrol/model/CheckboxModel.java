package com.cpz.processing.controls.checkboxcontrol.model;

import com.cpz.processing.controls.common.Enableable;

/**
 * @author CPZ
 */
public final class CheckboxModel implements Enableable {

    private boolean checked;
    private boolean enabled = true;

    public CheckboxModel() {
    }

    public CheckboxModel(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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
