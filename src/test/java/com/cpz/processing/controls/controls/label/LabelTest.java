package com.cpz.processing.controls.controls.label;

import com.cpz.processing.controls.controls.label.config.LabelStyleConfig;
import com.cpz.processing.controls.controls.label.style.DefaultLabelStyle;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PFont;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LabelTest {
    @Test
    void setTextColorUpdatesLiveStyleConfig() {
        Label label = new Label(sketch(800, 600), "label", "Status", 40.0F, 50.0F, 160.0F, 32.0F);

        label.setTextColor(0xFF50DC78);

        assertEquals(0xFF50DC78, label.getStyleConfig().textColor);
    }

    @Test
    void getTextColorReturnsUpdatedRuntimeColor() {
        Label label = new Label(sketch(800, 600), "label", "Status", 40.0F, 50.0F, 160.0F, 32.0F);

        label.setTextColor(0xFFFFB428);

        assertEquals(0xFFFFB428, label.getTextColor());
    }

    @Test
    void getTextColorUsesThemeFallbackWhenConfigColorIsNull() {
        Label label = new Label(sketch(800, 600), "label", "Status", 40.0F, 50.0F, 160.0F, 32.0F);

        assertNotNull(label.getStyleConfig());
        label.getStyleConfig().textColor = null;

        assertEquals(label.getStyle().getThemeSnapshot().tokens.onSurface, label.getTextColor());
    }

    @Test
    void changingTextColorDoesNotModifyText() {
        Label label = new Label(sketch(800, 600), "label", "Status", 40.0F, 50.0F, 160.0F, 32.0F);

        label.setTextColor(0xFFFF5050);

        assertEquals("Status", label.getText());
    }

    @Test
    void changingTextColorDoesNotAffectVisibleOrEnabled() {
        Label label = new Label(sketch(800, 600), "label", "Status", 40.0F, 50.0F, 160.0F, 32.0F);
        label.setVisible(false);
        label.setEnabled(false);

        label.setTextColor(0xFFFF5050);

        assertFalse(label.isVisible());
        assertFalse(label.isEnabled());
    }

    @Test
    void drawUsesUpdatedRuntimeTextColorFromStyleConfig() {
        RecordingApplet sketch = recordingSketch(800, 600);
        Label label = new Label(sketch, "label", "Status", 40.0F, 50.0F, 160.0F, 32.0F);

        label.setTextColor(0xFFFF5050);
        label.draw();

        assertEquals(0xFFFF5050, sketch.lastFillColor);
        assertEquals("Status", sketch.lastText);
        assertTrue(sketch.textCalls > 0);
    }

    @Test
    void setStyleKeepsTextColorApiWorkingWithDefaultLabelStyle() {
        Label label = new Label(sketch(800, 600), "label", "Status", 40.0F, 50.0F, 160.0F, 32.0F);
        LabelStyleConfig styleConfig = new LabelStyleConfig();
        styleConfig.textColor = 0xFF123456;
        label.setStyle(new DefaultLabelStyle(styleConfig));

        label.setTextColor(0xFF654321);

        assertEquals(0xFF654321, label.getTextColor());
        assertEquals(0xFF654321, label.getStyleConfig().textColor);
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

    private static final class RecordingApplet extends PApplet {
        private int lastFillColor;
        private int textCalls;
        private String lastText;

        @Override
        public void pushStyle() {
        }

        @Override
        public void popStyle() {
        }

        @Override
        public void fill(int rgb) {
            this.lastFillColor = rgb;
        }

        @Override
        public void textSize(float size) {
        }

        @Override
        public void textFont(PFont which, float size) {
        }

        @Override
        public void textAlign(int alignX, int alignY) {
        }

        @Override
        public float textAscent() {
            return 8.0F;
        }

        @Override
        public float textDescent() {
            return 2.0F;
        }

        @Override
        public void text(String str, float x, float y) {
            this.textCalls++;
            this.lastText = str;
        }
    }
}
