package com.cpz.processing.controls.core.style;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;

import java.util.Objects;

/**
 * Applies the optional font and size owned by one control.
 *
 * <p>This helper intentionally covers only the current per-control typography
 * contract. It does not resolve theme defaults or load resources.</p>
 *
 * @author CPZ
 */
public final class TypographySupport {
    /**
     * Size used when a control supplies a font without a text size.
     */
    public static final float DEFAULT_CUSTOM_FONT_SIZE = 16.0F;

    private static final float PROCESSING_DEFAULT_TEXT_SIZE = 12.0F;

    private TypographySupport() {
    }

    /**
     * Makes a custom font restorable by a following {@code pushStyle()} call.
     *
     * <p>Processing cannot restore a {@code null} text font from its style stack.
     * When a custom font is about to be applied and the graphics context has no
     * active font yet, this method initializes Processing's default font before
     * the caller saves the style. The custom font can then be safely restored by
     * {@code popStyle()}.</p>
     *
     * @param sketch Processing sketch
     * @param font font that will be applied, or {@code null}
     */
    public static void prepareStyleScope(PApplet sketch, PFont font) {
        Objects.requireNonNull(sketch, "sketch");
        if (font == null) {
            return;
        }

        PGraphics graphics = sketch.getGraphics();
        if (graphics != null && graphics.textFont == null) {
            float activeSize = graphics.textSize > 0.0F
                    ? graphics.textSize
                    : PROCESSING_DEFAULT_TEXT_SIZE;
            sketch.textSize(activeSize);
        }
    }

    /**
     * Applies the optional typography values.
     *
     * <ul>
     *     <li>No font and no size: preserves the active Processing state.</li>
     *     <li>No font and a size: changes only the active size.</li>
     *     <li>A font and a size: applies both values.</li>
     *     <li>A font without a size: applies the font at
     *     {@link #DEFAULT_CUSTOM_FONT_SIZE}.</li>
     * </ul>
     *
     * @param sketch Processing sketch
     * @param font optional control font
     * @param textSize optional control text size
     */
    public static void apply(PApplet sketch, PFont font, Float textSize) {
        Objects.requireNonNull(sketch, "sketch");
        if (font != null) {
            sketch.textFont(font, textSize != null ? textSize : DEFAULT_CUSTOM_FONT_SIZE);
        } else if (textSize != null) {
            sketch.textSize(textSize);
        }
    }
}
