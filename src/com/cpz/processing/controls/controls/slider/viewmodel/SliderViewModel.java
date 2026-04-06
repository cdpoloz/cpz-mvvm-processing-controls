package com.cpz.processing.controls.controls.slider.viewmodel;

import com.cpz.processing.controls.common.binding.ValueListener;
import com.cpz.processing.controls.controls.slider.model.SliderModel;
import com.cpz.processing.controls.controls.slider.model.SnapMode;
import com.cpz.processing.controls.core.viewmodel.AbstractControlViewModel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class SliderViewModel extends AbstractControlViewModel {
   private boolean hovered;
   private boolean pressed;
   private boolean dragging;
   private boolean showText = true;
   private Function<BigDecimal, String> formatter;
   private Consumer<BigDecimal> onValueChanged;
   private final List<ValueListener<BigDecimal>> listeners = new ArrayList<>();

   public SliderViewModel(SliderModel var1) {
      super(var1);
   }

   public void onPointerMoved(boolean var1) {
      this.hovered = this.isInteractive() && var1;
      if (!this.isInteractive()) {
         this.resetInteractionState();
      }

   }

   public void onPointerPressed(float var1) {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      } else {
         this.hovered = true;
         this.pressed = true;
         this.dragging = true;
         this.applyNormalizedValue(var1, this.shouldSnapDuringInteraction());
      }
   }

   public void onPointerDragged(float var1) {
      if (this.dragging && this.isInteractive()) {
         this.pressed = true;
         this.applyNormalizedValue(var1, this.shouldSnapDuringInteraction());
      }
   }

   public void onPointerReleased() {
      if (this.dragging && ((SliderModel)this.model).getSnapMode() == SnapMode.ON_RELEASE) {
         this.applyNormalizedValue(this.getNormalizedValue(), true);
      }

      this.dragging = false;
      this.pressed = false;
   }

   public void onMouseWheel(float var1, boolean var2, boolean var3) {
      if (this.isInteractive() && this.hovered && var1 != 0.0F) {
         BigDecimal var4 = ((SliderModel)this.model).getStep();
         if (var2) {
            var4 = var4.multiply(BigDecimal.TEN);
         } else if (var3) {
            var4 = var4.multiply(new BigDecimal("0.1"));
         }

         BigDecimal var5 = ((SliderModel)this.model).getValue();
         BigDecimal var6 = BigDecimal.valueOf((double)(-Math.signum(var1)));
         BigDecimal var7 = var5.add(var6.multiply(var4));
         ((SliderModel)this.model).setValue(((SliderModel)this.model).normalizeValue(var7, true));
         this.notifyValueChanged(var5);
      }
   }

   public boolean isHovered() {
      return this.hovered;
   }

   public boolean isPressed() {
      return this.pressed;
   }

   public boolean isDragging() {
      return this.dragging;
   }

   public boolean isShowText() {
      return this.showText;
   }

   public void setShowText(boolean var1) {
      this.showText = var1;
   }

   public void addListener(ValueListener<BigDecimal> var1) {
      if (var1 != null) {
         this.listeners.add(var1);
      }
   }

   public BigDecimal getValue() {
      return ((SliderModel)this.model).getValue();
   }

   public void setValue(BigDecimal var1) {
      BigDecimal var2 = ((SliderModel)this.model).getValue();
      if (var2 != null && var2.equals(var1)) {
         return;
      }

      ((SliderModel)this.model).setValue(var1);
      this.notifyValueChanged(var2);
   }

   public BigDecimal getMin() {
      return ((SliderModel)this.model).getMin();
   }

   public void setMin(BigDecimal var1) {
      BigDecimal var2 = ((SliderModel)this.model).getValue();
      ((SliderModel)this.model).setMin(var1);
      this.notifyValueChanged(var2);
   }

   public BigDecimal getMax() {
      return ((SliderModel)this.model).getMax();
   }

   public void setMax(BigDecimal var1) {
      BigDecimal var2 = ((SliderModel)this.model).getValue();
      ((SliderModel)this.model).setMax(var1);
      this.notifyValueChanged(var2);
   }

   public BigDecimal getStep() {
      return ((SliderModel)this.model).getStep();
   }

   public void setStep(BigDecimal var1) {
      BigDecimal var2 = ((SliderModel)this.model).getValue();
      ((SliderModel)this.model).setStep(var1);
      this.notifyValueChanged(var2);
   }

   public SnapMode getSnapMode() {
      return ((SliderModel)this.model).getSnapMode();
   }

   public void setSnapMode(SnapMode var1) {
      BigDecimal var2 = ((SliderModel)this.model).getValue();
      ((SliderModel)this.model).setSnapMode(var1);
      this.notifyValueChanged(var2);
   }

   public float getNormalizedValue() {
      BigDecimal var1 = ((SliderModel)this.model).getMax().subtract(((SliderModel)this.model).getMin());
      if (var1.signum() == 0) {
         return 0.0F;
      } else {
         BigDecimal var2 = ((SliderModel)this.model).getValue().subtract(((SliderModel)this.model).getMin());
         return clampNormalized(var2.divide(var1, 6, RoundingMode.HALF_UP).floatValue());
      }
   }

   public String getFormattedValue() {
      BigDecimal var1 = ((SliderModel)this.model).getValue();
      if (this.formatter != null) {
         return (String)this.formatter.apply(var1);
      } else {
         int var2 = Math.max(0, ((SliderModel)this.model).getStep().scale());
         return var1.setScale(var2, RoundingMode.HALF_UP).toPlainString();
      }
   }

   public void setFormatter(Function<BigDecimal, String> var1) {
      this.formatter = var1;
   }

   public void setOnValueChanged(Consumer<BigDecimal> var1) {
      this.onValueChanged = var1;
   }

   protected void onAvailabilityChanged() {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      }

   }

   private boolean isInteractive() {
      return this.isEnabled() && this.isVisible();
   }

   private void applyNormalizedValue(float var1, boolean var2) {
      float var3 = clampNormalized(var1);
      BigDecimal var4 = ((SliderModel)this.model).getMin();
      BigDecimal var5 = ((SliderModel)this.model).getMax();
      BigDecimal var6 = var5.subtract(var4);
      BigDecimal var7 = var4.add(var6.multiply(BigDecimal.valueOf((double)var3)));
      BigDecimal var8 = ((SliderModel)this.model).getValue();
      if (var2) {
         ((SliderModel)this.model).setValue(var7);
      } else {
         ((SliderModel)this.model).setValue(((SliderModel)this.model).normalizeValue(var7, false));
      }

      this.notifyValueChanged(var8);
   }

   private boolean shouldSnapDuringInteraction() {
      return ((SliderModel)this.model).getSnapMode() == SnapMode.ALWAYS;
   }

   private void notifyValueChanged(BigDecimal var1) {
      BigDecimal var2 = ((SliderModel)this.model).getValue();
      if (var1.compareTo(var2) != 0) {
         if (this.onValueChanged != null) {
            this.onValueChanged.accept(var2);
         }

         this.notifyListeners(var2);
      }

   }

   private void notifyListeners(BigDecimal var1) {
      for(ValueListener<BigDecimal> var3 : this.listeners) {
         var3.onChange(var1);
      }

   }

   private void resetInteractionState() {
      this.hovered = false;
      this.pressed = false;
      this.dragging = false;
   }

   private static float clampNormalized(float var0) {
      return Math.max(0.0F, Math.min(1.0F, var0));
   }
}
