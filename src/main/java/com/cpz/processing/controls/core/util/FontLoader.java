package com.cpz.processing.controls.core.util;

import processing.core.PApplet;
import processing.core.PFont;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Loads Processing fonts for config-driven controls.
 *
 * <p>This utility materializes an already validated font path. It does not
 * interpret JSON or resolve themes. Callers can either load one font directly or
 * create a bounded resolver that caches fonts by effective render size.</p>
 *
 * @author CPZ
 */
public final class FontLoader {
    private static final int MAX_CACHED_SIZES_PER_RESOLVER = 32;

    private FontLoader() {
    }

    /**
     * Creates a bounded resolver for a JSON font path.
     *
     * <p>The resolver creates a {@link PFont} for the effective render size and
     * reuses it on later frames. Sizes are rounded to whole pixels before they are
     * used as cache keys so tiny floating-point changes from relative layout do not
     * create unbounded font instances.</p>
     *
     * @param path font resource path
     * @param controlName control name used in diagnostics
     * @param sourcePath JSON source path, or {@code null} when unavailable
     * @return bounded font resolver
     */
    public static FontResolver resolver(String path, String controlName, String sourcePath) {
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(controlName, "controlName");
        return new CachedFontResolver(path, controlName, sourcePath);
    }

    /**
     * Resolves a configured font through the supplied resolver.
     *
     * @param resolver font resolver, or {@code null} when no JSON font exists
     * @param sketch Processing sketch used to create missing font sizes
     * @param effectiveTextSize resolved text size used for rendering
     * @param fallback current font when no resolver is configured
     * @return resolved font or fallback
     */
    public static PFont resolve(
            FontResolver resolver,
            PApplet sketch,
            float effectiveTextSize,
            PFont fallback
    ) {
        return resolver == null ? fallback : resolver.load(sketch, effectiveTextSize);
    }

    /**
     * Loads a font through the supplied sketch.
     *
     * @param sketch Processing sketch used to resolve and create the font
     * @param path font resource path
     * @param creationSize size used when creating the {@link PFont}
     * @param controlName control name used in diagnostics
     * @param sourcePath JSON source path, or {@code null} when unavailable
     * @return loaded font
     * @throws IllegalArgumentException when the resource cannot be found or created
     */
    public static PFont load(
            PApplet sketch,
            String path,
            float creationSize,
            String controlName,
            String sourcePath
    ) {
        Objects.requireNonNull(sketch, "sketch");
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(controlName, "controlName");

        String normalizedPath = path.trim();
        if (normalizedPath.isEmpty()) {
            throw failure(controlName, path, sourcePath, "the path is blank", null);
        }

        boolean resourceAvailable;
        try {
            resourceAvailable = resourceExists(sketch, normalizedPath);
        } catch (RuntimeException ex) {
            throw failure(controlName, normalizedPath, sourcePath, "Processing failed while resolving the font resource", ex);
        }
        if (!resourceAvailable) {
            throw failure(controlName, normalizedPath, sourcePath, "the resource does not exist or is not readable", null);
        }

        PFont font;
        try {
            font = sketch.createFont(normalizedPath, creationSize);
        } catch (RuntimeException ex) {
            throw failure(controlName, normalizedPath, sourcePath, "Processing failed while creating the font", ex);
        }
        if (font == null) {
            throw failure(controlName, normalizedPath, sourcePath, "Processing could not create a font from the resource", null);
        }
        return font;
    }

    private static float normalizedCreationSize(float effectiveTextSize) {
        if (!Float.isFinite(effectiveTextSize) || effectiveTextSize <= 0.0F) {
            return 1.0F;
        }
        return Math.max(1.0F, Math.round(effectiveTextSize));
    }

    private static boolean resourceExists(PApplet sketch, String path) {
        InputStream input = sketch.createInput(path);
        if (input == null && path.startsWith("data/")) {
            input = sketch.createInput(path.substring("data/".length()));
        }
        if (input == null) {
            return false;
        }

        try {
            input.close();
        } catch (IOException ignored) {
            // A failed close must not hide a successful resource lookup.
        }
        return true;
    }

    private static IllegalArgumentException failure(
            String controlName,
            String fontPath,
            String sourcePath,
            String cause,
            RuntimeException exception
    ) {
        String source = sourcePath == null || sourcePath.isBlank()
                ? ""
                : " in JSON source '" + sourcePath + "'";
        String message = "Could not load " + controlName + " style property 'font'" + source
                + " from path '" + fontPath + "': " + cause + ".";
        return exception == null
                ? new IllegalArgumentException(message)
                : new IllegalArgumentException(message, exception);
    }

    /**
     * Lazily loads fonts for an effective render size.
     */
    @FunctionalInterface
    public interface FontResolver {
        /**
         * Returns a font created for the effective render size.
         *
         * @param sketch Processing sketch used to create missing font sizes
         * @param effectiveTextSize resolved text size used for rendering
         * @return font created for the normalized render size
         */
        PFont load(PApplet sketch, float effectiveTextSize);
    }

    private static final class CachedFontResolver implements FontResolver {
        private final String path;
        private final String controlName;
        private final String sourcePath;
        private final Map<Float, PFont> fontsBySize = new LinkedHashMap<>(16, 0.75F, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Float, PFont> eldest) {
                return this.size() > MAX_CACHED_SIZES_PER_RESOLVER;
            }
        };

        private CachedFontResolver(String path, String controlName, String sourcePath) {
            this.path = path;
            this.controlName = controlName;
            this.sourcePath = sourcePath;
        }

        @Override
        public synchronized PFont load(PApplet sketch, float effectiveTextSize) {
            float creationSize = normalizedCreationSize(effectiveTextSize);
            PFont cached = this.fontsBySize.get(creationSize);
            if (cached != null) {
                return cached;
            }

            PFont font = FontLoader.load(
                    sketch,
                    this.path,
                    creationSize,
                    this.controlName,
                    this.sourcePath
            );
            this.fontsBySize.put(creationSize, font);
            return font;
        }
    }
}
