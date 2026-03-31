package com.cpz.processing.controls.slidercontrol.style.render;

import com.cpz.processing.controls.slidercontrol.SliderOrientation;
import com.cpz.processing.controls.slidercontrol.style.SliderRenderStyle;
import com.cpz.processing.controls.slidercontrol.style.SvgColorMode;
import com.cpz.processing.controls.slidercontrol.view.SliderGeometry;
import com.cpz.processing.controls.slidercontrol.view.SliderViewState;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;

public final class SliderRenderer {

    public void render(PApplet p, SliderGeometry geometry, SliderViewState state, SliderRenderStyle style) {
        p.pushStyle();
        drawBaseTrack(p, geometry, style);
        drawActiveTrack(p, geometry, state, style);
        drawThumb(p, geometry, state, style);
        drawValueText(p, geometry, state, style);
        p.popStyle();
    }

    private void drawBaseTrack(PApplet p, SliderGeometry geometry, SliderRenderStyle style) {
        p.rectMode(PConstants.CORNER);
        p.fill(style.trackColor());
        p.stroke(style.trackStrokeColor());
        p.strokeWeight(style.trackStrokeWeight());

        if (geometry.orientation() == SliderOrientation.HORIZONTAL) {
            float length = geometry.trackEndX() - geometry.trackStartX();
            float top = geometry.y() - (style.trackThickness() * 0.5f);
            p.rect(geometry.trackStartX(), top, length, style.trackThickness(), style.trackThickness() * 0.5f);
            return;
        }

        float length = geometry.trackStartY() - geometry.trackEndY();
        float left = geometry.x() - (style.trackThickness() * 0.5f);
        p.rect(left, geometry.trackEndY(), style.trackThickness(), length, style.trackThickness() * 0.5f);
    }

    private void drawActiveTrack(PApplet p, SliderGeometry geometry, SliderViewState state, SliderRenderStyle style) {
        p.noStroke();
        p.fill(style.activeTrackColor());
        float thumbX = geometry.thumbX(state.normalizedValue());
        float thumbY = geometry.thumbY(state.normalizedValue());

        if (geometry.orientation() == SliderOrientation.HORIZONTAL) {
            float length = thumbX - geometry.trackStartX();
            float top = geometry.y() - (style.trackThickness() * 0.5f);
            p.rect(geometry.trackStartX(), top, length, style.trackThickness(), style.trackThickness() * 0.5f);
            return;
        }

        float length = geometry.trackStartY() - thumbY;
        float left = geometry.x() - (style.trackThickness() * 0.5f);
        p.rect(left, thumbY, style.trackThickness(), length, style.trackThickness() * 0.5f);
    }

    private void drawThumb(PApplet p, SliderGeometry geometry, SliderViewState state, SliderRenderStyle style) {
        PShape thumbShape = style.thumbShape();
        if (thumbShape != null) {
            drawSvgThumb(p, geometry, state, style, thumbShape);
            return;
        }

        drawFallbackThumb(p, geometry, state, style);
    }

    private void drawSvgThumb(PApplet p, SliderGeometry geometry, SliderViewState state, SliderRenderStyle style, PShape thumbShape) {
        p.shapeMode(PConstants.CENTER);
        if (style.svgColorMode() == SvgColorMode.USE_RENDER_STYLE) {
            thumbShape.disableStyle();
            p.fill(style.thumbColor());
            p.stroke(style.thumbStrokeColor());
            p.strokeWeight(style.thumbStrokeWeight());
        } else {
            thumbShape.enableStyle();
        }
        p.shape(thumbShape, geometry.thumbX(state.normalizedValue()), geometry.thumbY(state.normalizedValue()), style.thumbSize(), style.thumbSize());
    }

    private void drawFallbackThumb(PApplet p, SliderGeometry geometry, SliderViewState state, SliderRenderStyle style) {
        p.ellipseMode(PConstants.CENTER);
        p.fill(style.thumbColor());
        p.stroke(style.thumbStrokeColor());
        p.strokeWeight(style.thumbStrokeWeight());
        p.ellipse(geometry.thumbX(state.normalizedValue()), geometry.thumbY(state.normalizedValue()), style.thumbSize(), style.thumbSize());
    }

    private void drawValueText(PApplet p, SliderGeometry geometry, SliderViewState state, SliderRenderStyle style) {
        if (!style.showValueText() || !state.showText() || state.displayText() == null || state.displayText().isEmpty()) {
            return;
        }

        p.fill(style.textColor());
        p.noStroke();
        if (geometry.orientation() == SliderOrientation.HORIZONTAL) {
            p.textAlign(PConstants.CENTER, PConstants.CENTER);
            p.text(state.displayText(), geometry.x(), geometry.y() + (geometry.height() * 0.8f));
            return;
        }

        p.textAlign(PConstants.LEFT, PConstants.CENTER);
        p.text(state.displayText(), geometry.x() + (geometry.width() * 0.8f), geometry.y());
    }
}
