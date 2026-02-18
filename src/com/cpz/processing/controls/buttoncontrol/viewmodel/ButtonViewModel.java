package com.cpz.processing.controls.buttoncontrol.viewmodel;

import com.cpz.processing.controls.buttoncontrol.model.ButtonModel;
import com.cpz.processing.controls.buttoncontrol.view.ButtonViewState;

/**
 * @author CPZ
 */
public final class ButtonViewModel {

    private final ButtonModel model;
    private boolean hovered;
    private boolean pressed;
    private Runnable onClick;

    public ButtonViewModel(ButtonModel model) {
        this.model = model;
    }

    public void onMouseMove(boolean inside) {
        hovered = model.isEnabled() && inside;
    }

    public void onMousePress(boolean inside) {
        pressed = model.isEnabled() && inside;
    }

    public void onMouseRelease(boolean inside) {
        boolean shouldRun = pressed
                && inside
                && model.isEnabled()
                && onClick != null;
        pressed = false;
        hovered = model.isEnabled() && inside;
        if (shouldRun) {
            onClick.run();
        }
    }

    public void setOnClick(Runnable action) {
        this.onClick = action;
    }

    public ButtonViewState buildViewState(float x, float y, float width, float height) {
        return new ButtonViewState(
                x,
                y,
                width,
                height,
                model.getText(),
                model.isEnabled(),
                hovered,
                pressed
        );
    }
}
