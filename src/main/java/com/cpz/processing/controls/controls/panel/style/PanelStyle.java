package com.cpz.processing.controls.controls.panel.style;

import com.cpz.processing.controls.controls.panel.style.render.DefaultPanelRenderer;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeProvider;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import processing.core.PApplet;

/**
 * Mutable runtime style for {@code Panel} visual chrome.
 *
 * <p>Background and stroke are hidden by default, preserving transparent panel
 * behavior. The default stroke weight is {@value #DEFAULT_STROKE_WEIGHT} and
 * the default corner radius is {@value #DEFAULT_CORNER_RADIUS}.</p>
 *
 * <p>Color getters return effective colors. If a color was not explicitly
 * configured, the current theme tokens are used: {@code surface} for the
 * background and {@code border} for the stroke. This keeps unset colors
 * responsive to theme changes.</p>
 *
 * <p>Negative stroke weight and corner radius values are normalized to
 * {@code 0.0f}. {@code NaN} and infinite values are rejected.</p>
 *
 * @author CPZ
 */
public final class PanelStyle {
    public static final boolean DEFAULT_BACKGROUND_VISIBLE = false;
    public static final boolean DEFAULT_STROKE_VISIBLE = false;
    public static final float DEFAULT_STROKE_WEIGHT = 1.0F;
    public static final float DEFAULT_CORNER_RADIUS = 0.0F;

    private Integer backgroundColor;
    private boolean backgroundVisible = DEFAULT_BACKGROUND_VISIBLE;
    private Integer strokeColor;
    private boolean strokeVisible = DEFAULT_STROKE_VISIBLE;
    private float strokeWeight = DEFAULT_STROKE_WEIGHT;
    private float cornerRadius = DEFAULT_CORNER_RADIUS;
    private ThemeProvider themeProvider;
    private final DefaultPanelRenderer renderer;

    public PanelStyle() {
        this((ThemeProvider) null, new DefaultPanelRenderer());
    }

    public PanelStyle(PanelStyle source) {
        this(source, source == null ? new DefaultPanelRenderer() : source.renderer);
    }

    public PanelStyle(ThemeProvider themeProvider) {
        this(themeProvider, new DefaultPanelRenderer());
    }

    PanelStyle(ThemeProvider themeProvider, DefaultPanelRenderer renderer) {
        this.themeProvider = themeProvider;
        this.renderer = renderer == null ? new DefaultPanelRenderer() : renderer;
    }

    private PanelStyle(PanelStyle source, DefaultPanelRenderer renderer) {
        this(source == null ? null : source.themeProvider, renderer);
        if (source != null) {
            this.backgroundColor = source.backgroundColor;
            this.backgroundVisible = source.backgroundVisible;
            this.strokeColor = source.strokeColor;
            this.strokeVisible = source.strokeVisible;
            this.strokeWeight = source.strokeWeight;
            this.cornerRadius = source.cornerRadius;
        }
    }

    public int getBackgroundColor() {
        return this.backgroundColor != null ? this.backgroundColor : this.currentTokens().surface;
    }

    /**
     * Sets an explicit background color.
     *
     * @param color ARGB color using the same integer color format as the rest
     *              of the library
     * @return this style instance
     */
    public PanelStyle setBackgroundColor(int color) {
        this.backgroundColor = color;
        return this;
    }

    public boolean isBackgroundVisible() {
        return this.backgroundVisible;
    }

    public PanelStyle setBackgroundVisible(boolean visible) {
        this.backgroundVisible = visible;
        return this;
    }

    public int getStrokeColor() {
        return this.strokeColor != null ? this.strokeColor : this.currentTokens().border;
    }

    /**
     * Sets an explicit stroke color.
     *
     * @param color ARGB color using the same integer color format as the rest
     *              of the library
     * @return this style instance
     */
    public PanelStyle setStrokeColor(int color) {
        this.strokeColor = color;
        return this;
    }

    public boolean isStrokeVisible() {
        return this.strokeVisible;
    }

    public PanelStyle setStrokeVisible(boolean visible) {
        this.strokeVisible = visible;
        return this;
    }

    public float getStrokeWeight() {
        return this.strokeWeight;
    }

    public PanelStyle setStrokeWeight(float weight) {
        this.strokeWeight = finiteNonNegative(weight, "strokeWeight");
        return this;
    }

    public float getCornerRadius() {
        return this.cornerRadius;
    }

    public PanelStyle setCornerRadius(float radius) {
        this.cornerRadius = finiteNonNegative(radius, "cornerRadius");
        return this;
    }

    public ThemeProvider getThemeProvider() {
        return this.themeProvider;
    }

    public PanelStyle setThemeProvider(ThemeProvider themeProvider) {
        this.themeProvider = themeProvider;
        return this;
    }

    public PanelStyle copy() {
        return new PanelStyle(this);
    }

    public PanelRenderStyle resolveRenderStyle() {
        return new PanelRenderStyle(
                this.getBackgroundColor(),
                this.backgroundVisible,
                this.getStrokeColor(),
                this.strokeVisible,
                this.strokeWeight,
                this.cornerRadius
        );
    }

    public void render(PApplet sketch, float x, float y, float width, float height) {
        this.renderer.render(sketch, x, y, width, height, this.resolveRenderStyle());
    }

    private ThemeTokens currentTokens() {
        ThemeProvider provider = this.themeProvider != null ? this.themeProvider : DefaultThemeProviderHolder.DEFAULT;
        ThemeSnapshot snapshot = provider.getSnapshot();
        return snapshot.tokens;
    }

    private static float finiteNonNegative(float value, String name) {
        if (!Float.isFinite(value)) {
            throw new IllegalArgumentException(name + " must be finite.");
        }
        return Math.max(0.0F, value);
    }

    private static final class DefaultThemeProviderHolder {
        private static final ThemeProvider DEFAULT = new ThemeManager();
    }
}
