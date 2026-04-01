package com.cpz.processing.controls.overlay;

import com.cpz.processing.controls.common.focus.FocusManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OverlayManager {

    private final List<OverlayEntry> overlays = new ArrayList<>();
    private final List<OverlayEntry> focusManagedOverlays = new ArrayList<>();
    private final Map<OverlayEntry, FocusManager.FocusToken> focusTokens = new HashMap<>();
    private FocusManager focusManager;

    public OverlayManager() {
    }

    public OverlayManager(FocusManager focusManager) {
        this.focusManager = focusManager;
    }

    public void setFocusManager(FocusManager focusManager) {
        this.focusManager = focusManager;
    }

    public void register(OverlayEntry entry) {
        if (entry == null || overlays.contains(entry)) {
            return;
        }
        overlays.add(entry);
        sort();
        handleFocusOnRegister(entry);
    }

    public void unregister(OverlayEntry entry) {
        if (entry == null) {
            return;
        }
        overlays.remove(entry);
        handleFocusOnUnregister(entry);
    }

    private void sort() {
        overlays.sort((a, b) -> Integer.compare(b.getZIndex(), a.getZIndex()));
    }

    public List<OverlayEntry> getActiveOverlays() {
        return Collections.unmodifiableList(overlays);
    }

    public Optional<OverlayEntry> getTopOverlay() {
        if (overlays.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(overlays.get(0));
    }

    public boolean isTopOverlay(OverlayEntry entry) {
        return entry != null && !overlays.isEmpty() && overlays.get(0) == entry;
    }

    public void clearAll() {
        for (int i = focusManagedOverlays.size() - 1; i >= 0; i--) {
            OverlayEntry entry = focusManagedOverlays.get(i);
            FocusManager.FocusToken token = focusTokens.remove(entry);
            if (focusManager != null) {
                focusManager.discardFocus(token);
            }
        }
        focusManagedOverlays.clear();
        overlays.clear();
    }

    private void handleFocusOnRegister(OverlayEntry entry) {
        if (focusManager == null || entry.getFocusTarget() == null) {
            return;
        }
        FocusManager.FocusToken token = focusManager.pushFocus();
        focusTokens.put(entry, token);
        focusManagedOverlays.add(entry);
        focusManager.requestFocus(entry.getFocusTarget());
    }

    private void handleFocusOnUnregister(OverlayEntry entry) {
        FocusManager.FocusToken token = focusTokens.remove(entry);
        if (token == null) {
            focusManagedOverlays.remove(entry);
            return;
        }
        int index = focusManagedOverlays.lastIndexOf(entry);
        boolean wasTop = index == focusManagedOverlays.size() - 1;
        if (index >= 0) {
            focusManagedOverlays.remove(index);
        }
        if (focusManager == null) {
            return;
        }
        if (wasTop) {
            focusManager.popFocus(token);
        } else {
            focusManager.discardFocus(token);
        }
    }
}
