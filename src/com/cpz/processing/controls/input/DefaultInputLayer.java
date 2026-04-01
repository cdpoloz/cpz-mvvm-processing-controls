package com.cpz.processing.controls.input;

public class DefaultInputLayer implements InputLayer {

    private final int priority;
    private boolean active = true;

    public DefaultInputLayer(int priority) {
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean isPointerCaptureEnabled() {
        return true;
    }

    @Override
    public boolean isKeyboardCaptureEnabled() {
        return true;
    }

    @Override
    public boolean handlePointerEvent(PointerEvent event) {
        return false;
    }

    @Override
    public boolean handleKeyboardEvent(KeyboardEvent event) {
        return false;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
