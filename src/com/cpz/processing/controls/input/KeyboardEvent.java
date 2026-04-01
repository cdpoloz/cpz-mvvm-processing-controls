package com.cpz.processing.controls.input;

public final class KeyboardEvent {

    public enum Type {
        PRESS,
        TYPE,
        RELEASE
    }

    private final Type type;
    private final char key;
    private final int keyCode;
    private final boolean shiftDown;
    private final boolean controlDown;
    private final boolean altDown;

    public KeyboardEvent(Type type, char key, int keyCode, boolean shiftDown, boolean controlDown, boolean altDown) {
        this.type = type;
        this.key = key;
        this.keyCode = keyCode;
        this.shiftDown = shiftDown;
        this.controlDown = controlDown;
        this.altDown = altDown;
    }

    public Type getType() {
        return type;
    }

    public char getKey() {
        return key;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public boolean isShiftDown() {
        return shiftDown;
    }

    public boolean isControlDown() {
        return controlDown;
    }

    public boolean isAltDown() {
        return altDown;
    }
}
