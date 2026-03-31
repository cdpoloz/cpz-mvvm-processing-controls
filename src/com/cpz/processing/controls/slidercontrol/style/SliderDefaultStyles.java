package com.cpz.processing.controls.slidercontrol.style;

import com.cpz.processing.controls.common.theme.Theme;
import com.cpz.processing.controls.common.theme.ThemeManager;
import com.cpz.processing.controls.util.Colors;

public final class SliderDefaultStyles {

    private SliderDefaultStyles() {
    }

    public static SliderStyle standard() {
        Theme theme = ThemeManager.getTheme();
        SliderStyleConfig config = new SliderStyleConfig();
        config.trackColor = Colors.gray(76);
        config.trackHoverColor = blend(config.trackColor, Colors.gray(255), theme.hoverModifier);
        config.trackPressedColor = blend(config.trackColor, Colors.gray(0), theme.pressedModifier);
        config.trackStrokeColor = Colors.gray(210);
        config.trackStrokeWeight = 1.5f;
        config.trackStrokeWeightHover = 2.5f;
        config.trackThickness = 8f;

        config.activeTrackColor = theme.primaryColor;
        config.activeTrackHoverColor = blend(config.activeTrackColor, Colors.gray(255), theme.hoverModifier);
        config.activeTrackPressedColor = blend(config.activeTrackColor, Colors.gray(0), theme.pressedModifier);

        config.thumbColor = Colors.gray(255);
        config.thumbHoverColor = blend(config.thumbColor, theme.primaryColor, 0.18f);
        config.thumbPressedColor = blend(config.thumbColor, Colors.gray(0), 0.12f);
        config.thumbStrokeColor = theme.primaryColor;
        config.thumbStrokeWeight = 2f;
        config.thumbStrokeWeightHover = 3f;
        config.thumbSize = 24f;

        config.textColor = theme.textColor;
        config.disabledAlpha = theme.disabledAlpha;
        config.showValueText = true;
        config.svgColorMode = SvgColorMode.USE_RENDER_STYLE;
        return new SliderStyle(config);
    }

    private static int blend(int from, int to, float amount) {
        float clampedAmount = Math.max(0f, Math.min(1f, amount));
        int a = blendChannel((from >>> 24) & 0xFF, (to >>> 24) & 0xFF, clampedAmount);
        int r = blendChannel((from >>> 16) & 0xFF, (to >>> 16) & 0xFF, clampedAmount);
        int g = blendChannel((from >>> 8) & 0xFF, (to >>> 8) & 0xFF, clampedAmount);
        int b = blendChannel(from & 0xFF, to & 0xFF, clampedAmount);
        return Colors.argb(a, r, g, b);
    }

    private static int blendChannel(int from, int to, float amount) {
        return Math.round(from + ((to - from) * amount));
    }
}
