package com.cpz.processing.controls.common.theme;

public final class ThemeManager {

    private static Theme currentTheme = new LightTheme();

    private ThemeManager() {
    }

    public static Theme getTheme() {
        return currentTheme;
    }

    public static void setTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("Theme null");
        }
        ThemeManager.currentTheme = theme;
    }
}
