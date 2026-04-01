package com.cpz.processing.controls.core.overlay.tooltip.view;

import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.overlay.tooltip.state.TooltipViewState;
import com.cpz.processing.controls.core.overlay.tooltip.style.DefaultTooltipStyle;
import com.cpz.processing.controls.core.overlay.tooltip.style.TooltipRenderStyle;
import com.cpz.processing.controls.core.overlay.tooltip.viewmodel.TooltipViewModel;
import com.cpz.processing.controls.core.view.ControlView;
import processing.core.PApplet;

public final class TooltipView implements ControlView {
   private static final float VERTICAL_OFFSET = 10.0F;
   private final PApplet sketch;
   private final TooltipViewModel viewModel;
   private float x;
   private float y;
   private float width;
   private float height;
   private DefaultTooltipStyle style;

   public TooltipView(PApplet var1, TooltipViewModel var2) {
      this.sketch = var1;
      this.viewModel = var2;
      this.style = new DefaultTooltipStyle(new TooltipStyleConfig());
   }

   public void draw() {
      if (this.viewModel.isVisible()) {
         this.measureFromText();
         this.style.render(this.sketch, new TooltipViewState(this.x, this.y, this.width, this.height, this.viewModel.getText(), this.viewModel.isEnabled(), this.style.getTextPadding(), this.style.resolveRenderStyle().cornerRadius()));
      }
   }

   public void setPosition(float var1, float var2) {
      this.x = var1;
      this.y = var2;
   }

   public void setAnchorBounds(float var1, float var2) {
      this.measureFromText();
      this.x = var1;
      this.y = var2 - this.height * 0.5F - 10.0F;
   }

   public void setStyle(DefaultTooltipStyle var1) {
      if (var1 != null) {
         this.style = var1;
      }

   }

   private void measureFromText() {
      TooltipRenderStyle var1 = this.style.resolveRenderStyle();
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
