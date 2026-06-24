package com.cpz.processing.controls.testsupport;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PShape;

import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class ProcessingTestSupport {
    private ProcessingTestSupport() {
    }

    public static PFont font(String name, int size) {
        return new PFont(new Font(name, Font.PLAIN, size), true);
    }

    public static RecordingGraphics graphics(PApplet sketch) {
        RecordingGraphics graphics = new RecordingGraphics();
        graphics.setParent(sketch);
        sketch.g = graphics;
        return graphics;
    }

    public static final class RecordingGraphics extends PGraphics {
        private final List<PFont> appliedFonts = new ArrayList<>();
        private final List<Float> appliedSizes = new ArrayList<>();

        @Override
        public void textFont(PFont font, float size) {
            this.appliedFonts.add(font);
            this.appliedSizes.add(size);
            super.textFont(font, size);
        }

        @Override
        public void textSize(float size) {
            this.appliedSizes.add(size);
            super.textSize(size);
        }

        public List<PFont> appliedFonts() {
            return this.appliedFonts;
        }

        public List<Float> appliedSizes() {
            return this.appliedSizes;
        }

        public void clearTypographyHistory() {
            this.appliedFonts.clear();
            this.appliedSizes.clear();
        }
    }

    public static class FontApplet extends PApplet {
        private final PFont font;
        private boolean resourceAvailable = true;
        private boolean returnNullFont;
        private RuntimeException createFontFailure;
        private int createFontCalls;
        private float lastCreateFontSize;

        public FontApplet(PFont font) {
            this.font = font;
        }

        @Override
        public InputStream createInput(String filename) {
            return this.resourceAvailable
                    ? new ByteArrayInputStream(new byte[]{1})
                    : null;
        }

        @Override
        public PFont createFont(String name, float size) {
            this.createFontCalls++;
            this.lastCreateFontSize = size;
            if (this.createFontFailure != null) {
                throw this.createFontFailure;
            }
            return this.returnNullFont ? null : this.font;
        }

        @Override
        public PShape loadShape(String filename) {
            return null;
        }

        public void setResourceAvailable(boolean resourceAvailable) {
            this.resourceAvailable = resourceAvailable;
        }

        public void setReturnNullFont(boolean returnNullFont) {
            this.returnNullFont = returnNullFont;
        }

        public void setCreateFontFailure(RuntimeException createFontFailure) {
            this.createFontFailure = createFontFailure;
        }

        public int getCreateFontCalls() {
            return this.createFontCalls;
        }

        public float getLastCreateFontSize() {
            return this.lastCreateFontSize;
        }
    }
}
