package com.cpz.processing.controls.common.theme;

public final class ThemeManager {

    private static Theme theme = new Theme();

    private ThemeManager() {
    }

    public static Theme getTheme() {
        return theme;
    }

    public static void setTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("Theme null");
        }
        ThemeManager.theme = theme;
    }
}
