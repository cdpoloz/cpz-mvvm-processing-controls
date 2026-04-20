package com.cpz.processing.controls.controls.slider.style.render;

import com.cpz.processing.controls.controls.slider.model.SliderOrientation;
import com.cpz.processing.controls.controls.slider.state.SliderViewState;
import com.cpz.processing.controls.controls.slider.style.SliderRenderStyle;
import com.cpz.processing.controls.controls.slider.style.SvgColorMode;
import com.cpz.processing.controls.controls.slider.view.SliderGeometry;
import processing.core.PApplet;
import processing.core.PShape;

/**
 * Renderer for slider renderer.
 *
 * Responsibilities:
 * - Draw already resolved frame data.
 * - Keep rendering concerns separate from state decisions.
 *
 * Behavior:
 * - Uses already resolved state and does not decide behavior.
 *
 * Notes:
 * - This type belongs to the visual styling pipeline.
 *
 * @author CPZ
 */
public final class SliderRenderer {
   /**
    * Renders the current frame.
    *
    * @param sketch parameter used by this operation
    * @param sliderGeometry parameter used by this operation
    * @param state parameter used by this operation
    * @param renderStyle parameter used by this operation
    *
    * Behavior:
    * - Uses already available state and does not define business rules.
    */
   public void render(PApplet sketch, SliderGeometry sliderGeometry, SliderViewState state, SliderRenderStyle renderStyle) {
      sketch.pushStyle();
      this.drawBaseTrack(sketch, sliderGeometry, renderStyle);
      this.drawActiveTrack(sketch, sliderGeometry, state, renderStyle);
      this.drawThumb(sketch, sliderGeometry, state, renderStyle);
      this.drawValueText(sketch, sliderGeometry, state, renderStyle);
      sketch.popStyle();
   }

   private void drawBaseTrack(PApplet sketch, SliderGeometry sliderGeometry, SliderRenderStyle renderStyle) {
      sketch.rectMode(0);
      sketch.fill(renderStyle.trackColor());
      sketch.stroke(renderStyle.trackStrokeColor());
      sketch.strokeWeight(renderStyle.trackStrokeWeight());
      if (sliderGeometry.orientation() == SliderOrientation.HORIZONTAL) {
         float value = sliderGeometry.trackEndX() - sliderGeometry.trackStartX();
         float value2 = sliderGeometry.y() - renderStyle.trackThickness() * 0.5F;
         sketch.rect(sliderGeometry.trackStartX(), value2, value, renderStyle.trackThickness(), renderStyle.trackThickness() * 0.5F);
      } else {
         float value3 = sliderGeometry.trackStartY() - sliderGeometry.trackEndY();
         float value4 = sliderGeometry.x() - renderStyle.trackThickness() * 0.5F;
         sketch.rect(value4, sliderGeometry.trackEndY(), renderStyle.trackThickness(), value3, renderStyle.trackThickness() * 0.5F);
      }
   }

   private void drawActiveTrack(PApplet sketch, SliderGeometry sliderGeometry, SliderViewState state, SliderRenderStyle renderStyle) {
      sketch.noStroke();
      sketch.fill(renderStyle.activeTrackColor());
      float value = sliderGeometry.thumbX(state.normalizedValue());
      float value2 = sliderGeometry.thumbY(state.normalizedValue());
      if (sliderGeometry.orientation() == SliderOrientation.HORIZONTAL) {
         float value3 = value - sliderGeometry.trackStartX();
         float value4 = sliderGeometry.y() - renderStyle.trackThickness() * 0.5F;
         sketch.rect(sliderGeometry.trackStartX(), value4, value3, renderStyle.trackThickness(), renderStyle.trackThickness() * 0.5F);
      } else {
         float value5 = sliderGeometry.trackStartY() - value2;
         float value6 = sliderGeometry.x() - renderStyle.trackThickness() * 0.5F;
         sketch.rect(value6, value2, renderStyle.trackThickness(), value5, renderStyle.trackThickness() * 0.5F);
      }
   }

   private void drawThumb(PApplet sketch, SliderGeometry sliderGeometry, SliderViewState state, SliderRenderStyle renderStyle) {
      PShape shape = renderStyle.thumbShape();
      if (shape != null) {
         this.drawSvgThumb(sketch, sliderGeometry, state, renderStyle, shape);
      } else {
         this.drawFallbackThumb(sketch, sliderGeometry, state, renderStyle);
      }
   }

   private void drawSvgThumb(PApplet sketch, SliderGeometry sliderGeometry, SliderViewState state, SliderRenderStyle renderStyle, PShape shape) {
      sketch.shapeMode(3);
      if (renderStyle.svgColorMode() == SvgColorMode.USE_RENDER_STYLE) {
         shape.disableStyle();
         sketch.fill(renderStyle.thumbColor());
         sketch.stroke(renderStyle.thumbStrokeColor());
         sketch.strokeWeight(renderStyle.thumbStrokeWeight());
      } else {
         shape.enableStyle();
      }

      sketch.shape(shape, sliderGeometry.thumbX(state.normalizedValue()), sliderGeometry.thumbY(state.normalizedValue()), renderStyle.thumbSize(), renderStyle.thumbSize());
   }

   private void drawFallbackThumb(PApplet sketch, SliderGeometry sliderGeometry, SliderViewState state, SliderRenderStyle renderStyle) {
      sketch.ellipseMode(3);
      sketch.fill(renderStyle.thumbColor());
      sketch.stroke(renderStyle.thumbStrokeColor());
      sketch.strokeWeight(renderStyle.thumbStrokeWeight());
      sketch.ellipse(sliderGeometry.thumbX(state.normalizedValue()), sliderGeometry.thumbY(state.normalizedValue()), renderStyle.thumbSize(), renderStyle.thumbSize());
   }

   private void drawValueText(PApplet sketch, SliderGeometry sliderGeometry, SliderViewState state, SliderRenderStyle renderStyle) {
      if (renderStyle.showValueText() && state.showText() && state.displayText() != null && !state.displayText().isEmpty()) {
         sketch.fill(renderStyle.textColor());
         sketch.noStroke();
         if (sliderGeometry.orientation() == SliderOrientation.HORIZONTAL) {
            sketch.textAlign(3, 3);
            sketch.text(state.displayText(), sliderGeometry.x(), sliderGeometry.y() + sliderGeometry.height() * 0.8F);
         } else {
            sketch.textAlign(37, 3);
            sketch.text(state.displayText(), sliderGeometry.x() + sliderGeometry.width() * 0.8F, sliderGeometry.y());
         }
      }
   }
}
