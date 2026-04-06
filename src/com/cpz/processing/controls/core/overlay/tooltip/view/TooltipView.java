package com.cpz.processing.controls.core.overlay.tooltip.view;

import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.overlay.tooltip.state.TooltipViewState;
import com.cpz.processing.controls.core.overlay.tooltip.style.DefaultTooltipStyle;
import com.cpz.processing.controls.core.overlay.tooltip.style.TooltipRenderStyle;
import com.cpz.processing.controls.core.overlay.tooltip.viewmodel.TooltipViewModel;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.view.ControlView;
import processing.core.PApplet;

/**
 * View for tooltip view.
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
public final class TooltipView implements ControlView {
   private static final float VERTICAL_OFFSET = 10.0F;
   private final PApplet sketch;
   private final TooltipViewModel viewModel;
   private float x;
   private float y;
   private float width;
   private float height;
   private DefaultTooltipStyle style;

   /**
    * Creates a tooltip view.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public TooltipView(PApplet var1, TooltipViewModel var2) {
      this.sketch = var1;
      this.viewModel = var2;
      this.style = new DefaultTooltipStyle(new TooltipStyleConfig());
   }

   /**
    * Draws the current frame.
    *
    * Behavior:
    * - Uses already available state and does not define business rules.
    */
   public void draw() {
      if (this.viewModel.isVisible()) {
         ThemeSnapshot var1 = this.style.getThemeSnapshot();
         TooltipRenderStyle var2 = this.style.resolveRenderStyle(var1);
         this.measureFromText(var2);
         this.style.render(this.sketch, new TooltipViewState(this.x, this.y, this.width, this.height, this.viewModel.getText(), this.viewModel.isEnabled(), this.style.getTextPadding(), var2.cornerRadius()), var1);
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
    * Updates anchor bounds.
    *
    * @param var1 new anchor bounds
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setAnchorBounds(float var1, float var2) {
      ThemeSnapshot var3 = this.style.getThemeSnapshot();
      this.measureFromText(this.style.resolveRenderStyle(var3));
      this.x = var1;
      this.y = var2 - this.height * 0.5F - 10.0F;
   }

   /**
    * Updates style.
    *
    * @param var1 new style
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setStyle(DefaultTooltipStyle var1) {
      if (var1 != null) {
         this.style = var1;
      }

   }

   private void measureFromText(TooltipRenderStyle var1) {
      this.sketch.pushStyle();
      if (var1.font() != null) {
         this.sketch.textFont(var1.font(), var1.textSize());
      } else {
         this.sketch.textSize(var1.textSize());
      }

      this.width = Math.max(40.0F, this.sketch.textWidth(this.viewModel.getText()) + var1.textPadding() * 2.0F);
      this.height = var1.minHeight();
      this.sketch.popStyle();
   }
}
