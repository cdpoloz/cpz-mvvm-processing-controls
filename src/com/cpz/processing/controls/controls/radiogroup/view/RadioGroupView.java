package com.cpz.processing.controls.controls.radiogroup.view;

import com.cpz.processing.controls.controls.radiogroup.state.RadioGroupItemViewState;
import com.cpz.processing.controls.controls.radiogroup.state.RadioGroupViewState;
import com.cpz.processing.controls.controls.radiogroup.style.RadioGroupDefaultStyles;
import com.cpz.processing.controls.controls.radiogroup.style.RadioGroupStyle;
import com.cpz.processing.controls.controls.radiogroup.viewmodel.RadioGroupViewModel;
import com.cpz.processing.controls.core.input.PointerInteractable;
import com.cpz.processing.controls.core.view.ControlView;
import java.util.ArrayList;
import processing.core.PApplet;

public final class RadioGroupView implements ControlView, PointerInteractable {
   private final PApplet sketch;
   private final RadioGroupViewModel viewModel;
   private float x;
   private float y;
   private float width;
   private float itemHeight;
   private float itemSpacing;
   private RadioGroupStyle style;
   private boolean customItemHeight;
   private boolean customItemSpacing;

   public RadioGroupView(PApplet var1, RadioGroupViewModel var2, float var3, float var4, float var5) {
      this.sketch = var1;
      this.viewModel = var2;
      this.x = var3;
      this.y = var4;
      this.width = var5;
      this.style = RadioGroupDefaultStyles.standard();
      this.applyConfiguredLayoutDefaults();
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

   public void setWidth(float var1) {
      this.width = var1;
   }

   public void setItemHeight(float var1) {
      this.itemHeight = Math.max(this.style.getMinimumItemHeight(), var1);
      this.customItemHeight = true;
   }

   public void setItemSpacing(float var1) {
      this.itemSpacing = Math.max(0.0F, var1);
      this.customItemSpacing = true;
   }

   public void setStyle(RadioGroupStyle var1) {
      if (var1 != null) {
         this.style = var1;
         this.applyConfiguredLayoutDefaults();
      }

   }

   public boolean contains(float var1, float var2) {
      return this.getOptionIndexAt(var1, var2) >= 0;
   }

   public int getOptionIndexAt(float var1, float var2) {
      float var3 = this.x - this.width * 0.5F;
      float var4 = this.y - this.itemHeight * 0.5F;
      if (!(var1 < var3) && !(var1 > var3 + this.width)) {
         for(int var5 = 0; var5 < this.viewModel.getOptions().size(); ++var5) {
            float var6 = var4 + (float)var5 * (this.itemHeight + this.itemSpacing);
            if (var2 >= var6 && var2 <= var6 + this.itemHeight) {
               return var5;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public float getHeight() {
      return this.viewModel.getOptions().isEmpty() ? 0.0F : (float)this.viewModel.getOptions().size() * this.itemHeight + (float)Math.max(0, this.viewModel.getOptions().size() - 1) * this.itemSpacing;
   }

   private RadioGroupViewState buildViewState() {
      ArrayList var1 = new ArrayList();
      float var2 = this.y - this.itemHeight * 0.5F;
      float var3 = this.x;
      float var4 = this.x - this.width * 0.5F;
      float var5 = this.style.getIndicatorOffsetX();
      float var6 = this.style.getTextOffsetX();

      for(int var7 = 0; var7 < this.viewModel.getOptions().size(); ++var7) {
         float var8 = var2 + (float)var7 * (this.itemHeight + this.itemSpacing);
         float var9 = var8 + this.itemHeight * 0.5F;
         var1.add(new RadioGroupItemViewState(var7, (String)this.viewModel.getOptions().get(var7), this.viewModel.getSelectedIndex() == var7, this.viewModel.getHoveredIndex() == var7, this.viewModel.getPressedIndex() == var7, this.viewModel.isFocused() && this.viewModel.getActiveIndex() == var7, var3, var9, this.width, this.itemHeight, var4 + var5, var9, var4 + var6));
      }

      return new RadioGroupViewState(this.x, this.y, this.width, this.getHeight(), var1.size(), this.viewModel.isEnabled(), var1);
   }

   private void applyConfiguredLayoutDefaults() {
      if (!this.customItemHeight) {
         this.itemHeight = this.style.getItemHeight();
      } else {
         this.itemHeight = Math.max(this.style.getMinimumItemHeight(), this.itemHeight);
      }

      if (!this.customItemSpacing) {
         this.itemSpacing = this.style.getItemSpacing();
      }

   }
}
