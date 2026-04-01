package com.cpz.processing.controls.numericfieldcontrol.style;

import com.cpz.processing.controls.common.theme.ThemeManager;
import com.cpz.processing.controls.common.theme.ThemeTokens;
import com.cpz.processing.controls.numericfieldcontrol.style.render.DefaultNumericFieldRenderer;
import com.cpz.processing.controls.numericfieldcontrol.view.NumericFieldViewState;
import processing.core.PApplet;

public final class NumericFieldStyle {

    private final NumericFieldStyleConfig config;
    private final DefaultNumericFieldRenderer renderer;

    public NumericFieldStyle(NumericFieldStyleConfig config) {
        this(config, new DefaultNumericFieldRenderer());
    }

    public NumericFieldStyle(NumericFieldStyleConfig config, DefaultNumericFieldRenderer renderer) {
        this.config = config == null ? new NumericFieldStyleConfig() : config;
        this.renderer = renderer == null ? new DefaultNumericFieldRenderer() : renderer;
    }

    public void render(PApplet sketch, NumericFieldViewState state) {
        renderer.render(sketch, state, resolveRenderStyle(state));
    }

    public NumericFieldRenderStyle resolveRenderStyle(NumericFieldViewState state) {
        ThemeTokens tokens = ThemeManager.getTheme().tokens();
        int backgroundColor = resolveColorOverride(tokens.surface, config.backgroundOverride, config.backgroundColor);
        int borderColor = resolveColorOverride(tokens.border, config.borderOverride, config.borderColor);
        int textColor = resolveColorOverride(tokens.onSurface, config.textOverride, config.textColor);
        int cursorColor = resolveColorOverride(tokens.cursor, config.cursorOverride, config.cursorColor);
        int selectionColor = resolveColorOverride(tokens.selection, config.selectionOverride, config.selectionColor);
        int selectionTextColor = resolveSelectionTextColor(tokens.onSurface);
        return new NumericFieldRenderStyle(
                backgroundColor,
                state.focused() ? blend(borderColor, cursorColor, 0.35f) : borderColor,
                textColor,
                state.focused() ? cursorColor : borderColor,
                selectionColor,
                selectionTextColor,
                config.textSize,
                config.font
        );
    }

    private int resolveSelectionTextColor(int defaultColor) {
        if (config.selectionTextOverride != null) {
            return config.selectionTextOverride;
        }
        if (config.selectionTextColor != 0) {
            return config.selectionTextColor;
        }
        return defaultColor;
    }

    private int resolveColorOverride(int baseColor, Integer override, int legacyOverride) {
        if (override != null) {
            return override;
        }
        return legacyOverride != 0 ? legacyOverride : baseColor;
    }

    private int blend(int from, int to, float amount) {
        float clampedAmount = Math.max(0f, Math.min(1f, amount));
        int a = blendChannel((from >>> 24) & 0xFF, (to >>> 24) & 0xFF, clampedAmount);
        int r = blendChannel((from >>> 16) & 0xFF, (to >>> 16) & 0xFF, clampedAmount);
        int g = blendChannel((from >>> 8) & 0xFF, (to >>> 8) & 0xFF, clampedAmount);
        int b = blendChannel(from & 0xFF, to & 0xFF, clampedAmount);
        return ((a & 0xFF) << 24)
                | ((r & 0xFF) << 16)
                | ((g & 0xFF) << 8)
                | (b & 0xFF);
    }

    private int blendChannel(int from, int to, float amount) {
        return Math.round(from + ((to - from) * amount));
    }
}
