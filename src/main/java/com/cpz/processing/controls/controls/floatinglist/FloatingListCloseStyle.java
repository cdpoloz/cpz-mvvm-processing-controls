package com.cpz.processing.controls.controls.floatinglist;

import com.cpz.processing.controls.controls.button.state.ButtonViewState;
import com.cpz.processing.controls.controls.button.style.ButtonRenderStyle;
import com.cpz.processing.controls.controls.button.style.ButtonStyle;
import com.cpz.processing.controls.controls.button.style.render.DefaultButtonRenderer;
import com.cpz.processing.controls.core.style.InteractiveStyleHelper;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.util.FontLoader;
import processing.core.PApplet;
import processing.core.PFont;

import static com.cpz.processing.controls.controls.floatinglist.FloatingListStyle.color;
import static com.cpz.processing.controls.controls.floatinglist.FloatingListStyle.pixels;

/** Adapts the private close button's existing interaction state to floating list chrome. */
final class FloatingListCloseStyle implements ButtonStyle {
    private final DefaultButtonRenderer renderer = new DefaultButtonRenderer();
    private final ThemeManager theme = new ThemeManager();
    private FloatingListStyle.CloseButton style;
    private FontLoader.FontResolver fontResolver;
    private boolean hovered;

    void update(FloatingListStyle.CloseButton style, FontLoader.FontResolver fontResolver, boolean hovered) {
        this.style = style;
        this.fontResolver = fontResolver;
        this.hovered = hovered;
    }

    @Override
    public ThemeSnapshot getThemeSnapshot() {
        return this.theme.getSnapshot();
    }

    @Override
    public void render(PApplet sketch, ButtonViewState state, ThemeSnapshot snapshot) {
        var tokens = snapshot.tokens;
        int base = color(this.style.backgroundColor(), tokens.primary);
        int hover = color(this.style.hoverColor(), InteractiveStyleHelper.applyOverlay(base, tokens.hoverOverlay));
        int pressed = color(this.style.pressedColor(), InteractiveStyleHelper.applyOverlay(base, tokens.pressedOverlay));
        int fill = InteractiveStyleHelper.resolveFillColor(base, hover, pressed, this.hovered, state.pressed());
        float edge = Math.min(state.width(), state.height());
        float stroke = pixels(this.hovered ? this.style.hoverStrokeWeight() : this.style.strokeWeight(),
                sketch.height, edge);
        float radius = pixels(this.style.cornerRadius(), sketch.height, edge / 2);
        float size = pixels(this.style.textSize(), sketch.height, state.height());
        PFont font = FontLoader.resolve(this.fontResolver, sketch, size, this.style.font());
        this.renderer.render(sketch, state.x(), state.y(), state.width(), state.height(),
                new ButtonRenderStyle(fill, stroke > 0 ? color(this.style.borderColor(), tokens.border) : 0x00FFFFFF,
                        stroke, color(this.style.textColor(), tokens.onPrimary), radius, state.showText(),
                        state.text(), font, size));
    }
}
