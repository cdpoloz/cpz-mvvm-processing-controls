package com.cpz.processing.controls.core.overlay.notification;

import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.style.TypographySupport;
import com.cpz.utils.time.SystemTimeSource;
import com.cpz.utils.time.TimeSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import processing.core.PApplet;
import processing.core.PShape;

/**
 * App-facing manager for toast-style runtime notifications.
 *
 * <p>The manager owns one render-only overlay entry and draws all active
 * notifications through it. It does not register input layers and does not
 * participate in focus management.</p>
 *
 * @author CPZ
 */
public final class NotificationManager {
    public static final int DEFAULT_Z_INDEX = 50;
    public static final long DEFAULT_DURATION_MILLIS = 3000L;
    public static final int DEFAULT_MAX_VISIBLE = 4;
    private static final long NANOS_PER_MILLI = 1_000_000L;
    private static final long DEFAULT_INFO_DURATION_MILLIS = 3000L;
    private static final long DEFAULT_SUCCESS_DURATION_MILLIS = 3000L;
    private static final long DEFAULT_WARNING_DURATION_MILLIS = 4500L;
    private static final long DEFAULT_ERROR_DURATION_MILLIS = 6000L;

    private final PApplet sketch;
    private final OverlayManager overlayManager;
    private final TimeSource timeSource;
    private final OverlayEntry overlayEntry;
    private final List<Notification> notifications = new ArrayList<>();
    private final EnumMap<NotificationSeverity, Long> severityDurations = new EnumMap<>(NotificationSeverity.class);
    private final EnumMap<NotificationSeverity, CachedIcon> severityIconCache = new EnumMap<>(NotificationSeverity.class);
    private NotificationPlacement placement = NotificationPlacement.TOP_RIGHT;
    private NotificationPosition position;
    private NotificationStyle style = new NotificationStyle();
    private int maxVisible = DEFAULT_MAX_VISIBLE;
    private boolean registered;

    public NotificationManager(PApplet sketch, OverlayManager overlayManager) {
        this(sketch, overlayManager, new SystemTimeSource());
    }

    NotificationManager(PApplet sketch, OverlayManager overlayManager, TimeSource timeSource) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
        this.overlayManager = Objects.requireNonNull(overlayManager, "overlayManager");
        this.timeSource = Objects.requireNonNull(timeSource, "timeSource");
        this.overlayEntry = new OverlayEntry(DEFAULT_Z_INDEX, this::render, null, this::clear, null);
        this.resetSeverityDurations();
    }

    public Notification show(String message) {
        return this.show(message, NotificationSeverity.INFO);
    }

    public Notification show(String message, NotificationSeverity severity) {
        NotificationSeverity resolvedSeverity = severity == null ? NotificationSeverity.INFO : severity;
        return this.show(message, resolvedSeverity, this.getSeverityDurationMillis(resolvedSeverity));
    }

    public Notification show(String message, NotificationSeverity severity, long durationMillis) {
        this.validateDurationMillis(durationMillis);
        Notification notification = new Notification(message, severity, this.nowMillis(), durationMillis);
        this.notifications.add(notification);
        this.trimToMaxVisible();
        this.registerOverlay();
        return notification;
    }

    public Notification info(String message) {
        return this.show(message, NotificationSeverity.INFO);
    }

    public Notification success(String message) {
        return this.show(message, NotificationSeverity.SUCCESS);
    }

    public Notification warning(String message) {
        return this.show(message, NotificationSeverity.WARNING);
    }

    public Notification error(String message) {
        return this.show(message, NotificationSeverity.ERROR);
    }

    public void clear() {
        this.notifications.clear();
        this.unregisterOverlay();
    }

    public void dispose() {
        this.clear();
    }

    public int size() {
        this.pruneExpired();
        return this.notifications.size();
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public NotificationPlacement getPlacement() {
        return this.placement;
    }

    public void setPlacement(NotificationPlacement placement) {
        this.placement = placement == null ? NotificationPlacement.TOP_RIGHT : placement;
    }

    /**
     * Returns the optional custom origin of the notification stack.
     *
     * @return custom position, or {@code null} when placement controls layout
     */
    public NotificationPosition getPosition() {
        return this.position;
    }

    /**
     * Sets the custom origin of the notification stack. A {@code null} value
     * clears the custom origin without changing the configured placement.
     *
     * @param position custom stack origin, or {@code null} to clear it
     */
    public void setPosition(NotificationPosition position) {
        this.position = position;
    }

    /**
     * Sets the custom origin from independently configured axis measures.
     *
     * @param x horizontal absolute or relative measure
     * @param y vertical absolute or relative measure
     * @throws NullPointerException if either coordinate is {@code null}
     * @throws IllegalArgumentException if either coordinate is non-finite
     */
    public void setPosition(ControlMeasure x, ControlMeasure y) {
        this.setPosition(NotificationPosition.of(x, y));
    }

    /**
     * Sets an absolute custom origin for the notification stack.
     *
     * @param x absolute horizontal coordinate
     * @param y absolute vertical coordinate
     * @throws IllegalArgumentException if either coordinate is non-finite
     */
    public void setPosition(float x, float y) {
        this.setPosition(NotificationPosition.absolute(x, y));
    }

    /**
     * Clears the custom stack origin and restores layout through the currently
     * configured placement.
     */
    public void clearPosition() {
        this.position = null;
    }

    public NotificationStyle getStyle() {
        return this.style;
    }

    public void setStyle(NotificationStyle style) {
        this.style = style == null ? new NotificationStyle() : style;
    }

    public long getDefaultDurationMillis() {
        return this.getSeverityDurationMillis(NotificationSeverity.INFO);
    }

    public void setDefaultDurationMillis(long durationMillis) {
        this.validateDurationMillis(durationMillis);
        for (NotificationSeverity severity : NotificationSeverity.values()) {
            this.severityDurations.put(severity, durationMillis);
        }
    }

    public long getSeverityDurationMillis(NotificationSeverity severity) {
        return this.severityDurations.get(Objects.requireNonNull(severity, "severity"));
    }

    public void setSeverityDurationMillis(NotificationSeverity severity, long durationMillis) {
        Objects.requireNonNull(severity, "severity");
        this.validateDurationMillis(durationMillis);
        this.severityDurations.put(severity, durationMillis);
    }

    public int getMaxVisible() {
        return this.maxVisible;
    }

    public void setMaxVisible(int maxVisible) {
        if (maxVisible <= 0) {
            throw new IllegalArgumentException("Notification maxVisible must be greater than 0.");
        }
        this.maxVisible = maxVisible;
        this.trimToMaxVisible();
        if (this.notifications.isEmpty()) {
            this.unregisterOverlay();
        }
    }

    OverlayEntry getOverlayEntry() {
        return this.overlayEntry;
    }

    List<Notification> getActiveNotifications() {
        this.pruneExpired();
        return Collections.unmodifiableList(this.notifications);
    }

    List<NotificationFrame> layoutFrames() {
        this.pruneExpired();
        if (this.notifications.isEmpty()) {
            return Collections.emptyList();
        }

        this.prepareTypography();
        this.sketch.pushStyle();
        try {
            this.applyTypography();
            return this.layoutFramesWithoutStyleMutation();
        } finally {
            this.sketch.popStyle();
        }
    }

    private List<NotificationFrame> layoutFramesWithoutStyleMutation() {
        List<Notification> ordered = this.notificationsInEdgeOrder();
        List<NotificationFrame> frames = new ArrayList<>();
        float width = this.resolvedWidth();
        LayoutOrigin origin = this.resolvedLayoutOrigin(width);
        float cursor = origin.y();
        for (Notification notification : ordered) {
            PShape icon = this.resolveSeverityIcon(notification.getSeverity());
            float iconTextOffset = icon == null ? 0.0F : this.style.getIconSize() + this.style.getIconTextGap();
            float textWidth = Math.max(
                    1.0F,
                    width - this.style.getAccentWidth() - this.style.getTextPadding() * 2.0F - iconTextOffset
            );
            List<String> lines = this.wrapLines(notification.getMessage(), textWidth);
            float height = this.heightFor(lines);
            float y;
            if (origin.stacksDown()) {
                y = cursor;
                cursor += height + this.style.getGap();
            } else {
                y = cursor - height;
                cursor -= height + this.style.getGap();
            }
            frames.add(new NotificationFrame(notification, origin.x(), y, width, height, lines, icon));
        }
        return frames;
    }

    private void render() {
        List<NotificationFrame> frames = this.layoutFrames();
        if (frames.isEmpty()) {
            this.unregisterOverlay();
            return;
        }

        this.prepareTypography();
        this.sketch.pushStyle();
        try {
            this.applyTypography();
            for (NotificationFrame frame : frames) {
                this.drawFrame(frame);
            }
        } finally {
            this.sketch.popStyle();
        }
    }

    private void drawFrame(NotificationFrame frame) {
        if (this.style.getStrokeWeight() > 0.0F) {
            this.sketch.stroke(this.style.getBorderColor());
            this.sketch.strokeWeight(this.style.getStrokeWeight());
        } else {
            this.sketch.noStroke();
        }
        Integer severityBackgroundColor = this.style.getSeverityBackgroundColor(frame.notification().getSeverity());
        this.sketch.fill(severityBackgroundColor != null ? severityBackgroundColor : this.style.getBackgroundColor());
        this.sketch.rect(frame.x(), frame.y(), frame.width(), frame.height(), this.style.getCornerRadius());

        this.sketch.noStroke();
        if (this.style.getAccentWidth() > 0.0F) {
            this.sketch.fill(this.style.accentColor(frame.notification().getSeverity()));
            this.sketch.rect(
                    frame.x(),
                    frame.y(),
                    this.style.getAccentWidth(),
                    frame.height(),
                    this.style.getCornerRadius(),
                    0.0F,
                    0.0F,
                    this.style.getCornerRadius()
            );
        }

        if (frame.icon() != null) {
            this.drawIcon(frame);
        }

        this.sketch.fill(this.style.getTextColor());
        this.sketch.textAlign(PApplet.LEFT, PApplet.TOP);
        float textX = frame.x() + this.style.getAccentWidth() + this.style.getTextPadding();
        if (frame.icon() != null) {
            textX += this.style.getIconSize() + this.style.getIconTextGap();
        }
        float textY = frame.y() + this.style.getTextPadding();
        float lineHeight = this.lineHeight();
        for (int i = 0; i < frame.lines().size(); i++) {
            this.sketch.text(frame.lines().get(i), textX, textY + i * lineHeight);
        }
    }

    private void registerOverlay() {
        if (!this.registered) {
            this.overlayManager.register(this.overlayEntry);
            this.registered = true;
        }
    }

    private void unregisterOverlay() {
        if (this.registered) {
            this.overlayManager.unregister(this.overlayEntry);
            this.registered = false;
        }
    }

    private void pruneExpired() {
        long now = this.nowMillis();
        this.notifications.removeIf(notification -> notification.isExpired(now));
        if (this.notifications.isEmpty()) {
            this.unregisterOverlay();
        }
    }

    private long nowMillis() {
        return this.timeSource.nowNanos() / NANOS_PER_MILLI;
    }

    private void resetSeverityDurations() {
        this.severityDurations.put(NotificationSeverity.INFO, DEFAULT_INFO_DURATION_MILLIS);
        this.severityDurations.put(NotificationSeverity.SUCCESS, DEFAULT_SUCCESS_DURATION_MILLIS);
        this.severityDurations.put(NotificationSeverity.WARNING, DEFAULT_WARNING_DURATION_MILLIS);
        this.severityDurations.put(NotificationSeverity.ERROR, DEFAULT_ERROR_DURATION_MILLIS);
    }

    private void validateDurationMillis(long durationMillis) {
        if (durationMillis <= 0L) {
            throw new IllegalArgumentException("Notification duration must be greater than 0.");
        }
    }

    private void trimToMaxVisible() {
        while (this.notifications.size() > this.maxVisible) {
            this.notifications.remove(0);
        }
    }

    private PShape resolveSeverityIcon(NotificationSeverity severity) {
        NotificationSeverity resolvedSeverity = severity == null ? NotificationSeverity.INFO : severity;
        String path = this.style.getSeverityIcon(resolvedSeverity);
        if (path == null) {
            this.severityIconCache.remove(resolvedSeverity);
            return null;
        }

        CachedIcon cached = this.severityIconCache.get(resolvedSeverity);
        if (cached != null && cached.path.equals(path)) {
            return cached.shape;
        }

        PShape shape = this.loadSeverityIcon(resolvedSeverity, path);
        this.severityIconCache.put(resolvedSeverity, new CachedIcon(path, shape));
        return shape;
    }

    private PShape loadSeverityIcon(NotificationSeverity severity, String path) {
        PShape shape;
        try {
            shape = this.sketch.loadShape(path);
            if (shape == null && path.startsWith("data/")) {
                shape = this.sketch.loadShape(path.substring("data/".length()));
            }
        } catch (RuntimeException exception) {
            throw new IllegalArgumentException(
                    "Could not load notification SVG icon for severity " + severity + ": " + path,
                    exception
            );
        }
        if (shape == null) {
            throw new IllegalArgumentException(
                    "Could not load notification SVG icon for severity " + severity + ": " + path
            );
        }
        return shape;
    }

    private void drawIcon(NotificationFrame frame) {
        float iconSize = this.style.getIconSize();
        if (iconSize <= 0.0F) {
            return;
        }

        PShape icon = frame.icon();
        float iconWidth = iconSize;
        float iconHeight = iconSize;
        if (icon.getWidth() > 0.0F && icon.getHeight() > 0.0F) {
            float scale = Math.min(iconSize / icon.getWidth(), iconSize / icon.getHeight());
            iconWidth = icon.getWidth() * scale;
            iconHeight = icon.getHeight() * scale;
        }

        float areaX = frame.x() + this.style.getAccentWidth() + this.style.getTextPadding();
        float iconX = areaX + (iconSize - iconWidth) * 0.5F;
        float iconY = frame.y() + (frame.height() - iconHeight) * 0.5F;
        this.sketch.shapeMode(PApplet.CORNER);
        this.sketch.shape(icon, iconX, iconY, iconWidth, iconHeight);
    }

    private List<Notification> notificationsInEdgeOrder() {
        List<Notification> ordered = new ArrayList<>(this.notifications.size());
        for (int i = this.notifications.size() - 1; i >= 0; i--) {
            ordered.add(this.notifications.get(i));
        }
        return ordered;
    }

    private boolean startsAtTop() {
        return this.placement == NotificationPlacement.TOP_RIGHT
                || this.placement == NotificationPlacement.TOP_LEFT
                || this.placement == NotificationPlacement.TOP_CENTER;
    }

    private float resolvedWidth() {
        float requestedWidth = this.style.getWidth();
        if (this.sketch.width <= 0) {
            return requestedWidth;
        }
        float availableWidth = Math.max(1.0F, this.sketch.width - this.style.getMargin() * 2.0F);
        return Math.min(requestedWidth, availableWidth);
    }

    private LayoutOrigin resolvedLayoutOrigin(float width) {
        if (this.position != null) {
            return this.resolvedCustomOrigin();
        }
        return this.resolvedPlacementOrigin(width);
    }

    private LayoutOrigin resolvedCustomOrigin() {
        return new LayoutOrigin(
                this.position.x().resolve(this.sketch.width),
                this.position.y().resolve(this.sketch.height),
                true
        );
    }

    private LayoutOrigin resolvedPlacementOrigin(float width) {
        float y = this.startsAtTop() ? this.style.getMargin() : this.sketch.height - this.style.getMargin();
        return new LayoutOrigin(this.resolvedPlacementX(width), y, this.startsAtTop());
    }

    private float resolvedPlacementX(float width) {
        switch (this.placement) {
            case TOP_LEFT:
            case BOTTOM_LEFT:
                return this.style.getMargin();
            case TOP_CENTER:
            case BOTTOM_CENTER:
                return Math.max(0.0F, ((float) this.sketch.width - width) * 0.5F);
            case TOP_RIGHT:
            case BOTTOM_RIGHT:
            default:
                if (this.sketch.width <= 0) {
                    return this.style.getMargin();
                }
                return Math.max(0.0F, this.sketch.width - this.style.getMargin() - width);
        }
    }

    private record LayoutOrigin(float x, float y, boolean stacksDown) {
    }

    private List<String> wrapLines(String text, float maxWidth) {
        List<String> lines = new ArrayList<>();
        String[] paragraphs = text.split("\\R", -1);
        for (String paragraph : paragraphs) {
            this.wrapParagraph(paragraph, maxWidth, lines);
        }
        return lines.isEmpty() ? List.of("") : lines;
    }

    private void wrapParagraph(String paragraph, float maxWidth, List<String> lines) {
        if (paragraph.isBlank()) {
            lines.add("");
            return;
        }

        String[] words = paragraph.trim().split("\\s+");
        StringBuilder current = new StringBuilder();
        for (String word : words) {
            if (current.length() == 0) {
                current.append(word);
                continue;
            }
            String candidate = current + " " + word;
            if (this.sketch.textWidth(candidate) <= maxWidth) {
                current.append(' ').append(word);
            } else {
                lines.add(current.toString());
                current.setLength(0);
                current.append(word);
            }
        }
        if (current.length() > 0) {
            lines.add(current.toString());
        }
    }

    private float heightFor(List<String> lines) {
        return Math.max(
                this.style.getMinHeight(),
                this.style.getTextPadding() * 2.0F + lines.size() * this.lineHeight()
        );
    }

    private float lineHeight() {
        return this.style.getTextSize() * 1.25F;
    }

    private void prepareTypography() {
        TypographySupport.prepareStyleScope(this.sketch, this.style.getFont());
    }

    private void applyTypography() {
        TypographySupport.apply(this.sketch, this.style.getFont(), this.style.getTextSize());
    }

    static final class NotificationFrame {
        private final Notification notification;
        private final float x;
        private final float y;
        private final float width;
        private final float height;
        private final List<String> lines;
        private final PShape icon;

        NotificationFrame(
                Notification notification,
                float x,
                float y,
                float width,
                float height,
                List<String> lines,
                PShape icon
        ) {
            this.notification = notification;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.lines = List.copyOf(lines);
            this.icon = icon;
        }

        Notification notification() {
            return this.notification;
        }

        float x() {
            return this.x;
        }

        float y() {
            return this.y;
        }

        float width() {
            return this.width;
        }

        float height() {
            return this.height;
        }

        List<String> lines() {
            return this.lines;
        }

        PShape icon() {
            return this.icon;
        }
    }

    private static final class CachedIcon {
        private final String path;
        private final PShape shape;

        private CachedIcon(String path, PShape shape) {
            this.path = path;
            this.shape = shape;
        }
    }
}
