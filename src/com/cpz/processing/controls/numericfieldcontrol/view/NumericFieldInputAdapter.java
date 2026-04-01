package com.cpz.processing.controls.numericfieldcontrol.view;

import com.cpz.processing.controls.common.focus.FocusManager;
import com.cpz.processing.controls.numericfieldcontrol.viewmodel.NumericFieldViewModel;

public final class NumericFieldInputAdapter {

    private final NumericFieldView view;
    private final NumericFieldViewModel viewModel;
    private final FocusManager focusManager;

    public NumericFieldInputAdapter(NumericFieldView view, NumericFieldViewModel viewModel, FocusManager focusManager) {
        this.view = view;
        this.viewModel = viewModel;
        this.focusManager = focusManager;
        this.focusManager.register(viewModel);
    }

    public void handleMouseMove(float mx, float my) {
        viewModel.onPointerMove(viewModel.isVisible() && view.contains(mx, my));
    }

    public boolean handleMousePress(float mx, float my) {
        if (!viewModel.isVisible() || !viewModel.isEnabled()) {
            viewModel.onPointerPress(false);
            return false;
        }
        boolean inside = view.contains(mx, my);
        viewModel.onPointerPress(inside);
        if (!inside) {
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
        viewModel.onPointerMove(view.contains(mx, my));
        view.handleMouseDrag(mx);
    }

    public void handleMouseRelease(float mx, float my) {
        view.handleMouseRelease();
        viewModel.onPointerRelease(view.contains(mx, my));
    }

    public void handleMouseWheel(float delta, boolean shiftDown, boolean ctrlDown) {
        viewModel.onMouseWheel(delta, shiftDown, ctrlDown);
    }
}
