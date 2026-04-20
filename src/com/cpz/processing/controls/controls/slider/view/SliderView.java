package com.cpz.processing.controls.controls.slider.view;

import com.cpz.processing.controls.controls.slider.model.SliderOrientation;
import com.cpz.processing.controls.controls.slider.state.SliderViewState;
import com.cpz.processing.controls.controls.slider.style.SliderDefaultStyles;
import com.cpz.processing.controls.controls.slider.style.SliderStyle;
import com.cpz.processing.controls.controls.slider.viewmodel.SliderViewModel;
import com.cpz.processing.controls.core.input.PointerInteractable;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.view.ControlView;
import processing.core.PApplet;

/**
 * View for slider view.
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
public final class SliderView implements ControlView, PointerInteractable {
   private final PApplet sketch;
   private final SliderViewModel viewModel;
   private float x;
   private float y;
   private float width;
   private float height;
   private SliderOrientation orientation;
   private SliderStyle style;

   /**
    * Creates a slider view.
    *
    * @param sketch parameter used by this operation
    * @param viewModel parameter used by this operation
    * @param x parameter used by this operation
    * @param y parameter used by this operation
    * @param width parameter used by this operation
    * @param height parameter used by this operation
    * @param sliderOrientation parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public SliderView(PApplet sketch, SliderViewModel viewModel, float x, float y, float width, float height, SliderOrientation sliderOrientation) {
      this.sketch = sketch;
      this.viewModel = viewModel;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.orientation = sliderOrientation == null ? SliderOrientation.HORIZONTAL : sliderOrientation;
      this.style = SliderDefaultStyles.standard();
      this.notifyLayoutChanged();
   }

   /**
    * Draws the current frame.
    *
    * Behavior:
    * - Uses already available state and does not define business rules.
    */
   public void draw() {
      if (this.viewModel.isVisible()) {
         SliderGeometry sliderGeometry = this.buildGeometry();
         ThemeSnapshot snapshot = this.style.getThemeSnapshot();
         this.style.render(this.sketch, this.buildViewState(), snapshot, sliderGeometry);
      }
   }

   /**
    * Updates position.
    *
    * @param x new position
    * @param y parameter used by this operation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setPosition(float x, float y) {
      this.x = x;
      this.y = y;
      this.notifyLayoutChanged();
   }

   /**
    * Updates size.
    *
    * @param width new size
    * @param height parameter used by this operation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setSize(float width, float height) {
      this.width = width;
      this.height = height;
      this.notifyLayoutChanged();
   }

   /**
    * Updates orientation.
    *
    * @param sliderOrientation new orientation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setOrientation(SliderOrientation sliderOrientation) {
      if (sliderOrientation != null) {
         this.orientation = sliderOrientation;
         this.notifyLayoutChanged();
      }
   }

   /**
    * Updates style.
    *
    * @param style new style
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setStyle(SliderStyle style) {
      if (style != null) {
         this.style = style;
      }

   }

   /**
    * Returns orientation.
    *
    * @return current orientation
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public SliderOrientation getOrientation() {
      return this.orientation;
   }

   /**
    * Performs contains.
    *
    * @param x parameter used by this operation
    * @param y parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public boolean contains(float x, float y) {
      float halfWidth = this.width * 0.5F;
      float halfHeight = this.height * 0.5F;
      return x >= this.x - halfWidth && x <= this.x + halfWidth && y >= this.y - halfHeight && y <= this.y + halfHeight;
   }

   private SliderViewState buildViewState() {
      return new SliderViewState(this.viewModel.getNormalizedValue(), this.viewModel.isHovered(), this.viewModel.isPressed(), this.viewModel.isDragging(), this.viewModel.isShowText(), this.viewModel.getFormattedValue(), this.viewModel.isEnabled());
   }

   private SliderGeometry buildGeometry() {
      return this.orientation == SliderOrientation.VERTICAL ? new SliderGeometry(this.x, this.y, this.width, this.height, this.orientation, 0.0F, this.height) : new SliderGeometry(this.x, this.y, this.width, this.height, this.orientation, this.x - this.width * 0.5F, this.x + this.width * 0.5F);
   }

   /**
    * Performs to normalized value.
    *
    * @param x parameter used by this operation
    * @param y parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public float toNormalizedValue(float x, float y) {
      SliderGeometry halfWidth = this.buildGeometry();
      if (this.orientation == SliderOrientation.VERTICAL) {
         float halfHeight = this.y + this.height * 0.5F - y;
         return halfWidth.positionToNormalized(halfHeight);
      } else {
         return halfWidth.positionToNormalized(x);
      }
   }

   private void notifyLayoutChanged() {
   }
}
