package com.cpz.processing.controls.common.focus;

import com.cpz.processing.controls.common.input.KeyboardInputTarget;

import java.util.ArrayList;
import java.util.List;

public final class FocusManager {

    private final List<Focusable> focusables = new ArrayList<>();
    private Focusable focused;
    private int focusedIndex = -1;

    public void register(Focusable focusable) {
        if (focusable != null && !focusables.contains(focusable)) {
            focusables.add(focusable);
        }
    }

    public void requestFocus(Focusable target) {
        if (target == null || !target.isVisible() || !target.isEnabled()) {
            clearFocus();
            return;
        }
        register(target);
        if (focused == target) {
            target.setFocused(true);
            return;
        }
        clearCurrentFocus();
        focused = target;
        focusedIndex = focusables.indexOf(target);
        focused.setFocused(true);
        focused.onFocusGained();
    }

    public void clearFocus() {
        clearCurrentFocus();
        focused = null;
        focusedIndex = -1;
    }

    public boolean isFocused(Focusable target) {
        return focused == target && target != null && target.isFocused();
    }

    public void focusNext() {
        if (focusables.isEmpty()) {
            clearFocus();
            return;
        }
        int startIndex = focusedIndex >= 0 ? focusedIndex : -1;
        for (int offset = 1; offset <= focusables.size(); offset++) {
            int candidateIndex = Math.floorMod(startIndex + offset, focusables.size());
            Focusable candidate = focusables.get(candidateIndex);
            if (candidate.isVisible() && candidate.isEnabled()) {
                requestFocus(candidate);
                return;
            }
        }
        clearFocus();
    }

    public void focusPrevious() {
        if (focusables.isEmpty()) {
            clearFocus();
            return;
        }
        int startIndex = focusedIndex >= 0 ? focusedIndex : 0;
        for (int offset = 1; offset <= focusables.size(); offset++) {
            int candidateIndex = Math.floorMod(startIndex - offset, focusables.size());
            Focusable candidate = focusables.get(candidateIndex);
            if (candidate.isVisible() && candidate.isEnabled()) {
                requestFocus(candidate);
                return;
            }
        }
        clearFocus();
    }

    public KeyboardInputTarget getFocusedKeyboardTarget() {
        return focused instanceof KeyboardInputTarget ? (KeyboardInputTarget) focused : null;
    }

    private void clearCurrentFocus() {
        if (focused != null) {
            focused.setFocused(false);
            focused.onFocusLost();
        }
    }
}
