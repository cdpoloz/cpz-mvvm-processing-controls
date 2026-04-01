package com.cpz.processing.controls.tooltipoverlay;

import com.cpz.processing.controls.common.viewmodel.AbstractControlViewModel;

public final class TooltipViewModel extends AbstractControlViewModel<TooltipModel> {

    public TooltipViewModel(TooltipModel model) {
        super(model);
    }

    public String getText() {
        return model.getText();
    }

    public void setText(String text) {
        model.setText(text);
    }
}
