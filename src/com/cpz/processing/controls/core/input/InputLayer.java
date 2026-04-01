package com.cpz.processing.controls.core.input;

public interface InputLayer {
   int getPriority();

   boolean isActive();

   boolean isPointerCaptureEnabled();

   boolean isKeyboardCaptureEnabled();

   boolean handlePointerEvent(PointerEvent var1);

   boolean handleKeyboardEvent(KeyboardEvent var1);
}
