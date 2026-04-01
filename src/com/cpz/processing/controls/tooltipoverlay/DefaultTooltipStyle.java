package com.cpz.processing.controls.tooltipoverlay;

import com.cpz.processing.controls.common.style.InteractiveStyleHelper;
import com.cpz.processing.controls.common.theme.ThemeManager;
import com.cpz.processing.controls.common.theme.ThemeTokens;
import com.cpz.processing.controls.util.Colors;
import processing.core.PApplet;

public final class DefaultTooltipStyle {

    private final TooltipStyleConfig config;
    private final DefaultTooltipRenderer renderer;

    public DefaultTooltipStyle(TooltipStyleConfig config) {
        this(config, new DefaultTooltipRenderer());
    }

    public DefaultTooltipStyle(TooltipStyleConfig config, DefaultTooltipRenderer renderer) {
        this.config = config == null ? new TooltipStyleConfig() : config;
        this.renderer = renderer == null ? new DefaultTooltipRenderer() : renderer;
    }

    public void render(PApplet sketch, TooltipViewState state) {
        renderer.render(sketch, state, resolveRenderStyle());
    }

    public TooltipRenderStyle resolveRenderStyle() {
        ThemeTokens tokens = ThemeManager.getTheme().tokens();
        int background = config.backgroundOverride != null
                ? config.backgroundOverride
                : InteractiveStyleHelper.applyOverlay(tokens.surfaceVariant, Colors.argb(220, 0, 0, 0));
        int textColor = config.textOverride != null ? config.textOverride : tokens.onSurface;
        int strokeColor = config.borderOverride != null ? config.borderOverride : tokens.border;
        return new TooltipRenderStyle(
                background,
                textColor,
                strokeColor,
                1f,
                config.textSize,
                config.textPadding,
                config.cornerRadius,
                config.minHeight,
                config.font
        );
    }

    public float getTextPadding() {
        return config.textPadding;
    }
}
