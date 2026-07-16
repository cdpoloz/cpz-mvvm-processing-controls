package com.cpz.processing.controls.controls.indicator.style;

import java.util.Locale;

/**
 * Visual style for the non-interactive indicator facade.
 *
 * @author CPZ
 */
public final class IndicatorStyle {
    public static final int DEFAULT_ON_COLOR = 0xFF2ECC71;
    public static final int DEFAULT_OFF_COLOR = 0xFF30343A;
    public static final int DEFAULT_STROKE_COLOR = 0xFF1F2328;
    public static final String RENDERER_SVG = "svg";
    public static final String RENDERER_PNG = "png";

    private int onColor = DEFAULT_ON_COLOR;
    private int offColor = DEFAULT_OFF_COLOR;
    private int strokeColor = DEFAULT_STROKE_COLOR;
    private float strokeWeight = 1.0F;
    private String rendererType;
    private String rendererPath;

    public IndicatorStyle() {
    }

    public IndicatorStyle(IndicatorStyle source) {
        if (source != null) {
            this.onColor = source.onColor;
            this.offColor = source.offColor;
            this.strokeColor = source.strokeColor;
            this.strokeWeight = source.strokeWeight;
            this.rendererType = source.rendererType;
            this.rendererPath = source.rendererPath;
        }
    }

    public int getOnColor() {
        return this.onColor;
    }

    public IndicatorStyle setOnColor(int color) {
        this.onColor = color;
        return this;
    }

    public int getOffColor() {
        return this.offColor;
    }

    public IndicatorStyle setOffColor(int color) {
        this.offColor = color;
        return this;
    }

    public int getStrokeColor() {
        return this.strokeColor;
    }

    public IndicatorStyle setStrokeColor(int color) {
        this.strokeColor = color;
        return this;
    }

    public float getStrokeWeight() {
        return this.strokeWeight;
    }

    public IndicatorStyle setStrokeWeight(float weight) {
        this.strokeWeight = Math.max(0.0F, weight);
        return this;
    }

    public String getRendererType() {
        return this.rendererType;
    }

    public String getRendererPath() {
        return this.rendererPath;
    }

    /**
     * Configures an indicator graphic renderer.
     *
     * <p>Supported renderer types are {@code svg} and {@code png}. PNG images
     * are interpreted as alpha masks and recolored by the indicator state.</p>
     *
     * @param type renderer type, case-insensitive
     * @param path non-empty Processing resource path
     * @return this style
     * @throws IllegalArgumentException when type, path, or path extension is unsupported
     */
    public IndicatorStyle setRenderer(String type, String path) {
        this.rendererType = normalizeRendererType(type, path);
        this.rendererPath = normalizeRendererPath(path);
        return this;
    }

    /**
     * Configures an indicator graphic renderer by inferring the renderer type
     * from the resource path extension.
     *
     * @param path non-empty Processing resource path ending in {@code .svg} or {@code .png}
     * @return this style
     */
    public IndicatorStyle setRenderer(String path) {
        return this.setRenderer(inferRendererType(path), path);
    }

    /**
     * Clears the configured graphic renderer so the indicator uses the default
     * circular rendering path.
     *
     * @return this style
     */
    public IndicatorStyle clearRenderer() {
        this.rendererType = null;
        this.rendererPath = null;
        return this;
    }

    public boolean isSvgRenderer() {
        return RENDERER_SVG.equals(this.rendererType);
    }

    public boolean isPngRenderer() {
        return RENDERER_PNG.equals(this.rendererType);
    }

    public boolean hasRenderer() {
        return this.rendererType != null;
    }

    public static String inferRendererType(String path) {
        String normalizedPath = normalizeRendererPath(path);
        String lowerPath = normalizedPath.toLowerCase(Locale.ROOT);
        if (lowerPath.endsWith(".svg")) {
            return RENDERER_SVG;
        }
        if (lowerPath.endsWith(".png")) {
            return RENDERER_PNG;
        }
        throw new IllegalArgumentException(
                "Unsupported indicator renderer path: " + normalizedPath + ". Supported file extensions: .svg, .png."
        );
    }

    public static String normalizeRendererType(String type, String path) {
        if (type == null) {
            throw new IllegalArgumentException("Indicator renderer type must not be null.");
        }
        String normalizedType = type.trim().toLowerCase(Locale.ROOT);
        if (!RENDERER_SVG.equals(normalizedType) && !RENDERER_PNG.equals(normalizedType)) {
            throw new IllegalArgumentException(
                    "Unsupported indicator renderer type: " + type + ". Supported values: svg, png."
            );
        }

        String inferredType = inferRendererType(path);
        if (!normalizedType.equals(inferredType)) {
            throw new IllegalArgumentException(
                    "Indicator renderer type '" + normalizedType + "' does not match resource extension for path: "
                            + normalizeRendererPath(path) + "."
            );
        }
        return normalizedType;
    }

    public static String normalizeRendererPath(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Indicator renderer path must not be null.");
        }
        String normalizedPath = path.trim();
        if (normalizedPath.isEmpty()) {
            throw new IllegalArgumentException("Indicator renderer path must not be empty.");
        }
        return normalizedPath;
    }
}
