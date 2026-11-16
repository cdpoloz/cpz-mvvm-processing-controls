package com.cpz.processing.controls.core.overlay.notification;

import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.InputLayer;
import com.cpz.processing.controls.core.input.PointerEvent;
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
 * <p>The manager owns one overlay entry. Its optional pointer layer only handles
 * close buttons on persistent items and never participates in focus management.
 * Hosts register {@link #getInputLayer()} with their input manager once.</p>
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
    private final CloseInputLayer inputLayer = new CloseInputLayer();
    private final List<Notification> notifications = new ArrayList<>();
    private final EnumMap<NotificationSeverity, Long> severityDurations = new EnumMap<>(NotificationSeverity.class);
    private final EnumMap<NotificationSeverity, CachedIcon> severityIconCache = new EnumMap<>(NotificationSeverity.class);
    private NotificationPlacement placement = NotificationPlacement.TOP_RIGHT;
    private NotificationPosition position;
    private NotificationStyle style = new NotificationStyle();
    private int maxVisible = DEFAULT_MAX_VISIBLE;
    private boolean registered;
    private boolean prioritizeSeverity;
    private boolean persistentDismissible;

    public NotificationManager(PApplet sketch, OverlayManager overlayManager) {
        this(sketch, overlayManager, new SystemTimeSource());
    }

    NotificationManager(PApplet sketch, OverlayManager overlayManager, TimeSource timeSource) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
        this.overlayManager = Objects.requireNonNull(overlayManager, "overlayManager");
        this.timeSource = Objects.requireNonNull(timeSource, "timeSource");
        this.overlayEntry = new OverlayEntry(DEFAULT_Z_INDEX, this::render, this.inputLayer, this::clear, null);
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
        return this.add(notification);
    }

    /** Creates an INFO notification that remains until dismissed or cleared. */
    public Notification showPersistent(String message) {
        return this.showPersistent(message, NotificationSeverity.INFO);
    }

    /** Creates a persistent item; configured timed durations do not apply. */
    public Notification showPersistent(String message, NotificationSeverity severity) {
        return this.add(new Notification(message, severity, this.nowMillis(), 0L, true));
    }

    private Notification add(Notification notification) {
        this.pruneExpired();
        this.notifications.add(notification);
        this.trimToMaxVisible();
        this.registerOverlay();
        return notification;
    }

    /**
     * Removes a visible or pending item by id, regardless of its close-button setting.
     * @param id id returned by {@link Notification#getId()}
     * @return whether an active item was removed; false for null or unknown ids
     */
    public boolean dismiss(String id) {
        this.pruneExpired();
        boolean removed = this.notifications.removeIf(notification -> notification.getId().equals(id));
        if (this.notifications.isEmpty()) {
            this.unregisterOverlay();
        }
        return removed;
    }

    /** Whether persistent items display a pointer-operated close button (default false). */
    public boolean isPersistentDismissible() {
        return this.persistentDismissible;
    }

    /** Enables or hides the close button on all current and future persistent items. */
    public void setPersistentDismissible(boolean persistentDismissible) {
        this.persistentDismissible = persistentDismissible;
    }

    /**
     * Stable, non-modal pointer layer. Register once with the host InputManager.
     * Only close-button gestures are consumed; keyboard input is never captured.
     * @return the same input layer used by the notification overlay
     */
    public InputLayer getInputLayer() {
        return this.inputLayer;
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

    /** Returns all active items, including persistent items pending display. */
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

    /** Whether visible notifications are retained and displayed by severity priority. */
    public boolean isPrioritizeSeverity() {
        return this.prioritizeSeverity;
    }

    /**
     * Enables ERROR > WARNING > SUCCESS > INFO priority. Equal levels prefer newer
     * messages. Disabled by default to preserve chronological behavior.
     */
    public void setPrioritizeSeverity(boolean prioritizeSeverity) {
        this.pruneExpired();
        this.prioritizeSeverity = prioritizeSeverity;
        this.trimToMaxVisible();
    }

    public int getMaxVisible() {
        return this.maxVisible;
    }

    public void setMaxVisible(int maxVisible) {
        if (maxVisible <= 0) {
            throw new IllegalArgumentException("Notification maxVisible must be greater than 0.");
        }
        this.pruneExpired();
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
        return this.layoutFrames(this.style.resolve(this.sketch));
    }

    private List<NotificationFrame> layoutFrames(NotificationStyle style) {
        this.pruneExpired();
        if (this.notifications.isEmpty()) {
            return Collections.emptyList();
        }

        this.prepareTypography(style);
        this.sketch.pushStyle();
        try {
            this.applyTypography(style);
            return this.layoutFramesWithoutStyleMutation(style);
        } finally {
            this.sketch.popStyle();
        }
    }

    private List<NotificationFrame> layoutFramesWithoutStyleMutation(NotificationStyle style) {
        List<Notification> ordered = this.notificationsInEdgeOrder();
        List<NotificationFrame> frames = new ArrayList<>();
        float width = this.resolvedWidth(style);
        LayoutOrigin origin = this.resolvedLayoutOrigin(width, style);
        float cursor = origin.y();
        for (Notification notification : ordered.subList(0, Math.min(this.maxVisible, ordered.size()))) {
            PShape icon = this.resolveSeverityIcon(notification.getSeverity());
            float iconTextOffset = icon == null ? 0.0F : style.getIconSize() + style.getIconTextGap();
            float closeSize = notification.isPersistent() && this.persistentDismissible
                    ? Math.min(width, this.lineHeight(style)) : 0.0F;
            float closeOffset = closeSize > 0.0F ? closeSize + style.getTextSize() * 0.5F : 0.0F;
            float textWidth = Math.max(
                    1.0F,
                    width - style.getAccentWidth() - style.getTextPadding() * 2.0F - iconTextOffset - closeOffset
            );
            List<String> lines = this.wrapLines(notification.getMessage(), textWidth);
            float height = Math.max(this.heightFor(lines, style), closeSize + style.getTextPadding() * 2.0F);
            float y;
            if (origin.stacksDown()) {
                y = cursor;
                cursor += height + style.getGap();
            } else {
                y = cursor - height;
                cursor -= height + style.getGap();
            }
            CloseBounds closeBounds = closeSize > 0.0F ? new CloseBounds(
                    origin.x() + Math.max(0.0F, width - style.getTextPadding() - closeSize),
                    y + style.getTextPadding(), closeSize) : null;
            frames.add(new NotificationFrame(notification, origin.x(), y, width, height, lines, icon, closeBounds));
        }
        return frames;
    }

    private void render() {
        NotificationStyle style = this.style.resolve(this.sketch);
        List<NotificationFrame> frames = this.layoutFrames(style);
        if (frames.isEmpty()) {
            this.unregisterOverlay();
            return;
        }

        this.prepareTypography(style);
        this.sketch.pushStyle();
        try {
            this.applyTypography(style);
            for (NotificationFrame frame : frames) {
                this.drawFrame(frame, style);
            }
        } finally {
            this.sketch.popStyle();
        }
    }

    private void drawFrame(NotificationFrame frame, NotificationStyle style) {
        if (style.getStrokeWeight() > 0.0F) {
            this.sketch.stroke(style.getBorderColor());
            this.sketch.strokeWeight(style.getStrokeWeight());
        } else {
            this.sketch.noStroke();
        }
        Integer severityBackgroundColor = style.getSeverityBackgroundColor(frame.notification().getSeverity());
        this.sketch.fill(severityBackgroundColor != null ? severityBackgroundColor : style.getBackgroundColor());
        this.sketch.rect(frame.x(), frame.y(), frame.width(), frame.height(), style.getCornerRadius());

        this.sketch.noStroke();
        if (style.getAccentWidth() > 0.0F) {
            this.sketch.fill(style.accentColor(frame.notification().getSeverity()));
            this.sketch.rect(
                    frame.x(),
                    frame.y(),
                    style.getAccentWidth(),
                    frame.height(),
                    style.getCornerRadius(),
                    0.0F,
                    0.0F,
                    style.getCornerRadius()
            );
        }

        if (frame.icon() != null) {
            this.drawIcon(frame, style);
        }

        this.sketch.fill(style.getTextColor());
        this.sketch.textAlign(PApplet.LEFT, PApplet.TOP);
        float textX = frame.x() + style.getAccentWidth() + style.getTextPadding();
        if (frame.icon() != null) {
            textX += style.getIconSize() + style.getIconTextGap();
        }
        float textY = frame.y() + style.getTextPadding();
        float lineHeight = this.lineHeight(style);
        for (int i = 0; i < frame.lines().size(); i++) {
            this.sketch.text(frame.lines().get(i), textX, textY + i * lineHeight);
        }
        if (frame.closeBounds() != null) {
            CloseBounds close = frame.closeBounds();
            float inset = close.size() * 0.25F;
            this.sketch.stroke(style.getTextColor());
            this.sketch.strokeWeight(Math.max(1.0F, style.getTextSize() / 10.0F));
            this.sketch.line(close.x() + inset, close.y() + inset,
                    close.x() + close.size() - inset, close.y() + close.size() - inset);
            this.sketch.line(close.x() + inset, close.y() + close.size() - inset,
                    close.x() + close.size() - inset, close.y() + inset);
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
        List<Notification> ordered = this.notificationsInEdgeOrder();
        for (int i = this.maxVisible; i < ordered.size(); i++) {
            Notification overflow = ordered.get(i);
            if (!overflow.isPersistent()) {
                this.notifications.remove(overflow);
            }
        }
    }

    private static int severityPriority(NotificationSeverity severity) {
        switch (severity) {
            case ERROR: return 3;
            case WARNING: return 2;
            case SUCCESS: return 1;
            case INFO: return 0;
            default: throw new IllegalArgumentException("Unknown notification severity: " + severity);
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

    private void drawIcon(NotificationFrame frame, NotificationStyle style) {
        float iconSize = style.getIconSize();
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

        float areaX = frame.x() + style.getAccentWidth() + style.getTextPadding();
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
        if (this.prioritizeSeverity) {
            // Stable sort preserves reverse insertion order for equal severities.
            ordered.sort((left, right) -> Integer.compare(
                    severityPriority(right.getSeverity()), severityPriority(left.getSeverity())));
        }
        return ordered;
    }

    private boolean startsAtTop() {
        return this.placement == NotificationPlacement.TOP_RIGHT
                || this.placement == NotificationPlacement.TOP_LEFT
                || this.placement == NotificationPlacement.TOP_CENTER;
    }

    private float resolvedWidth(NotificationStyle style) {
        float requestedWidth = style.getWidth();
        if (this.sketch.width <= 0) {
            return requestedWidth;
        }
        float availableWidth = Math.max(1.0F, this.sketch.width - style.getMargin() * 2.0F);
        return Math.min(requestedWidth, availableWidth);
    }

    private LayoutOrigin resolvedLayoutOrigin(float width, NotificationStyle style) {
        if (this.position != null) {
            return this.resolvedCustomOrigin();
        }
        return this.resolvedPlacementOrigin(width, style);
    }

    private LayoutOrigin resolvedCustomOrigin() {
        return new LayoutOrigin(
                this.position.x().resolve(this.sketch.width),
                this.position.y().resolve(this.sketch.height),
                true
        );
    }

    private LayoutOrigin resolvedPlacementOrigin(float width, NotificationStyle style) {
        float y = this.startsAtTop() ? style.getMargin() : this.sketch.height - style.getMargin();
        return new LayoutOrigin(this.resolvedPlacementX(width, style), y, this.startsAtTop());
    }

    private float resolvedPlacementX(float width, NotificationStyle style) {
        switch (this.placement) {
            case TOP_LEFT:
            case BOTTOM_LEFT:
                return style.getMargin();
            case TOP_CENTER:
            case BOTTOM_CENTER:
                return Math.max(0.0F, ((float) this.sketch.width - width) * 0.5F);
            case TOP_RIGHT:
            case BOTTOM_RIGHT:
            default:
                if (this.sketch.width <= 0) {
                    return style.getMargin();
                }
                return Math.max(0.0F, this.sketch.width - style.getMargin() - width);
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

    private float heightFor(List<String> lines, NotificationStyle style) {
        return Math.max(
                style.getMinHeight(),
                style.getTextPadding() * 2.0F + lines.size() * this.lineHeight(style)
        );
    }

    private float lineHeight(NotificationStyle style) {
        return style.getTextSize() * 1.25F;
    }

    private void prepareTypography(NotificationStyle style) {
        TypographySupport.prepareStyleScope(this.sketch, style.getFont());
    }

    private void applyTypography(NotificationStyle style) {
        TypographySupport.apply(this.sketch, style.getFont(), style.getTextSize());
    }

    private NotificationFrame closeTarget(float x, float y) {
        if (!this.persistentDismissible) return null;
        for (NotificationFrame frame : this.layoutFrames()) {
            if (frame.closeBounds() != null && frame.closeBounds().contains(x, y)) return frame;
        }
        return null;
    }

    private final class CloseInputLayer extends DefaultInputLayer {
        private String pressedId;

        private CloseInputLayer() {
            super(DEFAULT_Z_INDEX);
        }

        @Override
        public boolean isActive() {
            return this.pressedId != null || (registered && persistentDismissible);
        }

        @Override
        public boolean isKeyboardCaptureEnabled() {
            return false;
        }

        @Override
        public boolean handlePointerEvent(PointerEvent event) {
            if (event == null || !this.isActive()) return false;
            if (event.getType() == PointerEvent.Type.PRESS) {
                this.pressedId = null;
                if (event.getButton() != 0 && event.getButton() != PApplet.LEFT) return false;
                NotificationFrame target = closeTarget(event.getX(), event.getY());
                if (target == null) return false;
                this.pressedId = target.notification().getId();
                return true;
            }
            if (event.getType() == PointerEvent.Type.RELEASE && this.pressedId != null) {
                if (event.getButton() != 0 && event.getButton() != PApplet.LEFT) return false;
                String id = this.pressedId;
                this.pressedId = null;
                NotificationFrame target = closeTarget(event.getX(), event.getY());
                if (target != null && target.notification().getId().equals(id)) dismiss(id);
                return true;
            }
            return event.getType() == PointerEvent.Type.DRAG && this.pressedId != null;
        }
    }

    record CloseBounds(float x, float y, float size) {
        boolean contains(float x, float y) {
            return x >= this.x && x < this.x + this.size && y >= this.y && y < this.y + this.size;
        }
    }

    static final class NotificationFrame {
        private final Notification notification;
        private final float x;
        private final float y;
        private final float width;
        private final float height;
        private final List<String> lines;
        private final PShape icon;
        private final CloseBounds closeBounds;

        NotificationFrame(
                Notification notification,
                float x,
                float y,
                float width,
                float height,
                List<String> lines,
                PShape icon,
                CloseBounds closeBounds
        ) {
            this.notification = notification;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.lines = List.copyOf(lines);
            this.icon = icon;
            this.closeBounds = closeBounds;
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

        CloseBounds closeBounds() {
            return this.closeBounds;
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
