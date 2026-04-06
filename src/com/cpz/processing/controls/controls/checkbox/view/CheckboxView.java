package com.cpz.processing.controls.controls.checkbox.view;

import com.cpz.processing.controls.controls.checkbox.state.CheckboxViewState;
import com.cpz.processing.controls.controls.checkbox.style.CheckboxDefaultStyles;
import com.cpz.processing.controls.controls.checkbox.style.CheckboxStyle;
import com.cpz.processing.controls.controls.checkbox.viewmodel.CheckboxViewModel;
import com.cpz.processing.controls.core.input.PointerInteractable;
import com.cpz.processing.controls.core.input.hit.RectHitTest;
import com.cpz.processing.controls.core.input.hit.interfaces.HitTest;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import com.cpz.processing.controls.core.view.ControlView;
import processing.core.PApplet;

public final class CheckboxView implements ControlView, PointerInteractable {
   private final PApplet sketch;
   private final CheckboxViewModel viewModel;
   private float x;
   private float y;
   private float width;
   private float height;
   private CheckboxStyle style;
   private HitTest hitTest;

   public CheckboxView(PApplet var1, CheckboxViewModel var2, float var3, float var4, float var5) {
      this(var1, var2, var3, var4, var5, var5);
   }

   public CheckboxView(PApplet var1, CheckboxViewModel var2, float var3, float var4, float var5, float var6) {
      this.sketch = var1;
      this.viewModel = var2;
      this.x = var3;
      this.y = var4;
      this.width = var5;
      this.height = var6;
      this.style = CheckboxDefaultStyles.standard();
      this.hitTest = new RectHitTest();
      this.hitTest.onLayout(var3, var4, var5, var6);
   }

   public void draw() {
      if (this.viewModel.isVisible()) {
         ThemeSnapshot var1 = this.style.getThemeSnapshot();
         this.style.render(this.sketch, this.buildViewState(), var1);
      }
   }

   public void setPosition(float var1, float var2) {
      this.x = var1;
      this.y = var2;
      this.notifyLayoutChanged();
   }

   public void setSize(float var1) {
      this.width = var1;
      this.height = var1;
      this.notifyLayoutChanged();
   }

   public void setSize(float var1, float var2) {
      this.width = var1;
      this.height = var2;
      this.notifyLayoutChanged();
   }

   public void setStyle(CheckboxStyle var1) {
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
      return this.hitTest.contains(var1, var2);
   }

   private CheckboxViewState buildViewState() {
      return new CheckboxViewState(this.x, this.y, this.width, this.height, this.viewModel.isChecked(), this.viewModel.isHovered(), this.viewModel.isPressed(), this.viewModel.isEnabled());
   }

   private void notifyLayoutChanged() {
      this.hitTest.onLayout(this.x, this.y, this.width, this.height);
   }
}
