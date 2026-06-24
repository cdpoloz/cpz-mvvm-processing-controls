package com.cpz.processing.controls.core.util;

import processing.core.PApplet;
import processing.core.PFont;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Loads Processing fonts for config-driven controls.
 *
 * <p>This utility materializes an already validated font path. It does not
 * interpret JSON, resolve themes, cache resources, or load fonts during
 * rendering.</p>
 *
 * @author CPZ
 */
public final class FontLoader {
    private FontLoader() {
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
}
