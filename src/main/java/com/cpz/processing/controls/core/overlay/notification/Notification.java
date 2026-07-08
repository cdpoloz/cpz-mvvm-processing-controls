package com.cpz.processing.controls.core.overlay.notification;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Runtime notification item rendered by {@link NotificationManager}.
 *
 * <p>Notifications are runtime UI events, not public controls. They are mostly
 * read-only once created and expire after their configured duration.</p>
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

    Notification(String message, NotificationSeverity severity, long createdAtMillis, long durationMillis) {
        String normalizedMessage = Objects.requireNonNull(message, "message").trim();
        if (normalizedMessage.isEmpty()) {
            throw new IllegalArgumentException("Notification message must not be blank.");
        }
        if (durationMillis <= 0L) {
            throw new IllegalArgumentException("Notification duration must be greater than 0.");
        }
        this.id = "notification-" + NEXT_ID.getAndIncrement();
        this.message = normalizedMessage;
        this.severity = severity == null ? NotificationSeverity.INFO : severity;
        this.createdAtMillis = createdAtMillis;
        this.durationMillis = durationMillis;
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

    public long getDurationMillis() {
        return this.durationMillis;
    }

    public boolean isExpired(long nowMillis) {
        return nowMillis - this.createdAtMillis >= this.durationMillis;
    }
}
