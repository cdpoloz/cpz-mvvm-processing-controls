package com.cpz.processing.controls.input;

public interface InputLayer {

    int getPriority();

    boolean isActive();

    boolean isPointerCaptureEnabled();

    boolean isKeyboardCaptureEnabled();

    boolean handlePointerEvent(PointerEvent event);

    boolean handleKeyboardEvent(KeyboardEvent event);
}
