package com.cpz.processing.controls.controls.slider.view;

import com.cpz.processing.controls.controls.slider.model.SliderOrientation;

/**
 * View for slider geometry.
 *
 * Responsibilities:
 * - Own layout, hit testing, and frame-state construction.
 * - Delegate visual resolution to styles and renderers.
 *
 * Behavior:
 * - Does not own business rules or persistent model state.
 *
 * Notes:
 * - This type belongs to the MVVM View layer.
 *
 * @author CPZ
 */
public final class SliderGeometry {
   private final float x;
   private final float y;
   private final float width;
   private final float height;
   private final SliderOrientation orientation;
   private final float trackStart;
   private final float trackEnd;
   private final float trackLength;

   /**
    * Creates a slider geometry.
    *
    * @param x parameter used by this operation
    * @param y parameter used by this operation
    * @param width parameter used by this operation
    * @param height parameter used by this operation
    * @param sliderOrientation parameter used by this operation
    * @param radius parameter used by this operation
    * @param value parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public SliderGeometry(float x, float y, float width, float height, SliderOrientation sliderOrientation, float radius, float value) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.orientation = sliderOrientation == null ? SliderOrientation.HORIZONTAL : sliderOrientation;
      float value2 = Math.min(radius, value);
      float value3 = Math.max(radius, value);
      this.trackStart = value2;
      this.trackEnd = value3;
      this.trackLength = Math.max(0.0F, value3 - value2);
   }

   /**
    * Performs normalized to position.
    *
    * @param normalizedValue parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public float normalizedToPosition(float normalizedValue) {
      float value4 = clamp(normalizedValue, 0.0F, 1.0F);
      return this.trackStart + this.trackLength * value4;
   }

   /**
    * Performs position to normalized.
    *
    * @param normalizedValue parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public float positionToNormalized(float normalizedValue) {
      if (this.trackLength <= 0.0F) {
         return 0.0F;
      } else {
         float value4 = clamp(normalizedValue, this.trackStart, this.trackEnd);
         return (value4 - this.trackStart) / this.trackLength;
      }
   }

   /**
    * Performs x.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public float x() {
      return this.x;
   }

   /**
    * Performs y.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public float y() {
      return this.y;
   }

   /**
    * Performs width.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public float width() {
      return this.width;
   }

   /**
    * Performs height.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public float height() {
      return this.height;
   }

   /**
    * Performs orientation.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public SliderOrientation orientation() {
      return this.orientation;
   }

   /**
    * Performs track start.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public float trackStart() {
      return this.trackStart;
   }

   /**
    * Performs track end.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public float trackEnd() {
      return this.trackEnd;
   }

   /**
    * Performs track length.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public float trackLength() {
      return this.trackLength;
   }

   /**
    * Performs track start x.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public float trackStartX() {
      return this.orientation == SliderOrientation.HORIZONTAL ? this.trackStart : this.x;
   }

   /**
    * Performs track start y.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public float trackStartY() {
      return this.orientation == SliderOrientation.VERTICAL ? this.y + this.height * 0.5F : this.y;
   }

   /**
    * Performs track end x.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public float trackEndX() {
      return this.orientation == SliderOrientation.HORIZONTAL ? this.trackEnd : this.x;
   }

   /**
    * Performs track end y.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public float trackEndY() {
      return this.orientation == SliderOrientation.VERTICAL ? this.y - this.height * 0.5F : this.y;
   }

   /**
    * Performs thumb x.
    *
    * @param normalizedValue parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public float thumbX(float normalizedValue) {
      return this.orientation == SliderOrientation.HORIZONTAL ? this.normalizedToPosition(normalizedValue) : this.x;
   }

   /**
    * Performs thumb y.
    *
    * @param normalizedValue parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public float thumbY(float normalizedValue) {
      return this.orientation == SliderOrientation.VERTICAL ? this.y + this.height * 0.5F - this.normalizedToPosition(normalizedValue) : this.y;
   }

   private static float clamp(float x, float y, float width) {
      return Math.max(y, Math.min(width, x));
   }
}
