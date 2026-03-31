package com.cpz.processing.controls.slidercontrol.style;

import com.cpz.processing.controls.common.style.InteractiveStyleHelper;
import com.cpz.processing.controls.common.theme.ThemeManager;
import com.cpz.processing.controls.common.theme.ThemeTokens;
import com.cpz.processing.controls.slidercontrol.style.render.SliderRenderer;
import com.cpz.processing.controls.slidercontrol.view.SliderGeometry;
import com.cpz.processing.controls.slidercontrol.view.SliderViewState;
import processing.core.PApplet;

public final class SliderStyle {

    private final SliderStyleConfig config;
    private final SliderRenderer renderer;

    public SliderStyle(SliderStyleConfig config) {
        this(config, new SliderRenderer());
    }

    public SliderStyle(SliderStyleConfig config, SliderRenderer renderer) {
        if (config == null) {
            throw new IllegalArgumentException("config must not be null");
        }
        this.config = config;
        this.renderer = renderer == null ? new SliderRenderer() : renderer;
    }

    public void render(PApplet p, SliderViewState state, SliderGeometry geometry) {
        ThemeTokens tokens = ThemeManager.getTheme().tokens();
        boolean pressed = state.pressed() || state.dragging();
        int trackBase = resolveColorOverride(tokens.surfaceVariant, config.trackOverride, config.trackColor);
        int trackColor = InteractiveStyleHelper.resolveFillColor(
                trackBase,
                resolveInteractiveColor(trackBase, tokens.hoverOverlay, config.trackHoverOverride, config.trackHoverColor),
                resolveInteractiveColor(trackBase, tokens.pressedOverlay, config.trackPressedOverride, config.trackPressedColor),
                state.hovered(),
                pressed
        );
        int activeBase = resolveColorOverride(tokens.primary, config.progressOverride, config.activeTrackColor);
        int activeTrackColor = InteractiveStyleHelper.resolveFillColor(
                activeBase,
                resolveInteractiveColor(activeBase, tokens.hoverOverlay, config.progressHoverOverride, config.activeTrackHoverColor),
                resolveInteractiveColor(activeBase, tokens.pressedOverlay, config.progressPressedOverride, config.activeTrackPressedColor),
                state.hovered(),
                pressed
        );
        int thumbBase = resolveColorOverride(tokens.primary, config.thumbOverride, config.thumbColor);
        int thumbColor = InteractiveStyleHelper.resolveFillColor(
                thumbBase,
                resolveInteractiveColor(thumbBase, tokens.hoverOverlay, config.thumbHoverOverride, config.thumbHoverColor),
                resolveInteractiveColor(thumbBase, tokens.pressedOverlay, config.thumbPressedOverride, config.thumbPressedColor),
                state.hovered(),
                pressed
        );
        int disabledAlpha = config.disabledAlpha != 0 ? config.disabledAlpha : tokens.disabledAlpha;
        int trackStrokeColor = resolveColorOverride(tokens.border, config.trackStrokeOverride, config.trackStrokeColor);
        int thumbStrokeColor = resolveColorOverride(tokens.border, config.thumbStrokeOverride, config.thumbStrokeColor);
        int textColor = resolveColorOverride(tokens.onSurface, config.textOverride, config.textColor);
        SliderRenderStyle renderStyle = new SliderRenderStyle(
                InteractiveStyleHelper.applyDisabledAlpha(trackColor, state.enabled(), disabledAlpha),
                InteractiveStyleHelper.resolveStrokeColor(trackStrokeColor, state.enabled(), disabledAlpha),
                InteractiveStyleHelper.resolveStrokeWeight(config.trackStrokeWeight, config.trackStrokeWeightHover, state.hovered()),
                config.trackThickness,
                InteractiveStyleHelper.applyDisabledAlpha(activeTrackColor, state.enabled(), disabledAlpha),
                InteractiveStyleHelper.applyDisabledAlpha(thumbColor, state.enabled(), disabledAlpha),
                InteractiveStyleHelper.resolveStrokeColor(thumbStrokeColor, state.enabled(), disabledAlpha),
                InteractiveStyleHelper.resolveStrokeWeight(config.thumbStrokeWeight, config.thumbStrokeWeightHover, state.hovered()),
                config.thumbSize,
                InteractiveStyleHelper.resolveStrokeColor(textColor, state.enabled(), disabledAlpha),
                config.svgColorMode,
                config.thumbShape,
                config.showValueText
        );
        renderer.render(p, geometry, state, renderStyle);
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
