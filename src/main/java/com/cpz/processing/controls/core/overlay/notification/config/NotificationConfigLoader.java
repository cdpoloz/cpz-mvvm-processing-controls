package com.cpz.processing.controls.core.overlay.notification.config;

import com.cpz.processing.controls.core.overlay.notification.NotificationManager;
import com.cpz.processing.controls.core.overlay.notification.NotificationPlacement;
import com.cpz.processing.controls.core.overlay.notification.NotificationSeverity;
import com.cpz.processing.controls.core.util.JsonConfigSupport;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import processing.core.PApplet;
import processing.data.JSONObject;

/**
 * Loads standalone notification manager/style configuration from JSON.
 *
 * <p>This loader is intentionally not connected to {@code ControlConfigLoader}
 * or {@code ControlFactoryRegistry}.</p>
 *
 * @author CPZ
 */
public final class NotificationConfigLoader {
    private NotificationConfigLoader() {
    }

    public static NotificationConfig load(PApplet sketch, String path) {
        Objects.requireNonNull(sketch, "sketch");
        Objects.requireNonNull(path, "path");
        return loadFromJson(JsonConfigSupport.loadRequiredObject(sketch, path, "notification"), path);
    }

    public static void apply(PApplet sketch, String path, NotificationManager manager) {
        load(sketch, path).applyTo(manager);
    }

    public static NotificationConfig loadFromJson(JSONObject root, String path) {
        Objects.requireNonNull(root, "root");
        Objects.requireNonNull(path, "path");

        return new NotificationConfig(
                readPlacement(root),
                readPositiveInt(root, "maxVisible"),
                readPositiveLong(root, "defaultDurationMillis"),
                readSeverityDurations(root),
                readStyle(root, path)
        );
    }

    private static NotificationPlacement readPlacement(JSONObject root) {
        if (!root.hasKey("placement") || root.isNull("placement")) {
            return null;
        }
        return parsePlacement(root.get("placement"));
    }

    private static NotificationPlacement parsePlacement(Object raw) {
        String normalized = normalizeEnumToken(raw);
        if (normalized.isEmpty()) {
            return NotificationPlacement.TOP_RIGHT;
        }
        try {
            return NotificationPlacement.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            return NotificationPlacement.TOP_RIGHT;
        }
    }

    private static Map<NotificationSeverity, Long> readSeverityDurations(JSONObject root) {
        EnumMap<NotificationSeverity, Long> durations = new EnumMap<>(NotificationSeverity.class);
        if (!root.hasKey("severityDurations") || root.isNull("severityDurations")) {
            return durations;
        }

        Object rawDurations = root.get("severityDurations");
        if (!(rawDurations instanceof JSONObject)) {
            return durations;
        }

        JSONObject severityDurations = (JSONObject) rawDurations;
        for (Object rawKey : severityDurations.keys()) {
            NotificationSeverity severity = parseSeverity(rawKey);
            if (severity == null) {
                continue;
            }
            Long duration = readPositiveLong(severityDurations, String.valueOf(rawKey));
            if (duration != null) {
                durations.put(severity, duration);
            }
        }
        return durations;
    }

    private static NotificationSeverity parseSeverity(Object raw) {
        String normalized = normalizeEnumToken(raw);
        if (normalized.isEmpty()) {
            return null;
        }
        try {
            return NotificationSeverity.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private static NotificationConfig.StyleConfig readStyle(JSONObject root, String path) {
        if (!root.hasKey("style") || root.isNull("style")) {
            return null;
        }

        Object rawStyle = root.get("style");
        if (!(rawStyle instanceof JSONObject)) {
            return null;
        }

        JSONObject style = (JSONObject) rawStyle;
        return new NotificationConfig.StyleConfig(
                JsonConfigSupport.getOptionalColor(style, "backgroundColor", path),
                JsonConfigSupport.getOptionalColor(style, "textColor", path),
                JsonConfigSupport.getOptionalColor(style, "borderColor", path),
                JsonConfigSupport.getOptionalFloat(style, "strokeWeight"),
                JsonConfigSupport.getOptionalFloat(style, "cornerRadius"),
                JsonConfigSupport.getOptionalFloat(style, "textSize"),
                JsonConfigSupport.getOptionalFloat(style, "textPadding"),
                JsonConfigSupport.getOptionalFloat(style, "gap"),
                JsonConfigSupport.getOptionalFloat(style, "margin"),
                JsonConfigSupport.getOptionalFloat(style, "width"),
                JsonConfigSupport.getOptionalFloat(style, "minHeight"),
                JsonConfigSupport.getOptionalFloat(style, "accentWidth"),
                JsonConfigSupport.getOptionalColor(style, "infoAccentColor", path),
                JsonConfigSupport.getOptionalColor(style, "successAccentColor", path),
                JsonConfigSupport.getOptionalColor(style, "warningAccentColor", path),
                JsonConfigSupport.getOptionalColor(style, "errorAccentColor", path)
        );
    }

    private static Integer readPositiveInt(JSONObject json, String key) {
        Long value = readPositiveLong(json, key);
        if (value == null || value > Integer.MAX_VALUE) {
            return null;
        }
        return value.intValue();
    }

    private static Long readPositiveLong(JSONObject json, String key) {
        if (!json.hasKey(key) || json.isNull(key)) {
            return null;
        }
        Object raw = json.get(key);
        if (!(raw instanceof Number)) {
            return null;
        }
        long value = ((Number) raw).longValue();
        return value > 0L ? value : null;
    }

    private static String normalizeEnumToken(Object raw) {
        if (raw == null) {
            return "";
        }
        return String.valueOf(raw)
                .trim()
                .toUpperCase(Locale.ROOT)
                .replace('-', '_')
                .replaceAll("\\s+", "_");
    }
}
