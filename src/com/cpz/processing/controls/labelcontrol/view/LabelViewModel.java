package com.cpz.processing.controls.labelcontrol.view;

import com.cpz.processing.controls.labelcontrol.LabelModel;
import com.cpz.processing.controls.common.viewmodel.AbstractControlViewModel;

/**
 * @author CPZ
 */
public final class LabelViewModel extends AbstractControlViewModel<LabelModel> {

    public LabelViewModel(LabelModel model) {
        super(model);
    }

    public String getText() {
        return model.getText();
    }

    public void setText(String text) {
        model.setText(text);
    }
}
