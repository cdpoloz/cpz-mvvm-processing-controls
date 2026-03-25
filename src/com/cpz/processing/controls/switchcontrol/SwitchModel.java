package com.cpz.processing.controls.switchcontrol;

import com.cpz.processing.controls.common.Enableable;

/**
 * @author CPZ
 */
public final class SwitchModel implements Enableable {

    private int state;
    private int prevState;
    private int totalStates = 2;
    private boolean enabled = true;

    public void setTotalStates(int totalStates) {
        this.totalStates = totalStates;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPrevState() {
        return prevState;
    }

    public void setPrevState(int prevState) {
        this.prevState = prevState;
    }

    public int getTotalStates() {
        return totalStates;
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
