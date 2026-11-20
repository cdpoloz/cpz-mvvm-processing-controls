package com.cpz.processing.controls.controls.floatinglist;

import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import processing.core.PFont;

import java.util.Objects;

import static com.cpz.processing.controls.controls.geometry.ControlMeasure.absolute;

/** Immutable chrome and typography for a floating list. Relative measures use the canvas height. */
public record FloatingListStyle(Frame frame, Integer headerColor, Text title, Text empty,
                                Scrollbar scrollbar, CloseButton closeButton) {
    public FloatingListStyle {
        Objects.requireNonNull(frame, "frame");
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(empty, "empty");
        Objects.requireNonNull(scrollbar, "scrollbar");
        Objects.requireNonNull(closeButton, "closeButton");
    }

    /** Uses the original panel colors and the default button theme. */
    public static FloatingListStyle defaults() {
        return new FloatingListStyle(
                new Frame(0xFF202832, null, absolute(1), absolute(0)), null,
                new Text(0xFFF4F6F8, null, absolute(16), absolute(8), absolute(4)),
                new Text(0xFFF4F6F8, null, absolute(14), absolute(8), absolute(4)),
                new Scrollbar(0xFF151A20, 0xFF94A8B7, null, null, absolute(0)),
                new CloseButton(null, null, null, null, absolute(2), absolute(3), absolute(10),
                        null, null, absolute(14)));
    }

    /** A null background or border color disables that part of the frame. */
    public record Frame(Integer backgroundColor, Integer borderColor, ControlMeasure strokeWeight,
                        ControlMeasure cornerRadius) {
        public Frame {
            nonNegative(strokeWeight, "frame.strokeWeight");
            nonNegative(cornerRadius, "frame.cornerRadius");
        }
    }

    /** A null font preserves the Processing font in effect when the panel is drawn. */
    public record Text(int color, PFont font, ControlMeasure size, ControlMeasure paddingLeft,
                       ControlMeasure paddingRight) {
        public Text {
            positive(size, "text.size");
            nonNegative(paddingLeft, "text.paddingLeft");
            nonNegative(paddingRight, "text.paddingRight");
        }
    }

    /** Null hover and pressed colors use the normal thumb color. */
    public record Scrollbar(int trackColor, int thumbColor, Integer hoverColor, Integer pressedColor,
                            ControlMeasure cornerRadius) {
        public Scrollbar {
            nonNegative(cornerRadius, "scrollbar.cornerRadius");
        }
    }

    /** Null color overrides use the default button theme, including its interaction overlays. */
    public record CloseButton(Integer backgroundColor, Integer hoverColor, Integer pressedColor,
                              Integer borderColor, ControlMeasure strokeWeight, ControlMeasure hoverStrokeWeight,
                              ControlMeasure cornerRadius, Integer textColor, PFont font, ControlMeasure textSize) {
        public CloseButton {
            nonNegative(strokeWeight, "closeButton.strokeWeight");
            nonNegative(hoverStrokeWeight, "closeButton.hoverStrokeWeight");
            nonNegative(cornerRadius, "closeButton.cornerRadius");
            positive(textSize, "closeButton.textSize");
        }
    }

    static void positive(ControlMeasure measure, String name) {
        if (Objects.requireNonNull(measure, name).value() <= 0) {
            throw new IllegalArgumentException(name + " must be positive");
        }
    }

    static void nonNegative(ControlMeasure measure, String name) {
        if (Objects.requireNonNull(measure, name).value() < 0) {
            throw new IllegalArgumentException(name + " must be nonnegative");
        }
    }

    /** Resolves once and saturates relative overflow before handing pixels to Processing. */
    static float pixels(ControlMeasure measure, int canvasHeight, float limit) {
        return Math.min(limit, measure.resolve(canvasHeight));
    }

    static int color(Integer configured, int fallback) {
        return configured == null ? fallback : configured;
    }
}
