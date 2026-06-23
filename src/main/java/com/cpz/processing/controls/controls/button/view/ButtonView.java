package com.cpz.processing.controls.controls.button.view;

import com.cpz.processing.controls.controls.button.state.ButtonViewState;
import com.cpz.processing.controls.controls.button.style.ButtonDefaultStyles;
import com.cpz.processing.controls.controls.button.style.ButtonStyle;
import com.cpz.processing.controls.controls.button.viewmodel.ButtonViewModel;
import com.cpz.processing.controls.core.input.PointerInteractable;
import com.cpz.processing.controls.core.input.hit.RectHitTest;
import com.cpz.processing.controls.core.input.hit.interfaces.HitTest;
import com.cpz.processing.controls.core.layout.LayoutConfig;
import com.cpz.processing.controls.core.layout.LayoutResolver;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.view.ControlView;
import processing.core.PApplet;

/**
 * View for button view.
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
public final class ButtonView implements ControlView, PointerInteractable {
   private final PApplet sketch;
   private final ButtonViewModel viewModel;
   private float x;
   private float y;
   private float width;
   private float height;
   private ButtonStyle style;
   private HitTest hitTest;
   private LayoutConfig layoutConfig;

   /**
    * Creates a button view.
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
   public ButtonView(PApplet sketch, ButtonViewModel viewModel, float x, float y, float width, float height) {
      this.sketch = sketch;
      this.viewModel = viewModel;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.style = ButtonDefaultStyles.primary();
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
         this.applyLayoutIfNeeded();
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
    * Updates layout config.
    *
    * @param layout new layout config
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setLayoutConfig(LayoutConfig layout) {
      this.layoutConfig = layout;
      this.applyLayoutIfNeeded();
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
   public void setStyle(ButtonStyle style) {
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
      this.applyLayoutIfNeeded();
      return this.hitTest.contains(x, y);
   }

   /**
    * Returns x.
    *
    * @return current x
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getX() {
      return this.x;
   }

   /**
    * Returns y.
    *
    * @return current y
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getY() {
      return this.y;
   }

   /**
    * Returns width.
    *
    * @return current width
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getWidth() {
      return this.width;
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
      return this.height;
   }

   private ButtonViewState buildViewState() {
      return new ButtonViewState(this.x, this.y, this.width, this.height, this.viewModel.getText(), this.viewModel.isShowText(), this.viewModel.isEnabled(), this.viewModel.isHovered(), this.viewModel.isPressed());
   }

   private void notifyLayoutChanged() {
      this.hitTest.onLayout(this.x, this.y, this.width, this.height);
   }

   private void applyLayoutIfNeeded() {
      if (this.layoutConfig != null) {
         float value = LayoutResolver.resolveX(this.layoutConfig, this.width, (float)this.sketch.width);
         float value2 = LayoutResolver.resolveY(this.layoutConfig, this.height, (float)this.sketch.height);
         this.x = value + this.width * 0.5F;
         this.y = value2 + this.height * 0.5F;
         this.notifyLayoutChanged();
      }
   }
}
