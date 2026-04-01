package com.cpz.processing.controls.input;

public final class PointerEvent {

    public enum Type {
        MOVE,
        PRESS,
        RELEASE
    }

    private final Type type;
    private final float x;
    private final float y;
    private final int button;

    public PointerEvent(Type type, float x, float y) {
        this(type, x, y, 0);
    }

    public PointerEvent(Type type, float x, float y, int button) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.button = button;
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
}
