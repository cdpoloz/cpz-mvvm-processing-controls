package com.cpz.processing.controls.switchcontrol.view;

import com.cpz.processing.controls.switchcontrol.SwitchModel;

/**
 * @author CPZ
 */
public class SwitchViewModel {

    // <editor-fold defaultstate="collapsed" desc="*** variables ***">
    private final SwitchModel model;
    private int totalStates = 2;
    private boolean enabled = true;
    private boolean display = true;
    // </editor-fold>

    public SwitchViewModel(SwitchModel model) {
        this.model = model;
    }

    public void onActivate() {
        if (!enabled || !display) {
            return;
        }
        model.nextState(totalStates);
    }

    public int getState() {
        return model.getState();
    }

    public int getPrevState() {
        return model.getPrevState();
    }

    public boolean hasChanged() {
        return model.hasChanged();
    }

    public void setTotalStates(int totalStates) {
        this.totalStates = Math.max(1, totalStates);
        model.clampState(this.totalStates);
    }

    public int getTotalStates() {
        return totalStates;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isDisplay() {
        return display;
    }
}
