package com.cpz.processing.controls.switchcontrol.style;

import com.cpz.processing.controls.common.style.InteractiveStyleHelper;
import com.cpz.processing.controls.common.theme.ThemeManager;
import com.cpz.processing.controls.common.theme.ThemeTokens;
import com.cpz.processing.controls.switchcontrol.style.interfaces.SwitchStyle;
import com.cpz.processing.controls.switchcontrol.view.SwitchViewState;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public final class ParametricSwitchStyle implements SwitchStyle {

    private final SwitchStyleConfig cfg;

    public ParametricSwitchStyle(SwitchStyleConfig cfg) {
        if (cfg == null) throw new IllegalArgumentException("Config null");
        if (cfg.shape == null) throw new IllegalArgumentException("ShapeRenderer requerido");
        this.cfg = cfg;
    }

    @Override
    public void render(PApplet p, SwitchViewState s) {
        ThemeTokens tokens = ThemeManager.getTheme().tokens();
        int stateIndex = Math.max(0, s.stateIndex());
        int baseFill = stateIndex == 0
                ? resolveStateColor(tokens.surfaceVariant, cfg.offFillOverride, 0)
                : resolveStateColor(tokens.primary, cfg.onFillOverride, stateIndex);
        int interactiveFill = InteractiveStyleHelper.resolveFillColor(
                baseFill,
                resolveInteractiveColor(baseFill, tokens.hoverOverlay, cfg.hoverFillOverride, cfg.hoverBlendWithWhite, p, true),
                resolveInteractiveColor(baseFill, tokens.pressedOverlay, cfg.pressedFillOverride, cfg.pressedBlendWithBlack, p, false),
                s.hovered(),
                s.pressed()
        );
        int disabledAlpha = cfg.disabledAlpha != 0 ? cfg.disabledAlpha : tokens.disabledAlpha;
        int strokeColor = resolveColorOverride(tokens.border, cfg.strokeOverride, cfg.strokeColor);
        SwitchRenderStyle style = new SwitchRenderStyle(
                InteractiveStyleHelper.applyDisabledAlpha(interactiveFill, s.enabled(), disabledAlpha),
                InteractiveStyleHelper.resolveStrokeColor(strokeColor, s.enabled(), disabledAlpha),
                InteractiveStyleHelper.resolveStrokeWeight(cfg.strokeWidth, cfg.strokeWidthHover, s.hovered())
        );
        cfg.shape.render(p, s.x(), s.y(), s.width(), s.height(), style);
    }

    private int resolveStateColor(int tokenColor, Integer override, int stateIndex) {
        if (cfg.stateColors != null && stateIndex < cfg.stateColors.length && cfg.stateColors[stateIndex] != 0) {
            return cfg.stateColors[stateIndex];
        }
        if (override != null) {
            return override;
        }
        return tokenColor;
    }

    private int resolveInteractiveColor(int baseColor,
                                        int overlayColor,
                                        Integer override,
                                        float legacyBlend,
                                        PApplet p,
                                        boolean useWhiteBlend) {
        if (override != null) {
            return override;
        }
        if (legacyBlend != 0f) {
            return p.lerpColor(baseColor, p.color(useWhiteBlend ? 255 : 0), legacyBlend);
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
