package com.cpz.processing.controls.controls.label.view;

import com.cpz.processing.controls.controls.label.state.LabelViewState;
import com.cpz.processing.controls.controls.label.style.LabelDefaultStyles;
import com.cpz.processing.controls.controls.label.style.LabelStyle;
import com.cpz.processing.controls.controls.label.style.LabelTypography;
import com.cpz.processing.controls.controls.label.style.render.LabelAlignMapper;
import com.cpz.processing.controls.controls.label.viewmodel.LabelViewModel;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.view.ControlView;
import java.util.Objects;
import processing.core.PApplet;

public final class LabelView implements ControlView {
   private final PApplet sketch;
   private final LabelViewModel viewModel;
   private float x;
   private float y;
   private LabelStyle style;
   private float cachedWidth;
   private float cachedHeight;
   private boolean metricsDirty = true;
   private String lastMeasuredText = "";

   public LabelView(PApplet var1, LabelViewModel var2, float var3, float var4) {
      this.sketch = var1;
      this.viewModel = var2;
      this.x = var3;
      this.y = var4;
      this.style = LabelDefaultStyles.defaultText();
      String var5 = var2.getText();
      this.lastMeasuredText = var5 == null ? "" : var5;
   }

   public void draw() {
      if (this.viewModel.isVisible()) {
         ThemeSnapshot var1 = this.style.getThemeSnapshot();
         this.style.render(this.sketch, this.buildViewState(), var1);
      }
   }

   private void updateTextMetrics() {
      String var1 = this.viewModel.getText();
      if (!Objects.equals(var1, this.lastMeasuredText)) {
         this.metricsDirty = true;
      }

      if (this.metricsDirty) {
         String var2 = var1;
         if (var1 == null) {
            var2 = "";
         }

         this.sketch.pushStyle();
         LabelTypography var3 = this.style.resolveTypography();
         if (var3.font() != null) {
            this.sketch.textFont(var3.font());
         }

         this.sketch.textSize(var3.textSize());
         this.sketch.textAlign(LabelAlignMapper.mapHorizontal(var3.textAlignHorizontal()), LabelAlignMapper.mapVertical(var3.textAlignVertical()));
         String[] var4 = var2.split("\n", -1);
         float var5 = 0.0F;
         float var6 = (this.sketch.textAscent() + this.sketch.textDescent()) * var3.lineSpacingMultiplier();

         for(String var10 : var4) {
            float var11 = this.sketch.textWidth(var10);
            if (var11 > var5) {
               var5 = var11;
            }
         }

         this.cachedWidth = var5;
         this.cachedHeight = (float)var4.length * var6;
         this.sketch.popStyle();
         this.lastMeasuredText = var1;
         this.metricsDirty = false;
      }
   }

   public float getWidth() {
      if (this.metricsDirty) {
         this.updateTextMetrics();
      }

      return this.cachedWidth;
   }

   public float getHeight() {
      if (this.metricsDirty) {
         this.updateTextMetrics();
      }

      return this.cachedHeight;
   }

   public void centerBlockAround(float var1, float var2) {
      if (this.metricsDirty) {
         this.updateTextMetrics();
      }

      this.x = var1 - this.cachedWidth * 0.5F;
      this.y = var2 - this.cachedHeight * 0.5F;
   }

   private LabelViewState buildViewState() {
      return new LabelViewState(this.x, this.y, this.viewModel.getText(), this.viewModel.isEnabled());
   }

   public void setStyle(LabelStyle var1) {
      if (var1 != null) {
         this.style = var1;
         this.metricsDirty = true;
      }

   }

   public void setPosition(float var1, float var2) {
      this.x = var1;
      this.y = var2;
      this.metricsDirty = true;
   }

   public void drawDebugBounds() {
      if (this.metricsDirty) {
         this.updateTextMetrics();
      }

      this.sketch.pushStyle();
      this.sketch.noFill();
      this.sketch.stroke(255.0F, 0.0F, 0.0F);
      this.sketch.rect(this.x, this.y, this.cachedWidth, this.cachedHeight);
      this.sketch.popStyle();
   }
}
