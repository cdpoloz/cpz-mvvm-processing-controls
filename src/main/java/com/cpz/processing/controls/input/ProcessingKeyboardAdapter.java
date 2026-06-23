package com.cpz.processing.controls.input;

import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;

/**
 * @author CPZ
 */
public final class ProcessingKeyboardAdapter {
   private final KeyboardState keyboardState;
   private final InputManager inputManager;

   public ProcessingKeyboardAdapter(KeyboardState keyboardState, InputManager inputManager) {
      this.keyboardState = keyboardState;
      this.inputManager = inputManager;
   }

   public void keyPressed(char key, int keyCode) {
      this.keyboardState.keyPressed(keyCode);
      this.dispatch(KeyboardEvent.Type.PRESS, key, keyCode);
   }

   public void keyReleased(char key, int keyCode) {
      this.keyboardState.keyReleased(keyCode);
      this.dispatch(KeyboardEvent.Type.RELEASE, key, keyCode);
   }

   public void keyTyped(char key, int keyCode) {
      this.dispatch(KeyboardEvent.Type.TYPE, key, keyCode);
   }

   private void dispatch(KeyboardEvent.Type type, char key, int value) {
      this.inputManager.dispatchKeyboard(new KeyboardEvent(type, key, value, this.keyboardState.isShiftDown(), this.keyboardState.isCtrlDown(), this.keyboardState.isAltDown()));
   }
}
