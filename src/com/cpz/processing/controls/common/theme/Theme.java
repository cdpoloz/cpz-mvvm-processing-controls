package com.cpz.processing.controls.common.theme;

public class Theme {

    private final ThemeTokens tokens;

    public Theme(ThemeTokens tokens) {
        if (tokens == null) {
            throw new IllegalArgumentException("tokens must not be null");
        }
        this.tokens = tokens;
    }

    public ThemeTokens tokens() {
        return tokens;
    }
}
