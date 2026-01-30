package com.cpz.processing.controls.switchcontrol.view;

import com.cpz.processing.controls.switchcontrol.SwitchModel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * @author CPZ
 */
public class SwitchViewModel {

    // <editor-fold defaultstate="collapsed" desc="*** variables ***">
    private final SwitchModel model;
    @Getter
    private int totalStates = 2;
    @Getter
    private boolean enabled = true;
    @Getter
    private boolean display = true;
    // </editor-fold>

    public SwitchViewModel(@NotNull SwitchModel model) {
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

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }
}
