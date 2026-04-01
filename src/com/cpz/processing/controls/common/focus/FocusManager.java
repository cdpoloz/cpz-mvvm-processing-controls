package com.cpz.processing.controls.common.focus;

import com.cpz.processing.controls.common.input.KeyboardInputTarget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class FocusManager {

    public static final class FocusToken {

        private final String id = UUID.randomUUID().toString();
    }

    private static final class FocusSnapshot {

        private final Focusable target;
        private final int index;

        private FocusSnapshot(Focusable target, int index) {
            this.target = target;
            this.index = index;
        }
    }

    private final List<Focusable> focusables = new ArrayList<>();
    private final List<FocusToken> focusHistory = new ArrayList<>();
    private final Map<FocusToken, FocusSnapshot> snapshots = new HashMap<>();
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

    public FocusToken pushFocus() {
        FocusToken token = new FocusToken();
        snapshots.put(token, new FocusSnapshot(focused, focusedIndex));
        focusHistory.add(token);
        return token;
    }

    public void popFocus(FocusToken token) {
        releaseFocusToken(token, true);
    }

    public void discardFocus(FocusToken token) {
        releaseFocusToken(token, false);
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

    public Focusable getFocused() {
        return focused;
    }

    private void clearCurrentFocus() {
        if (focused != null) {
            focused.setFocused(false);
            focused.onFocusLost();
        }
    }

    private void releaseFocusToken(FocusToken token, boolean restore) {
        if (token == null) {
            return;
        }
        int index = focusHistory.lastIndexOf(token);
        if (index < 0) {
            snapshots.remove(token);
            return;
        }
        boolean wasTop = index == focusHistory.size() - 1;
        focusHistory.remove(index);
        FocusSnapshot snapshot = snapshots.remove(token);
        if (!restore || !wasTop || snapshot == null) {
            return;
        }
        restoreSnapshot(snapshot);
    }

    private void restoreSnapshot(FocusSnapshot snapshot) {
        if (snapshot.target == null) {
            clearFocus();
            return;
        }
        if (!snapshot.target.isVisible() || !snapshot.target.isEnabled()) {
            clearFocus();
            return;
        }
        requestFocus(snapshot.target);
        focusedIndex = snapshot.index >= 0 ? snapshot.index : focusables.indexOf(snapshot.target);
    }
}
