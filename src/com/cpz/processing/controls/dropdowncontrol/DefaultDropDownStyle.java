package com.cpz.processing.controls.dropdowncontrol;

import com.cpz.processing.controls.common.style.InteractiveStyleHelper;
import com.cpz.processing.controls.common.theme.ThemeManager;
import com.cpz.processing.controls.common.theme.ThemeTokens;
import com.cpz.processing.controls.util.Colors;
import processing.core.PApplet;

public final class DefaultDropDownStyle {

    private final DropDownStyleConfig config;
    private final DefaultDropDownRenderer renderer;

    public DefaultDropDownStyle(DropDownStyleConfig config) {
        this(config, new DefaultDropDownRenderer());
    }

    public DefaultDropDownStyle(DropDownStyleConfig config, DefaultDropDownRenderer renderer) {
        if (config == null) {
            throw new IllegalArgumentException("config must not be null");
        }
        this.config = config;
        this.renderer = renderer == null ? new DefaultDropDownRenderer() : renderer;
    }

    public void render(PApplet sketch, DropDownViewState state) {
        renderer.render(sketch, state, resolveRenderStyle(state));
    }

    public float getCornerRadius() {
        return config.cornerRadius;
    }

    public float getListCornerRadius() {
        return config.listCornerRadius;
    }

    public float getStrokeWeight() {
        return config.strokeWeight;
    }

    public float getFocusedStrokeWeight() {
        return config.focusedStrokeWeight;
    }

    public float getItemHeight() {
        return config.itemHeight;
    }

    public float getTextPadding() {
        return config.textPadding;
    }

    public float getArrowPadding() {
        return config.arrowPadding;
    }

    public int getMaxVisibleItems() {
        return Math.max(1, config.maxVisibleItems);
    }

    public DropDownRenderStyle resolveRenderStyle(DropDownViewState state) {
        ThemeTokens tokens = ThemeManager.getTheme().tokens();

        int baseFill = resolveColor(tokens.surface, config.baseFillOverride);
        int listFill = resolveColor(tokens.surfaceVariant, config.listFillOverride);
        int textColor = resolveColor(tokens.onSurface, config.textOverride);
        int borderColor = resolveColor(tokens.border, config.borderOverride);
        int focusBorderColor = resolveColor(tokens.primary, config.focusedBorderOverride);
        int hoverItemOverlay = resolveColor(tokens.hoverOverlay, config.hoverItemOverlayOverride);
        int selectedItemOverlay = resolveColor(Colors.alpha(36, tokens.primary), config.selectedItemOverlayOverride);

        int baseInteractiveFill = resolveBaseFill(baseFill, hoverItemOverlay, tokens.pressedOverlay, state);
        int strokeColor = state.focused()
                ? blend(borderColor, focusBorderColor, 0.35f)
                : borderColor;
        float strokeWeight = (state.hovered() || state.focused()) ? config.focusedStrokeWeight : config.strokeWeight;
        int itemHoverColor = InteractiveStyleHelper.applyOverlay(listFill, hoverItemOverlay);
        int itemSelectedColor = InteractiveStyleHelper.applyOverlay(listFill, selectedItemOverlay);
        int disabledAlpha = config.disabledAlpha != 0 ? config.disabledAlpha : tokens.disabledAlpha;

        return new DropDownRenderStyle(
                InteractiveStyleHelper.applyDisabledAlpha(baseInteractiveFill, state.enabled(), disabledAlpha),
                InteractiveStyleHelper.applyDisabledAlpha(listFill, state.enabled(), disabledAlpha),
                InteractiveStyleHelper.applyDisabledAlpha(itemHoverColor, state.enabled(), disabledAlpha),
                InteractiveStyleHelper.applyDisabledAlpha(itemSelectedColor, state.enabled(), disabledAlpha),
                InteractiveStyleHelper.applyDisabledAlpha(strokeColor, state.enabled(), disabledAlpha),
                strokeWeight,
                InteractiveStyleHelper.applyDisabledAlpha(textColor, state.enabled(), disabledAlpha),
                InteractiveStyleHelper.applyDisabledAlpha(textColor, state.enabled(), disabledAlpha),
                state.cornerRadius(),
                state.listCornerRadius(),
                config.textSize,
                state.itemHeight(),
                state.textPadding(),
                state.arrowPadding(),
                Math.max(1, state.maxVisibleItems()),
                config.font
        );
    }

    private int resolveBaseFill(int baseFill, int hoverOverlay, int pressedOverlay, DropDownViewState state) {
        return InteractiveStyleHelper.resolveFillColorWithOverlays(
                baseFill,
                hoverOverlay,
                pressedOverlay,
                state.hovered(),
                state.pressed()
        );
    }

    private int resolveColor(int fallback, Integer override) {
        return override != null ? override : fallback;
    }

    private int blend(int from, int to, float amount) {
        float clampedAmount = Math.max(0f, Math.min(1f, amount));
        int a = blendChannel((from >>> 24) & 0xFF, (to >>> 24) & 0xFF, clampedAmount);
        int r = blendChannel((from >>> 16) & 0xFF, (to >>> 16) & 0xFF, clampedAmount);
        int g = blendChannel((from >>> 8) & 0xFF, (to >>> 8) & 0xFF, clampedAmount);
        int b = blendChannel(from & 0xFF, to & 0xFF, clampedAmount);
        return Colors.argb(a, r, g, b);
    }

    private int blendChannel(int from, int to, float amount) {
        return Math.round(from + ((to - from) * amount));
    }
}
