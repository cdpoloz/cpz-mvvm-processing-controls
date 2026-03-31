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

    public static int resolveFillColorWithOverlays(int baseColor,
                                                   int hoverOverlayColor,
                                                   int pressedOverlayColor,
                                                   boolean hovered,
                                                   boolean pressed) {
        return resolveFillColor(
                baseColor,
                applyOverlay(baseColor, hoverOverlayColor),
                applyOverlay(baseColor, pressedOverlayColor),
                hovered,
                pressed
        );
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

    public static int applyOverlay(int baseColor, int overlayColor) {
        float amount = ((overlayColor >>> 24) & 0xFF) / 255f;
        int a = blendChannel((baseColor >>> 24) & 0xFF, (overlayColor >>> 24) & 0xFF, amount);
        int r = blendChannel((baseColor >>> 16) & 0xFF, (overlayColor >>> 16) & 0xFF, amount);
        int g = blendChannel((baseColor >>> 8) & 0xFF, (overlayColor >>> 8) & 0xFF, amount);
        int b = blendChannel(baseColor & 0xFF, overlayColor & 0xFF, amount);
        return Colors.argb(a, r, g, b);
    }

    public static float resolveStrokeWeight(float normalWeight, float hoveredWeight, boolean hovered) {
        return hovered ? hoveredWeight : normalWeight;
    }

    public static int resolveStrokeColor(int normalColor, boolean enabled, int disabledAlpha) {
        return applyDisabledAlpha(normalColor, enabled, disabledAlpha);
    }

    private static int blendChannel(int from, int to, float amount) {
        return Math.round(from + ((to - from) * amount));
    }
}
