package com.cpz.processing.controls.common.theme;

import com.cpz.processing.controls.util.Colors;

public final class LightTheme extends Theme {

    public LightTheme() {
        super(createTokens());
    }

    private static ThemeTokens createTokens() {
        ThemeTokens tokens = new ThemeTokens();
        tokens.surface = Colors.rgb(245, 247, 250);
        tokens.surfaceVariant = Colors.rgb(230, 235, 241);
        tokens.onSurface = Colors.rgb(28, 36, 46);
        tokens.onSurfaceVariant = Colors.rgb(86, 98, 112);
        tokens.primary = Colors.rgb(54, 122, 224);
        tokens.onPrimary = Colors.gray(255);
        tokens.border = Colors.rgb(154, 167, 184);
        tokens.hoverOverlay = Colors.argb(28, 255, 255, 255);
        tokens.pressedOverlay = Colors.argb(40, 0, 0, 0);
        tokens.disabledAlpha = 120;
        tokens.cursor = Colors.rgb(32, 102, 210);
        tokens.selection = Colors.argb(110, 89, 156, 255);
        return tokens;
    }
}
