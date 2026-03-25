package com.cpz.processing.controls.switchcontrol.view;

import com.cpz.processing.controls.common.viewmodel.AbstractInteractiveControlViewModel;
import com.cpz.processing.controls.switchcontrol.SwitchModel;

/**
 * @author CPZ
 */
public class SwitchViewModel extends AbstractInteractiveControlViewModel<SwitchModel> {

    public SwitchViewModel(SwitchModel model) {
        super(model);
        setTotalStates(model.getTotalStates());
    }

    public int getState() {
        return model.getState();
    }

    public int getPrevState() {
        return model.getPrevState();
    }

    public boolean hasChanged() {
        return model.getState() != model.getPrevState();
    }

    public boolean isFirstState() {
        return model.getState() == 0;
    }

    public void setTotalStates(int totalStates) {
        int normalizedTotalStates = Math.max(1, totalStates);
        model.setTotalStates(normalizedTotalStates);
        clampState(normalizedTotalStates);
    }

    public int getTotalStates() {
        return model.getTotalStates();
    }

    @Override
    protected void activate() {
        int totalStates = Math.max(1, model.getTotalStates());
        int currentState = model.getState();
        model.setPrevState(currentState);
        model.setState((currentState + 1) % totalStates);
    }

    private void clampState(int totalStates) {
        if (model.getState() >= totalStates) {
            model.setPrevState(model.getState());
            model.setState(totalStates - 1);
        }
    }
}
