package com.cpz.processing.controls.textfieldcontrol.style;

import com.cpz.processing.controls.textfieldcontrol.style.interfaces.TextFieldRenderer;
import com.cpz.processing.controls.textfieldcontrol.style.interfaces.TextFieldStyle;
import com.cpz.processing.controls.textfieldcontrol.style.render.DefaultTextFieldRenderer;
import com.cpz.processing.controls.textfieldcontrol.view.TextFieldViewState;
import processing.core.PApplet;

public final class DefaultTextFieldStyle implements TextFieldStyle {

    private final TextFieldStyleConfig config;
    private final TextFieldRenderer renderer;

    public DefaultTextFieldStyle(TextFieldStyleConfig config) {
        this(config, new DefaultTextFieldRenderer());
    }

    public DefaultTextFieldStyle(TextFieldStyleConfig config, TextFieldRenderer renderer) {
        if (config == null) {
            throw new IllegalArgumentException("config must not be null");
        }
        this.config = config;
        this.renderer = renderer == null ? new DefaultTextFieldRenderer() : renderer;
    }

    @Override
    public void render(PApplet sketch, TextFieldViewState state) {
        renderer.render(sketch, state, resolveRenderStyle(state));
    }

    @Override
    public TextFieldRenderStyle resolveRenderStyle(TextFieldViewState state) {
        return new TextFieldRenderStyle(
                config.backgroundColor,
                state.focused() ? blend(config.borderColor, 0xFFFFFFFF, 0.18f) : config.borderColor,
                config.textColor,
                state.focused() ? config.cursorColor : config.borderColor,
                config.selectionColor,
                resolveSelectionTextColor(),
                config.textSize,
                config.font
        );
    }

    private int resolveSelectionTextColor() {
        return config.selectionTextColor != 0 ? config.selectionTextColor : config.textColor;
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
