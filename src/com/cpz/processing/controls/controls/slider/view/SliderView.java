package com.cpz.processing.controls.controls.slider.view;

import com.cpz.processing.controls.controls.slider.model.SliderOrientation;
import com.cpz.processing.controls.controls.slider.state.SliderViewState;
import com.cpz.processing.controls.controls.slider.style.SliderDefaultStyles;
import com.cpz.processing.controls.controls.slider.style.SliderStyle;
import com.cpz.processing.controls.controls.slider.viewmodel.SliderViewModel;
import com.cpz.processing.controls.core.input.PointerInteractable;
import com.cpz.processing.controls.core.view.ControlView;
import processing.core.PApplet;

public final class SliderView implements ControlView, PointerInteractable {
   private final PApplet sketch;
   private final SliderViewModel viewModel;
   private float x;
   private float y;
   private float width;
   private float height;
   private SliderOrientation orientation;
   private SliderStyle style;

   public SliderView(PApplet var1, SliderViewModel var2, float var3, float var4, float var5, float var6, SliderOrientation var7) {
      this.sketch = var1;
      this.viewModel = var2;
      this.x = var3;
      this.y = var4;
      this.width = var5;
      this.height = var6;
      this.orientation = var7 == null ? SliderOrientation.HORIZONTAL : var7;
      this.style = SliderDefaultStyles.standard();
      this.notifyLayoutChanged();
   }

   public void draw() {
      if (this.viewModel.isVisible()) {
         SliderGeometry var1 = this.buildGeometry();
         this.style.render(this.sketch, this.buildViewState(), var1);
      }
   }

   public void setPosition(float var1, float var2) {
      this.x = var1;
      this.y = var2;
      this.notifyLayoutChanged();
   }

   public void setSize(float var1, float var2) {
      this.width = var1;
      this.height = var2;
      this.notifyLayoutChanged();
   }

   public void setOrientation(SliderOrientation var1) {
      if (var1 != null) {
         this.orientation = var1;
         this.notifyLayoutChanged();
      }
   }

   public void setStyle(SliderStyle var1) {
      if (var1 != null) {
         this.style = var1;
      }

   }

   public SliderOrientation getOrientation() {
      return this.orientation;
   }

   public boolean contains(float var1, float var2) {
      float var3 = this.width * 0.5F;
      float var4 = this.height * 0.5F;
      return var1 >= this.x - var3 && var1 <= this.x + var3 && var2 >= this.y - var4 && var2 <= this.y + var4;
   }

   private SliderViewState buildViewState() {
      return new SliderViewState(this.viewModel.getNormalizedValue(), this.viewModel.isHovered(), this.viewModel.isPressed(), this.viewModel.isDragging(), this.viewModel.isShowText(), this.viewModel.getFormattedValue(), this.viewModel.isEnabled());
   }

   private SliderGeometry buildGeometry() {
      return this.orientation == SliderOrientation.VERTICAL ? new SliderGeometry(this.x, this.y, this.width, this.height, this.orientation, 0.0F, this.height) : new SliderGeometry(this.x, this.y, this.width, this.height, this.orientation, this.x - this.width * 0.5F, this.x + this.width * 0.5F);
   }

   public float toNormalizedValue(float var1, float var2) {
      SliderGeometry var3 = this.buildGeometry();
      if (this.orientation == SliderOrientation.VERTICAL) {
         float var4 = this.y + this.height * 0.5F - var2;
         return var3.positionToNormalized(var4);
      } else {
         return var3.positionToNormalized(var1);
      }
   }

   private void notifyLayoutChanged() {
   }
}
