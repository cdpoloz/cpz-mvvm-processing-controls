package com.cpz.processing.controls.switchcontrol;

/**
 * @author CPZ
 */
public final class SwitchModel {

    private int state, prevState;

    public void nextState(int totalStates) {
        if (totalStates <= 0) {
            return;
        }
        prevState = state;
        state = (state + 1) % totalStates;
    }

    public void clampState(int totalStates) {
        if (totalStates <= 0) {
            return;
        }
        if (state >= totalStates) {
            prevState = state;
            state = totalStates - 1;
        }
    }

    public boolean hasChanged() {
        return state != prevState;
    }

    public boolean isFirstState() {
        return state == 0;
    }

    public int getState() {
        return state;
    }

    public int getPrevState() {
        return prevState;
    }

}
