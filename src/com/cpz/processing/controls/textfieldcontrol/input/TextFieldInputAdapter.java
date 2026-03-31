package com.cpz.processing.controls.textfieldcontrol.input;

import com.cpz.processing.controls.common.focus.FocusManager;
import com.cpz.processing.controls.textfieldcontrol.view.TextFieldView;
import com.cpz.processing.controls.textfieldcontrol.viewmodel.TextFieldViewModel;

public final class TextFieldInputAdapter {

    private final TextFieldView view;
    private final TextFieldViewModel viewModel;
    private final FocusManager focusManager;

    public TextFieldInputAdapter(TextFieldView view, TextFieldViewModel viewModel, FocusManager focusManager) {
        this.view = view;
        this.viewModel = viewModel;
        this.focusManager = focusManager;
        this.focusManager.register(viewModel);
    }

    public boolean handleMousePress(float mx, float my) {
        if (!viewModel.isVisible() || !viewModel.isEnabled()) {
            return false;
        }
        if (!view.contains(mx, my)) {
            return false;
        }
        focusManager.requestFocus(viewModel);
        view.handleMousePress(mx);
        return true;
    }

    public void handleMouseDrag(float mx, float my) {
        if (!viewModel.isVisible() || !viewModel.isEnabled()) {
            return;
        }
        view.handleMouseDrag(mx);
    }

    public void handleMouseRelease() {
        view.handleMouseRelease();
    }
}
