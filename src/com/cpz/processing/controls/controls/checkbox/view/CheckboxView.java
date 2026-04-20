package com.cpz.processing.controls.controls.checkbox.view;

import com.cpz.processing.controls.controls.checkbox.state.CheckboxViewState;
import com.cpz.processing.controls.controls.checkbox.style.CheckboxDefaultStyles;
import com.cpz.processing.controls.controls.checkbox.style.CheckboxStyle;
import com.cpz.processing.controls.controls.checkbox.viewmodel.CheckboxViewModel;
import com.cpz.processing.controls.core.input.PointerInteractable;
import com.cpz.processing.controls.core.input.hit.RectHitTest;
import com.cpz.processing.controls.core.input.hit.interfaces.HitTest;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.view.ControlView;
import processing.core.PApplet;

/**
 * View for checkbox view.
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
public final class CheckboxView implements ControlView, PointerInteractable {
   private final PApplet sketch;
   private final CheckboxViewModel viewModel;
   private float x;
   private float y;
   private float width;
   private float height;
   private CheckboxStyle style;
   private HitTest hitTest;

   /**
    * Creates a checkbox view.
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
   public CheckboxView(PApplet sketch, CheckboxViewModel viewModel, float x, float y, float width) {
      this(sketch, viewModel, x, y, width, width);
   }

   /**
    * Creates a checkbox view.
    *
    * @param sketch parameter used by this operation
    * @param viewModel parameter used by this operation
    * @param x parameter used by this operation
    * @param y parameter used by this operation
    * @param width parameter used by this operation
    * @param height parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public CheckboxView(PApplet sketch, CheckboxViewModel viewModel, float x, float y, float width, float height) {
      this.sketch = sketch;
      this.viewModel = viewModel;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.style = CheckboxDefaultStyles.standard();
      this.hitTest = new RectHitTest();
      this.hitTest.onLayout(x, y, width, height);
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
      this.notifyLayoutChanged();
   }

   /**
    * Updates size.
    *
    * @param width new size
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setSize(float width) {
      this.width = width;
      this.height = width;
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
    * Updates style.
    *
    * @param style new style
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setStyle(CheckboxStyle style) {
      if (style != null) {
         this.style = style;
      }

   }

   /**
    * Updates hit test.
    *
    * @param hitTest new hit test
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setHitTest(HitTest hitTest) {
      if (hitTest != null) {
         this.hitTest = hitTest;
         this.hitTest.onLayout(this.x, this.y, this.width, this.height);
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
      return this.hitTest.contains(x, y);
   }

   private CheckboxViewState buildViewState() {
      return new CheckboxViewState(this.x, this.y, this.width, this.height, this.viewModel.isChecked(), this.viewModel.isHovered(), this.viewModel.isPressed(), this.viewModel.isEnabled());
   }

   private void notifyLayoutChanged() {
      this.hitTest.onLayout(this.x, this.y, this.width, this.height);
   }
}
