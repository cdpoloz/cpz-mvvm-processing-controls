package com.cpz.processing.controls.controls.button.style.render;

import com.cpz.processing.controls.controls.button.style.ButtonRenderStyle;
import com.cpz.processing.controls.core.style.TypographySupport;
import java.util.Objects;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Renderer that uses a PNG alpha mask as the complete button shape.
 *
 * <p>The source RGB channels are ignored. The source alpha channel is retained
 * and tinted with the already resolved button fill color, so normal, hover,
 * pressed, and disabled state colors remain owned by the button style. The
 * image is scaled uniformly and centered inside the button bounds without
 * cropping or deformation. Button text is drawn afterwards on top of the
 * image.</p>
 *
 * <p>Stroke color, stroke weight, and corner radius do not affect this raster
 * renderer.</p>
 *
 * @author CPZ
 */
public final class PngButtonRenderer implements ButtonRenderer {
   private final PImage image;

   /**
    * Creates a PNG button renderer and loads its alpha mask once.
    *
    * <p>A {@code null} or empty path creates an inert renderer. A missing or
    * invalid image also leaves the renderer inert, matching the resource
    * failure behavior of {@link SvgButtonRenderer}.</p>
    *
    * @param sketch sketch used to load the Processing image resource
    * @param path Processing resource path
    */
   public PngButtonRenderer(PApplet sketch, String path) {
      this.image = loadAndNormalizePng(sketch, path);
   }

   /**
    * Draws the normalized PNG mask and then the button text.
    *
    * @param sketch sketch used for drawing
    * @param x horizontal center of the button
    * @param y vertical center of the button
    * @param width button width
    * @param height button height
    * @param renderStyle already resolved button render style
    */
   @Override
   public void render(PApplet sketch, float x, float y, float width, float height, ButtonRenderStyle renderStyle) {
      String text = renderStyle.text();
      if (text == null) {
         text = "";
      }

      TypographySupport.prepareStyleScope(sketch, renderStyle.showText() ? renderStyle.font() : null);
      sketch.pushStyle();
      try {
         if (this.image != null && this.image.width > 0 && this.image.height > 0 && width > 0.0F && height > 0.0F) {
            float scale = Math.min(width / this.image.width, height / this.image.height);
            float imageWidth = this.image.width * scale;
            float imageHeight = this.image.height * scale;
            float imageX = x - imageWidth * 0.5F;
            float imageY = y - imageHeight * 0.5F;

            sketch.imageMode(PApplet.CORNER);
            sketch.tint(renderStyle.fillColor());
            try {
               sketch.image(this.image, imageX, imageY, imageWidth, imageHeight);
            } finally {
               sketch.noTint();
            }
         }

         if (renderStyle.showText()) {
            TypographySupport.apply(sketch, renderStyle.font(), renderStyle.textSize());
            sketch.fill(renderStyle.textColor());
            sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
            sketch.text(text, x, y);
         }
      } finally {
         sketch.popStyle();
      }
   }

   private static PImage loadAndNormalizePng(PApplet sketch, String path) {
      if (sketch == null || path == null || path.isEmpty()) {
         return null;
      }

      PImage source = sketch.loadImage(path);
      if (source == null && path.startsWith("data/")) {
         source = sketch.loadImage(path.substring("data/".length()));
      }
      if (source == null || source.width <= 0 || source.height <= 0) {
         return null;
      }
      return normalizePngMask(source);
   }

   static PImage normalizePngMask(PImage source) {
      Objects.requireNonNull(source, "source");
      if (source.width <= 0 || source.height <= 0) {
         throw new IllegalArgumentException(
               "Invalid PNG image dimensions: " + source.width + "x" + source.height + "."
         );
      }

      source.loadPixels();
      PImage normalized = new PImage(source.width, source.height, PApplet.ARGB);
      normalized.loadPixels();
      for (int i = 0; i < source.pixels.length; i++) {
         int alpha = source.pixels[i] & 0xFF000000;
         normalized.pixels[i] = alpha | 0x00FFFFFF;
      }
      normalized.updatePixels();
      return normalized;
   }
}
