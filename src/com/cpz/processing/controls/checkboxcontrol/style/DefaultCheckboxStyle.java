package com.cpz.processing.controls.checkboxcontrol.style;

import com.cpz.processing.controls.checkboxcontrol.style.interfaces.CheckboxRenderer;
import com.cpz.processing.controls.checkboxcontrol.style.render.DefaultCheckboxRenderer;
import com.cpz.processing.controls.checkboxcontrol.view.CheckboxViewState;
import com.cpz.processing.controls.common.style.InteractiveStyleHelper;
import com.cpz.processing.controls.common.theme.ThemeManager;
import com.cpz.processing.controls.common.theme.ThemeTokens;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class DefaultCheckboxStyle implements CheckboxStyle {

    private final CheckboxStyleConfig config;
    private final CheckboxRenderer renderer;

    public DefaultCheckboxStyle(CheckboxStyleConfig config) {
        this(config, config != null && config.renderer != null ? config.renderer : new DefaultCheckboxRenderer());
    }

    public DefaultCheckboxStyle(CheckboxStyleConfig config, CheckboxRenderer renderer) {
        this.config = config;
        this.renderer = renderer;
    }

    @Override
    public void render(PApplet p, CheckboxViewState state) {
        ThemeTokens tokens = ThemeManager.getTheme().tokens();
        int baseFill = state.checked()
                ? resolveBaseFill(tokens.primary, config.checkedFillOverride)
                : resolveBaseFill(tokens.surface, config.uncheckedFillOverride);
        int boxColor = InteractiveStyleHelper.resolveFillColor(
                baseFill,
                resolveInteractiveColor(baseFill, tokens.hoverOverlay, config.hoverFillOverride, config.boxHoverColor),
                resolveInteractiveColor(baseFill, tokens.pressedOverlay, config.pressedFillOverride, config.boxPressedColor),
                state.hovered(),
                state.pressed()
        );
        int disabledAlpha = config.disabledAlpha != 0 ? config.disabledAlpha : tokens.disabledAlpha;
        int borderColor = resolveColorOverride(tokens.border, config.strokeOverride, config.borderColor);
        int checkColor = resolveColorOverride(tokens.onPrimary, config.checkOverride, config.checkColor);
        CheckboxRenderStyle style = new CheckboxRenderStyle(
                InteractiveStyleHelper.applyDisabledAlpha(boxColor, state.enabled(), disabledAlpha),
                InteractiveStyleHelper.resolveStrokeColor(borderColor, state.enabled(), disabledAlpha),
                InteractiveStyleHelper.resolveStrokeWeight(config.borderWidth, config.borderWidthHover, state.hovered()),
                config.cornerRadius,
                state.checked(),
                InteractiveStyleHelper.resolveStrokeColor(checkColor, state.enabled(), disabledAlpha),
                config.checkInset,
                Math.max(2.5f, state.width() * 0.12f)
        );
        renderer.render(p, state.x(), state.y(), state.width(), state.height(), style);
    }

    private int resolveBaseFill(int tokenColor, Integer override) {
        if (override != null) {
            return override;
        }
        return config.boxColor != 0 ? config.boxColor : tokenColor;
    }

    private int resolveInteractiveColor(int baseColor, int overlayColor, Integer override, int legacyOverride) {
        if (override != null) {
            return override;
        }
        if (legacyOverride != 0) {
            return legacyOverride;
        }
        return InteractiveStyleHelper.applyOverlay(baseColor, overlayColor);
    }

    private int resolveColorOverride(int tokenColor, Integer override, int legacyOverride) {
        if (override != null) {
            return override;
        }
        return legacyOverride != 0 ? legacyOverride : tokenColor;
    }
}
