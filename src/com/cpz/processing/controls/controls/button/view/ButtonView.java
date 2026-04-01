package com.cpz.processing.controls.controls.button.view;

import com.cpz.processing.controls.controls.button.state.ButtonViewState;
import com.cpz.processing.controls.controls.button.style.ButtonDefaultStyles;
import com.cpz.processing.controls.controls.button.style.ButtonStyle;
import com.cpz.processing.controls.controls.button.viewmodel.ButtonViewModel;
import com.cpz.processing.controls.core.input.PointerInteractable;
import com.cpz.processing.controls.core.input.hit.RectHitTest;
import com.cpz.processing.controls.core.input.hit.interfaces.HitTest;
import com.cpz.processing.controls.core.layout.LayoutConfig;
import com.cpz.processing.controls.core.layout.LayoutResolver;
import com.cpz.processing.controls.core.view.ControlView;
import processing.core.PApplet;

public final class ButtonView implements ControlView, PointerInteractable {
   private final PApplet sketch;
   private final ButtonViewModel viewModel;
   private float x;
   private float y;
   private float width;
   private float height;
   private ButtonStyle style;
   private HitTest hitTest;
   private LayoutConfig layoutConfig;

   public ButtonView(PApplet var1, ButtonViewModel var2, float var3, float var4, float var5, float var6) {
      this.sketch = var1;
      this.viewModel = var2;
      this.x = var3;
      this.y = var4;
      this.width = var5;
      this.height = var6;
      this.style = ButtonDefaultStyles.primary();
      this.hitTest = new RectHitTest();
      this.hitTest.onLayout(var3, var4, var5, var6);
   }

   public void draw() {
      if (this.viewModel.isVisible()) {
         this.applyLayoutIfNeeded();
         this.style.render(this.sketch, this.buildViewState());
      }
   }

   public void setPosition(float var1, float var2) {
      this.x = var1;
      this.y = var2;
      this.notifyLayoutChanged();
   }

   public void setLayoutConfig(LayoutConfig var1) {
      this.layoutConfig = var1;
      this.applyLayoutIfNeeded();
   }

   public void setSize(float var1, float var2) {
      this.width = var1;
      this.height = var2;
      this.notifyLayoutChanged();
   }

   public void setStyle(ButtonStyle var1) {
      if (var1 != null) {
         this.style = var1;
      }

   }

   public void setHitTest(HitTest var1) {
      if (var1 != null) {
         this.hitTest = var1;
         this.hitTest.onLayout(this.x, this.y, this.width, this.height);
      }
   }

   public boolean contains(float var1, float var2) {
      this.applyLayoutIfNeeded();
      return this.hitTest.contains(var1, var2);
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public float getWidth() {
      return this.width;
   }

   public float getHeight() {
      return this.height;
   }

   private ButtonViewState buildViewState() {
      return new ButtonViewState(this.x, this.y, this.width, this.height, this.viewModel.getText(), this.viewModel.isShowText(), this.viewModel.isEnabled(), this.viewModel.isHovered(), this.viewModel.isPressed());
   }

   private void notifyLayoutChanged() {
      this.hitTest.onLayout(this.x, this.y, this.width, this.height);
   }

   private void applyLayoutIfNeeded() {
      if (this.layoutConfig != null) {
         float var1 = LayoutResolver.resolveX(this.layoutConfig, this.width, (float)this.sketch.width);
         float var2 = LayoutResolver.resolveY(this.layoutConfig, this.height, (float)this.sketch.height);
         this.x = var1 + this.width * 0.5F;
         this.y = var2 + this.height * 0.5F;
         this.notifyLayoutChanged();
      }
   }
}
