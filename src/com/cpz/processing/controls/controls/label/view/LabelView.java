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

/**
 * View for label view.
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
public final class LabelView implements ControlView {
   private final PApplet sketch;
   private final LabelViewModel viewModel;
   private float x;
   private float y;
   private float width;
   private float height;
   private LabelStyle style;
   private float cachedWidth;
   private float cachedHeight;
   private boolean metricsDirty = true;
   private String lastMeasuredText = "";

   /**
    * Creates a label view.
    *
    * @param sketch parameter used by this operation
    * @param viewModel parameter used by this operation
    * @param x parameter used by this operation
    * @param y parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public LabelView(PApplet sketch, LabelViewModel viewModel, float x, float y) {
      this(sketch, viewModel, x, y, 0.0F, 0.0F);
   }

   public LabelView(PApplet sketch, LabelViewModel viewModel, float x, float y, float width, float height) {
      this.sketch = sketch;
      this.viewModel = viewModel;
      this.x = x;
      this.y = y;
      this.width = Math.max(0.0F, width);
      this.height = Math.max(0.0F, height);
      this.style = LabelDefaultStyles.defaultText();
      String text = viewModel.getText();
      this.lastMeasuredText = text == null ? "" : text;
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

   private void updateTextMetrics() {
      String text = this.viewModel.getText();
      if (!Objects.equals(text, this.lastMeasuredText)) {
         this.metricsDirty = true;
      }

      if (this.metricsDirty) {
         String text2 = text;
         if (text == null) {
            text2 = "";
         }

         this.sketch.pushStyle();
         LabelTypography labelTypography = this.style.resolveTypography();
         if (labelTypography.font() != null) {
            this.sketch.textFont(labelTypography.font());
         }

         this.sketch.textSize(labelTypography.textSize());
         this.sketch.textAlign(LabelAlignMapper.mapHorizontal(labelTypography.textAlignHorizontal()), LabelAlignMapper.mapVertical(labelTypography.textAlignVertical()));
         String[] text3 = text2.split("\n", -1);
         float value = 0.0F;
         float value2 = (this.sketch.textAscent() + this.sketch.textDescent()) * labelTypography.lineSpacingMultiplier();

         for(String text4 : text3) {
            float value3 = this.sketch.textWidth(text4);
            if (value3 > value) {
               value = value3;
            }
         }

         this.cachedWidth = value;
         this.cachedHeight = (float)text3.length * value2;
         this.sketch.popStyle();
         this.lastMeasuredText = text;
         this.metricsDirty = false;
      }
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
      if (this.width > 0.0F) {
         return this.width;
      }

      if (this.metricsDirty) {
         this.updateTextMetrics();
      }

      return this.cachedWidth;
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
      if (this.height > 0.0F) {
         return this.height;
      }

      if (this.metricsDirty) {
         this.updateTextMetrics();
      }

      return this.cachedHeight;
   }

   /**
    * Performs center block around.
    *
    * @param x parameter used by this operation
    * @param y parameter used by this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void centerBlockAround(float x, float y) {
      if (this.metricsDirty) {
         this.updateTextMetrics();
      }

      this.x = x - this.cachedWidth * 0.5F;
      this.y = y - this.cachedHeight * 0.5F;
   }

   private LabelViewState buildViewState() {
      return new LabelViewState(this.x, this.y, this.width, this.height, this.viewModel.getText(), this.viewModel.isEnabled());
   }

   /**
    * Updates style.
    *
    * @param style new style
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setStyle(LabelStyle style) {
      if (style != null) {
         this.style = style;
         this.metricsDirty = true;
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
      this.metricsDirty = true;
   }

   public void setSize(float width, float height) {
      this.width = Math.max(0.0F, width);
      this.height = Math.max(0.0F, height);
      this.metricsDirty = true;
   }

   /**
    * Draws the current frame.
    *
    * Behavior:
    * - Uses already available state and does not define business rules.
    */
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
