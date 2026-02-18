package com.cpz.processing.controls.buttoncontrol.input;

import com.cpz.processing.controls.buttoncontrol.view.ButtonView;
import com.cpz.processing.controls.buttoncontrol.viewmodel.ButtonViewModel;
import com.cpz.processing.controls.hit.RectHitTest;

/**
 * @author CPZ
 */
public final class ButtonInputAdapter {

    private final ButtonView view;
    private final ButtonViewModel viewModel;
    private final RectHitTest hitTest;

    public ButtonInputAdapter(ButtonView view, ButtonViewModel viewModel) {
        this.view = view;
        this.viewModel = viewModel;
        this.hitTest = new RectHitTest();
    }

    public void handleMouseMove(float mx, float my) {
        updateLayout();
        boolean inside = hitTest.contains(mx, my);
        viewModel.onMouseMove(inside);
    }

    public void handleMousePress(float mx, float my) {
        updateLayout();
        boolean inside = hitTest.contains(mx, my);
        viewModel.onMousePress(inside);
    }

    public void handleMouseRelease(float mx, float my) {
        updateLayout();
        boolean inside = hitTest.contains(mx, my);
        viewModel.onMouseRelease(inside);
    }

    private void updateLayout() {
        hitTest.onLayout(view.getX(), view.getY(), view.getWidth(), view.getHeight());
    }
}
