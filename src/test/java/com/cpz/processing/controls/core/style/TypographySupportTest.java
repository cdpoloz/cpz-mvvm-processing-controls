package com.cpz.processing.controls.core.style;

import com.cpz.processing.controls.testsupport.ProcessingTestSupport;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PFont;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

class TypographySupportTest {
    @Test
    void appliesAllButtonAndSliderTypographyCombinations() {
        PApplet sketch = new PApplet();
        ProcessingTestSupport.RecordingGraphics graphics = ProcessingTestSupport.graphics(sketch);
        PFont ambient = ProcessingTestSupport.font("Dialog", 13);
        PFont custom = ProcessingTestSupport.font("Monospaced", 21);

        graphics.textFont(ambient, 13.0F);
        TypographySupport.apply(sketch, null, null);
        assertSame(ambient, graphics.textFont);
        assertEquals(13.0F, graphics.textSize);

        TypographySupport.apply(sketch, null, 19.0F);
        assertSame(ambient, graphics.textFont);
        assertEquals(19.0F, graphics.textSize);

        TypographySupport.apply(sketch, custom, 18.0F);
        assertSame(custom, graphics.textFont);
        assertEquals(18.0F, graphics.textSize);

        TypographySupport.apply(sketch, custom, null);
        assertSame(custom, graphics.textFont);
        assertEquals(TypographySupport.DEFAULT_CUSTOM_FONT_SIZE, graphics.textSize);
    }

    @Test
    void initializesAProcessingFontBeforePushStyleSoCustomFontCanBeRestored() {
        PApplet sketch = new PApplet();
        ProcessingTestSupport.RecordingGraphics graphics = ProcessingTestSupport.graphics(sketch);
        PFont custom = ProcessingTestSupport.font("Monospaced", 16);

        assertEquals(null, graphics.textFont);
        TypographySupport.prepareStyleScope(sketch, custom);

        PFont processingFont = graphics.textFont;
        assertNotNull(processingFont);
        assertNotSame(custom, processingFont);

        sketch.pushStyle();
        TypographySupport.apply(sketch, custom, 17.0F);
        assertSame(custom, graphics.textFont);
        sketch.popStyle();

        assertSame(processingFont, graphics.textFont);
        assertNotSame(custom, graphics.textFont);
    }

    @Test
    void doesNotInitializeAFontWhenControlHasNoFontOverride() {
        PApplet sketch = new PApplet();
        ProcessingTestSupport.RecordingGraphics graphics = ProcessingTestSupport.graphics(sketch);

        TypographySupport.prepareStyleScope(sketch, null);

        assertEquals(null, graphics.textFont);
    }
}
