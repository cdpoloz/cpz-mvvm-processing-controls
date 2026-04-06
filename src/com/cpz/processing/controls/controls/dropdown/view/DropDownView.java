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
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    * @param var4 parameter used by this operation
    * @param var5 parameter used by this operation
    * @param var6 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public DropDownView(PApplet var1, DropDownViewModel var2, float var3, float var4, float var5, float var6) {
      this.sketch = var1;
      this.viewModel = var2;
      this.x = var3;
      this.y = var4;
      this.width = var5;
      this.height = var6;
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
         ThemeSnapshot var1 = this.style.getThemeSnapshot();
         this.style.render(this.sketch, this.buildViewState(), var1);
      }
   }

   /**
    * Updates position.
    *
    * @param var1 new position
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setPosition(float var1, float var2) {
      this.x = var1;
      this.y = var2;
   }

   /**
    * Updates layout config.
    *
    * @param var1 new layout config
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setLayoutConfig(LayoutConfig var1) {
      this.layoutConfig = var1;
      this.applyLayoutIfNeeded();
   }

   /**
    * Updates size.
    *
    * @param var1 new size
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setSize(float var1, float var2) {
      this.width = var1;
      this.height = var2;
   }

   /**
    * Updates style.
    *
    * @param var1 new style
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setStyle(DefaultDropDownStyle var1) {
      if (var1 != null) {
         this.style = var1;
      }

   }

   /**
    * Performs contains.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public boolean contains(float var1, float var2) {
      this.applyLayoutIfNeeded();
      return this.containsBase(var1, var2) || this.containsExpandedList(var1, var2);
   }

   /**
    * Handles mouse move.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public boolean handleMouseMove(float var1, float var2) {
      this.applyLayoutIfNeeded();
      if (!this.isInteractive()) {
         this.hoveredIndex = -1;
         this.viewModel.setHovered(false);
         this.viewModel.setPressed(false);
         return false;
      } else {
         this.hoveredIndex = this.resolveHoveredIndex(var1, var2);
         this.viewModel.setHovered(this.contains(var1, var2));
         return this.contains(var1, var2);
      }
   }

   /**
    * Handles mouse press.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public boolean handleMousePress(float var1, float var2, FocusManager var3) {
      this.applyLayoutIfNeeded();
      if (!this.isInteractive()) {
         this.hoveredIndex = -1;
         this.viewModel.close();
         this.viewModel.setHovered(false);
         this.viewModel.setPressed(false);
         return false;
      } else if (this.containsBase(var1, var2)) {
         if (var3 != null) {
            var3.requestFocus(this.viewModel);
         }

         this.viewModel.setPressed(true);
         this.viewModel.toggleExpanded();
         this.hoveredIndex = this.resolveHoveredIndex(var1, var2);
         this.viewModel.setHovered(true);
         return true;
      } else {
         int var4 = this.resolveHoveredIndex(var1, var2);
         if (var4 >= 0) {
            if (var3 != null) {
               var3.requestFocus(this.viewModel);
            }

            this.viewModel.selectIndex(var4);
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
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseRelease(float var1, float var2) {
      this.applyLayoutIfNeeded();
      this.viewModel.setPressed(false);
      this.viewModel.setHovered(this.contains(var1, var2));
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

   private DropDownViewState buildViewState() {
      return new DropDownViewState(this.x, this.y, this.width, this.height, this.viewModel.isExpanded(), this.viewModel.getSelectedText(), this.viewModel.getItems(), this.viewModel.getSelectedIndex(), this.hoveredIndex, this.viewModel.isHovered(), this.viewModel.isPressed(), this.viewModel.isFocused(), this.viewModel.isEnabled(), this.style.getItemHeight(), this.style.getMaxVisibleItems(), this.style.getTextPadding(), this.style.getArrowPadding(), this.style.getCornerRadius(), this.style.getListCornerRadius(), !this.viewModel.isHovered() && !this.viewModel.isFocused() ? this.style.getStrokeWeight() : this.style.getFocusedStrokeWeight());
   }

   private boolean containsBase(float var1, float var2) {
      float var3 = this.width * 0.5F;
      float var4 = this.height * 0.5F;
      return var1 >= this.x - var3 && var1 <= this.x + var3 && var2 >= this.y - var4 && var2 <= this.y + var4;
   }

   private boolean containsExpandedList(float var1, float var2) {
      if (!this.viewModel.isExpanded()) {
         return false;
      } else {
         List var3 = this.viewModel.getItems();
         int var4 = Math.min(var3.size(), this.style.getMaxVisibleItems());
         if (var4 <= 0) {
            return false;
         } else {
            float var5 = this.x - this.width * 0.5F;
            float var6 = this.y + this.height * 0.5F;
            float var7 = (float)var4 * this.style.getItemHeight();
            return var1 >= var5 && var1 <= var5 + this.width && var2 >= var6 && var2 <= var6 + var7;
         }
      }
   }

   private int resolveHoveredIndex(float var1, float var2) {
      if (!this.viewModel.isExpanded()) {
         return -1;
      } else {
         List var3 = this.viewModel.getItems();
         int var4 = Math.min(var3.size(), this.style.getMaxVisibleItems());
         if (var4 <= 0) {
            return -1;
         } else {
            float var5 = this.x - this.width * 0.5F;
            float var6 = this.y + this.height * 0.5F;
            float var7 = this.style.getItemHeight();
            if (!(var1 < var5) && !(var1 > var5 + this.width) && !(var2 < var6) && !(var2 > var6 + (float)var4 * var7)) {
               int var8 = (int)((var2 - var6) / var7);
               return var8 >= 0 && var8 < var4 ? var8 : -1;
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
         float var1 = LayoutResolver.resolveX(this.layoutConfig, this.width, (float)this.sketch.width);
         float var2 = LayoutResolver.resolveY(this.layoutConfig, this.height, (float)this.sketch.height);
         this.x = var1 + this.width * 0.5F;
         this.y = var2 + this.height * 0.5F;
      }
   }
}
