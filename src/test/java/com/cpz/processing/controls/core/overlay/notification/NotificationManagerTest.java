package com.cpz.processing.controls.core.overlay.notification;

import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.testsupport.ProcessingTestSupport;
import com.cpz.utils.time.TimeSource;
import java.util.List;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificationManagerTest {
    @Test
    void managerRegistersOneOverlayWhenFirstNotificationIsShown() {
        OverlayManager overlayManager = new OverlayManager();
        NotificationManager manager = manager(sketch(), overlayManager, new FakeTimeSource(0L));

        manager.show("Saved");

        assertEquals(1, overlayManager.getActiveOverlays().size());
        assertSame(manager.getOverlayEntry(), overlayManager.getActiveOverlays().get(0));
    }

    @Test
    void managerDoesNotRegisterDuplicateOverlaysForMultipleNotifications() {
        OverlayManager overlayManager = new OverlayManager();
        NotificationManager manager = manager(sketch(), overlayManager, new FakeTimeSource(0L));

        manager.info("One");
        manager.success("Two");
        manager.error("Three");

        assertEquals(1, overlayManager.getActiveOverlays().size());
        assertEquals(3, manager.size());
    }

    @Test
    void managerUnregistersOverlayAfterAllNotificationsExpireDuringRender() {
        OverlayManager overlayManager = new OverlayManager();
        FakeTimeSource clock = new FakeTimeSource(0L);
        NotificationManager manager = manager(sketch(), overlayManager, clock);
        manager.show("Short", NotificationSeverity.INFO, 100L);

        clock.setMillis(100L);
        manager.getOverlayEntry().getRender().run();

        assertEquals(0, overlayManager.getActiveOverlays().size());
        assertTrue(manager.isEmpty());
    }

    @Test
    void notificationExpirationDuringOverlayIterationIsSafe() {
        OverlayManager overlayManager = new OverlayManager();
        FakeTimeSource clock = new FakeTimeSource(0L);
        NotificationManager manager = manager(sketch(), overlayManager, clock);
        manager.show("First", NotificationSeverity.INFO, 100L);
        manager.show("Second", NotificationSeverity.SUCCESS, 100L);

        clock.setMillis(100L);

        assertDoesNotThrow(() -> {
            for (OverlayEntry entry : overlayManager.getActiveOverlays()) {
                entry.getRender().run();
            }
        });
        assertEquals(0, overlayManager.getActiveOverlays().size());
        assertTrue(manager.isEmpty());
    }

    @Test
    void clearUnregistersOverlayAndClearsItems() {
        OverlayManager overlayManager = new OverlayManager();
        NotificationManager manager = manager(sketch(), overlayManager, new FakeTimeSource(0L));
        manager.show("Saved");

        manager.clear();

        assertEquals(0, overlayManager.getActiveOverlays().size());
        assertTrue(manager.isEmpty());
    }

    @Test
    void disposeUnregistersOverlayAndClearsItems() {
        OverlayManager overlayManager = new OverlayManager();
        NotificationManager manager = manager(sketch(), overlayManager, new FakeTimeSource(0L));
        manager.show("Saved");

        manager.dispose();

        assertEquals(0, overlayManager.getActiveOverlays().size());
        assertTrue(manager.isEmpty());
    }

    @Test
    void showAfterClearOrDisposeRegistersOverlayAgain() {
        OverlayManager overlayManager = new OverlayManager();
        NotificationManager manager = manager(sketch(), overlayManager, new FakeTimeSource(0L));

        manager.show("Before clear");
        manager.clear();
        manager.show("After clear");

        assertEquals(1, overlayManager.getActiveOverlays().size());
        assertEquals(1, manager.size());

        manager.dispose();
        manager.show("After dispose");

        assertEquals(1, overlayManager.getActiveOverlays().size());
        assertEquals(1, manager.size());
        assertEquals("After dispose", manager.getActiveNotifications().get(0).getMessage());
    }

    @Test
    void severityConvenienceMethodsCreateExpectedNotifications() {
        NotificationManager manager = manager(sketch(), new OverlayManager(), new FakeTimeSource(0L));

        assertEquals(NotificationSeverity.INFO, manager.info("Info").getSeverity());
        assertEquals(NotificationSeverity.SUCCESS, manager.success("Success").getSeverity());
        assertEquals(NotificationSeverity.WARNING, manager.warning("Warning").getSeverity());
        assertEquals(NotificationSeverity.ERROR, manager.error("Error").getSeverity());
    }

    @Test
    void defaultSeverityDurationsMatchSeverityPolicy() {
        NotificationManager manager = manager(sketch(), new OverlayManager(), new FakeTimeSource(0L));

        assertEquals(3000L, manager.getSeverityDurationMillis(NotificationSeverity.INFO));
        assertEquals(3000L, manager.getSeverityDurationMillis(NotificationSeverity.SUCCESS));
        assertEquals(4500L, manager.getSeverityDurationMillis(NotificationSeverity.WARNING));
        assertEquals(6000L, manager.getSeverityDurationMillis(NotificationSeverity.ERROR));
        assertEquals(3000L, manager.getDefaultDurationMillis());
    }

    @Test
    void convenienceMethodsUseConfiguredSeverityDurations() {
        NotificationManager manager = manager(sketch(), new OverlayManager(), new FakeTimeSource(0L));

        assertEquals(3000L, manager.info("Info").getDurationMillis());
        assertEquals(3000L, manager.success("Success").getDurationMillis());
        assertEquals(4500L, manager.warning("Warning").getDurationMillis());
        assertEquals(6000L, manager.error("Error").getDurationMillis());
    }

    @Test
    void showWithSeverityUsesConfiguredSeverityDuration() {
        NotificationManager manager = manager(sketch(), new OverlayManager(), new FakeTimeSource(0L));
        manager.setSeverityDurationMillis(NotificationSeverity.WARNING, 7000L);

        Notification notification = manager.show("Warning", NotificationSeverity.WARNING);

        assertEquals(NotificationSeverity.WARNING, notification.getSeverity());
        assertEquals(7000L, notification.getDurationMillis());
    }

    @Test
    void explicitDurationOverridesConfiguredSeverityDuration() {
        NotificationManager manager = manager(sketch(), new OverlayManager(), new FakeTimeSource(0L));
        manager.setSeverityDurationMillis(NotificationSeverity.ERROR, 6000L);

        Notification notification = manager.show("Custom", NotificationSeverity.ERROR, 10000L);

        assertEquals(NotificationSeverity.ERROR, notification.getSeverity());
        assertEquals(10000L, notification.getDurationMillis());
    }

    @Test
    void setSeverityDurationChangesOnlyThatSeverity() {
        NotificationManager manager = manager(sketch(), new OverlayManager(), new FakeTimeSource(0L));

        manager.setSeverityDurationMillis(NotificationSeverity.WARNING, 7000L);

        assertEquals(3000L, manager.getSeverityDurationMillis(NotificationSeverity.INFO));
        assertEquals(3000L, manager.getSeverityDurationMillis(NotificationSeverity.SUCCESS));
        assertEquals(7000L, manager.getSeverityDurationMillis(NotificationSeverity.WARNING));
        assertEquals(6000L, manager.getSeverityDurationMillis(NotificationSeverity.ERROR));
    }

    @Test
    void setDefaultDurationUpdatesAllSeverityDurations() {
        NotificationManager manager = manager(sketch(), new OverlayManager(), new FakeTimeSource(0L));

        manager.setDefaultDurationMillis(2500L);

        assertEquals(2500L, manager.getDefaultDurationMillis());
        assertEquals(2500L, manager.getSeverityDurationMillis(NotificationSeverity.INFO));
        assertEquals(2500L, manager.getSeverityDurationMillis(NotificationSeverity.SUCCESS));
        assertEquals(2500L, manager.getSeverityDurationMillis(NotificationSeverity.WARNING));
        assertEquals(2500L, manager.getSeverityDurationMillis(NotificationSeverity.ERROR));

        manager.setSeverityDurationMillis(NotificationSeverity.ERROR, 8000L);

        assertEquals(2500L, manager.info("Info").getDurationMillis());
        assertEquals(8000L, manager.error("Error").getDurationMillis());
    }

    @Test
    void showCreatesExpectedNotificationItems() {
        FakeTimeSource clock = new FakeTimeSource(25L);
        NotificationManager manager = manager(sketch(), new OverlayManager(), clock);

        Notification notification = manager.show("  Saved  ", NotificationSeverity.SUCCESS, 1500L);

        assertTrue(notification.getId().startsWith("notification-"));
        assertEquals("Saved", notification.getMessage());
        assertEquals(NotificationSeverity.SUCCESS, notification.getSeverity());
        assertEquals(25L, notification.getCreatedAtMillis());
        assertEquals(1500L, notification.getDurationMillis());
        assertFalse(notification.isExpired(1524L));
        assertTrue(notification.isExpired(1525L));
        assertTrue(notification.isExpired(1600L));
    }

    @Test
    void nullSeverityDefaultsToInfo() {
        NotificationManager manager = manager(sketch(), new OverlayManager(), new FakeTimeSource(0L));

        Notification notification = manager.show("Message", null);

        assertEquals(NotificationSeverity.INFO, notification.getSeverity());
        assertEquals(3000L, notification.getDurationMillis());
    }

    @Test
    void nullAndBlankMessagesFailFast() {
        NotificationManager manager = manager(sketch(), new OverlayManager(), new FakeTimeSource(0L));

        assertThrows(NullPointerException.class, () -> manager.show(null));
        assertThrows(IllegalArgumentException.class, () -> manager.show("   "));
    }

    @Test
    void invalidDurationFailsFast() {
        NotificationManager manager = manager(sketch(), new OverlayManager(), new FakeTimeSource(0L));

        assertThrows(IllegalArgumentException.class, () -> manager.show("Invalid", NotificationSeverity.INFO, 0L));
        assertThrows(IllegalArgumentException.class, () -> manager.setDefaultDurationMillis(-1L));
        assertThrows(IllegalArgumentException.class, () -> manager.setSeverityDurationMillis(NotificationSeverity.INFO, 0L));
    }

    @Test
    void nullSeverityDurationAccessFailsFast() {
        NotificationManager manager = manager(sketch(), new OverlayManager(), new FakeTimeSource(0L));

        assertThrows(NullPointerException.class, () -> manager.getSeverityDurationMillis(null));
        assertThrows(NullPointerException.class, () -> manager.setSeverityDurationMillis(null, 3000L));
    }

    @Test
    void defaultAndCustomDurationDriveExpiry() {
        OverlayManager overlayManager = new OverlayManager();
        FakeTimeSource clock = new FakeTimeSource(0L);
        NotificationManager manager = manager(sketch(), overlayManager, clock);
        manager.setDefaultDurationMillis(500L);

        Notification defaultDuration = manager.show("Default");
        Notification customDuration = manager.show("Custom", NotificationSeverity.INFO, 1000L);

        assertEquals(500L, defaultDuration.getDurationMillis());
        assertEquals(1000L, customDuration.getDurationMillis());

        clock.setMillis(500L);
        assertEquals(1, manager.size());
        assertEquals("Custom", manager.getActiveNotifications().get(0).getMessage());

        clock.setMillis(1000L);
        assertTrue(manager.isEmpty());
        assertEquals(0, overlayManager.getActiveOverlays().size());
    }

    @Test
    void maxVisibleDropsOldestNotifications() {
        NotificationManager manager = manager(sketch(), new OverlayManager(), new FakeTimeSource(0L));
        manager.setMaxVisible(2);

        manager.show("First");
        manager.show("Second");
        manager.show("Third");

        List<Notification> notifications = manager.getActiveNotifications();
        assertEquals(2, notifications.size());
        assertEquals("Second", notifications.get(0).getMessage());
        assertEquals("Third", notifications.get(1).getMessage());
    }

    @Test
    void topPlacementsStackDownwardWithNewestClosestToEdge() {
        NotificationManager manager = manager(sketch(), new OverlayManager(), new FakeTimeSource(0L));
        NotificationStyle style = new NotificationStyle()
                .setMargin(10.0F)
                .setGap(5.0F)
                .setMinHeight(40.0F)
                .setTextSize(12.0F);
        manager.setStyle(style);
        manager.setPlacement(NotificationPlacement.TOP_RIGHT);
        manager.show("Older");
        manager.show("Newest");

        List<NotificationManager.NotificationFrame> frames = manager.layoutFrames();

        assertEquals("Newest", frames.get(0).notification().getMessage());
        assertEquals(10.0F, frames.get(0).y());
        assertTrue(frames.get(1).y() > frames.get(0).y());
    }

    @Test
    void bottomPlacementsStackUpwardWithNewestClosestToEdge() {
        NotificationManager manager = manager(sketch(), new OverlayManager(), new FakeTimeSource(0L));
        NotificationStyle style = new NotificationStyle()
                .setMargin(10.0F)
                .setGap(5.0F)
                .setMinHeight(40.0F)
                .setTextSize(12.0F);
        manager.setStyle(style);
        manager.setPlacement(NotificationPlacement.BOTTOM_LEFT);
        manager.show("Older");
        manager.show("Newest");

        List<NotificationManager.NotificationFrame> frames = manager.layoutFrames();

        assertEquals("Newest", frames.get(0).notification().getMessage());
        assertEquals(350.0F, frames.get(0).y());
        assertTrue(frames.get(1).y() < frames.get(0).y());
    }

    @Test
    void layoutClampsWidthAndWrapsLongMessages() {
        NotificationManager manager = manager(sketch(), new OverlayManager(), new FakeTimeSource(0L));
        manager.setStyle(new NotificationStyle()
                .setWidth(800.0F)
                .setMargin(20.0F)
                .setAccentWidth(5.0F)
                .setTextPadding(10.0F));
        manager.show("This notification message is intentionally long enough to wrap across multiple rendered lines.");

        List<NotificationManager.NotificationFrame> frames = manager.layoutFrames();

        assertEquals(460.0F, frames.get(0).width());
        assertTrue(frames.get(0).lines().size() > 1);
    }

    @Test
    void centerPlacementUsesCurrentCanvasCenterOnEachLayout() {
        RecordingApplet sketch = recordingSketch();
        NotificationManager manager = manager(sketch, new OverlayManager(), new FakeTimeSource(0L));
        manager.setStyle(new NotificationStyle()
                .setWidth(200.0F)
                .setMargin(10.0F));
        manager.setPlacement(NotificationPlacement.TOP_CENTER);
        manager.show("Centered");

        assertEquals(150.0F, manager.layoutFrames().get(0).x());

        sketch.width = 300;

        assertEquals(50.0F, manager.layoutFrames().get(0).x());
    }

    @Test
    void allPlacementsResolveExpectedHorizontalPosition() {
        assertPlacementX(NotificationPlacement.TOP_LEFT, 10.0F);
        assertPlacementX(NotificationPlacement.BOTTOM_LEFT, 10.0F);
        assertPlacementX(NotificationPlacement.TOP_RIGHT, 290.0F);
        assertPlacementX(NotificationPlacement.BOTTOM_RIGHT, 290.0F);
        assertPlacementX(NotificationPlacement.TOP_CENTER, 150.0F);
        assertPlacementX(NotificationPlacement.BOTTOM_CENTER, 150.0F);
    }

    @Test
    void overlayEntryHasNoFocusTargetAndNoInputLayer() {
        NotificationManager manager = manager(sketch(), new OverlayManager(), new FakeTimeSource(0L));
        OverlayEntry entry = manager.getOverlayEntry();

        assertNull(entry.getFocusTarget());
        assertNull(entry.getInputLayer());
    }

    @Test
    void notificationZIndexIsBelowDropDownZIndex() {
        NotificationManager manager = manager(sketch(), new OverlayManager(), new FakeTimeSource(0L));

        assertEquals(NotificationManager.DEFAULT_Z_INDEX, manager.getOverlayEntry().getZIndex());
        assertTrue(manager.getOverlayEntry().getZIndex() < 100);
    }

    @Test
    void setStyleNullRestoresDefaultStyle() {
        NotificationManager manager = manager(sketch(), new OverlayManager(), new FakeTimeSource(0L));
        NotificationStyle custom = new NotificationStyle().setBackgroundColor(0xFF111111);

        manager.setStyle(custom);
        manager.setStyle(null);

        assertNotSame(custom, manager.getStyle());
        assertEquals(NotificationStyle.DEFAULT_BACKGROUND_COLOR, manager.getStyle().getBackgroundColor());
    }

    @Test
    void notificationStyleCopyConstructorPreservesFont() {
        PFont font = ProcessingTestSupport.font("Monospaced", 18);
        NotificationStyle source = new NotificationStyle()
                .setFont(font)
                .setTextSize(18.0F);

        NotificationStyle copy = new NotificationStyle(source);

        assertSame(font, copy.getFont());
        assertEquals(18.0F, copy.getTextSize());
    }

    @Test
    void negativeStrokeWeightIsClampedToZero() {
        NotificationStyle style = new NotificationStyle();

        style.setStrokeWeight(-2.0F);

        assertEquals(0.0F, style.getStrokeWeight());
    }

    @Test
    void zeroStrokeWeightDisablesBorderRendering() {
        RecordingApplet sketch = recordingSketch();
        NotificationManager manager = manager(sketch, new OverlayManager(), new FakeTimeSource(0L));
        manager.setStyle(new NotificationStyle().setStrokeWeight(0.0F));
        manager.show("No border");

        manager.getOverlayEntry().getRender().run();

        assertEquals(0, sketch.strokeCalls);
        assertTrue(sketch.noStrokeCalls > 0);
    }

    @Test
    void renderAppliesTypographyBeforeMeasurementAndDrawing() {
        TypographyRecordingApplet sketch = typographySketch();
        NotificationManager manager = manager(sketch, new OverlayManager(), new FakeTimeSource(0L));
        PFont font = ProcessingTestSupport.font("Monospaced", 18);
        manager.setStyle(new NotificationStyle()
                .setFont(font)
                .setTextSize(18.0F)
                .setWidth(180.0F)
                .setTextPadding(12.0F)
                .setAccentWidth(5.0F));
        manager.show("This notification message must wrap across multiple lines to force measurement.");

        manager.getOverlayEntry().getRender().run();

        assertTrue(sketch.textWidthCalls > 0);
        assertSame(font, sketch.fontDuringTextWidth);
        assertEquals(18.0F, sketch.textSizeDuringTextWidth);
        assertTrue(sketch.textCalls > 0);
        assertSame(font, sketch.fontDuringTextDraw);
        assertEquals(18.0F, sketch.textSizeDuringTextDraw);
    }

    private static NotificationManager manager(PApplet sketch, OverlayManager overlayManager, TimeSource clock) {
        return new NotificationManager(sketch, overlayManager, clock);
    }

    private static void assertPlacementX(NotificationPlacement placement, float expectedX) {
        NotificationManager manager = manager(recordingSketch(), new OverlayManager(), new FakeTimeSource(0L));
        manager.setStyle(new NotificationStyle()
                .setWidth(200.0F)
                .setMargin(10.0F));
        manager.setPlacement(placement);
        manager.show("Placed");

        assertEquals(expectedX, manager.layoutFrames().get(0).x());
    }

    private static PApplet sketch() {
        return recordingSketch();
    }

    private static RecordingApplet recordingSketch() {
        RecordingApplet sketch = new RecordingApplet();
        sketch.width = 500;
        sketch.height = 400;
        return sketch;
    }

    private static TypographyRecordingApplet typographySketch() {
        TypographyRecordingApplet sketch = new TypographyRecordingApplet();
        sketch.width = 500;
        sketch.height = 400;
        ProcessingTestSupport.graphics(sketch);
        return sketch;
    }

    private static final class FakeTimeSource implements TimeSource {
        private long nowNanos;

        private FakeTimeSource(long nowMillis) {
            this.setMillis(nowMillis);
        }

        void setMillis(long nowMillis) {
            this.nowNanos = nowMillis * 1_000_000L;
        }

        public long nowNanos() {
            return this.nowNanos;
        }
    }

    private static final class RecordingApplet extends PApplet {
        private int strokeCalls;
        private int noStrokeCalls;

        @Override
        public void pushStyle() {
        }

        @Override
        public void popStyle() {
        }

        @Override
        public void fill(int rgb) {
        }

        @Override
        public void stroke(int rgb) {
            this.strokeCalls++;
        }

        @Override
        public void strokeWeight(float weight) {
        }

        @Override
        public void noStroke() {
            this.noStrokeCalls++;
        }

        @Override
        public void textSize(float size) {
        }

        @Override
        public void textAlign(int alignX, int alignY) {
        }

        @Override
        public float textWidth(String text) {
            return text == null ? 0.0F : text.length() * 7.0F;
        }

        @Override
        public void text(String str, float x, float y) {
        }

        @Override
        public void rect(float a, float b, float c, float d, float r) {
        }

        @Override
        public void rect(float a, float b, float c, float d, float tl, float tr, float br, float bl) {
        }
    }

    private static final class TypographyRecordingApplet extends PApplet {
        private int textWidthCalls;
        private int textCalls;
        private PFont fontDuringTextWidth;
        private float textSizeDuringTextWidth;
        private PFont fontDuringTextDraw;
        private float textSizeDuringTextDraw;

        @Override
        public void pushStyle() {
        }

        @Override
        public void popStyle() {
        }

        @Override
        public void fill(int rgb) {
        }

        @Override
        public void stroke(int rgb) {
        }

        @Override
        public void strokeWeight(float weight) {
        }

        @Override
        public void noStroke() {
        }

        @Override
        public void textAlign(int alignX, int alignY) {
        }

        @Override
        public float textWidth(String text) {
            PGraphics graphics = this.getGraphics();
            this.textWidthCalls++;
            this.fontDuringTextWidth = graphics.textFont;
            this.textSizeDuringTextWidth = graphics.textSize;
            return text == null ? 0.0F : text.length() * 7.0F;
        }

        @Override
        public void text(String str, float x, float y) {
            PGraphics graphics = this.getGraphics();
            this.textCalls++;
            this.fontDuringTextDraw = graphics.textFont;
            this.textSizeDuringTextDraw = graphics.textSize;
        }

        @Override
        public void rect(float a, float b, float c, float d, float r) {
        }

        @Override
        public void rect(float a, float b, float c, float d, float tl, float tr, float br, float bl) {
        }
    }
}
