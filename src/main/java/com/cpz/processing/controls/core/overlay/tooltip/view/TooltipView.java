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
 *
 * @author CPZ
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
    * @param sketch parameter used by this operation
    * @param viewModel parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public TooltipView(PApplet sketch, TooltipViewModel viewModel) {
      this.sketch = sketch;
      this.viewModel = viewModel;
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
         ThemeSnapshot snapshot = this.style.getThemeSnapshot();
         TooltipRenderStyle renderStyle = this.style.resolveRenderStyle(snapshot);
         this.measureFromText(renderStyle);
         this.style.render(this.sketch, new TooltipViewState(this.x, this.y, this.width, this.height, this.viewModel.getText(), this.viewModel.isEnabled(), this.style.getTextPadding(), renderStyle.cornerRadius()), snapshot);
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
    * Updates anchor bounds.
    *
    * @param x new anchor bounds
    * @param y parameter used by this operation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setAnchorBounds(float x, float y) {
      ThemeSnapshot snapshot = this.style.getThemeSnapshot();
      this.measureFromText(this.style.resolveRenderStyle(snapshot));
      this.x = x;
      this.y = y - this.height * 0.5F - 10.0F;
   }

   /**
    * Updates style.
    *
    * @param style new style
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setStyle(DefaultTooltipStyle style) {
      if (style != null) {
         this.style = style;
      }

   }

   private void measureFromText(TooltipRenderStyle renderStyle) {
      this.sketch.pushStyle();
      if (renderStyle.font() != null) {
         this.sketch.textFont(renderStyle.font(), renderStyle.textSize());
      } else {
         this.sketch.textSize(renderStyle.textSize());
      }

      this.width = Math.max(40.0F, this.sketch.textWidth(this.viewModel.getText()) + renderStyle.textPadding() * 2.0F);
      this.height = renderStyle.minHeight();
      this.sketch.popStyle();
   }
}
