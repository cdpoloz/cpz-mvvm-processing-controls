package com.cpz.processing.controls.controls.numericfield.view;

import com.cpz.processing.controls.controls.numericfield.state.NumericFieldViewState;
import com.cpz.processing.controls.controls.numericfield.style.NumericFieldDefaultStyles;
import com.cpz.processing.controls.controls.numericfield.style.NumericFieldRenderStyle;
import com.cpz.processing.controls.controls.numericfield.style.NumericFieldStyle;
import com.cpz.processing.controls.controls.numericfield.viewmodel.NumericFieldViewModel;
import com.cpz.processing.controls.core.input.PointerInteractable;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.view.ControlView;
import processing.core.PApplet;

/**
 * View for numeric field view.
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
public final class NumericFieldView implements ControlView, PointerInteractable {
   private static final float HORIZONTAL_PADDING = 10.0F;
   private static final long DOUBLE_CLICK_THRESHOLD = 250L;
   private final PApplet sketch;
   private final NumericFieldViewModel viewModel;
   private float x;
   private float y;
   private float width;
   private float height;
   private NumericFieldStyle style;
   private long lastClickTime;
   private int clickCount;

   /**
    * Creates a numeric field view.
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
   public NumericFieldView(PApplet var1, NumericFieldViewModel var2, float var3, float var4, float var5, float var6) {
      this.sketch = var1;
      this.viewModel = var2;
      this.x = var3;
      this.y = var4;
      this.width = var5;
      this.height = var6;
      this.style = NumericFieldDefaultStyles.standard();
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
         this.style.render(this.sketch, this.buildViewState(var1), var1);
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
   public void setStyle(NumericFieldStyle var1) {
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
      float var3 = this.width * 0.5F;
      float var4 = this.height * 0.5F;
      return var1 >= this.x - var3 && var1 <= this.x + var3 && var2 >= this.y - var4 && var2 <= this.y + var4;
   }

   /**
    * Performs position to cursor index.
    *
    * @param var1 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public int positionToCursorIndex(float var1) {
      String var2 = this.viewModel.getText();
      float var3 = var1 - this.getTextStartX();
      if (!(var3 <= 0.0F) && !var2.isEmpty()) {
         float var4 = 0.0F;
         ThemeSnapshot var8 = this.style.getThemeSnapshot();

         for(int var5 = 0; var5 < var2.length(); ++var5) {
            float var6 = this.measureTextWidth(var2.substring(0, var5 + 1), var8);
            float var7 = var4 + (var6 - var4) * 0.5F;
            if (var3 < var7) {
               return var5;
            }

            var4 = var6;
         }

         return var2.length();
      } else {
         return 0;
      }
   }

   /**
    * Handles mouse press.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMousePress(float var1) {
      int var2 = this.positionToCursorIndex(var1);
      this.updateClickCount();
      if (this.clickCount >= 3) {
         this.viewModel.selectAll();
         this.viewModel.setSelecting(false);
         this.clickCount = 0;
      } else if (this.clickCount == 2) {
         this.selectTokenAt(var2);
         this.viewModel.setSelecting(false);
      } else {
         this.viewModel.setSelectionAnchor(var2);
         this.viewModel.setCursorPositionWithoutSelectionReset(var2);
         this.viewModel.setSelectionStart(var2);
         this.viewModel.setSelectionEnd(var2);
         this.viewModel.setSelecting(true);
      }
   }

   /**
    * Handles mouse drag.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseDrag(float var1) {
      if (this.viewModel.isSelecting()) {
         int var2 = this.positionToCursorIndex(var1);
         this.viewModel.setSelectionEnd(var2);
         this.viewModel.setCursorPositionWithoutSelectionReset(var2);
      }
   }

   /**
    * Handles mouse release.
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseRelease() {
      this.viewModel.setSelecting(false);
   }

   private NumericFieldViewState buildViewState(ThemeSnapshot var1) {
      String var2 = this.viewModel.getText();
      float var3 = this.getTextStartX();
      int var4 = this.viewModel.getSelectionMin();
      int var5 = this.viewModel.getSelectionMax();
      String var6 = var2.substring(0, var4);
      String var7 = var2.substring(var4, var5);
      String var8 = var2.substring(var5);
      float var9 = var3 + this.measureTextWidth(var6, var1);
      float var10 = var9 + this.measureTextWidth(var7, var1);
      float var11 = var3 + this.measureTextWidth(var2.substring(0, this.viewModel.getCursorPosition()), var1);
      return new NumericFieldViewState(this.x, this.y, this.width, this.height, var2, var6, var7, var8, this.viewModel.getCursorPosition(), this.viewModel.getSelectionStart(), this.viewModel.getSelectionEnd(), this.viewModel.isFocused(), this.viewModel.isHovered(), this.viewModel.isPressed(), this.viewModel.isEditing(), this.viewModel.isShowCursor(), this.viewModel.isEnabled(), var3, var11, var9, var10);
   }

   private float getTextStartX() {
      return this.x - this.width * 0.5F + 10.0F;
   }

   private void updateClickCount() {
      long var1 = System.currentTimeMillis();
      if (var1 - this.lastClickTime < 250L) {
         ++this.clickCount;
      } else {
         this.clickCount = 1;
      }

      this.lastClickTime = var1;
   }

   private void selectTokenAt(int var1) {
      String var2 = this.viewModel.getText();
      if (var2.isEmpty()) {
         this.viewModel.setSelectionAnchor(0);
         this.viewModel.setSelectionStart(0);
         this.viewModel.setSelectionEnd(0);
         this.viewModel.setCursorPositionWithoutSelectionReset(0);
      } else {
         int var3 = Math.max(0, Math.min(var1, var2.length() - 1));
         int var4 = var3;

         int var5;
         for(var5 = var3 + 1; var4 > 0 && this.isTokenChar(var2.charAt(var4 - 1)); --var4) {
         }

         while(var5 < var2.length() && this.isTokenChar(var2.charAt(var5))) {
            ++var5;
         }

         this.viewModel.setSelectionAnchor(var4);
         this.viewModel.setSelectionStart(var4);
         this.viewModel.setSelectionEnd(var5);
         this.viewModel.setCursorPositionWithoutSelectionReset(var5);
      }
   }

   private boolean isTokenChar(char var1) {
      return Character.isDigit(var1) || var1 == '.' || var1 == '-';
   }

   private float measureTextWidth(String var1, ThemeSnapshot var2) {
      NumericFieldRenderStyle var3 = this.style.resolveRenderStyle(this.buildMeasureState(), var2);
      this.sketch.pushStyle();
      if (var3.font() != null) {
         this.sketch.textFont(var3.font(), var3.textSize());
      } else {
         this.sketch.textSize(var3.textSize());
      }

      this.sketch.textAlign(37, 3);
      float var4 = this.sketch.textWidth(var1 == null ? "" : var1);
      this.sketch.popStyle();
      return var4;
   }

   private NumericFieldViewState buildMeasureState() {
      float var2 = this.getTextStartX();
      return new NumericFieldViewState(this.x, this.y, this.width, this.height, this.viewModel.getText(), "", "", "", this.viewModel.getCursorPosition(), this.viewModel.getSelectionStart(), this.viewModel.getSelectionEnd(), this.viewModel.isFocused(), this.viewModel.isHovered(), this.viewModel.isPressed(), this.viewModel.isEditing(), this.viewModel.isShowCursor(), this.viewModel.isEnabled(), var2, var2, var2, var2);
   }
}
