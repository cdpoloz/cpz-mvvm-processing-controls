package com.cpz.processing.controls.core.overlay.tooltip.view;

import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import com.cpz.processing.controls.core.overlay.tooltip.state.TooltipViewState;
import com.cpz.processing.controls.core.overlay.tooltip.style.DefaultTooltipStyle;
import com.cpz.processing.controls.core.overlay.tooltip.style.TooltipRenderStyle;
import com.cpz.processing.controls.core.overlay.tooltip.viewmodel.TooltipViewModel;
import com.cpz.processing.controls.core.style.TypographySupport;
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
   private TooltipBounds targetBounds;

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
         this.positionAroundTarget(this.style.getOffset());
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

   public void setTargetBounds(TooltipBounds bounds) {
      this.targetBounds = bounds;
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
      TypographySupport.prepareStyleScope(this.sketch, renderStyle.font());
      this.sketch.pushStyle();
      try {
         TypographySupport.apply(this.sketch, renderStyle.font(), renderStyle.textSize());
         this.width = Math.max(40.0F, this.sketch.textWidth(this.viewModel.getText()) + renderStyle.textPadding() * 2.0F);
         this.height = renderStyle.minHeight();
      } finally {
         this.sketch.popStyle();
      }
   }

   private void positionAroundTarget(float offset) {
      if (this.targetBounds == null) {
         return;
      }

      float centerX = this.targetBounds.centerX();
      float aboveY = this.targetBounds.y() - offset - this.height * 0.5F;
      float belowY = this.targetBounds.bottomY() + offset + this.height * 0.5F;
      float centerY = aboveY - this.height * 0.5F >= 0.0F ? aboveY : belowY;
      float halfWidth = this.width * 0.5F;
      float halfHeight = this.height * 0.5F;
      if (this.sketch.width > 0) {
         centerX = Math.max(halfWidth, Math.min((float)this.sketch.width - halfWidth, centerX));
      }
      if (this.sketch.height > 0) {
         centerY = Math.max(halfHeight, Math.min((float)this.sketch.height - halfHeight, centerY));
      }
      this.x = centerX;
      this.y = centerY;
   }
}
