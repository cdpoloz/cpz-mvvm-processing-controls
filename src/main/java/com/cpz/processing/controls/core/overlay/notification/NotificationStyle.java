package com.cpz.processing.controls.core.overlay.notification;

import java.util.EnumMap;
import java.util.Objects;
import processing.core.PFont;

/**
 * Mutable visual style for toast-style notifications.
 *
 * @author CPZ
 */
public final class NotificationStyle {
    public static final int DEFAULT_BACKGROUND_COLOR = 0xF21B1F26;
    public static final int DEFAULT_TEXT_COLOR = 0xFFFFFFFF;
    public static final int DEFAULT_BORDER_COLOR = 0x668A94A6;
    public static final int DEFAULT_INFO_ACCENT_COLOR = 0xFF2F80ED;
    public static final int DEFAULT_SUCCESS_ACCENT_COLOR = 0xFF2ECC71;
    public static final int DEFAULT_WARNING_ACCENT_COLOR = 0xFFF2994A;
    public static final int DEFAULT_ERROR_ACCENT_COLOR = 0xFFEB5757;

    private int backgroundColor = DEFAULT_BACKGROUND_COLOR;
    private int textColor = DEFAULT_TEXT_COLOR;
    private int borderColor = DEFAULT_BORDER_COLOR;
    private float strokeWeight = 1.0F;
    private float cornerRadius = 8.0F;
    private float textSize = 14.0F;
    private float textPadding = 12.0F;
    private float gap = 8.0F;
    private float margin = 16.0F;
    private float width = 320.0F;
    private float minHeight = 48.0F;
    private int infoAccentColor = DEFAULT_INFO_ACCENT_COLOR;
    private int successAccentColor = DEFAULT_SUCCESS_ACCENT_COLOR;
    private int warningAccentColor = DEFAULT_WARNING_ACCENT_COLOR;
    private int errorAccentColor = DEFAULT_ERROR_ACCENT_COLOR;
    private float accentWidth = 5.0F;
    private float iconSize = 24.0F;
    private float iconTextGap = 8.0F;
    private final EnumMap<NotificationSeverity, String> severityIcons = new EnumMap<>(NotificationSeverity.class);
    private final EnumMap<NotificationSeverity, Integer> severityBackgroundColors = new EnumMap<>(NotificationSeverity.class);
    private PFont font;

    public NotificationStyle() {
    }

    public NotificationStyle(NotificationStyle source) {
        if (source != null) {
            this.backgroundColor = source.backgroundColor;
            this.textColor = source.textColor;
            this.borderColor = source.borderColor;
            this.strokeWeight = source.strokeWeight;
            this.cornerRadius = source.cornerRadius;
            this.textSize = source.textSize;
            this.textPadding = source.textPadding;
            this.gap = source.gap;
            this.margin = source.margin;
            this.width = source.width;
            this.minHeight = source.minHeight;
            this.infoAccentColor = source.infoAccentColor;
            this.successAccentColor = source.successAccentColor;
            this.warningAccentColor = source.warningAccentColor;
            this.errorAccentColor = source.errorAccentColor;
            this.accentWidth = source.accentWidth;
            this.iconSize = source.iconSize;
            this.iconTextGap = source.iconTextGap;
            this.severityIcons.putAll(source.severityIcons);
            this.severityBackgroundColors.putAll(source.severityBackgroundColors);
            this.font = source.font;
        }
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public NotificationStyle setBackgroundColor(int color) {
        this.backgroundColor = color;
        return this;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public NotificationStyle setTextColor(int color) {
        this.textColor = color;
        return this;
    }

    public int getBorderColor() {
        return this.borderColor;
    }

    public NotificationStyle setBorderColor(int color) {
        this.borderColor = color;
        return this;
    }

    public float getStrokeWeight() {
        return this.strokeWeight;
    }

    public NotificationStyle setStrokeWeight(float weight) {
        this.strokeWeight = Math.max(0.0F, weight);
        return this;
    }

    public float getCornerRadius() {
        return this.cornerRadius;
    }

    public NotificationStyle setCornerRadius(float cornerRadius) {
        this.cornerRadius = Math.max(0.0F, cornerRadius);
        return this;
    }

    public float getTextSize() {
        return this.textSize;
    }

    public NotificationStyle setTextSize(float textSize) {
        this.textSize = Math.max(1.0F, textSize);
        return this;
    }

    public float getTextPadding() {
        return this.textPadding;
    }

    public NotificationStyle setTextPadding(float textPadding) {
        this.textPadding = Math.max(0.0F, textPadding);
        return this;
    }

    public float getGap() {
        return this.gap;
    }

    public NotificationStyle setGap(float gap) {
        this.gap = Math.max(0.0F, gap);
        return this;
    }

    public float getMargin() {
        return this.margin;
    }

    public NotificationStyle setMargin(float margin) {
        this.margin = Math.max(0.0F, margin);
        return this;
    }

    public float getWidth() {
        return this.width;
    }

    public NotificationStyle setWidth(float width) {
        this.width = Math.max(1.0F, width);
        return this;
    }

    public float getMinHeight() {
        return this.minHeight;
    }

    public NotificationStyle setMinHeight(float minHeight) {
        this.minHeight = Math.max(1.0F, minHeight);
        return this;
    }

    public int getInfoAccentColor() {
        return this.infoAccentColor;
    }

    public NotificationStyle setInfoAccentColor(int color) {
        this.infoAccentColor = color;
        return this;
    }

    public int getSuccessAccentColor() {
        return this.successAccentColor;
    }

    public NotificationStyle setSuccessAccentColor(int color) {
        this.successAccentColor = color;
        return this;
    }

    public int getWarningAccentColor() {
        return this.warningAccentColor;
    }

    public NotificationStyle setWarningAccentColor(int color) {
        this.warningAccentColor = color;
        return this;
    }

    public int getErrorAccentColor() {
        return this.errorAccentColor;
    }

    public NotificationStyle setErrorAccentColor(int color) {
        this.errorAccentColor = color;
        return this;
    }

    public float getAccentWidth() {
        return this.accentWidth;
    }

    public NotificationStyle setAccentWidth(float accentWidth) {
        this.accentWidth = Math.max(0.0F, accentWidth);
        return this;
    }

    public float getIconSize() {
        return this.iconSize;
    }

    public NotificationStyle setIconSize(float iconSize) {
        this.iconSize = Math.max(0.0F, iconSize);
        return this;
    }

    public float getIconTextGap() {
        return this.iconTextGap;
    }

    public NotificationStyle setIconTextGap(float iconTextGap) {
        this.iconTextGap = Math.max(0.0F, iconTextGap);
        return this;
    }

    /**
     * Associates an optional SVG resource path with a notification severity.
     * A {@code null} or blank path clears the association.
     *
     * @param severity notification severity
     * @param path Processing SVG resource path, or {@code null} to clear it
     * @return this style
     */
    public NotificationStyle setSeverityIcon(NotificationSeverity severity, String path) {
        Objects.requireNonNull(severity, "severity");
        if (path == null || path.trim().isEmpty()) {
            this.severityIcons.remove(severity);
        } else {
            this.severityIcons.put(severity, path.trim());
        }
        return this;
    }

    /**
     * Returns the optional SVG resource path associated with a severity.
     *
     * @param severity notification severity
     * @return configured Processing SVG resource path, or {@code null}
     */
    public String getSeverityIcon(NotificationSeverity severity) {
        return this.severityIcons.get(Objects.requireNonNull(severity, "severity"));
    }

    /**
     * Removes the optional SVG resource path associated with a severity.
     *
     * @param severity notification severity
     * @return this style
     */
    public NotificationStyle clearSeverityIcon(NotificationSeverity severity) {
        this.severityIcons.remove(Objects.requireNonNull(severity, "severity"));
        return this;
    }

    /**
     * Associates an optional background color with a notification severity.
     * A {@code null} color clears the association and restores the general
     * background color fallback.
     *
     * @param severity notification severity
     * @param color background color, or {@code null} to clear it
     * @return this style
     */
    public NotificationStyle setSeverityBackgroundColor(NotificationSeverity severity, Integer color) {
        Objects.requireNonNull(severity, "severity");
        if (color == null) {
            this.severityBackgroundColors.remove(severity);
        } else {
            this.severityBackgroundColors.put(severity, color);
        }
        return this;
    }

    /**
     * Returns the optional background color associated with a severity.
     *
     * @param severity notification severity
     * @return configured background color, or {@code null}
     */
    public Integer getSeverityBackgroundColor(NotificationSeverity severity) {
        return this.severityBackgroundColors.get(Objects.requireNonNull(severity, "severity"));
    }

    /**
     * Removes the optional background color associated with a severity.
     *
     * @param severity notification severity
     * @return this style
     */
    public NotificationStyle clearSeverityBackgroundColor(NotificationSeverity severity) {
        this.severityBackgroundColors.remove(Objects.requireNonNull(severity, "severity"));
        return this;
    }

    public PFont getFont() {
        return this.font;
    }

    public NotificationStyle setFont(PFont font) {
        this.font = font;
        return this;
    }

    int accentColor(NotificationSeverity severity) {
        NotificationSeverity resolvedSeverity = severity == null ? NotificationSeverity.INFO : severity;
        switch (resolvedSeverity) {
            case SUCCESS:
                return this.successAccentColor;
            case WARNING:
                return this.warningAccentColor;
            case ERROR:
                return this.errorAccentColor;
            case INFO:
            default:
                return this.infoAccentColor;
        }
    }
}
