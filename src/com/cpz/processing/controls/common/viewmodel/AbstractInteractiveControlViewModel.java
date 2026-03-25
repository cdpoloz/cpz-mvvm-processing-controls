package com.cpz.processing.controls.common.viewmodel;

import com.cpz.processing.controls.common.Enableable;
import com.cpz.processing.controls.common.input.PointerInputTarget;

public abstract class AbstractInteractiveControlViewModel<M extends Enableable>
        extends AbstractControlViewModel<M>
        implements PointerInputTarget {

    private boolean hovered;
    private boolean pressed;

    protected AbstractInteractiveControlViewModel(M model) {
        super(model);
    }

    @Override
    public final void onPointerMove(boolean inside) {
        if (!isInteractive()) {
            resetInteractionState();
            return;
        }
        hovered = inside;
    }

    @Override
    public final void onPointerPress(boolean inside) {
        if (!isInteractive()) {
            resetInteractionState();
            return;
        }
        hovered = inside;
        pressed = inside;
    }

    @Override
    public final void onPointerRelease(boolean inside) {
        boolean shouldActivate = shouldActivateOnRelease(inside);
        pressed = false;
        hovered = isInteractive() && inside;
        if (shouldActivate) {
            activate();
        }
    }

    public final boolean isHovered() {
        return hovered;
    }

    public final boolean isPressed() {
        return pressed;
    }

    protected final boolean isInteractive() {
        return isEnabled() && isVisible();
    }

    protected void resetInteractionState() {
        hovered = false;
        pressed = false;
    }

    @Override
    protected final void onAvailabilityChanged() {
        if (!isInteractive()) {
            resetInteractionState();
        }
    }

    protected boolean shouldActivateOnRelease(boolean inside) {
        return pressed && inside && isInteractive();
    }

    protected abstract void activate();
}
