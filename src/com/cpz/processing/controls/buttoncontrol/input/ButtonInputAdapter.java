package com.cpz.processing.controls.buttoncontrol.input;

import com.cpz.processing.controls.common.input.PointerInputAdapter;
import com.cpz.processing.controls.common.input.PointerInteractable;
import com.cpz.processing.controls.common.input.PointerInputTarget;

/**
 * @author CPZ
 */
public final class ButtonInputAdapter {

    private final PointerInputAdapter delegate;

    public ButtonInputAdapter(PointerInteractable view, PointerInputTarget viewModel) {
        this.delegate = new PointerInputAdapter(view, viewModel);
    }

    public void handleMouseMove(float mx, float my) {
        delegate.handleMouseMove(mx, my);
    }

    public void handleMousePress(float mx, float my) {
        delegate.handleMousePress(mx, my);
    }

    public void handleMouseRelease(float mx, float my) {
        delegate.handleMouseRelease(mx, my);
    }
}
