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
 *
 * @author CPZ
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
   public NumericFieldView(PApplet sketch, NumericFieldViewModel viewModel, float x, float y, float width, float height) {
      this.sketch = sketch;
      this.viewModel = viewModel;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
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
         ThemeSnapshot snapshot = this.style.getThemeSnapshot();
         this.style.render(this.sketch, this.buildViewState(snapshot), snapshot);
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
   public void setStyle(NumericFieldStyle style) {
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
      float relativeX = this.width * 0.5F;
      float halfHeight = this.height * 0.5F;
      return x >= this.x - relativeX && x <= this.x + relativeX && y >= this.y - halfHeight && y <= this.y + halfHeight;
   }

   /**
    * Performs position to cursor index.
    *
    * @param x parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public int positionToCursorIndex(float x) {
      String text = this.viewModel.getText();
      float relativeX = x - this.getTextStartX();
      if (!(relativeX <= 0.0F) && !text.isEmpty()) {
         float halfHeight = 0.0F;
         ThemeSnapshot snapshot = this.style.getThemeSnapshot();

         for(int index = 0; index < text.length(); ++index) {
            float currentWidth = this.measureTextWidth(text.substring(0, index + 1), snapshot);
            float midpoint = halfHeight + (currentWidth - halfHeight) * 0.5F;
            if (relativeX < midpoint) {
               return index;
            }

            halfHeight = currentWidth;
         }

         return text.length();
      } else {
         return 0;
      }
   }

   /**
    * Handles mouse press.
    *
    * @param mouseX parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMousePress(float mouseX) {
      int value = this.positionToCursorIndex(mouseX);
      this.updateClickCount();
      if (this.clickCount >= 3) {
         this.viewModel.selectAll();
         this.viewModel.setSelecting(false);
         this.clickCount = 0;
      } else if (this.clickCount == 2) {
         this.selectTokenAt(value);
         this.viewModel.setSelecting(false);
      } else {
         this.viewModel.setSelectionAnchor(value);
         this.viewModel.setCursorPositionWithoutSelectionReset(value);
         this.viewModel.setSelectionStart(value);
         this.viewModel.setSelectionEnd(value);
         this.viewModel.setSelecting(true);
      }
   }

   /**
    * Handles mouse drag.
    *
    * @param mouseX parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleMouseDrag(float mouseX) {
      if (this.viewModel.isSelecting()) {
         int value = this.positionToCursorIndex(mouseX);
         this.viewModel.setSelectionEnd(value);
         this.viewModel.setCursorPositionWithoutSelectionReset(value);
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

   private NumericFieldViewState buildViewState(ThemeSnapshot snapshot) {
      String text = this.viewModel.getText();
      float value = this.getTextStartX();
      int value2 = this.viewModel.getSelectionMin();
      int value3 = this.viewModel.getSelectionMax();
      String path = text.substring(0, value2);
      String path2 = text.substring(value2, value3);
      String path3 = text.substring(value3);
      float value4 = value + this.measureTextWidth(path, snapshot);
      float value5 = value4 + this.measureTextWidth(path2, snapshot);
      float value6 = value + this.measureTextWidth(text.substring(0, this.viewModel.getCursorPosition()), snapshot);
      return new NumericFieldViewState(this.x, this.y, this.width, this.height, text, path, path2, path3, this.viewModel.getCursorPosition(), this.viewModel.getSelectionStart(), this.viewModel.getSelectionEnd(), this.viewModel.isFocused(), this.viewModel.isHovered(), this.viewModel.isPressed(), this.viewModel.isEditing(), this.viewModel.isShowCursor(), this.viewModel.isEnabled(), value, value6, value4, value5);
   }

   private float getTextStartX() {
      return this.x - this.width * 0.5F + 10.0F;
   }

   private void updateClickCount() {
      long clickTime = System.currentTimeMillis();
      if (clickTime - this.lastClickTime < 250L) {
         ++this.clickCount;
      } else {
         this.clickCount = 1;
      }

      this.lastClickTime = clickTime;
   }

   private void selectTokenAt(int value) {
      String text = this.viewModel.getText();
      if (text.isEmpty()) {
         this.viewModel.setSelectionAnchor(0);
         this.viewModel.setSelectionStart(0);
         this.viewModel.setSelectionEnd(0);
         this.viewModel.setCursorPositionWithoutSelectionReset(0);
      } else {
         int value2 = Math.max(0, Math.min(value, text.length() - 1));
         int value3 = value2;

         int value4;
         for(value4 = value2 + 1; value3 > 0 && this.isTokenChar(text.charAt(value3 - 1)); --value3) {
         }

         while(value4 < text.length() && this.isTokenChar(text.charAt(value4))) {
            ++value4;
         }

         this.viewModel.setSelectionAnchor(value3);
         this.viewModel.setSelectionStart(value3);
         this.viewModel.setSelectionEnd(value4);
         this.viewModel.setCursorPositionWithoutSelectionReset(value4);
      }
   }

   private boolean isTokenChar(char key) {
      return Character.isDigit(key) || key == '.' || key == '-';
   }

   private float measureTextWidth(String text, ThemeSnapshot snapshot) {
      NumericFieldRenderStyle renderStyle = this.style.resolveRenderStyle(this.buildMeasureState(), snapshot);
      this.sketch.pushStyle();
      if (renderStyle.font() != null) {
         this.sketch.textFont(renderStyle.font(), renderStyle.textSize());
      } else {
         this.sketch.textSize(renderStyle.textSize());
      }

      this.sketch.textAlign(37, 3);
      float value = this.sketch.textWidth(text == null ? "" : text);
      this.sketch.popStyle();
      return value;
   }

   private NumericFieldViewState buildMeasureState() {
      float value = this.getTextStartX();
      return new NumericFieldViewState(this.x, this.y, this.width, this.height, this.viewModel.getText(), "", "", "", this.viewModel.getCursorPosition(), this.viewModel.getSelectionStart(), this.viewModel.getSelectionEnd(), this.viewModel.isFocused(), this.viewModel.isHovered(), this.viewModel.isPressed(), this.viewModel.isEditing(), this.viewModel.isShowCursor(), this.viewModel.isEnabled(), value, value, value, value);
   }
}
