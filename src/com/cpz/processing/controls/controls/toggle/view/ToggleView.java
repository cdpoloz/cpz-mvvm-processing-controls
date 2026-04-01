package com.cpz.processing.controls.controls.toggle.view;

import com.cpz.processing.controls.controls.toggle.state.ToggleViewState;
import com.cpz.processing.controls.controls.toggle.style.ToggleDefaultStyles;
import com.cpz.processing.controls.controls.toggle.style.ToggleStyle;
import com.cpz.processing.controls.controls.toggle.viewmodel.ToggleViewModel;
import com.cpz.processing.controls.core.input.PointerInteractable;
import com.cpz.processing.controls.core.input.hit.CircleHitTest;
import com.cpz.processing.controls.core.input.hit.interfaces.HitTest;
import com.cpz.processing.controls.core.view.ControlView;
import processing.core.PApplet;

public final class ToggleView implements ControlView, PointerInteractable {
   private final PApplet sketch;
   private final ToggleViewModel viewModel;
   private float x;
   private float y;
   private float width;
   private float height;
   private ToggleStyle style;
   private HitTest hitTest;

   public ToggleView(PApplet var1, ToggleViewModel var2, float var3, float var4, float var5) {
      this(var1, var2, var3, var4, var5, var5);
   }

   public ToggleView(PApplet var1, ToggleViewModel var2, float var3, float var4, float var5, float var6) {
      this.sketch = var1;
      this.viewModel = var2;
      this.x = var3;
      this.y = var4;
      this.width = var5;
      this.height = var6;
      this.style = ToggleDefaultStyles.circular();
      float var7 = Math.min(var5, var6);
      this.hitTest = new CircleHitTest(var3, var4, var7 * 0.5F);
   }

   public void draw() {
      if (this.viewModel.isVisible()) {
         this.style.render(this.sketch, this.buildViewState());
      }
   }

   private ToggleViewState buildViewState() {
      return new ToggleViewState(this.x, this.y, this.width, this.height, this.viewModel.getState(), this.viewModel.getTotalStates(), this.viewModel.isHovered(), this.viewModel.isPressed(), this.viewModel.isEnabled());
   }

   public boolean contains(float var1, float var2) {
      return this.hitTest.contains(var1, var2);
   }

   public void setHitTest(HitTest var1) {
      this.hitTest = var1;
      this.hitTest.onLayout(this.x, this.y, this.width, this.height);
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

   private void notifyLayoutChanged() {
      this.hitTest.onLayout(this.x, this.y, this.width, this.height);
   }

   public void setStyle(ToggleStyle var1) {
      this.style = var1;
   }
}
