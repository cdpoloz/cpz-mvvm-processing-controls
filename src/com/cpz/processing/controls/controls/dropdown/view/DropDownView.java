package com.cpz.processing.controls.controls.dropdown.view;

import com.cpz.processing.controls.controls.dropdown.config.DropDownStyleConfig;
import com.cpz.processing.controls.controls.dropdown.state.DropDownViewState;
import com.cpz.processing.controls.controls.dropdown.style.DefaultDropDownStyle;
import com.cpz.processing.controls.controls.dropdown.viewmodel.DropDownViewModel;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.input.PointerInteractable;
import com.cpz.processing.controls.core.layout.LayoutConfig;
import com.cpz.processing.controls.core.layout.LayoutResolver;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.view.ControlView;
import java.util.List;
import processing.core.PApplet;

/**
 * View for drop down view.
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
public final class DropDownView implements ControlView, PointerInteractable {
   private final PApplet sketch;
   private final DropDownViewModel viewModel;
   private float x;
   private float y;
   private float width;
   private float height;
   private DefaultDropDownStyle style;
   private int hoveredIndex = -1;
   private LayoutConfig layoutConfig;

   /**
    * Creates a drop down view.
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
   public DropDownView(PApplet sketch, DropDownViewModel viewModel, float x, float y, float width, float height) {
      this.sketch = sketch;
      this.viewModel = viewModel;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.style = new DefaultDropDownStyle(new DropDownStyleConfig());
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
   }

   /**
    * Updates style.
    *
    * @param style new style
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setStyle(DefaultDropDownStyle style) {
      if (style != null) {
         this.style = style;
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
      return this.containsBase(x, y) || this.containsExpandedList(x, y);
   }

   public boolean containsBaseBounds(float x, float y) {
      this.applyLayoutIfNeeded();
      return this.containsBase(x, y);
   }

   /**
    * Handles mouse move.
    *
    * @param x parameter used by this operation
    * @param y parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public boolean handleMouseMove(float x, float y) {
      this.applyLayoutIfNeeded();
      if (!this.isInteractive()) {
         this.hoveredIndex = -1;
         this.viewModel.setHovered(false);
         this.viewModel.setPressed(false);
         return false;
      } else {
         this.hoveredIndex = this.resolveHoveredIndex(x, y);
         this.viewModel.setHovered(this.contains(x, y));
         return this.contains(x, y);
      }
   }

   /**
    * Handles mouse press.
    *
    * @param x parameter used by this operation
    * @param y parameter used by this operation
    * @param focusManager parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public boolean handleMousePress(float x, float y, FocusManager focusManager) {
      this.applyLayoutIfNeeded();
      if (!this.isInteractive()) {
         this.hoveredIndex = -1;
         this.viewModel.close();
         this.viewModel.setHovered(false);
         this.viewModel.setPressed(false);
         return false;
      } else if (this.containsBase(x, y)) {
         if (focusManager != null) {
            focusManager.requestFocus(this.viewModel);
         }

         this.viewModel.setPressed(true);
         this.viewModel.toggleExpanded();
         this.hoveredIndex = this.resolveHoveredIndex(x, y);
         this.viewModel.setHovered(true);
         return true;
      } else {
         int index2 = this.resolveHoveredIndex(x, y);
         if (index2 >= 0) {
            if (focusManager != null) {
               focusManager.requestFocus(this.viewModel);
            }

            this.viewModel.selectIndex(index2);
            this.viewModel.close();
            this.viewModel.setPressed(false);
            this.viewModel.setHovered(true);
            this.hoveredIndex = -1;
            return true;
         } else {
            this.hoveredIndex = -1;
            this.viewModel.setPressed(false);
            this.viewModel.setHovered(false);
            this.viewModel.close();
            return false;
         }
      }
   }

   /**
    * Handles mouse release.
    *
    * @param mouseX parameter used by this operation
    * @param mouseY parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseRelease(float mouseX, float mouseY) {
      this.applyLayoutIfNeeded();
      this.viewModel.setPressed(false);
      this.viewModel.setHovered(this.contains(mouseX, mouseY));
   }

   /**
    * Returns whether expanded.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isExpanded() {
      return this.viewModel.isExpanded();
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
      this.applyLayoutIfNeeded();
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
      this.applyLayoutIfNeeded();
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

   /**
    * Returns hovered index.
    *
    * @return current hovered index
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getHoveredIndex() {
      return this.hoveredIndex;
   }

   public void clearHoverState() {
      this.hoveredIndex = -1;
      this.viewModel.setHovered(false);
      this.viewModel.setPressed(false);
   }

   private DropDownViewState buildViewState() {
      return new DropDownViewState(this.x, this.y, this.width, this.height, this.viewModel.isExpanded(), this.viewModel.getSelectedText(), this.viewModel.getItems(), this.viewModel.getSelectedIndex(), this.hoveredIndex, this.viewModel.isHovered(), this.viewModel.isPressed(), this.viewModel.isFocused(), this.viewModel.isEnabled(), this.style.getItemHeight(), this.style.getMaxVisibleItems(), this.style.getTextPadding(), this.style.getArrowPadding(), this.style.getCornerRadius(), this.style.getListCornerRadius(), !this.viewModel.isHovered() && !this.viewModel.isFocused() ? this.style.getStrokeWeight() : this.style.getFocusedStrokeWeight());
   }

   private boolean containsBase(float x, float y) {
      float value = this.width * 0.5F;
      float value2 = this.height * 0.5F;
      return x >= this.x - value && x <= this.x + value && y >= this.y - value2 && y <= this.y + value2;
   }

   private boolean containsExpandedList(float x, float y) {
      if (!this.viewModel.isExpanded()) {
         return false;
      } else {
         List list = this.viewModel.getItems();
         int value = Math.min(list.size(), this.style.getMaxVisibleItems());
         if (value <= 0) {
            return false;
         } else {
            float left = this.x - this.width * 0.5F;
            float value2 = this.y + this.height * 0.5F;
            float value3 = (float)value * this.style.getItemHeight();
            return x >= left && x <= left + this.width && y >= value2 && y <= value2 + value3;
         }
      }
   }

   private int resolveHoveredIndex(float x, float y) {
      if (!this.viewModel.isExpanded()) {
         return -1;
      } else {
         List list = this.viewModel.getItems();
         int value = Math.min(list.size(), this.style.getMaxVisibleItems());
         if (value <= 0) {
            return -1;
         } else {
            float left = this.x - this.width * 0.5F;
            float value2 = this.y + this.height * 0.5F;
            float value3 = this.style.getItemHeight();
            if (!(x < left) && !(x > left + this.width) && !(y < value2) && !(y > value2 + (float)value * value3)) {
               int value4 = (int)((y - value2) / value3);
               return value4 >= 0 && value4 < value ? value4 : -1;
            } else {
               return -1;
            }
         }
      }
   }

   private boolean isInteractive() {
      return this.viewModel.isVisible() && this.viewModel.isEnabled();
   }

   private void applyLayoutIfNeeded() {
      if (this.layoutConfig != null) {
         float value = LayoutResolver.resolveX(this.layoutConfig, this.width, (float)this.sketch.width);
         float value2 = LayoutResolver.resolveY(this.layoutConfig, this.height, (float)this.sketch.height);
         this.x = value + this.width * 0.5F;
         this.y = value2 + this.height * 0.5F;
      }
   }
}
