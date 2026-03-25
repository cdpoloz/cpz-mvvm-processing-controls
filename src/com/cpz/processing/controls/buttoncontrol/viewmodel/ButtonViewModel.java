package com.cpz.processing.controls.buttoncontrol.viewmodel;

import com.cpz.processing.controls.buttoncontrol.ButtonListener;
import com.cpz.processing.controls.buttoncontrol.model.ButtonModel;
import com.cpz.processing.controls.common.viewmodel.AbstractInteractiveControlViewModel;

/**
 * @author CPZ
 */
public final class ButtonViewModel extends AbstractInteractiveControlViewModel<ButtonModel> {

    private ButtonListener clickListener;

    public ButtonViewModel(ButtonModel model) {
        super(model);
    }

    public void setClickListener(ButtonListener listener) {
        this.clickListener = listener;
    }

    public String getText() {
        return model.getText();
    }

    public void setText(String text) {
        model.setText(text);
    }

    @Deprecated
    public void setOnClick(Runnable action) {
        if (action == null) {
            this.clickListener = null;
        } else {
            this.clickListener = action::run;
        }
    }

    @Override
    protected void activate() {
        if (clickListener != null) {
            clickListener.onClick();
        }
    }
}
