package com.cpz.processing.controls.input;

import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;

/**
 * @author CPZ
 */
public final class ProcessingKeyboardAdapter {
   private static final char PROCESSING_CODED_KEY = (char)65535;
   private static final char PROCESSING_DELETE_KEY = 127;
   private static final int KEY_CODE_HOME = 36;
   private static final int KEY_CODE_END = 35;
   private static final int KEY_CODE_DELETE = 127;
   private static final int JOGL_KEY_CODE_HOME = 2;
   private static final int JOGL_KEY_CODE_END = 3;
   private static final int JOGL_KEY_CODE_DELETE = 147;
   private final KeyboardState keyboardState;
   private final InputManager inputManager;

   public ProcessingKeyboardAdapter(KeyboardState keyboardState, InputManager inputManager) {
      this.keyboardState = keyboardState;
      this.inputManager = inputManager;
   }

   public void keyPressed(char key, int keyCode) {
      int normalizedKeyCode = this.normalizeKeyCode(key, keyCode);
      this.keyboardState.keyPressed(normalizedKeyCode);
      this.dispatch(KeyboardEvent.Type.PRESS, key, normalizedKeyCode);
   }

   public void keyReleased(char key, int keyCode) {
      int normalizedKeyCode = this.normalizeKeyCode(key, keyCode);
      this.keyboardState.keyReleased(normalizedKeyCode);
      this.dispatch(KeyboardEvent.Type.RELEASE, key, normalizedKeyCode);
   }

   public void keyTyped(char key, int keyCode) {
      this.dispatch(KeyboardEvent.Type.TYPE, key, this.normalizeKeyCode(key, keyCode));
   }

   private void dispatch(KeyboardEvent.Type type, char key, int value) {
      this.inputManager.dispatchKeyboard(new KeyboardEvent(type, key, value, this.keyboardState.isShiftDown(), this.keyboardState.isCtrlDown(), this.keyboardState.isAltDown()));
   }

   private int normalizeKeyCode(char key, int keyCode) {
      if (key == PROCESSING_DELETE_KEY || keyCode == JOGL_KEY_CODE_DELETE) {
         return KEY_CODE_DELETE;
      }

      if (key == PROCESSING_CODED_KEY) {
         if (keyCode == JOGL_KEY_CODE_HOME) {
            return KEY_CODE_HOME;
         } else if (keyCode == JOGL_KEY_CODE_END) {
            return KEY_CODE_END;
         }
      }

      return keyCode;
   }
}
