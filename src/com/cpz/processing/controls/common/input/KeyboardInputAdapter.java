package com.cpz.processing.controls.common.input;

import com.cpz.processing.controls.common.focus.FocusManager;
import processing.core.PConstants;
import processing.event.KeyEvent;

public final class KeyboardInputAdapter {

    private final FocusManager focusManager;
    private boolean suppressTypedOnce;

    public KeyboardInputAdapter(FocusManager focusManager) {
        this.focusManager = focusManager;
    }

    public void onKeyTyped(char key) {
        if (suppressTypedOnce) {
            suppressTypedOnce = false;
            return;
        }
        KeyboardInputTarget target = focusManager.getFocusedKeyboardTarget();
        if (target == null || !target.isVisible() || !target.isEnabled()) {
            return;
        }
        target.onKeyTyped(key);
    }

    public void onKeyPressed(char key, int keyCode, KeyEvent event) {
        boolean shift = event != null && event.isShiftDown();
        boolean ctrl = event != null && event.isControlDown();

        if (keyCode == PConstants.TAB) {
            suppressTypedOnce = true;
            if (shift) {
                focusManager.focusPrevious();
            } else {
                focusManager.focusNext();
            }
            return;
        }

        KeyboardInputTarget target = focusManager.getFocusedKeyboardTarget();
        if (target == null || !target.isVisible() || !target.isEnabled()) {
            return;
        }
        if (ctrl) {
            int code = event.getKeyCode();
            switch (code) {
                case java.awt.event.KeyEvent.VK_A:
                    suppressTypedOnce = true;
                    target.selectAll();
                    return;
                case java.awt.event.KeyEvent.VK_C:
                    suppressTypedOnce = true;
                    target.copySelection();
                    return;
                case java.awt.event.KeyEvent.VK_X:
                    suppressTypedOnce = true;
                    target.cutSelection();
                    return;
                case java.awt.event.KeyEvent.VK_V:
                    suppressTypedOnce = true;
                    target.pasteFromClipboard();
                    return;
                default:
                    break;
            }
        }
        if (keyCode == PConstants.BACKSPACE) {
            target.backspace();
            return;
        }
        if (keyCode == PConstants.DELETE) {
            target.deleteForward();
            return;
        }
        if (keyCode == PConstants.LEFT) {
            if (shift) {
                target.moveCursorLeftWithSelection();
            } else {
                target.moveCursorLeft();
            }
        } else if (keyCode == PConstants.RIGHT) {
            if (shift) {
                target.moveCursorRightWithSelection();
            } else {
                target.moveCursorRight();
            }
        }
    }
}
