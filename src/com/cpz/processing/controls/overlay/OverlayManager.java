package com.cpz.processing.controls.overlay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OverlayManager {

    private final List<OverlayEntry> overlays = new ArrayList<>();

    public void register(OverlayEntry entry) {
        if (entry == null || overlays.contains(entry)) {
            return;
        }
        overlays.add(entry);
        sort();
    }

    public void unregister(OverlayEntry entry) {
        overlays.remove(entry);
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

    public void clearAll() {
        overlays.clear();
    }
}
