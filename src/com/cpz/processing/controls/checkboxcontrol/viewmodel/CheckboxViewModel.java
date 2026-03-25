package com.cpz.processing.controls.checkboxcontrol.viewmodel;

import com.cpz.processing.controls.checkboxcontrol.model.CheckboxModel;
import com.cpz.processing.controls.common.viewmodel.AbstractInteractiveControlViewModel;

/**
 * @author CPZ
 */
public final class CheckboxViewModel extends AbstractInteractiveControlViewModel<CheckboxModel> {

    public CheckboxViewModel(CheckboxModel model) {
        super(model);
    }

    public boolean isChecked() {
        return model.isChecked();
    }

    public void setChecked(boolean checked) {
        model.setChecked(checked);
    }

    @Override
    protected void activate() {
        model.setChecked(!model.isChecked());
    }
}
