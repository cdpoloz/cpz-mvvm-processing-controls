package com.cpz.processing.controls.core.overlay;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OverlayManagerTest {
    @Test
    void getActiveOverlaysReturnsSnapshotSafeForSelfUnregisterDuringRendering() {
        OverlayManager overlayManager = new OverlayManager();
        OverlayEntry stable = new OverlayEntry(10, () -> {
        }, null);
        OverlayEntry unregistering = new OverlayEntry(20, () -> overlayManager.unregister(stable), null);
        overlayManager.register(unregistering);
        overlayManager.register(stable);

        assertDoesNotThrow(() -> {
            for (OverlayEntry entry : overlayManager.getActiveOverlays()) {
                entry.getRender().run();
            }
        });

        assertEquals(1, overlayManager.getActiveOverlays().size());
        assertEquals(unregistering, overlayManager.getActiveOverlays().get(0));
    }

    @Test
    void activeOverlaySnapshotKeepsDescendingZIndexOrder() {
        OverlayManager overlayManager = new OverlayManager();
        OverlayEntry low = new OverlayEntry(10, () -> {
        }, null);
        OverlayEntry high = new OverlayEntry(50, () -> {
        }, null);
        OverlayEntry middle = new OverlayEntry(30, () -> {
        }, null);

        overlayManager.register(low);
        overlayManager.register(high);
        overlayManager.register(middle);

        assertEquals(high, overlayManager.getActiveOverlays().get(0));
        assertEquals(middle, overlayManager.getActiveOverlays().get(1));
        assertEquals(low, overlayManager.getActiveOverlays().get(2));
    }

    @Test
    void activeOverlaySnapshotIsImmutable() {
        OverlayManager overlayManager = new OverlayManager();
        overlayManager.register(new OverlayEntry(10, () -> {
        }, null));

        assertThrows(UnsupportedOperationException.class, () -> overlayManager.getActiveOverlays().clear());
        assertEquals(1, overlayManager.getActiveOverlays().size());
    }
}
