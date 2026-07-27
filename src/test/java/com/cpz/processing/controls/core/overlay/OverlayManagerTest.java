package com.cpz.processing.controls.core.overlay;

import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.focus.Focusable;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void clearAllInvokesEveryCloseCallbackOnceWhenCallbacksMutateRegistration() {
        OverlayManager overlayManager = new OverlayManager();
        AtomicInteger firstCloses = new AtomicInteger();
        AtomicInteger secondCloses = new AtomicInteger();
        OverlayEntry[] entries = new OverlayEntry[3];
        entries[0] = new OverlayEntry(30, () -> {
        }, null, () -> {
            firstCloses.incrementAndGet();
            overlayManager.unregister(entries[0]);
            overlayManager.unregister(entries[1]);
        });
        entries[1] = new OverlayEntry(20, () -> {
        }, null, () -> {
            secondCloses.incrementAndGet();
            overlayManager.unregister(entries[1]);
        });
        entries[2] = new OverlayEntry(10, () -> {
        }, null);
        overlayManager.register(entries[0]);
        overlayManager.register(entries[1]);
        overlayManager.register(entries[2]);

        assertDoesNotThrow(overlayManager::clearAll);

        assertEquals(1, firstCloses.get());
        assertEquals(1, secondCloses.get(),
                "an entry present at the start must still be notified if another callback unregisters it");
        assertTrue(overlayManager.getActiveOverlays().isEmpty());

        overlayManager.clearAll();

        assertEquals(1, firstCloses.get());
        assertEquals(1, secondCloses.get());
    }

    @Test
    void clearAllAlsoClosesEntriesRegisteredByACloseCallback() {
        OverlayManager overlayManager = new OverlayManager();
        AtomicInteger addedCloses = new AtomicInteger();
        OverlayEntry[] added = new OverlayEntry[1];
        added[0] = new OverlayEntry(5, () -> {
        }, null, () -> {
            addedCloses.incrementAndGet();
            overlayManager.unregister(added[0]);
        });
        OverlayEntry original = new OverlayEntry(10, () -> {
        }, null, () -> overlayManager.register(added[0]));
        overlayManager.register(original);

        overlayManager.clearAll();

        assertEquals(1, addedCloses.get());
        assertTrue(overlayManager.getActiveOverlays().isEmpty());
    }

    @Test
    void clearAllRestoresFocusLikeClosingFocusManagedOverlaysInReverseRegistrationOrder() {
        FocusManager focusManager = new FocusManager();
        OverlayManager overlayManager = new OverlayManager(focusManager);
        FocusTarget base = new FocusTarget();
        FocusTarget firstOverlayTarget = new FocusTarget();
        FocusTarget secondOverlayTarget = new FocusTarget();
        OverlayEntry[] entries = new OverlayEntry[2];
        entries[0] = new OverlayEntry(100, () -> {
        }, null, () -> overlayManager.unregister(entries[0]), firstOverlayTarget);
        entries[1] = new OverlayEntry(10, () -> {
        }, null, () -> overlayManager.unregister(entries[1]), secondOverlayTarget);
        focusManager.requestFocus(base);
        overlayManager.register(entries[0]);
        overlayManager.register(entries[1]);
        assertTrue(secondOverlayTarget.isFocused());

        overlayManager.clearAll();

        assertTrue(base.isFocused());
        assertFalse(firstOverlayTarget.isFocused());
        assertFalse(secondOverlayTarget.isFocused());
        assertTrue(overlayManager.getActiveOverlays().isEmpty());
    }

    @Test
    void clearAllFinishesCleanupAndRethrowsCallbackFailures() {
        OverlayManager overlayManager = new OverlayManager();
        AtomicInteger callbacks = new AtomicInteger();
        overlayManager.register(new OverlayEntry(20, () -> {
        }, null, () -> {
            callbacks.incrementAndGet();
            throw new IllegalStateException("first");
        }));
        overlayManager.register(new OverlayEntry(10, () -> {
        }, null, () -> {
            callbacks.incrementAndGet();
            throw new IllegalArgumentException("second");
        }));

        IllegalStateException failure = assertThrows(IllegalStateException.class, overlayManager::clearAll);

        assertEquals("first", failure.getMessage());
        assertEquals(1, failure.getSuppressed().length);
        assertEquals("second", failure.getSuppressed()[0].getMessage());
        assertEquals(2, callbacks.get());
        assertTrue(overlayManager.getActiveOverlays().isEmpty());
    }

    private static final class FocusTarget implements Focusable {
        private boolean focused;
        private boolean visible = true;
        private boolean enabled = true;

        @Override
        public boolean isFocused() {
            return this.focused;
        }

        @Override
        public void setFocused(boolean focused) {
            this.focused = focused;
        }

        @Override
        public boolean isVisible() {
            return this.visible;
        }

        @Override
        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        @Override
        public boolean isEnabled() {
            return this.enabled;
        }

        @Override
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
