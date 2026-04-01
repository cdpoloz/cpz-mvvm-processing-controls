package com.cpz.processing.controls.controls.slider.view;

import com.cpz.processing.controls.controls.slider.model.SliderOrientation;

public final class SliderGeometry {
   private final float x;
   private final float y;
   private final float width;
   private final float height;
   private final SliderOrientation orientation;
   private final float trackStart;
   private final float trackEnd;
   private final float trackLength;

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

   public float normalizedToPosition(float var1) {
      float var2 = clamp(var1, 0.0F, 1.0F);
      return this.trackStart + this.trackLength * var2;
   }

   public float positionToNormalized(float var1) {
      if (this.trackLength <= 0.0F) {
         return 0.0F;
      } else {
         float var2 = clamp(var1, this.trackStart, this.trackEnd);
         return (var2 - this.trackStart) / this.trackLength;
      }
   }

   public float x() {
      return this.x;
   }

   public float y() {
      return this.y;
   }

   public float width() {
      return this.width;
   }

   public float height() {
      return this.height;
   }

   public SliderOrientation orientation() {
      return this.orientation;
   }

   public float trackStart() {
      return this.trackStart;
   }

   public float trackEnd() {
      return this.trackEnd;
   }

   public float trackLength() {
      return this.trackLength;
   }

   public float trackStartX() {
      return this.orientation == SliderOrientation.HORIZONTAL ? this.trackStart : this.x;
   }

   public float trackStartY() {
      return this.orientation == SliderOrientation.VERTICAL ? this.y + this.height * 0.5F : this.y;
   }

   public float trackEndX() {
      return this.orientation == SliderOrientation.HORIZONTAL ? this.trackEnd : this.x;
   }

   public float trackEndY() {
      return this.orientation == SliderOrientation.VERTICAL ? this.y - this.height * 0.5F : this.y;
   }

   public float thumbX(float var1) {
      return this.orientation == SliderOrientation.HORIZONTAL ? this.normalizedToPosition(var1) : this.x;
   }

   public float thumbY(float var1) {
      return this.orientation == SliderOrientation.VERTICAL ? this.y + this.height * 0.5F - this.normalizedToPosition(var1) : this.y;
   }

   private static float clamp(float var0, float var1, float var2) {
      return Math.max(var1, Math.min(var2, var0));
   }
}
