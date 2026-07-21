package com.cpz.processing.controls.core.util;

import com.cpz.processing.controls.testsupport.ProcessingTestSupport;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PFont;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FontLoaderTest {
    @Test
    void loadsTheBundledTtfThroughARealProcessingApplet() {
        PApplet sketch = new ResourceApplet();
        ProcessingTestSupport.graphics(sketch);

        PFont font = FontLoader.load(
                sketch,
                "data/font/JetBrainsMono.ttf",
                16.0F,
                "label",
                "data/config/label-test.json"
        );

        assertNotNull(font);
    }

    private static final class ResourceApplet extends PApplet {
        @Override
        public InputStream createInput(String filename) {
            try {
                return Files.newInputStream(Path.of(filename));
            } catch (IOException exception) {
                return null;
            }
        }
    }

    @Test
    void returnsTheExactFontCreatedByProcessing() {
        PFont expected = ProcessingTestSupport.font("Monospaced", 16);
        ProcessingTestSupport.FontApplet sketch = new ProcessingTestSupport.FontApplet(expected);

        PFont actual = FontLoader.load(sketch, "data/font/test.ttf", 16.0F, "button", "controls.json");

        assertSame(expected, actual);
        assertEquals(1, sketch.getCreateFontCalls());
    }

    @Test
    void resolverCachesFontsByNormalizedEffectiveTextSize() {
        PFont expected = ProcessingTestSupport.font("Monospaced", 21);
        ProcessingTestSupport.FontApplet sketch = new ProcessingTestSupport.FontApplet(expected);
        FontLoader.FontResolver resolver = FontLoader.resolver("data/font/test.ttf", "label", "labels.json");

        PFont first = resolver.load(sketch, 20.6F);
        PFont second = resolver.load(sketch, 21.4F);
        resolver.load(sketch, 21.6F);

        assertSame(expected, first);
        assertSame(first, second);
        assertEquals(2, sketch.getCreateFontCalls());
        assertEquals(List.of(21.0F, 22.0F), sketch.getCreateFontSizes());
    }

    @Test
    void rejectsMissingResourcesWithNormalizedDiagnostics() {
        ProcessingTestSupport.FontApplet sketch =
                new ProcessingTestSupport.FontApplet(ProcessingTestSupport.font("Dialog", 16));
        sketch.setResourceAvailable(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> FontLoader.load(sketch, "missing.ttf", 16.0F, "slider", "ui.json")
        );

        assertTrue(exception.getMessage().contains("slider"));
        assertTrue(exception.getMessage().contains("'font'"));
        assertTrue(exception.getMessage().contains("ui.json"));
        assertTrue(exception.getMessage().contains("missing.ttf"));
        assertTrue(exception.getMessage().contains("does not exist"));
        assertEquals(0, sketch.getCreateFontCalls());
    }

    @Test
    void rejectsInvalidFontResources() {
        ProcessingTestSupport.FontApplet sketch =
                new ProcessingTestSupport.FontApplet(ProcessingTestSupport.font("Dialog", 16));
        sketch.setReturnNullFont(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> FontLoader.load(sketch, "invalid.ttf", 16.0F, "label", "labels.json")
        );

        assertTrue(exception.getMessage().contains("label"));
        assertTrue(exception.getMessage().contains("could not create"));
        assertEquals(1, sketch.getCreateFontCalls());
    }

    @Test
    void wrapsProcessingCreationFailures() {
        ProcessingTestSupport.FontApplet sketch =
                new ProcessingTestSupport.FontApplet(ProcessingTestSupport.font("Dialog", 16));
        sketch.setCreateFontFailure(new IllegalArgumentException("broken font"));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> FontLoader.load(sketch, "broken.ttf", 16.0F, "drop down", "controls.json")
        );

        assertTrue(exception.getMessage().contains("Processing failed"));
        assertTrue(exception.getCause().getMessage().contains("broken font"));
    }
}
