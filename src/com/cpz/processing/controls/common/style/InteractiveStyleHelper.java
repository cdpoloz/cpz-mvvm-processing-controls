package com.cpz.processing.controls.common.style;

import com.cpz.processing.controls.util.Colors;
import processing.core.PApplet;

public final class InteractiveStyleHelper {

    private InteractiveStyleHelper() {
    }

    public static int resolveFillColor(int normalColor, int hoveredColor, int pressedColor, boolean hovered, boolean pressed) {
        if (pressed) {
            return pressedColor;
        }
        if (hovered) {
            return hoveredColor;
        }
        return normalColor;
    }

    public static int resolveFillColor(PApplet p,
                                       int baseColor,
                                       float hoverBlendWithWhite,
                                       float pressedBlendWithBlack,
                                       boolean hovered,
                                       boolean pressed) {
        int hoveredColor = p.lerpColor(baseColor, p.color(255), hoverBlendWithWhite);
        int pressedColor = p.lerpColor(baseColor, p.color(0), pressedBlendWithBlack);
        return resolveFillColor(baseColor, hoveredColor, pressedColor, hovered, pressed);
    }

    public static int applyDisabledAlpha(int color, boolean enabled, int disabledAlpha) {
        return enabled ? color : Colors.alpha(disabledAlpha, color);
    }

    public static float resolveStrokeWeight(float normalWeight, float hoveredWeight, boolean hovered) {
        return hovered ? hoveredWeight : normalWeight;
    }

    public static int resolveStrokeColor(int normalColor, boolean enabled, int disabledAlpha) {
        return applyDisabledAlpha(normalColor, enabled, disabledAlpha);
    }
}
