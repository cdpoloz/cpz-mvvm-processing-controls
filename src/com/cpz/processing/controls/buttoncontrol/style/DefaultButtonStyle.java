package com.cpz.processing.controls.buttoncontrol.style;

import com.cpz.processing.controls.buttoncontrol.style.interfaces.ButtonRenderer;
import com.cpz.processing.controls.buttoncontrol.style.render.DefaultButtonRenderer;
import com.cpz.processing.controls.buttoncontrol.view.ButtonViewState;
import com.cpz.processing.controls.common.style.InteractiveStyleHelper;
import com.cpz.processing.controls.common.theme.ThemeManager;
import com.cpz.processing.controls.common.theme.ThemeTokens;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class DefaultButtonStyle implements ButtonStyle {

    private final ButtonStyleConfig config;
    private final ButtonRenderer renderer;

    public DefaultButtonStyle(ButtonStyleConfig config) {
        this(config, config != null && config.renderer != null ? config.renderer : new DefaultButtonRenderer());
    }

    public DefaultButtonStyle(ButtonStyleConfig config, ButtonRenderer renderer) {
        this.config = config;
        this.renderer = renderer;
    }

    @Override
    public void render(PApplet p, ButtonViewState state) {
        ThemeTokens tokens = ThemeManager.getTheme().tokens();
        int baseColor = resolveColorOverride(tokens.primary, config.fillOverride, config.baseColor);
        int hoveredColor = resolveInteractiveOverride(
                baseColor,
                tokens.hoverOverlay,
                config.hoverBlendWithWhite == 0f ? null : p.lerpColor(baseColor, p.color(255), config.hoverBlendWithWhite)
        );
        int pressedColor = resolveInteractiveOverride(
                baseColor,
                tokens.pressedOverlay,
                config.pressedBlendWithBlack == 0f ? null : p.lerpColor(baseColor, p.color(0), config.pressedBlendWithBlack)
        );
        int background = InteractiveStyleHelper.resolveFillColor(
                baseColor,
                hoveredColor,
                pressedColor,
                state.hovered(),
                state.pressed()
        );
        int disabledAlpha = config.disabledAlpha != 0 ? config.disabledAlpha : tokens.disabledAlpha;
        int strokeColor = resolveColorOverride(tokens.border, config.strokeOverride, config.strokeColor);
        int textColor = resolveColorOverride(tokens.onPrimary, config.textOverride, config.textColor);
        ButtonRenderStyle style = new ButtonRenderStyle(
                InteractiveStyleHelper.applyDisabledAlpha(background, state.enabled(), disabledAlpha),
                InteractiveStyleHelper.resolveStrokeColor(strokeColor, state.enabled(), disabledAlpha),
                InteractiveStyleHelper.resolveStrokeWeight(config.strokeWeight, config.strokeWeightHover, state.hovered()),
                InteractiveStyleHelper.applyDisabledAlpha(textColor, state.enabled(), disabledAlpha),
                config.cornerRadius,
                state.showText(),
                state.text()
        );
        renderer.render(p, state.x(), state.y(), state.width(), state.height(), style);
    }

    private int resolveColorOverride(int baseColor, Integer override, int legacyOverride) {
        if (override != null) {
            return override;
        }
        return legacyOverride != 0 ? legacyOverride : baseColor;
    }

    private int resolveInteractiveOverride(int baseColor, int overlayColor, Integer legacyOverride) {
        if (legacyOverride != null) {
            return legacyOverride;
        }
        return InteractiveStyleHelper.applyOverlay(baseColor, overlayColor);
    }
}
