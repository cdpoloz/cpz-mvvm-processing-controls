package com.cpz.processing.controls.common.input;

public final class PointerInputAdapter {

    private final PointerInteractable view;
    private final PointerInputTarget viewModel;

    public PointerInputAdapter(PointerInteractable view, PointerInputTarget viewModel) {
        this.view = view;
        this.viewModel = viewModel;
    }

    public void handleMouseMove(float mx, float my) {
        if (!viewModel.isVisible()) {
            viewModel.onPointerMove(false);
            return;
        }
        viewModel.onPointerMove(view.contains(mx, my));
    }

    public void handleMousePress(float mx, float my) {
        if (!viewModel.isVisible() || !viewModel.isEnabled()) {
            viewModel.onPointerPress(false);
            return;
        }
        viewModel.onPointerPress(view.contains(mx, my));
    }

    public void handleMouseRelease(float mx, float my) {
        if (!viewModel.isVisible()) {
            viewModel.onPointerRelease(false);
            return;
        }
        viewModel.onPointerRelease(view.contains(mx, my));
    }
}
