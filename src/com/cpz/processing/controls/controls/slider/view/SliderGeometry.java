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
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    * @param var4 parameter used by this operation
    * @param var5 parameter used by this operation
    * @param var6 parameter used by this operation
    * @param var7 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public SliderGeometry(float var1, float var2, float var3, float var4, SliderOrientation var5, float var6, float var7) {
      this.x = var1;
      this.y = var2;
      this.width = var3;
      this.height = var4;
      this.orientation = var5 == null ? SliderOrientation.HORIZONTAL : var5;
      float var8 = Math.min(var6, var7);
      float var9 = Math.max(var6, var7);
      this.trackStart = var8;
      this.trackEnd = var9;
      this.trackLength = Math.max(0.0F, var9 - var8);
   }

   /**
    * Performs normalized to position.
    *
    * @param var1 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public float normalizedToPosition(float var1) {
      float var2 = clamp(var1, 0.0F, 1.0F);
      return this.trackStart + this.trackLength * var2;
   }

   /**
    * Performs position to normalized.
    *
    * @param var1 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public float positionToNormalized(float var1) {
      if (this.trackLength <= 0.0F) {
         return 0.0F;
      } else {
         float var2 = clamp(var1, this.trackStart, this.trackEnd);
         return (var2 - this.trackStart) / this.trackLength;
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
    * @param var1 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public float thumbX(float var1) {
      return this.orientation == SliderOrientation.HORIZONTAL ? this.normalizedToPosition(var1) : this.x;
   }

   /**
    * Performs thumb y.
    *
    * @param var1 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public float thumbY(float var1) {
      return this.orientation == SliderOrientation.VERTICAL ? this.y + this.height * 0.5F - this.normalizedToPosition(var1) : this.y;
   }

   private static float clamp(float var0, float var1, float var2) {
      return Math.max(var1, Math.min(var2, var0));
   }
}
