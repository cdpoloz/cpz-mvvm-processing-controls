package com.cpz.processing.controls.config;

import com.cpz.processing.controls.controls.button.ButtonFactory;
import com.cpz.processing.controls.controls.button.config.ButtonConfig;
import com.cpz.processing.controls.controls.button.config.ButtonConfigLoader;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;
import processing.data.JSONObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ButtonJsonConfigTest {
    @Test
    void pngRendererLoadsFromStyleRendererConfig() {
        ButtonConfig config = buttonConfig(renderer("png", "data/img/button-mask.png"));

        assertEquals("png", config.getStyle().getRenderer().getType());
        assertEquals("data/img/button-mask.png", config.getStyle().getRenderer().getPath());
    }

    @Test
    void pngRendererTypeIsTrimmedAndCaseInsensitive() {
        ButtonConfig config = buttonConfig(renderer(" PNG ", "  data/img/button-mask.PNG  "));

        assertEquals("png", config.getStyle().getRenderer().getType());
        assertEquals("data/img/button-mask.PNG", config.getStyle().getRenderer().getPath());
    }

    @Test
    void rendererTypeSelectsPngEvenWhenPathExtensionDoesNotMatch() {
        ButtonConfig config = buttonConfig(renderer("png", "data/img/button-mask.svg"));
        JsonApplet sketch = new JsonApplet();
        sketch.loadedImage = image();

        ButtonFactory.create(sketch, config);

        assertEquals(1, sketch.loadImageCalls);
        assertEquals(0, sketch.loadShapeCalls);
        assertEquals("data/img/button-mask.svg", sketch.lastImagePath);
    }

    @Test
    void blankRendererPathFailsClearly() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> buttonConfig(renderer("png", " "))
        );

        assertTrue(exception.getMessage().contains("style.renderer"));
        assertTrue(exception.getMessage().contains("non-empty"));
    }

    @Test
    void unsupportedRendererTypeListsSvgAndPng() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> buttonConfig(renderer("jpg", "data/img/button-mask.png"))
        );

        assertTrue(exception.getMessage().contains("svg"));
        assertTrue(exception.getMessage().contains("png"));
    }

    @Test
    void factoryCreatesPngRendererAndLoadsImageOnce() {
        ButtonConfig config = buttonConfig(renderer("png", "data/img/button-mask.png"));
        JsonApplet sketch = new JsonApplet();
        sketch.loadedImage = image();

        ButtonFactory.create(sketch, config);

        assertEquals(1, sketch.loadImageCalls);
        assertEquals("data/img/button-mask.png", sketch.lastImagePath);
        assertEquals(0, sketch.loadShapeCalls);
    }

    @Test
    void svgAndDefaultFactoryPathsRemainUnchanged() {
        JsonApplet sketch = new JsonApplet();

        ButtonFactory.create(sketch, buttonConfig(renderer("svg", "data/img/test.svg")));
        ButtonFactory.create(sketch, buttonConfig(""));

        assertEquals(1, sketch.loadShapeCalls);
        assertEquals("data/img/test.svg", sketch.lastShapePath);
        assertEquals(0, sketch.loadImageCalls);
    }

    private static ButtonConfig buttonConfig(String styleFragment) {
        return new ButtonConfigLoader(new PApplet()).loadFromJson(
                JSONObject.parse("{\"code\":\"button\",\"text\":\"Button\",\"x\":20,\"y\":20,"
                        + "\"width\":40,\"height\":20" + styleFragment + "}"),
                "button.json"
        );
    }

    private static String renderer(String type, String path) {
        return ",\"style\":{\"renderer\":{\"type\":\"" + type + "\",\"path\":\"" + path + "\"}}";
    }

    private static PImage image() {
        PImage image = new PImage(2, 2, PApplet.ARGB);
        image.loadPixels();
        for (int i = 0; i < image.pixels.length; i++) {
            image.pixels[i] = 0xFF123456;
        }
        image.updatePixels();
        return image;
    }

    private static final class JsonApplet extends PApplet {
        private PImage loadedImage;
        private int loadImageCalls;
        private int loadShapeCalls;
        private String lastImagePath;
        private String lastShapePath;

        @Override
        public PImage loadImage(String filename) {
            this.loadImageCalls++;
            this.lastImagePath = filename;
            return this.loadedImage;
        }

        @Override
        public PShape loadShape(String filename) {
            this.loadShapeCalls++;
            this.lastShapePath = filename;
            return new PShape();
        }
    }
}
