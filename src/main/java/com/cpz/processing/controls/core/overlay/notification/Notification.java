package com.cpz.processing.controls.core.overlay.notification;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Runtime notification item rendered by {@link NotificationManager}.
 *
 * <p>Notifications are runtime UI events, not public controls. They are mostly
 * read-only once created. Timed items expire after their configured duration;
 * persistent items remain until explicitly dismissed or cleared.</p>
 *
 * @author CPZ
 */
public final class Notification {
    private static final AtomicLong NEXT_ID = new AtomicLong(1L);

    private final String id;
    private final String message;
    private final NotificationSeverity severity;
    private final long createdAtMillis;
    private final long durationMillis;
    private final boolean persistent;

    Notification(String message, NotificationSeverity severity, long createdAtMillis, long durationMillis) {
        this(message, severity, createdAtMillis, durationMillis, false);
    }

    Notification(String message, NotificationSeverity severity, long createdAtMillis, long durationMillis, boolean persistent) {
        String normalizedMessage = Objects.requireNonNull(message, "message").trim();
        if (normalizedMessage.isEmpty()) {
            throw new IllegalArgumentException("Notification message must not be blank.");
        }
        if (!persistent && durationMillis <= 0L) {
            throw new IllegalArgumentException("Notification duration must be greater than 0.");
        }
        this.id = "notification-" + NEXT_ID.getAndIncrement();
        this.message = normalizedMessage;
        this.severity = severity == null ? NotificationSeverity.INFO : severity;
        this.createdAtMillis = createdAtMillis;
        this.durationMillis = persistent ? 0L : durationMillis;
        this.persistent = persistent;
    }

    public String getId() {
        return this.id;
    }

    public String getMessage() {
        return this.message;
    }

    public NotificationSeverity getSeverity() {
        return this.severity;
    }

    public long getCreatedAtMillis() {
        return this.createdAtMillis;
    }

    /** Returns the timed duration, or zero for an explicitly persistent item. */
    public long getDurationMillis() {
        return this.durationMillis;
    }

    public boolean isPersistent() {
        return this.persistent;
    }

    public boolean isExpired(long nowMillis) {
        return !this.persistent && nowMillis - this.createdAtMillis >= this.durationMillis;
    }
}
