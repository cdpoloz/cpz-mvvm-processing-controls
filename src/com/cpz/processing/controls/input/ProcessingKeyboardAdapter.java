package com.cpz.processing.controls.input;

import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;

public final class ProcessingKeyboardAdapter {
   private final KeyboardState keyboardState;
   private final InputManager inputManager;

   public ProcessingKeyboardAdapter(KeyboardState var1, InputManager var2) {
      this.keyboardState = var1;
      this.inputManager = var2;
   }

   public void keyPressed(char var1, int var2) {
      this.keyboardState.keyPressed(var2);
      this.dispatch(KeyboardEvent.Type.PRESS, var1, var2);
   }

   public void keyReleased(char var1, int var2) {
      this.keyboardState.keyReleased(var2);
      this.dispatch(KeyboardEvent.Type.RELEASE, var1, var2);
   }

   public void keyTyped(char var1, int var2) {
      this.dispatch(KeyboardEvent.Type.TYPE, var1, var2);
   }

   private void dispatch(KeyboardEvent.Type var1, char var2, int var3) {
      this.inputManager.dispatchKeyboard(new KeyboardEvent(var1, var2, var3, this.keyboardState.isShiftDown(), this.keyboardState.isCtrlDown(), this.keyboardState.isAltDown()));
   }
}
