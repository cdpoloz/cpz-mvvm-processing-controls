package com.cpz.processing.controls.core.overlay.notification;

import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.geometry.MeasureMode;
import com.cpz.processing.controls.core.util.FontLoader;
import java.util.EnumMap;
import java.util.Objects;
import processing.core.PFont;
import processing.core.PApplet;

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
    private FontLoader.FontResolver fontResolver;
    private ControlMeasure strokeWeightMeasure;
    private ControlMeasure cornerRadiusMeasure;
    private ControlMeasure textSizeMeasure;
    private ControlMeasure textPaddingMeasure;
    private ControlMeasure gapMeasure;
    private ControlMeasure marginMeasure;
    private ControlMeasure widthMeasure;
    private ControlMeasure minHeightMeasure;
    private ControlMeasure accentWidthMeasure;
    private ControlMeasure iconSizeMeasure;
    private ControlMeasure iconTextGapMeasure;


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
            this.fontResolver = source.fontResolver;
            this.strokeWeightMeasure = source.strokeWeightMeasure;
            this.cornerRadiusMeasure = source.cornerRadiusMeasure;
            this.textSizeMeasure = source.textSizeMeasure;
            this.textPaddingMeasure = source.textPaddingMeasure;
            this.gapMeasure = source.gapMeasure;
            this.marginMeasure = source.marginMeasure;
            this.widthMeasure = source.widthMeasure;
            this.minHeightMeasure = source.minHeightMeasure;
            this.accentWidthMeasure = source.accentWidthMeasure;
            this.iconSizeMeasure = source.iconSizeMeasure;
            this.iconTextGapMeasure = source.iconTextGapMeasure;

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

    /**
     * Sets an explicit measure; relative values use the current sketch height.
     * @param measure absolute pixels or relative factor
     * @return this style
     */
    public NotificationStyle setStrokeWeight(ControlMeasure measure) {
        Objects.requireNonNull(measure, "measure");
        if (measure.mode() == MeasureMode.ABSOLUTE) {
            return this.setStrokeWeight(measure.value());
        }
        this.strokeWeightMeasure = measure;
        return this;
    }

    /**
     * Returns the retained measure, without resolving it.
     * @return explicit measure or the legacy absolute value
     */
    public ControlMeasure getStrokeWeightMeasure() {
        return this.strokeWeightMeasure == null ? ControlMeasure.absolute(this.strokeWeight) : this.strokeWeightMeasure;
    }

    public float getStrokeWeight() {
        return this.strokeWeight;
    }

    public NotificationStyle setStrokeWeight(float weight) {
        this.strokeWeightMeasure = null;
        this.strokeWeight = Math.max(0.0F, weight);
        return this;
    }

    /**
     * Sets an explicit measure; relative values use the current sketch height.
     * @param measure absolute pixels or relative factor
     * @return this style
     */
    public NotificationStyle setCornerRadius(ControlMeasure measure) {
        Objects.requireNonNull(measure, "measure");
        if (measure.mode() == MeasureMode.ABSOLUTE) {
            return this.setCornerRadius(measure.value());
        }
        this.cornerRadiusMeasure = measure;
        return this;
    }

    /**
     * Returns the retained measure, without resolving it.
     * @return explicit measure or the legacy absolute value
     */
    public ControlMeasure getCornerRadiusMeasure() {
        return this.cornerRadiusMeasure == null ? ControlMeasure.absolute(this.cornerRadius) : this.cornerRadiusMeasure;
    }

    public float getCornerRadius() {
        return this.cornerRadius;
    }

    public NotificationStyle setCornerRadius(float cornerRadius) {
        this.cornerRadiusMeasure = null;
        this.cornerRadius = Math.max(0.0F, cornerRadius);
        return this;
    }

    /**
     * Sets an explicit measure; relative values use the current sketch height.
     * @param measure absolute pixels or relative factor
     * @return this style
     */
    public NotificationStyle setTextSize(ControlMeasure measure) {
        Objects.requireNonNull(measure, "measure");
        if (measure.mode() == MeasureMode.ABSOLUTE) {
            return this.setTextSize(measure.value());
        }
        this.textSizeMeasure = measure;
        return this;
    }

    /**
     * Returns the retained measure, without resolving it.
     * @return explicit measure or the legacy absolute value
     */
    public ControlMeasure getTextSizeMeasure() {
        return this.textSizeMeasure == null ? ControlMeasure.absolute(this.textSize) : this.textSizeMeasure;
    }

    public float getTextSize() {
        return this.textSize;
    }

    public NotificationStyle setTextSize(float textSize) {
        this.textSizeMeasure = null;
        this.textSize = Math.max(1.0F, textSize);
        return this;
    }

    /**
     * Sets an explicit measure; relative values use the current sketch height.
     * @param measure absolute pixels or relative factor
     * @return this style
     */
    public NotificationStyle setTextPadding(ControlMeasure measure) {
        Objects.requireNonNull(measure, "measure");
        if (measure.mode() == MeasureMode.ABSOLUTE) {
            return this.setTextPadding(measure.value());
        }
        this.textPaddingMeasure = measure;
        return this;
    }

    /**
     * Returns the retained measure, without resolving it.
     * @return explicit measure or the legacy absolute value
     */
    public ControlMeasure getTextPaddingMeasure() {
        return this.textPaddingMeasure == null ? ControlMeasure.absolute(this.textPadding) : this.textPaddingMeasure;
    }

    public float getTextPadding() {
        return this.textPadding;
    }

    public NotificationStyle setTextPadding(float textPadding) {
        this.textPaddingMeasure = null;
        this.textPadding = Math.max(0.0F, textPadding);
        return this;
    }

    /**
     * Sets an explicit measure; relative values use the current sketch height.
     * @param measure absolute pixels or relative factor
     * @return this style
     */
    public NotificationStyle setGap(ControlMeasure measure) {
        Objects.requireNonNull(measure, "measure");
        if (measure.mode() == MeasureMode.ABSOLUTE) {
            return this.setGap(measure.value());
        }
        this.gapMeasure = measure;
        return this;
    }

    /**
     * Returns the retained measure, without resolving it.
     * @return explicit measure or the legacy absolute value
     */
    public ControlMeasure getGapMeasure() {
        return this.gapMeasure == null ? ControlMeasure.absolute(this.gap) : this.gapMeasure;
    }

    public float getGap() {
        return this.gap;
    }

    public NotificationStyle setGap(float gap) {
        this.gapMeasure = null;
        this.gap = Math.max(0.0F, gap);
        return this;
    }

    /**
     * Sets an explicit measure; relative values use the current sketch height.
     * @param measure absolute pixels or relative factor
     * @return this style
     */
    public NotificationStyle setMargin(ControlMeasure measure) {
        Objects.requireNonNull(measure, "measure");
        if (measure.mode() == MeasureMode.ABSOLUTE) {
            return this.setMargin(measure.value());
        }
        this.marginMeasure = measure;
        return this;
    }

    /**
     * Returns the retained measure, without resolving it.
     * @return explicit measure or the legacy absolute value
     */
    public ControlMeasure getMarginMeasure() {
        return this.marginMeasure == null ? ControlMeasure.absolute(this.margin) : this.marginMeasure;
    }

    public float getMargin() {
        return this.margin;
    }

    public NotificationStyle setMargin(float margin) {
        this.marginMeasure = null;
        this.margin = Math.max(0.0F, margin);
        return this;
    }

    /**
     * Sets an explicit measure; relative values use the current sketch height.
     * @param measure absolute pixels or relative factor
     * @return this style
     */
    public NotificationStyle setWidth(ControlMeasure measure) {
        Objects.requireNonNull(measure, "measure");
        if (measure.mode() == MeasureMode.ABSOLUTE) {
            return this.setWidth(measure.value());
        }
        this.widthMeasure = measure;
        return this;
    }

    /**
     * Returns the retained measure, without resolving it.
     * @return explicit measure or the legacy absolute value
     */
    public ControlMeasure getWidthMeasure() {
        return this.widthMeasure == null ? ControlMeasure.absolute(this.width) : this.widthMeasure;
    }

    public float getWidth() {
        return this.width;
    }

    public NotificationStyle setWidth(float width) {
        this.widthMeasure = null;
        this.width = Math.max(1.0F, width);
        return this;
    }

    /**
     * Sets an explicit measure; relative values use the current sketch height.
     * @param measure absolute pixels or relative factor
     * @return this style
     */
    public NotificationStyle setMinHeight(ControlMeasure measure) {
        Objects.requireNonNull(measure, "measure");
        if (measure.mode() == MeasureMode.ABSOLUTE) {
            return this.setMinHeight(measure.value());
        }
        this.minHeightMeasure = measure;
        return this;
    }

    /**
     * Returns the retained measure, without resolving it.
     * @return explicit measure or the legacy absolute value
     */
    public ControlMeasure getMinHeightMeasure() {
        return this.minHeightMeasure == null ? ControlMeasure.absolute(this.minHeight) : this.minHeightMeasure;
    }

    public float getMinHeight() {
        return this.minHeight;
    }

    public NotificationStyle setMinHeight(float minHeight) {
        this.minHeightMeasure = null;
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

    /**
     * Sets an explicit measure; relative values use the current sketch height.
     * @param measure absolute pixels or relative factor
     * @return this style
     */
    public NotificationStyle setAccentWidth(ControlMeasure measure) {
        Objects.requireNonNull(measure, "measure");
        if (measure.mode() == MeasureMode.ABSOLUTE) {
            return this.setAccentWidth(measure.value());
        }
        this.accentWidthMeasure = measure;
        return this;
    }

    /**
     * Returns the retained measure, without resolving it.
     * @return explicit measure or the legacy absolute value
     */
    public ControlMeasure getAccentWidthMeasure() {
        return this.accentWidthMeasure == null ? ControlMeasure.absolute(this.accentWidth) : this.accentWidthMeasure;
    }

    public float getAccentWidth() {
        return this.accentWidth;
    }

    public NotificationStyle setAccentWidth(float accentWidth) {
        this.accentWidthMeasure = null;
        this.accentWidth = Math.max(0.0F, accentWidth);
        return this;
    }

    /**
     * Sets an explicit measure; relative values use the current sketch height.
     * @param measure absolute pixels or relative factor
     * @return this style
     */
    public NotificationStyle setIconSize(ControlMeasure measure) {
        Objects.requireNonNull(measure, "measure");
        if (measure.mode() == MeasureMode.ABSOLUTE) {
            return this.setIconSize(measure.value());
        }
        this.iconSizeMeasure = measure;
        return this;
    }

    /**
     * Returns the retained measure, without resolving it.
     * @return explicit measure or the legacy absolute value
     */
    public ControlMeasure getIconSizeMeasure() {
        return this.iconSizeMeasure == null ? ControlMeasure.absolute(this.iconSize) : this.iconSizeMeasure;
    }

    public float getIconSize() {
        return this.iconSize;
    }

    public NotificationStyle setIconSize(float iconSize) {
        this.iconSizeMeasure = null;
        this.iconSize = Math.max(0.0F, iconSize);
        return this;
    }

    /**
     * Sets an explicit measure; relative values use the current sketch height.
     * @param measure absolute pixels or relative factor
     * @return this style
     */
    public NotificationStyle setIconTextGap(ControlMeasure measure) {
        Objects.requireNonNull(measure, "measure");
        if (measure.mode() == MeasureMode.ABSOLUTE) {
            return this.setIconTextGap(measure.value());
        }
        this.iconTextGapMeasure = measure;
        return this;
    }

    /**
     * Returns the retained measure, without resolving it.
     * @return explicit measure or the legacy absolute value
     */
    public ControlMeasure getIconTextGapMeasure() {
        return this.iconTextGapMeasure == null ? ControlMeasure.absolute(this.iconTextGap) : this.iconTextGapMeasure;
    }

    public float getIconTextGap() {
        return this.iconTextGap;
    }

    public NotificationStyle setIconTextGap(float iconTextGap) {
        this.iconTextGapMeasure = null;
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
        this.fontResolver = null;
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
    /**
     * Installs a cached JSON font resolver for the effective render size.
     * @param resolver resolver, or null to retain the configured font
     * @return this style
     */
    public NotificationStyle setFontResolver(FontLoader.FontResolver resolver) {
        this.fontResolver = resolver;
        return this;
    }

    /**
     * Resolves an independent pixel style for one frame, preserving this style's measures.
     * Legacy float getters expose the configured pixel fallback; use measure getters
     * to inspect relative configuration.
     * @param sketch overlay surface
     * @return resolved pixel style
     */
    public NotificationStyle resolve(PApplet sketch) {
        NotificationStyle result = new NotificationStyle(this);
        result.setStrokeWeight(this.getStrokeWeightMeasure().resolve(sketch.height));
        result.setCornerRadius(this.getCornerRadiusMeasure().resolve(sketch.height));
        result.setTextSize(this.getTextSizeMeasure().resolve(sketch.height));
        result.setTextPadding(this.getTextPaddingMeasure().resolve(sketch.height));
        result.setGap(this.getGapMeasure().resolve(sketch.height));
        result.setMargin(this.getMarginMeasure().resolve(sketch.height));
        result.setWidth(this.getWidthMeasure().resolve(sketch.height));
        result.setMinHeight(this.getMinHeightMeasure().resolve(sketch.height));
        result.setAccentWidth(this.getAccentWidthMeasure().resolve(sketch.height));
        result.setIconSize(this.getIconSizeMeasure().resolve(sketch.height));
        result.setIconTextGap(this.getIconTextGapMeasure().resolve(sketch.height));
        result.font = FontLoader.resolve(this.fontResolver, sketch, result.textSize, this.font);
        result.fontResolver = null;
        return result;
    }
}
