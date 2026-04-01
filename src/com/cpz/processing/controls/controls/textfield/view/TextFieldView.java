package com.cpz.processing.controls.controls.textfield.view;

import com.cpz.processing.controls.controls.textfield.state.TextFieldViewState;
import com.cpz.processing.controls.controls.textfield.style.TextFieldDefaultStyles;
import com.cpz.processing.controls.controls.textfield.style.TextFieldRenderStyle;
import com.cpz.processing.controls.controls.textfield.style.TextFieldStyle;
import com.cpz.processing.controls.controls.textfield.viewmodel.TextFieldViewModel;
import com.cpz.processing.controls.core.input.PointerInteractable;
import com.cpz.processing.controls.core.view.ControlView;
import processing.core.PApplet;

public final class TextFieldView implements ControlView, PointerInteractable {
   private static final float HORIZONTAL_PADDING = 10.0F;
   private static final long DOUBLE_CLICK_THRESHOLD = 250L;
   private final PApplet sketch;
   private final TextFieldViewModel viewModel;
   private float x;
   private float y;
   private float width;
   private float height;
   private TextFieldStyle style;
   private long lastClickTime;
   private int clickCount;

   public TextFieldView(PApplet var1, TextFieldViewModel var2, float var3, float var4, float var5, float var6) {
      this.sketch = var1;
      this.viewModel = var2;
      this.x = var3;
      this.y = var4;
      this.width = var5;
      this.height = var6;
      this.style = TextFieldDefaultStyles.standard();
   }

   public void draw() {
      if (this.viewModel.isVisible()) {
         this.style.render(this.sketch, this.buildViewState());
      }
   }

   public void setPosition(float var1, float var2) {
      this.x = var1;
      this.y = var2;
   }

   public void setSize(float var1, float var2) {
      this.width = var1;
      this.height = var2;
   }

   public void setStyle(TextFieldStyle var1) {
      if (var1 != null) {
         this.style = var1;
      }

   }

   public boolean contains(float var1, float var2) {
      float var3 = this.width * 0.5F;
      float var4 = this.height * 0.5F;
      return var1 >= this.x - var3 && var1 <= this.x + var3 && var2 >= this.y - var4 && var2 <= this.y + var4;
   }

   public int positionToCursorIndex(float var1) {
      String var2 = this.viewModel.getText();
      float var3 = var1 - this.getTextStartX();
      if (!(var3 <= 0.0F) && !var2.isEmpty()) {
         float var4 = 0.0F;

         for(int var5 = 0; var5 < var2.length(); ++var5) {
            float var6 = this.measureTextWidth(var2.substring(0, var5 + 1));
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

   public void handleMousePress(float var1) {
      int var2 = this.positionToCursorIndex(var1);
      this.updateClickCount();
      if (this.clickCount >= 3) {
         this.viewModel.selectAll();
         this.viewModel.setSelecting(false);
         this.clickCount = 0;
      } else if (this.clickCount == 2) {
         this.selectWordAt(var2);
         this.viewModel.setSelecting(false);
      } else {
         this.viewModel.setSelectionAnchor(var2);
         this.viewModel.setCursorIndexWithoutSelectionReset(var2);
         this.viewModel.setSelectionStart(var2);
         this.viewModel.setSelectionEnd(var2);
         this.viewModel.setSelecting(true);
      }
   }

   public void handleMouseDrag(float var1) {
      if (this.viewModel.isSelecting()) {
         int var2 = this.positionToCursorIndex(var1);
         this.viewModel.setSelectionEnd(var2);
         this.viewModel.setCursorIndexWithoutSelectionReset(var2);
      }
   }

   public void handleMouseRelease() {
      this.viewModel.setSelecting(false);
   }

   private TextFieldViewState buildViewState() {
      String var1 = this.viewModel.getText();
      float var2 = this.getTextStartX();
      int var3 = this.viewModel.getSelectionMin();
      int var4 = this.viewModel.getSelectionMax();
      String var5 = var1.substring(0, var3);
      String var6 = var1.substring(var3, var4);
      String var7 = var1.substring(var4);
      float var8 = var2 + this.measureTextWidth(var5);
      float var9 = var8 + this.measureTextWidth(var6);
      float var10 = var2 + this.measureTextWidth(var1.substring(0, this.viewModel.getCursorIndex()));
      return new TextFieldViewState(this.x, this.y, this.width, this.height, var1, var5, var6, var7, this.viewModel.getCursorIndex(), this.viewModel.getSelectionStart(), this.viewModel.getSelectionEnd(), this.viewModel.isFocused(), this.viewModel.isShowCursor(), this.viewModel.isEnabled(), var2, var10, var8, var9);
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

   private void selectWordAt(int var1) {
      String var2 = this.viewModel.getText();
      if (var2.isEmpty()) {
         byte var6 = 0;
         this.viewModel.setSelectionAnchor(var6);
         this.viewModel.setSelectionStart(var6);
         this.viewModel.setSelectionEnd(var6);
         this.viewModel.setCursorIndexWithoutSelectionReset(var6);
      } else {
         int var3 = Math.max(0, Math.min(var1, var2.length() - 1));
         int var4 = var3;
         int var5 = var3 + 1;
         if (!Character.isWhitespace(var2.charAt(var3))) {
            while(var4 > 0 && !Character.isWhitespace(var2.charAt(var4 - 1))) {
               --var4;
            }

            while(var5 < var2.length() && !Character.isWhitespace(var2.charAt(var5))) {
               ++var5;
            }
         }

         this.viewModel.setSelectionAnchor(var4);
         this.viewModel.setSelectionStart(var4);
         this.viewModel.setSelectionEnd(var5);
         this.viewModel.setCursorIndexWithoutSelectionReset(var5);
      }
   }

   private float measureTextWidth(String var1) {
      TextFieldRenderStyle var2 = this.style.resolveRenderStyle(this.buildMeasureState());
      this.sketch.pushStyle();
      if (var2.font() != null) {
         this.sketch.textFont(var2.font(), var2.textSize());
      } else {
         this.sketch.textSize(var2.textSize());
      }

      this.sketch.textAlign(37, 3);
      float var3 = this.sketch.textWidth(var1 == null ? "" : var1);
      this.sketch.popStyle();
      return var3;
   }

   private TextFieldViewState buildMeasureState() {
      return new TextFieldViewState(this.x, this.y, this.width, this.height, this.viewModel.getText(), "", "", "", this.viewModel.getCursorIndex(), this.viewModel.getSelectionStart(), this.viewModel.getSelectionEnd(), this.viewModel.isFocused(), this.viewModel.isShowCursor(), this.viewModel.isEnabled(), this.getTextStartX(), this.getTextStartX(), this.getTextStartX(), this.getTextStartX());
   }
}
