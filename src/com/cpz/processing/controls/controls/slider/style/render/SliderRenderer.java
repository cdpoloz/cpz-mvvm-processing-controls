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
 */
public final class SliderRenderer {
   /**
    * Renders the current frame.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    * @param var4 parameter used by this operation
    *
    * Behavior:
    * - Uses already available state and does not define business rules.
    */
   public void render(PApplet var1, SliderGeometry var2, SliderViewState var3, SliderRenderStyle var4) {
      var1.pushStyle();
      this.drawBaseTrack(var1, var2, var4);
      this.drawActiveTrack(var1, var2, var3, var4);
      this.drawThumb(var1, var2, var3, var4);
      this.drawValueText(var1, var2, var3, var4);
      var1.popStyle();
   }

   private void drawBaseTrack(PApplet var1, SliderGeometry var2, SliderRenderStyle var3) {
      var1.rectMode(0);
      var1.fill(var3.trackColor());
      var1.stroke(var3.trackStrokeColor());
      var1.strokeWeight(var3.trackStrokeWeight());
      if (var2.orientation() == SliderOrientation.HORIZONTAL) {
         float var6 = var2.trackEndX() - var2.trackStartX();
         float var7 = var2.y() - var3.trackThickness() * 0.5F;
         var1.rect(var2.trackStartX(), var7, var6, var3.trackThickness(), var3.trackThickness() * 0.5F);
      } else {
         float var4 = var2.trackStartY() - var2.trackEndY();
         float var5 = var2.x() - var3.trackThickness() * 0.5F;
         var1.rect(var5, var2.trackEndY(), var3.trackThickness(), var4, var3.trackThickness() * 0.5F);
      }
   }

   private void drawActiveTrack(PApplet var1, SliderGeometry var2, SliderViewState var3, SliderRenderStyle var4) {
      var1.noStroke();
      var1.fill(var4.activeTrackColor());
      float var5 = var2.thumbX(var3.normalizedValue());
      float var6 = var2.thumbY(var3.normalizedValue());
      if (var2.orientation() == SliderOrientation.HORIZONTAL) {
         float var9 = var5 - var2.trackStartX();
         float var10 = var2.y() - var4.trackThickness() * 0.5F;
         var1.rect(var2.trackStartX(), var10, var9, var4.trackThickness(), var4.trackThickness() * 0.5F);
      } else {
         float var7 = var2.trackStartY() - var6;
         float var8 = var2.x() - var4.trackThickness() * 0.5F;
         var1.rect(var8, var6, var4.trackThickness(), var7, var4.trackThickness() * 0.5F);
      }
   }

   private void drawThumb(PApplet var1, SliderGeometry var2, SliderViewState var3, SliderRenderStyle var4) {
      PShape var5 = var4.thumbShape();
      if (var5 != null) {
         this.drawSvgThumb(var1, var2, var3, var4, var5);
      } else {
         this.drawFallbackThumb(var1, var2, var3, var4);
      }
   }

   private void drawSvgThumb(PApplet var1, SliderGeometry var2, SliderViewState var3, SliderRenderStyle var4, PShape var5) {
      var1.shapeMode(3);
      if (var4.svgColorMode() == SvgColorMode.USE_RENDER_STYLE) {
         var5.disableStyle();
         var1.fill(var4.thumbColor());
         var1.stroke(var4.thumbStrokeColor());
         var1.strokeWeight(var4.thumbStrokeWeight());
      } else {
         var5.enableStyle();
      }

      var1.shape(var5, var2.thumbX(var3.normalizedValue()), var2.thumbY(var3.normalizedValue()), var4.thumbSize(), var4.thumbSize());
   }

   private void drawFallbackThumb(PApplet var1, SliderGeometry var2, SliderViewState var3, SliderRenderStyle var4) {
      var1.ellipseMode(3);
      var1.fill(var4.thumbColor());
      var1.stroke(var4.thumbStrokeColor());
      var1.strokeWeight(var4.thumbStrokeWeight());
      var1.ellipse(var2.thumbX(var3.normalizedValue()), var2.thumbY(var3.normalizedValue()), var4.thumbSize(), var4.thumbSize());
   }

   private void drawValueText(PApplet var1, SliderGeometry var2, SliderViewState var3, SliderRenderStyle var4) {
      if (var4.showValueText() && var3.showText() && var3.displayText() != null && !var3.displayText().isEmpty()) {
         var1.fill(var4.textColor());
         var1.noStroke();
         if (var2.orientation() == SliderOrientation.HORIZONTAL) {
            var1.textAlign(3, 3);
            var1.text(var3.displayText(), var2.x(), var2.y() + var2.height() * 0.8F);
         } else {
            var1.textAlign(37, 3);
            var1.text(var3.displayText(), var2.x() + var2.width() * 0.8F, var2.y());
         }
      }
   }
}
