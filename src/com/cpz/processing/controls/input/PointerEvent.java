package com.cpz.processing.controls.input;

public final class PointerEvent {

    public enum Type {
        MOVE,
        DRAG,
        WHEEL,
        PRESS,
        RELEASE
    }

    private final Type type;
    private final float x;
    private final float y;
    private final int button;
    private final float wheelDelta;
    private final boolean shiftDown;
    private final boolean controlDown;

    public PointerEvent(Type type, float x, float y) {
        this(type, x, y, 0, 0f, false, false);
    }

    public PointerEvent(Type type, float x, float y, int button) {
        this(type, x, y, button, 0f, false, false);
    }

    public PointerEvent(Type type, float x, float y, int button, float wheelDelta, boolean shiftDown, boolean controlDown) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.button = button;
        this.wheelDelta = wheelDelta;
        this.shiftDown = shiftDown;
        this.controlDown = controlDown;
    }

    public Type getType() {
        return type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getButton() {
        return button;
    }

    public float getWheelDelta() {
        return wheelDelta;
    }

    public boolean isShiftDown() {
        return shiftDown;
    }

    public boolean isControlDown() {
        return controlDown;
    }
}
