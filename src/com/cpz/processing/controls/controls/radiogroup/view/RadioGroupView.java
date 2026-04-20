package com.cpz.processing.controls.controls.radiogroup.view;

import com.cpz.processing.controls.controls.radiogroup.state.RadioGroupItemViewState;
import com.cpz.processing.controls.controls.radiogroup.state.RadioGroupViewState;
import com.cpz.processing.controls.controls.radiogroup.style.RadioGroupDefaultStyles;
import com.cpz.processing.controls.controls.radiogroup.style.RadioGroupStyle;
import com.cpz.processing.controls.controls.radiogroup.viewmodel.RadioGroupViewModel;
import com.cpz.processing.controls.core.input.PointerInteractable;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.view.ControlView;
import java.util.ArrayList;
import processing.core.PApplet;

/**
 * View for radio group view.
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
public final class RadioGroupView implements ControlView, PointerInteractable {
   private final PApplet sketch;
   private final RadioGroupViewModel viewModel;
   private float x;
   private float y;
   private float width;
   private float itemHeight;
   private float itemSpacing;
   private RadioGroupStyle style;
   private boolean customItemHeight;
   private boolean customItemSpacing;

   /**
    * Creates a radio group view.
    *
    * @param sketch parameter used by this operation
    * @param viewModel parameter used by this operation
    * @param x parameter used by this operation
    * @param y parameter used by this operation
    * @param width parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public RadioGroupView(PApplet sketch, RadioGroupViewModel viewModel, float x, float y, float width) {
      this.sketch = sketch;
      this.viewModel = viewModel;
      this.x = x;
      this.y = y;
      this.width = width;
      this.style = RadioGroupDefaultStyles.standard();
      this.applyConfiguredLayoutDefaults();
   }

   /**
    * Draws the current frame.
    *
    * Behavior:
    * - Uses already available state and does not define business rules.
    */
   public void draw() {
      if (this.viewModel.isVisible()) {
         ThemeSnapshot snapshot = this.style.getThemeSnapshot();
         this.style.render(this.sketch, this.buildViewState(), snapshot);
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
   }

   /**
    * Updates width.
    *
    * @param x new width
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setWidth(float x) {
      this.width = x;
   }

   /**
    * Updates item height.
    *
    * @param x new item height
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setItemHeight(float x) {
      this.itemHeight = Math.max(this.style.getMinimumItemHeight(), x);
      this.customItemHeight = true;
   }

   /**
    * Updates item spacing.
    *
    * @param x new item spacing
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setItemSpacing(float x) {
      this.itemSpacing = Math.max(0.0F, x);
      this.customItemSpacing = true;
   }

   /**
    * Updates style.
    *
    * @param style new style
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setStyle(RadioGroupStyle style) {
      if (style != null) {
         this.style = style;
         this.applyConfiguredLayoutDefaults();
      }

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
      return this.getOptionIndexAt(x, y) >= 0;
   }

   /**
    * Returns option index at.
    *
    * @param x parameter used by this operation
    * @param y parameter used by this operation
    * @return current option index at
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getOptionIndexAt(float x, float y) {
      float halfWidth = this.x - this.width * 0.5F;
      float midpoint = this.y - this.itemHeight * 0.5F;
      if (!(x < halfWidth) && !(x > halfWidth + this.width)) {
         for(int index2 = 0; index2 < this.viewModel.getOptions().size(); ++index2) {
            float midpoint2 = midpoint + (float)index2 * (this.itemHeight + this.itemSpacing);
            if (y >= midpoint2 && y <= midpoint2 + this.itemHeight) {
               return index2;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   /**
    * Returns height.
    *
    * @return current height
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getHeight() {
      return this.viewModel.getOptions().isEmpty() ? 0.0F : (float)this.viewModel.getOptions().size() * this.itemHeight + (float)Math.max(0, this.viewModel.getOptions().size() - 1) * this.itemSpacing;
   }

   private RadioGroupViewState buildViewState() {
      ArrayList arrayList = new ArrayList();
      float value = this.y - this.itemHeight * 0.5F;
      float left = this.x - this.width * 0.5F;
      float value2 = this.style.getIndicatorOffsetX();
      float value3 = this.style.getTextOffsetX();

      for(int value4 = 0; value4 < this.viewModel.getOptions().size(); ++value4) {
         float value5 = value + (float)value4 * (this.itemHeight + this.itemSpacing);
         float value6 = value5 + this.itemHeight * 0.5F;
         arrayList.add(new RadioGroupItemViewState(value4, (String)this.viewModel.getOptions().get(value4), this.viewModel.getSelectedIndex() == value4, this.viewModel.getHoveredIndex() == value4, this.viewModel.getPressedIndex() == value4, this.viewModel.isFocused() && this.viewModel.getActiveIndex() == value4, this.x, value6, this.width, this.itemHeight, left + value2, value6, left + value3));
      }

      return new RadioGroupViewState(this.x, this.y, this.width, this.getHeight(), arrayList.size(), this.viewModel.isEnabled(), arrayList);
   }

   private void applyConfiguredLayoutDefaults() {
      if (!this.customItemHeight) {
         this.itemHeight = this.style.getItemHeight();
      } else {
         this.itemHeight = Math.max(this.style.getMinimumItemHeight(), this.itemHeight);
      }

      if (!this.customItemSpacing) {
         this.itemSpacing = this.style.getItemSpacing();
      }

   }
}
