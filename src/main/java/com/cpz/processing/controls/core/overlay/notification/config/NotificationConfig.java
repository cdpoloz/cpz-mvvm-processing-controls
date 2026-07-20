package com.cpz.processing.controls.core.overlay.notification.config;

import com.cpz.processing.controls.core.overlay.notification.NotificationManager;
import com.cpz.processing.controls.core.overlay.notification.NotificationPlacement;
import com.cpz.processing.controls.core.overlay.notification.NotificationSeverity;
import com.cpz.processing.controls.core.overlay.notification.NotificationStyle;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * Optional standalone configuration for {@link NotificationManager}.
 *
 * <p>This config is intentionally separate from control JSON loading. It
 * configures the runtime notification manager and style only; it does not
 * create notification messages.</p>
 *
 * @author CPZ
 */
public final class NotificationConfig {
    private final NotificationPlacement placement;
    private final Integer maxVisible;
    private final Long defaultDurationMillis;
    private final EnumMap<NotificationSeverity, Long> severityDurations;
    private final StyleConfig style;

    NotificationConfig(
            NotificationPlacement placement,
            Integer maxVisible,
            Long defaultDurationMillis,
            Map<NotificationSeverity, Long> severityDurations,
            StyleConfig style
    ) {
        this.placement = placement;
        this.maxVisible = maxVisible;
        this.defaultDurationMillis = defaultDurationMillis;
        this.severityDurations = new EnumMap<>(NotificationSeverity.class);
        if (severityDurations != null) {
            this.severityDurations.putAll(severityDurations);
        }
        this.style = style;
    }

    public NotificationPlacement getPlacement() {
        return this.placement;
    }

    public Integer getMaxVisible() {
        return this.maxVisible;
    }

    public Long getDefaultDurationMillis() {
        return this.defaultDurationMillis;
    }

    public Map<NotificationSeverity, Long> getSeverityDurations() {
        return Collections.unmodifiableMap(this.severityDurations);
    }

    public StyleConfig getStyle() {
        return this.style;
    }

    public void applyTo(NotificationManager manager) {
        this.applyTo(null, manager);
    }

    public void applyTo(PApplet sketch, NotificationManager manager) {
        Objects.requireNonNull(manager, "manager");

        if (this.placement != null) {
            manager.setPlacement(this.placement);
        }
        if (this.maxVisible != null && this.maxVisible > 0) {
            manager.setMaxVisible(this.maxVisible);
        }
        if (this.defaultDurationMillis != null && this.defaultDurationMillis > 0L) {
            manager.setDefaultDurationMillis(this.defaultDurationMillis);
        }
        for (Map.Entry<NotificationSeverity, Long> entry : this.severityDurations.entrySet()) {
            Long duration = entry.getValue();
            if (duration != null && duration > 0L) {
                manager.setSeverityDurationMillis(entry.getKey(), duration);
            }
        }
        if (this.style != null) {
            NotificationStyle target = new NotificationStyle(manager.getStyle());
            this.style.applyTo(target, sketch);
            manager.setStyle(target);
        }
    }

    public static final class StyleConfig {
        private final Integer backgroundColor;
        private final Integer textColor;
        private final Integer borderColor;
        private final Float strokeWeight;
        private final Float cornerRadius;
        private final Float textSize;
        private final Float textPadding;
        private final Float gap;
        private final Float margin;
        private final Float width;
        private final Float minHeight;
        private final Float accentWidth;
        private final Float iconSize;
        private final Float iconTextGap;
        private final Integer infoAccentColor;
        private final Integer successAccentColor;
        private final Integer warningAccentColor;
        private final Integer errorAccentColor;
        private final EnumMap<NotificationSeverity, String> severityIcons;
        private final String fontPath;
        private final String sourcePath;
        private final FontResolver fontResolver;

        StyleConfig(
                Integer backgroundColor,
                Integer textColor,
                Integer borderColor,
                Float strokeWeight,
                Float cornerRadius,
                Float textSize,
                Float textPadding,
                Float gap,
                Float margin,
                Float width,
                Float minHeight,
                Float accentWidth,
                Float iconSize,
                Float iconTextGap,
                Integer infoAccentColor,
                Integer successAccentColor,
                Integer warningAccentColor,
                Integer errorAccentColor,
                Map<NotificationSeverity, String> severityIcons,
                String fontPath,
                String sourcePath,
                FontResolver fontResolver
        ) {
            this.backgroundColor = backgroundColor;
            this.textColor = textColor;
            this.borderColor = borderColor;
            this.strokeWeight = strokeWeight;
            this.cornerRadius = cornerRadius;
            this.textSize = textSize;
            this.textPadding = textPadding;
            this.gap = gap;
            this.margin = margin;
            this.width = width;
            this.minHeight = minHeight;
            this.accentWidth = accentWidth;
            this.iconSize = iconSize;
            this.iconTextGap = iconTextGap;
            this.infoAccentColor = infoAccentColor;
            this.successAccentColor = successAccentColor;
            this.warningAccentColor = warningAccentColor;
            this.errorAccentColor = errorAccentColor;
            this.severityIcons = new EnumMap<>(NotificationSeverity.class);
            if (severityIcons != null) {
                this.severityIcons.putAll(severityIcons);
            }
            this.fontPath = fontPath;
            this.sourcePath = sourcePath;
            this.fontResolver = fontResolver;
        }

        public Integer getBackgroundColor() {
            return this.backgroundColor;
        }

        public Integer getTextColor() {
            return this.textColor;
        }

        public Integer getBorderColor() {
            return this.borderColor;
        }

        public Float getStrokeWeight() {
            return this.strokeWeight;
        }

        public Float getCornerRadius() {
            return this.cornerRadius;
        }

        public Float getTextSize() {
            return this.textSize;
        }

        public Float getTextPadding() {
            return this.textPadding;
        }

        public Float getGap() {
            return this.gap;
        }

        public Float getMargin() {
            return this.margin;
        }

        public Float getWidth() {
            return this.width;
        }

        public Float getMinHeight() {
            return this.minHeight;
        }

        public Float getAccentWidth() {
            return this.accentWidth;
        }

        public Float getIconSize() {
            return this.iconSize;
        }

        public Float getIconTextGap() {
            return this.iconTextGap;
        }

        public Integer getInfoAccentColor() {
            return this.infoAccentColor;
        }

        public Integer getSuccessAccentColor() {
            return this.successAccentColor;
        }

        public Integer getWarningAccentColor() {
            return this.warningAccentColor;
        }

        public Integer getErrorAccentColor() {
            return this.errorAccentColor;
        }

        public Map<NotificationSeverity, String> getSeverityIcons() {
            return Collections.unmodifiableMap(this.severityIcons);
        }

        public String getFontPath() {
            return this.fontPath;
        }

        public String getSourcePath() {
            return this.sourcePath;
        }

        void applyTo(NotificationStyle target, PApplet sketch) {
            if (this.backgroundColor != null) {
                target.setBackgroundColor(this.backgroundColor);
            }
            if (this.textColor != null) {
                target.setTextColor(this.textColor);
            }
            if (this.borderColor != null) {
                target.setBorderColor(this.borderColor);
            }
            if (this.strokeWeight != null) {
                target.setStrokeWeight(this.strokeWeight);
            }
            if (this.cornerRadius != null) {
                target.setCornerRadius(this.cornerRadius);
            }
            if (this.textSize != null) {
                target.setTextSize(this.textSize);
            }
            if (this.textPadding != null) {
                target.setTextPadding(this.textPadding);
            }
            if (this.gap != null) {
                target.setGap(this.gap);
            }
            if (this.margin != null) {
                target.setMargin(this.margin);
            }
            if (this.width != null) {
                target.setWidth(this.width);
            }
            if (this.minHeight != null) {
                target.setMinHeight(this.minHeight);
            }
            if (this.accentWidth != null) {
                target.setAccentWidth(this.accentWidth);
            }
            if (this.iconSize != null) {
                target.setIconSize(this.iconSize);
            }
            if (this.iconTextGap != null) {
                target.setIconTextGap(this.iconTextGap);
            }
            if (this.infoAccentColor != null) {
                target.setInfoAccentColor(this.infoAccentColor);
            }
            if (this.successAccentColor != null) {
                target.setSuccessAccentColor(this.successAccentColor);
            }
            if (this.warningAccentColor != null) {
                target.setWarningAccentColor(this.warningAccentColor);
            }
            if (this.errorAccentColor != null) {
                target.setErrorAccentColor(this.errorAccentColor);
            }
            for (Map.Entry<NotificationSeverity, String> entry : this.severityIcons.entrySet()) {
                target.setSeverityIcon(entry.getKey(), entry.getValue());
            }
            if (this.fontPath != null) {
                PFont resolvedFont = this.resolveFont(sketch, target.getTextSize());
                if (resolvedFont != null) {
                    target.setFont(resolvedFont);
                }
            }
        }

        private PFont resolveFont(PApplet sketch, float effectiveTextSize) {
            if (sketch != null) {
                return this.fontResolver == null ? null : this.fontResolver.load(sketch, effectiveTextSize);
            }
            return this.fontResolver == null ? null : this.fontResolver.load(effectiveTextSize);
        }
    }

    interface FontResolver {
        PFont load(float effectiveTextSize);

        default PFont load(PApplet sketch, float effectiveTextSize) {
            return this.load(effectiveTextSize);
        }
    }
}
