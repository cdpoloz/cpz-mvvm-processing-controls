package com.cpz.processing.controls.controls.label;

import com.cpz.processing.controls.controls.label.config.LabelConfig;
import com.cpz.processing.controls.controls.label.config.LabelStyleConfig;
import com.cpz.processing.controls.controls.label.style.DefaultLabelStyle;
import com.cpz.processing.controls.controls.label.style.HorizontalAlign;
import com.cpz.processing.controls.controls.label.style.VerticalAlign;
import processing.core.PApplet;
import processing.core.PFont;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Factory for creating the public label facade from external config.
 *
 * @author CPZ
 */
public final class LabelFactory {
    public static Label create(PApplet sketch, LabelConfig config) {
        Objects.requireNonNull(sketch, "sketch");
        Objects.requireNonNull(config, "config");

        Label label = new Label(
                sketch,
                config.getCode(),
                config.getText(),
                config.getX(),
                config.getY(),
                config.getWidth(),
                config.getHeight()
        );
        label.setEnabled(config.isEnabled());
        label.setVisible(config.isVisible());

        if (config.getStyle() != null) {
            label.setStyle(new DefaultLabelStyle(toStyleConfig(sketch, config.getStyle())));
        }

        return label;
    }

    private static LabelStyleConfig toStyleConfig(PApplet sketch, LabelConfig.StyleConfig style) {
        LabelStyleConfig result = new LabelStyleConfig();
        if (style.getTextSize() != null) {
            result.textSize = style.getTextSize();
        }
        if (style.getFontPath() != null) {
            result.font = loadFont(sketch, style.getFontPath(), result.textSize, style.getSourcePath());
        }
        result.textColor = style.getTextColor();
        if (style.getLineSpacingMultiplier() != null) {
            result.lineSpacingMultiplier = style.getLineSpacingMultiplier();
        }
        result.alignX = style.getAlignX() != null ? style.getAlignX() : HorizontalAlign.START;
        result.alignY = style.getAlignY() != null ? style.getAlignY() : VerticalAlign.BASELINE;
        result.disabledAlpha = style.getDisabledAlpha();
        return result;
    }

    private static PFont loadFont(PApplet sketch, String path, float textSize, String sourcePath) {
        if (!fontExists(sketch, path)) {
            throw new IllegalArgumentException("Could not load label font '" + path + "' referenced by JSON config: " + sourcePath);
        }

        try {
            PFont font = sketch.createFont(path, textSize);
            if (font == null) {
                throw new IllegalArgumentException("Could not load label font '" + path + "' referenced by JSON config: " + sourcePath);
            }
            return font;
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Could not load label font '" + path + "' referenced by JSON config: " + sourcePath, ex);
        }
    }

    private static boolean fontExists(PApplet sketch, String path) {
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
            // Closing a read probe should not hide a successful resource lookup.
        }
        return true;
    }
}
