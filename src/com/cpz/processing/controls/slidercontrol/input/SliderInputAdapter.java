package com.cpz.processing.controls.slidercontrol.input;

import com.cpz.processing.controls.common.input.PointerInteractable;
import com.cpz.processing.controls.slidercontrol.view.SliderView;
import com.cpz.processing.controls.slidercontrol.viewmodel.SliderViewModel;

public final class SliderInputAdapter {

    private final PointerInteractable view;
    private final SliderView sliderView;
    private final SliderViewModel viewModel;

    public SliderInputAdapter(SliderView view, SliderViewModel viewModel) {
        this.view = view;
        this.sliderView = view;
        this.viewModel = viewModel;
    }

    public void handleMouseMove(float mx, float my) {
        viewModel.onPointerMoved(viewModel.isVisible() && view.contains(mx, my));
    }

    public void handleMousePress(float mx, float my) {
        if (!viewModel.isVisible() || !viewModel.isEnabled()) {
            viewModel.onPointerMoved(false);
            return;
        }
        if (view.contains(mx, my)) {
            viewModel.onPointerPressed(sliderView.toNormalizedValue(mx, my));
        } else {
            viewModel.onPointerMoved(false);
        }
    }

    public void handleMouseDrag(float mx, float my) {
        if (!viewModel.isVisible()) {
            return;
        }
        viewModel.onPointerMoved(view.contains(mx, my));
        viewModel.onPointerDragged(sliderView.toNormalizedValue(mx, my));
    }

    public void handleMouseRelease(float mx, float my) {
        viewModel.onPointerReleased();
        handleMouseMove(mx, my);
    }

    public void handleMouseWheel(float delta, boolean isShiftDown, boolean isCtrlDown) {
        viewModel.onMouseWheel(delta, isShiftDown, isCtrlDown);
    }
}
