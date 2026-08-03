package com.cpz.processing.controls.controls.button.style.render;

import com.cpz.processing.controls.controls.button.style.ButtonRenderStyle;
import com.cpz.processing.controls.controls.button.config.ButtonStyleConfig;
import com.cpz.processing.controls.controls.button.state.ButtonViewState;
import com.cpz.processing.controls.controls.button.style.DefaultButtonStyle;
import com.cpz.utils.color.Colors;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PngButtonRendererTest {
    @Test
    void validPngLoadsOnceAndDrawsAColoredMask() {
        RecordingApplet sketch = new RecordingApplet();
        sketch.loadedImage = image(2, 4, 0xFF123456);
        PngButtonRenderer renderer = new PngButtonRenderer(sketch, "data/img/button-mask.png");

        renderer.render(sketch, 60.0F, 70.0F, 40.0F, 40.0F, style(0xFF00AA00, false));
        renderer.render(sketch, 60.0F, 70.0F, 40.0F, 40.0F, style(0xFF00AA00, false));

        assertEquals(1, sketch.loadImageCalls);
        assertEquals("data/img/button-mask.png", sketch.lastLoadImagePath);
        assertEquals(2, sketch.imageCalls);
        assertEquals(0xFF00AA00, sketch.lastTintColor);
        assertEquals(50.0F, sketch.lastImageX);
        assertEquals(50.0F, sketch.lastImageY);
        assertEquals(20.0F, sketch.lastImageWidth);
        assertEquals(40.0F, sketch.lastImageHeight);
    }

    @Test
    void normalizationPreservesAlphaDiscardsRgbAndDoesNotModifySource() {
        PImage source = new PImage(2, 2, PApplet.ARGB);
        source.loadPixels();
        source.pixels[0] = 0x00000000;
        source.pixels[1] = 0x80112233;
        source.pixels[2] = 0xFFFF0000;
        source.pixels[3] = 0x4000FF00;
        source.updatePixels();
        int[] originalPixels = source.pixels.clone();

        PImage normalized = PngButtonRenderer.normalizePngMask(source);

        assertNotSame(source, normalized);
        normalized.loadPixels();
        assertArrayEquals(originalPixels, source.pixels);
        assertArrayEquals(
                new int[]{0x00FFFFFF, 0x80FFFFFF, 0xFFFFFFFF, 0x40FFFFFF},
                normalized.pixels
        );
    }

    @Test
    void pngExampleResourceIsValidAndContainsAUsefulAlphaMask() throws IOException {
        BufferedImage image = ImageIO.read(Path.of("data/img/button-mask.png").toFile());

        assertTrue(image != null && image.getWidth() > 0 && image.getHeight() > 0);
        boolean hasTransparent = false;
        boolean hasSemiTransparent = false;
        boolean hasOpaque = false;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int alpha = image.getRGB(x, y) >>> 24;
                hasTransparent |= alpha == 0;
                hasSemiTransparent |= alpha > 0 && alpha < 255;
                hasOpaque |= alpha == 255;
            }
        }

        assertTrue(hasTransparent);
        assertTrue(hasSemiTransparent);
        assertTrue(hasOpaque);
    }

    @Test
    void resolvedNormalHoverPressedAndDisabledColorsAreUsedDirectlyAsTint() {
        RecordingApplet sketch = new RecordingApplet();
        sketch.loadedImage = image(2, 2, 0xFFFFFFFF);
        PngButtonRenderer renderer = new PngButtonRenderer(sketch, "button-mask.png");
        int[] colors = {0xFF3062DB, 0xFF4876E0, 0xFF2449A4, 0x5A3062DB};

        for (int color : colors) {
            renderer.render(sketch, 20.0F, 20.0F, 20.0F, 20.0F, style(color, false));
        }

        assertEquals(List.of(0xFF3062DB, 0xFF4876E0, 0xFF2449A4, 0x5A3062DB), sketch.tintColors);
        assertEquals(4, sketch.noTintCalls);
    }

    @Test
    void buttonStyleStatesResolveToThePngTintWithoutAParallelPalette() {
        RecordingApplet sketch = new RecordingApplet();
        sketch.loadedImage = image(2, 2, 0xFFFFFFFF);
        ButtonStyleConfig config = new ButtonStyleConfig();
        config.baseColor = 0xFF3062DB;
        config.hoverBlendWithWhite = 0.12F;
        config.pressedBlendWithBlack = 0.25F;
        config.disabledAlpha = 90;
        config.setRenderer(new PngButtonRenderer(sketch, "button-mask.png"));
        DefaultButtonStyle style = new DefaultButtonStyle(config);

        style.render(sketch, state(true, false, false), style.getThemeSnapshot());
        style.render(sketch, state(true, true, false), style.getThemeSnapshot());
        style.render(sketch, state(true, true, true), style.getThemeSnapshot());
        style.render(sketch, state(false, false, false), style.getThemeSnapshot());

        assertEquals(List.of(
                0xFF3062DB,
                sketch.lerpColor(0xFF3062DB, sketch.color(255), 0.12F),
                sketch.lerpColor(0xFF3062DB, sketch.color(0), 0.25F),
                Colors.alpha(90, 0xFF3062DB)
        ), sketch.tintColors);
    }

    @Test
    void horizontalPngPreservesAspectRatioAndCentersInsideButtonBounds() {
        RecordingApplet sketch = new RecordingApplet();
        sketch.loadedImage = image(4, 2, 0xFFFFFFFF);
        PngButtonRenderer renderer = new PngButtonRenderer(sketch, "button-mask.png");

        renderer.render(sketch, 30.0F, 40.0F, 40.0F, 40.0F, style(0xFFFFFFFF, false));

        assertEquals(10.0F, sketch.lastImageX);
        assertEquals(30.0F, sketch.lastImageY);
        assertEquals(40.0F, sketch.lastImageWidth);
        assertEquals(20.0F, sketch.lastImageHeight);
    }

    @Test
    void nullAndEmptyPathsCreateInertRenderers() {
        RecordingApplet sketch = new RecordingApplet();

        new PngButtonRenderer(sketch, null)
                .render(sketch, 20.0F, 20.0F, 20.0F, 20.0F, style(0xFFFFFFFF, false));
        new PngButtonRenderer(sketch, "")
                .render(sketch, 20.0F, 20.0F, 20.0F, 20.0F, style(0xFFFFFFFF, false));

        assertEquals(0, sketch.loadImageCalls);
        assertEquals(0, sketch.imageCalls);
        assertEquals(0, sketch.tintCalls);
    }

    @Test
    void missingDataPngRetriesWithoutDataPrefixAndRemainsInert() {
        RecordingApplet sketch = new RecordingApplet();
        sketch.returnNullImage = true;
        PngButtonRenderer renderer = new PngButtonRenderer(sketch, "data/img/missing.png");

        renderer.render(sketch, 20.0F, 20.0F, 20.0F, 20.0F, style(0xFFFFFFFF, false));

        assertEquals(2, sketch.loadImageCalls);
        assertEquals("img/missing.png", sketch.lastLoadImagePath);
        assertEquals(0, sketch.imageCalls);
    }

    @Test
    void invalidPngDimensionsRemainInert() {
        RecordingApplet sketch = new RecordingApplet();
        sketch.loadedImage = new PImage(0, 2, PApplet.ARGB);
        PngButtonRenderer renderer = new PngButtonRenderer(sketch, "invalid.png");

        renderer.render(sketch, 20.0F, 20.0F, 20.0F, 20.0F, style(0xFFFFFFFF, false));

        assertEquals(1, sketch.loadImageCalls);
        assertEquals(0, sketch.imageCalls);
    }

    @Test
    void tintIsClearedBeforeTextIsDrawn() {
        RecordingApplet sketch = new RecordingApplet();
        sketch.loadedImage = image(2, 2, 0xFFFFFFFF);
        PngButtonRenderer renderer = new PngButtonRenderer(sketch, "button-mask.png");

        renderer.render(sketch, 20.0F, 30.0F, 40.0F, 20.0F,
                new ButtonRenderStyle(0xFF3062DB, 0xFFFFFFFF, 9.0F, 0xFFFFFFFF, 18.0F, true, "PNG"));

        assertEquals(List.of("image", "noTint", "text"), sketch.drawEvents);
        assertEquals("PNG", sketch.lastText);
        assertEquals(20.0F, sketch.lastTextX);
        assertEquals(30.0F, sketch.lastTextY);
    }

    @Test
    void strokeWeightAndCornerRadiusDoNotAffectPngDrawing() {
        RecordingApplet sketch = new RecordingApplet();
        sketch.loadedImage = image(2, 2, 0xFFFFFFFF);
        PngButtonRenderer renderer = new PngButtonRenderer(sketch, "button-mask.png");

        renderer.render(sketch, 20.0F, 20.0F, 20.0F, 20.0F,
                new ButtonRenderStyle(0xFF3062DB, 0xFFFF0000, 12.0F, 0xFFFFFFFF, 30.0F, false, ""));

        assertEquals(0, sketch.strokeCalls);
        assertEquals(0, sketch.strokeWeightCalls);
        assertEquals(0, sketch.rectCalls);
        assertEquals(1, sketch.imageCalls);
    }

    @Test
    void defaultAndSvgRenderersKeepTheirExistingDrawingPaths() {
        RecordingApplet sketch = new RecordingApplet();
        ButtonRenderStyle style = style(0xFF3062DB, false);

        new DefaultButtonRenderer().render(sketch, 20.0F, 20.0F, 40.0F, 20.0F, style);
        new SvgButtonRenderer(sketch, "button.svg").render(sketch, 20.0F, 20.0F, 40.0F, 20.0F, style);

        assertEquals(1, sketch.rectCalls);
        assertEquals(1, sketch.loadShapeCalls);
        assertEquals(1, sketch.shapeCalls);
        assertEquals(0, sketch.imageCalls);
    }

    private static ButtonRenderStyle style(int fillColor, boolean showText) {
        return new ButtonRenderStyle(fillColor, 0xFFFFFFFF, 2.0F, 0xFFFFFFFF, 10.0F, showText, "Button");
    }

    private static ButtonViewState state(boolean enabled, boolean hovered, boolean pressed) {
        return new ButtonViewState(20.0F, 20.0F, 20.0F, 20.0F, "", false, enabled, hovered, pressed);
    }

    private static PImage image(int width, int height, int color) {
        PImage image = new PImage(width, height, PApplet.ARGB);
        image.loadPixels();
        for (int i = 0; i < image.pixels.length; i++) {
            image.pixels[i] = color;
        }
        image.updatePixels();
        return image;
    }

    private static final class RecordingApplet extends PApplet {
        private final List<Integer> tintColors = new ArrayList<>();
        private final List<String> drawEvents = new ArrayList<>();
        private PImage loadedImage;
        private boolean returnNullImage;
        private int loadImageCalls;
        private int imageCalls;
        private int tintCalls;
        private int noTintCalls;
        private int strokeCalls;
        private int strokeWeightCalls;
        private int rectCalls;
        private int loadShapeCalls;
        private int shapeCalls;
        private int lastTintColor;
        private String lastLoadImagePath;
        private float lastImageX;
        private float lastImageY;
        private float lastImageWidth;
        private float lastImageHeight;
        private String lastText;
        private float lastTextX;
        private float lastTextY;

        @Override
        public void pushStyle() {
        }

        @Override
        public void popStyle() {
        }

        @Override
        public void imageMode(int mode) {
        }

        @Override
        public void shapeMode(int mode) {
        }

        @Override
        public void textAlign(int alignX, int alignY) {
        }

        @Override
        public void fill(int rgb) {
        }

        @Override
        public void stroke(int rgb) {
            this.strokeCalls++;
        }

        @Override
        public void strokeWeight(float weight) {
            this.strokeWeightCalls++;
        }

        @Override
        public void rect(float a, float b, float c, float d, float radius) {
            this.rectCalls++;
        }

        @Override
        public void tint(int rgb) {
            this.tintCalls++;
            this.lastTintColor = rgb;
            this.tintColors.add(rgb);
        }

        @Override
        public void noTint() {
            this.noTintCalls++;
            this.drawEvents.add("noTint");
        }

        @Override
        public void image(PImage image, float a, float b, float c, float d) {
            this.imageCalls++;
            this.lastImageX = a;
            this.lastImageY = b;
            this.lastImageWidth = c;
            this.lastImageHeight = d;
            this.drawEvents.add("image");
        }

        @Override
        public void text(String text, float x, float y) {
            this.lastText = text;
            this.lastTextX = x;
            this.lastTextY = y;
            this.drawEvents.add("text");
        }

        @Override
        public PImage loadImage(String filename) {
            this.loadImageCalls++;
            this.lastLoadImagePath = filename;
            return this.returnNullImage ? null : this.loadedImage;
        }

        @Override
        public PShape loadShape(String filename) {
            this.loadShapeCalls++;
            return new PShape();
        }

        @Override
        public void shape(PShape shape, float x, float y, float width, float height) {
            this.shapeCalls++;
        }
    }
}
