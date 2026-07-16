package com.cpz.processing.controls.controls.indicator;

import com.cpz.processing.controls.controls.KeyboardRoutableControl;
import com.cpz.processing.controls.controls.PointerRoutableControl;
import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.indicator.style.IndicatorStyle;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipAttachable;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IndicatorTest {
    @Test
    void defaultsToOff() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        assertFalse(indicator.isOn());
    }

    @Test
    void setOnUpdatesLogicalState() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        indicator.setOn(true);
        assertTrue(indicator.isOn());

        indicator.setOn(false);
        assertFalse(indicator.isOn());
    }

    @Test
    void colorSettersUpdateRuntimeColors() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        indicator.setOnColor(0xFF00AA00);
        indicator.setOffColor(0xFF111111);

        assertEquals(0xFF00AA00, indicator.getOnColor());
        assertEquals(0xFF111111, indicator.getOffColor());
    }

    @Test
    void defaultsToConfiguredStrokeDefaults() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        assertEquals(Indicator.DEFAULT_BORDER_COLOR, indicator.getStrokeColor());
        assertEquals(1.0F, indicator.getStrokeWeight());
    }

    @Test
    void strokeSettersUpdateRuntimeStroke() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        indicator.setStrokeColor(0xFFFFFFFF);
        indicator.setStrokeWeight(2.5F);

        assertEquals(0xFFFFFFFF, indicator.getStrokeColor());
        assertEquals(2.5F, indicator.getStrokeWeight());
    }

    @Test
    void negativeStrokeWeightIsClampedToZero() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        indicator.setStrokeWeight(-2.0F);

        assertEquals(0.0F, indicator.getStrokeWeight());
    }

    @Test
    void hasDefaultStyle() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        assertNotNull(indicator.getStyle());
        assertEquals(Indicator.DEFAULT_ON_COLOR, indicator.getStyle().getOnColor());
        assertEquals(Indicator.DEFAULT_OFF_COLOR, indicator.getStyle().getOffColor());
        assertEquals(Indicator.DEFAULT_BORDER_COLOR, indicator.getStyle().getStrokeColor());
        assertEquals(1.0F, indicator.getStyle().getStrokeWeight());
    }

    @Test
    void setStyleUpdatesVisualProperties() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);
        IndicatorStyle style = new IndicatorStyle()
                .setOnColor(0xFF00AA00)
                .setOffColor(0xFF111111)
                .setStrokeColor(0xFFFFFFFF)
                .setStrokeWeight(2.5F);

        indicator.setStyle(style);

        assertSame(style, indicator.getStyle());
        assertEquals(0xFF00AA00, indicator.getOnColor());
        assertEquals(0xFF111111, indicator.getOffColor());
        assertEquals(0xFFFFFFFF, indicator.getStrokeColor());
        assertEquals(2.5F, indicator.getStrokeWeight());
    }

    @Test
    void nullStyleResetsToDefaultStyle() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        indicator.setStyle(null);

        assertNotNull(indicator.getStyle());
        assertEquals(Indicator.DEFAULT_ON_COLOR, indicator.getOnColor());
        assertEquals(Indicator.DEFAULT_OFF_COLOR, indicator.getOffColor());
        assertEquals(Indicator.DEFAULT_BORDER_COLOR, indicator.getStrokeColor());
        assertEquals(1.0F, indicator.getStrokeWeight());
    }

    @Test
    void directSettersDelegateToStyle() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        indicator.setOnColor(0xFF00AA00);
        indicator.setOffColor(0xFF111111);
        indicator.setStrokeColor(0xFFFFFFFF);
        indicator.setStrokeWeight(2.5F);

        assertEquals(0xFF00AA00, indicator.getStyle().getOnColor());
        assertEquals(0xFF111111, indicator.getStyle().getOffColor());
        assertEquals(0xFFFFFFFF, indicator.getStyle().getStrokeColor());
        assertEquals(2.5F, indicator.getStyle().getStrokeWeight());
    }

    @Test
    void drawUsesOffColorWhenOff() {
        RecordingApplet sketch = recordingSketch(800, 600);
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 30.0F);
        indicator.setOffColor(0xFF111111);

        indicator.draw();

        assertEquals(0xFF111111, sketch.lastFillColor);
        assertEquals(1, sketch.circleCalls);
        assertEquals(52.0F, sketch.lastCircleX);
        assertEquals(65.0F, sketch.lastCircleY);
        assertEquals(24.0F, sketch.lastCircleDiameter);
    }

    @Test
    void drawUsesConfiguredStroke() {
        RecordingApplet sketch = recordingSketch(800, 600);
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 30.0F);
        indicator.setStrokeColor(0xFFFFFFFF);
        indicator.setStrokeWeight(2.5F);

        indicator.draw();

        assertEquals(0xFFFFFFFF, sketch.lastStrokeColor);
        assertEquals(2.5F, sketch.lastStrokeWeight);
        assertEquals(1, sketch.strokeCalls);
        assertEquals(0, sketch.noStrokeCalls);
    }

    @Test
    void zeroStrokeWeightDisablesStroke() {
        RecordingApplet sketch = recordingSketch(800, 600);
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 30.0F);
        indicator.setStrokeWeight(0.0F);

        indicator.draw();

        assertEquals(1, sketch.noStrokeCalls);
        assertEquals(0, sketch.strokeCalls);
    }

    @Test
    void drawUsesOnColorWhenOn() {
        RecordingApplet sketch = recordingSketch(800, 600);
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 30.0F);
        indicator.setOnColor(0xFF00AA00);
        indicator.setOn(true);

        indicator.draw();

        assertEquals(0xFF00AA00, sketch.lastFillColor);
    }

    @Test
    void svgConstructorLoadsAndDrawsTintedShape() {
        RecordingApplet sketch = recordingSketch(800, 600);
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 30.0F, "data/img/test.svg");
        indicator.setOnColor(0xFF00AA00);
        indicator.setStrokeColor(0xFFFFFFFF);
        indicator.setStrokeWeight(2.0F);
        indicator.setOn(true);

        indicator.draw();

        assertEquals(1, sketch.loadShapeCalls);
        assertEquals("data/img/test.svg", sketch.lastLoadShapePath);
        assertEquals(0xFF00AA00, sketch.lastFillColor);
        assertEquals(0xFFFFFFFF, sketch.lastStrokeColor);
        assertEquals(2.0F, sketch.lastStrokeWeight);
        assertEquals(0, sketch.circleCalls);
        assertEquals(1, sketch.shapeCalls);
        assertEquals(52.0F, sketch.lastShapeX);
        assertEquals(65.0F, sketch.lastShapeY);
        assertEquals(24.0F, sketch.lastShapeWidth);
        assertEquals(30.0F, sketch.lastShapeHeight);
    }

    @Test
    void pngConstructorLoadsAndDrawsTintedMask() {
        RecordingApplet sketch = recordingSketch(800, 600);
        sketch.loadedImage = maskImage(2, 4);
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 40.0F, 40.0F, "src/test/resources/data/img/indicator-mask.png");
        indicator.setOnColor(0xFF00AA00);
        indicator.setOn(true);

        indicator.draw();

        assertEquals("png", indicator.getRendererType());
        assertEquals("src/test/resources/data/img/indicator-mask.png", indicator.getRendererPath());
        assertEquals(1, sketch.loadImageCalls);
        assertEquals("src/test/resources/data/img/indicator-mask.png", sketch.lastLoadImagePath);
        assertEquals(0, sketch.circleCalls);
        assertEquals(0, sketch.shapeCalls);
        assertEquals(1, sketch.imageCalls);
        assertEquals(0xFF00AA00, sketch.lastTintColor);
        assertEquals(1, sketch.noTintCalls);
        assertEquals(50.0F, sketch.lastImageX);
        assertEquals(50.0F, sketch.lastImageY);
        assertEquals(20.0F, sketch.lastImageWidth);
        assertEquals(40.0F, sketch.lastImageHeight);
    }

    @Test
    void pngExtensionIsCaseInsensitive() {
        RecordingApplet sketch = recordingSketch(800, 600);
        sketch.loadedImage = maskImage(2, 2);
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 24.0F, "src/test/resources/data/img/indicator-mask.PNG");

        indicator.draw();

        assertEquals("png", indicator.getRendererType());
        assertEquals(1, sketch.loadImageCalls);
        assertEquals(1, sketch.imageCalls);
    }

    @Test
    void pngMaskNormalizationPreservesAlphaAndIgnoresOriginalRgb() {
        PImage source = new PImage(2, 2, PApplet.ARGB);
        source.loadPixels();
        source.pixels[0] = 0x00000000;
        source.pixels[1] = 0x80112233;
        source.pixels[2] = 0xFFFF0000;
        source.pixels[3] = 0x4000FF00;
        source.updatePixels();

        PImage normalized = Indicator.normalizePngMask(source);

        normalized.loadPixels();
        assertEquals(0x00FFFFFF, normalized.pixels[0]);
        assertEquals(0x80FFFFFF, normalized.pixels[1]);
        assertEquals(0xFFFFFFFF, normalized.pixels[2]);
        assertEquals(0x40FFFFFF, normalized.pixels[3]);
    }

    @Test
    void pngTestResourceContainsTransparentSemiTransparentAndOpaquePixels() throws IOException {
        BufferedImage image = ImageIO.read(Path.of("src/test/resources/data/img/indicator-mask.png").toFile());

        assertNotNull(image);
        assertEquals(4, image.getWidth());
        assertEquals(4, image.getHeight());
        assertEquals(0x00000000, image.getRGB(0, 0));
        assertEquals(0x80FF0000, image.getRGB(1, 0));
        assertEquals(0xFF00FF00, image.getRGB(2, 0));
        assertEquals(0x400000FF, image.getRGB(3, 0));
    }

    @Test
    void pngTintUsesCurrentStateColor() {
        RecordingApplet sketch = recordingSketch(800, 600);
        sketch.loadedImage = maskImage(2, 2);
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 24.0F, "src/test/resources/data/img/indicator-mask.png");
        indicator.setOnColor(0xFF00AA00);
        indicator.setOffColor(0xFF111111);

        indicator.draw();
        assertEquals(0xFF111111, sketch.lastTintColor);

        indicator.setOn(true);
        indicator.draw();
        assertEquals(0xFF00AA00, sketch.lastTintColor);
    }

    @Test
    void pngResourceIsNotReloadedOnEveryFrame() {
        RecordingApplet sketch = recordingSketch(800, 600);
        sketch.loadedImage = maskImage(2, 2);
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 24.0F, "src/test/resources/data/img/indicator-mask.png");

        indicator.draw();
        PImage firstDrawImage = sketch.lastImage;
        indicator.draw();

        assertEquals(1, sketch.loadImageCalls);
        assertSame(firstDrawImage, sketch.lastImage);
        assertEquals(2, sketch.imageCalls);
    }

    @Test
    void pngScalingPreservesAspectRatioAndCentersInBounds() {
        RecordingApplet sketch = recordingSketch(800, 600);
        sketch.loadedImage = maskImage(4, 2);
        Indicator indicator = new Indicator(sketch, 10.0F, 20.0F, 40.0F, 40.0F, "src/test/resources/data/img/indicator-mask.png");

        indicator.draw();

        assertEquals(10.0F, sketch.lastImageX);
        assertEquals(30.0F, sketch.lastImageY);
        assertEquals(40.0F, sketch.lastImageWidth);
        assertEquals(20.0F, sketch.lastImageHeight);
    }

    @Test
    void pngRendererCanBeChangedAtRuntime() {
        RecordingApplet sketch = recordingSketch(800, 600);
        sketch.loadedImage = maskImage(2, 2);
        Indicator indicator = new Indicator(sketch, 10.0F, 20.0F, 20.0F, 20.0F);

        indicator.setRenderer("png", "src/test/resources/data/img/indicator-mask.png");
        indicator.draw();

        assertEquals("png", indicator.getRendererType());
        assertEquals(1, sketch.loadImageCalls);
        assertEquals(1, sketch.imageCalls);
    }

    @Test
    void clearRendererReturnsToDefaultCircle() {
        RecordingApplet sketch = recordingSketch(800, 600);
        sketch.loadedImage = maskImage(2, 2);
        Indicator indicator = new Indicator(sketch, 10.0F, 20.0F, 20.0F, 20.0F, "src/test/resources/data/img/indicator-mask.png");

        indicator.clearRenderer();
        indicator.draw();

        assertEquals(null, indicator.getRendererType());
        assertEquals(null, indicator.getRendererPath());
        assertEquals(1, sketch.circleCalls);
        assertEquals(0, sketch.imageCalls);
    }

    @Test
    void missingPngUsesExistingRendererBehaviorAndDrawsNothing() {
        RecordingApplet sketch = recordingSketch(800, 600);
        sketch.returnNullImage = true;
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 30.0F, "data/img/missing.png");

        indicator.draw();

        assertEquals(2, sketch.loadImageCalls);
        assertEquals("img/missing.png", sketch.lastLoadImagePath);
        assertEquals(0, sketch.imageCalls);
        assertEquals(0, sketch.circleCalls);
    }

    @Test
    void invalidPngDimensionsDrawNothing() {
        RecordingApplet sketch = recordingSketch(800, 600);
        sketch.loadedImage = new PImage(0, 2, PApplet.ARGB);
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 30.0F, "data/img/invalid.png");

        indicator.draw();

        assertEquals(1, sketch.loadImageCalls);
        assertEquals(0, sketch.imageCalls);
        assertEquals(0, sketch.circleCalls);
    }

    @Test
    void unsupportedRendererExtensionFailsClearly() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Indicator(recordingSketch(800, 600), 40.0F, 50.0F, 24.0F, 30.0F, "data/img/icon.jpg")
        );

        assertTrue(exception.getMessage().contains(".svg"));
        assertTrue(exception.getMessage().contains(".png"));
    }

    @Test
    void emptyRuntimeRendererPathFailsClearly() {
        Indicator indicator = new Indicator(recordingSketch(800, 600), 40.0F, 50.0F, 24.0F, 30.0F);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> indicator.setRenderer("png", " ")
        );

        assertTrue(exception.getMessage().contains("path"));
    }

    @Test
    void pngDrawRestoresTintForFollowingRenderers() {
        RecordingApplet sketch = recordingSketch(800, 600);
        sketch.loadedImage = maskImage(2, 2);
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 24.0F, "src/test/resources/data/img/indicator-mask.png");

        indicator.draw();

        assertEquals(1, sketch.tintCalls);
        assertEquals(1, sketch.noTintCalls);
    }

    @Test
    void svgConstructorWithRelativeBoundsResolvesAgainstCanvas() {
        RecordingApplet sketch = recordingSketch(800, 600);
        Indicator indicator = new Indicator(
                sketch,
                "ind",
                ControlBounds.relative(0.1F, 0.2F, 0.05F, 0.1F),
                "data/img/test.svg"
        );

        indicator.draw();

        assertBounds(indicator.getTooltipBounds(), 80.0F, 120.0F, 30.0F, 60.0F);
        assertEquals(1, sketch.shapeCalls);
        assertEquals(95.0F, sketch.lastShapeX);
        assertEquals(150.0F, sketch.lastShapeY);
        assertEquals(30.0F, sketch.lastShapeWidth);
        assertEquals(60.0F, sketch.lastShapeHeight);
    }

    @Test
    void missingSvgUsesExistingRendererBehaviorAndDrawsNothing() {
        RecordingApplet sketch = recordingSketch(800, 600);
        sketch.returnNullShape = true;
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 30.0F, "data/img/missing.svg");

        indicator.draw();

        assertEquals(2, sketch.loadShapeCalls);
        assertEquals("img/missing.svg", sketch.lastLoadShapePath);
        assertEquals(0, sketch.shapeCalls);
        assertEquals(0, sketch.circleCalls);
    }

    @Test
    void invisibleIndicatorDoesNotDraw() {
        RecordingApplet sketch = recordingSketch(800, 600);
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 30.0F);

        indicator.setVisible(false);
        indicator.draw();

        assertEquals(0, sketch.circleCalls);
        assertEquals(0, sketch.strokeCalls);
        assertEquals(0, sketch.noStrokeCalls);
    }

    @Test
    void disabledIndicatorDoesNotChangeLogicalState() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        indicator.setOn(true);
        indicator.setEnabled(false);

        assertTrue(indicator.isOn());
    }

    @Test
    void disabledIndicatorDoesNotChangeStroke() {
        RecordingApplet sketch = recordingSketch(800, 600);
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 24.0F);
        indicator.setStrokeColor(0xFFFFFFFF);
        indicator.setStrokeWeight(3.0F);

        indicator.setEnabled(false);
        indicator.draw();

        assertEquals(0xFFFFFFFF, sketch.lastStrokeColor);
        assertEquals(3.0F, sketch.lastStrokeWeight);
    }

    @Test
    void absoluteBoundsAreTopLeftLogicalBounds() {
        Indicator indicator = new Indicator(sketch(800, 600), "ind", 40.0F, 50.0F, 24.0F, 30.0F);

        assertBounds(indicator.getTooltipBounds(), 40.0F, 50.0F, 24.0F, 30.0F);
    }

    @Test
    void relativeBoundsResolveAgainstCanvas() {
        Indicator indicator = new Indicator(
                sketch(800, 600),
                "ind",
                ControlBounds.relative(0.1F, 0.2F, 0.05F, 0.1F)
        );

        assertBounds(indicator.getTooltipBounds(), 80.0F, 120.0F, 30.0F, 60.0F);
    }

    @Test
    void controlBoundsOfCanMixRelativeAndAbsoluteMeasures() {
        Indicator indicator = new Indicator(
                sketch(800, 600),
                "ind",
                ControlBounds.of(
                        ControlMeasure.relative(0.1F),
                        ControlMeasure.relative(0.2F),
                        ControlMeasure.absolute(24.0F),
                        ControlMeasure.absolute(30.0F)
                )
        );

        assertBounds(indicator.getTooltipBounds(), 80.0F, 120.0F, 24.0F, 30.0F);
    }

    @Test
    void relativeBoundsInsidePanelResolveAgainstPanel() {
        PApplet sketch = sketch(800, 600);
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 400.0F, 200.0F);
        Indicator indicator = new Indicator(sketch, "ind", ControlBounds.relative(0.25F, 0.5F, 0.25F, 0.1F));
        panel.add(indicator);

        TooltipBounds bounds = panel.tooltipTarget(indicator).getTooltipBounds();

        assertBounds(bounds, 200.0F, 180.0F, 50.0F, 20.0F);
    }

    @Test
    void setPositionAfterRelativeBoundsMakesPositionAbsoluteAndKeepsRelativeSize() {
        Indicator indicator = new Indicator(
                sketch(800, 600),
                "ind",
                ControlBounds.relative(0.25F, 0.1F, 0.5F, 0.2F)
        );

        indicator.setPosition(300.0F, 240.0F);

        assertBounds(indicator.getTooltipBounds(), 300.0F, 240.0F, 300.0F, 120.0F);
    }

    @Test
    void setSizeAfterRelativeBoundsMakesSizeAbsoluteAndKeepsRelativePosition() {
        Indicator indicator = new Indicator(
                sketch(800, 600),
                "ind",
                ControlBounds.relative(0.25F, 0.1F, 0.5F, 0.2F)
        );

        indicator.setSize(160.0F, 40.0F);

        assertBounds(indicator.getTooltipBounds(), 200.0F, 60.0F, 160.0F, 40.0F);
    }

    @Test
    void implementsTooltipAttachable() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        assertInstanceOf(TooltipAttachable.class, indicator);
    }

    @Test
    void setTooltipTextUpdatesTooltipAtRuntime() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        indicator.setTooltipText("initial");
        indicator.setTooltipText("runtime");

        assertEquals("runtime", indicator.getTooltip().getText());
    }

    @Test
    void invisibleIndicatorIsNotATooltipTarget() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F)
                .setTooltip("status");

        indicator.setVisible(false);

        assertFalse(indicator.isTooltipTargetVisible());
    }

    @Test
    void disabledIndicatorStillAllowsTooltipTarget() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F)
                .setTooltip("status");

        indicator.setEnabled(false);

        assertTrue(indicator.isTooltipTargetVisible());
        assertTrue(indicator.isTooltipTargetEnabled());
        assertFalse(indicator.isEnabled());
    }

    @Test
    void indicatorIsNotInteractive() {
        Object indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        assertFalse(indicator instanceof PointerRoutableControl);
        assertFalse(indicator instanceof KeyboardRoutableControl);
    }

    @Test
    void svgIndicatorIsNotInteractive() {
        Object indicator = new Indicator(recordingSketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F, "data/img/test.svg");

        assertFalse(indicator instanceof PointerRoutableControl);
        assertFalse(indicator instanceof KeyboardRoutableControl);
    }

    private static void assertBounds(TooltipBounds bounds, float x, float y, float width, float height) {
        assertEquals(x, bounds.x());
        assertEquals(y, bounds.y());
        assertEquals(width, bounds.width());
        assertEquals(height, bounds.height());
    }

    private static PApplet sketch(int width, int height) {
        PApplet sketch = new PApplet();
        sketch.width = width;
        sketch.height = height;
        return sketch;
    }

    private static RecordingApplet recordingSketch(int width, int height) {
        RecordingApplet sketch = new RecordingApplet();
        sketch.width = width;
        sketch.height = height;
        return sketch;
    }

    private static PImage maskImage(int width, int height) {
        PImage image = new PImage(width, height, PApplet.ARGB);
        image.loadPixels();
        for (int i = 0; i < image.pixels.length; i++) {
            image.pixels[i] = 0xFF123456;
        }
        image.updatePixels();
        return image;
    }

    private static final class RecordingApplet extends PApplet {
        private int lastFillColor;
        private int lastStrokeColor;
        private int lastTintColor;
        private int circleCalls;
        private int shapeCalls;
        private int imageCalls;
        private int loadShapeCalls;
        private int loadImageCalls;
        private int strokeCalls;
        private int noStrokeCalls;
        private int tintCalls;
        private int noTintCalls;
        private float lastCircleX;
        private float lastCircleY;
        private float lastCircleDiameter;
        private float lastShapeX;
        private float lastShapeY;
        private float lastShapeWidth;
        private float lastShapeHeight;
        private float lastImageX;
        private float lastImageY;
        private float lastImageWidth;
        private float lastImageHeight;
        private float lastStrokeWeight;
        private String lastLoadShapePath;
        private String lastLoadImagePath;
        private PImage loadedImage;
        private PImage lastImage;
        private boolean returnNullShape;
        private boolean returnNullImage;

        @Override
        public void pushStyle() {
        }

        @Override
        public void popStyle() {
        }

        @Override
        public void ellipseMode(int mode) {
        }

        @Override
        public void shapeMode(int mode) {
        }

        @Override
        public void imageMode(int mode) {
        }

        @Override
        public void stroke(int rgb) {
            this.strokeCalls++;
            this.lastStrokeColor = rgb;
        }

        @Override
        public void strokeWeight(float weight) {
            this.lastStrokeWeight = weight;
        }

        @Override
        public void noStroke() {
            this.noStrokeCalls++;
        }

        @Override
        public void fill(int rgb) {
            this.lastFillColor = rgb;
        }

        @Override
        public void tint(int rgb) {
            this.tintCalls++;
            this.lastTintColor = rgb;
        }

        @Override
        public void noTint() {
            this.noTintCalls++;
        }

        @Override
        public void circle(float x, float y, float extent) {
            this.circleCalls++;
            this.lastCircleX = x;
            this.lastCircleY = y;
            this.lastCircleDiameter = extent;
        }

        @Override
        public void shape(PShape shape, float x, float y, float c, float d) {
            this.shapeCalls++;
            this.lastShapeX = x;
            this.lastShapeY = y;
            this.lastShapeWidth = c;
            this.lastShapeHeight = d;
        }

        @Override
        public void image(PImage image, float a, float b, float c, float d) {
            this.imageCalls++;
            this.lastImage = image;
            this.lastImageX = a;
            this.lastImageY = b;
            this.lastImageWidth = c;
            this.lastImageHeight = d;
        }

        @Override
        public PShape loadShape(String filename) {
            this.loadShapeCalls++;
            this.lastLoadShapePath = filename;
            return this.returnNullShape ? null : new PShape();
        }

        @Override
        public PImage loadImage(String filename) {
            this.loadImageCalls++;
            this.lastLoadImagePath = filename;
            return this.returnNullImage ? null : this.loadedImage;
        }
    }
}
