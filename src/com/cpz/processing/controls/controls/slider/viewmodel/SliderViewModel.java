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

/**
 * ViewModel for slider view model.
 *
 * Responsibilities:
 * - Expose control state to the view layer.
 * - Coordinate interaction and synchronize with the backing model.
 *
 * Behavior:
 * - Does not perform drawing directly.
 *
 * Notes:
 * - This type belongs to the MVVM ViewModel layer.
 */
public final class SliderViewModel extends AbstractControlViewModel {
   private boolean hovered;
   private boolean pressed;
   private boolean dragging;
   private boolean showText = true;
   private Function<BigDecimal, String> formatter;
   private Consumer<BigDecimal> onValueChanged;
   private final List<ValueListener<BigDecimal>> listeners = new ArrayList<>();

   /**
    * Creates a slider view model.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public SliderViewModel(SliderModel var1) {
      super(var1);
   }

   /**
    * Handles pointer moved.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onPointerMoved(boolean var1) {
      this.hovered = this.isInteractive() && var1;
      if (!this.isInteractive()) {
         this.resetInteractionState();
      }

   }

   /**
    * Handles pointer pressed.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
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

   /**
    * Handles pointer dragged.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onPointerDragged(float var1) {
      if (this.dragging && this.isInteractive()) {
         this.pressed = true;
         this.applyNormalizedValue(var1, this.shouldSnapDuringInteraction());
      }
   }

   /**
    * Handles pointer released.
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onPointerReleased() {
      if (this.dragging && ((SliderModel)this.model).getSnapMode() == SnapMode.ON_RELEASE) {
         this.applyNormalizedValue(this.getNormalizedValue(), true);
      }

      this.dragging = false;
      this.pressed = false;
   }

   /**
    * Handles mouse wheel.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
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

   /**
    * Returns whether hovered.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isHovered() {
      return this.hovered;
   }

   /**
    * Returns whether pressed.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isPressed() {
      return this.pressed;
   }

   /**
    * Returns whether dragging.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isDragging() {
      return this.dragging;
   }

   /**
    * Returns whether show text.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isShowText() {
      return this.showText;
   }

   /**
    * Updates show text.
    *
    * @param var1 new show text
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setShowText(boolean var1) {
      this.showText = var1;
   }

   /**
    * Adds listener.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void addListener(ValueListener<BigDecimal> var1) {
      if (var1 != null) {
         this.listeners.add(var1);
      }
   }

   /**
    * Returns value.
    *
    * @return current value
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public BigDecimal getValue() {
      return ((SliderModel)this.model).getValue();
   }

   /**
    * Updates value.
    *
    * @param var1 new value
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setValue(BigDecimal var1) {
      BigDecimal var2 = ((SliderModel)this.model).getValue();
      if (var2 != null && var2.equals(var1)) {
         return;
      }

      ((SliderModel)this.model).setValue(var1);
      this.notifyValueChanged(var2);
   }

   /**
    * Returns min.
    *
    * @return current min
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public BigDecimal getMin() {
      return ((SliderModel)this.model).getMin();
   }

   /**
    * Updates min.
    *
    * @param var1 new min
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setMin(BigDecimal var1) {
      BigDecimal var2 = ((SliderModel)this.model).getValue();
      ((SliderModel)this.model).setMin(var1);
      this.notifyValueChanged(var2);
   }

   /**
    * Returns max.
    *
    * @return current max
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public BigDecimal getMax() {
      return ((SliderModel)this.model).getMax();
   }

   /**
    * Updates max.
    *
    * @param var1 new max
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setMax(BigDecimal var1) {
      BigDecimal var2 = ((SliderModel)this.model).getValue();
      ((SliderModel)this.model).setMax(var1);
      this.notifyValueChanged(var2);
   }

   /**
    * Returns step.
    *
    * @return current step
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public BigDecimal getStep() {
      return ((SliderModel)this.model).getStep();
   }

   /**
    * Updates step.
    *
    * @param var1 new step
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setStep(BigDecimal var1) {
      BigDecimal var2 = ((SliderModel)this.model).getValue();
      ((SliderModel)this.model).setStep(var1);
      this.notifyValueChanged(var2);
   }

   /**
    * Returns snap mode.
    *
    * @return current snap mode
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public SnapMode getSnapMode() {
      return ((SliderModel)this.model).getSnapMode();
   }

   /**
    * Updates snap mode.
    *
    * @param var1 new snap mode
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setSnapMode(SnapMode var1) {
      BigDecimal var2 = ((SliderModel)this.model).getValue();
      ((SliderModel)this.model).setSnapMode(var1);
      this.notifyValueChanged(var2);
   }

   /**
    * Returns normalized value.
    *
    * @return current normalized value
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getNormalizedValue() {
      BigDecimal var1 = ((SliderModel)this.model).getMax().subtract(((SliderModel)this.model).getMin());
      if (var1.signum() == 0) {
         return 0.0F;
      } else {
         BigDecimal var2 = ((SliderModel)this.model).getValue().subtract(((SliderModel)this.model).getMin());
         return clampNormalized(var2.divide(var1, 6, RoundingMode.HALF_UP).floatValue());
      }
   }

   /**
    * Returns formatted value.
    *
    * @return current formatted value
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public String getFormattedValue() {
      BigDecimal var1 = ((SliderModel)this.model).getValue();
      if (this.formatter != null) {
         return (String)this.formatter.apply(var1);
      } else {
         int var2 = Math.max(0, ((SliderModel)this.model).getStep().scale());
         return var1.setScale(var2, RoundingMode.HALF_UP).toPlainString();
      }
   }

   /**
    * Updates formatter.
    *
    * @param Function<BigDecimal parameter used by this operation
    * @param var1 new formatter
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setFormatter(Function<BigDecimal, String> var1) {
      this.formatter = var1;
   }

   /**
    * Updates on value changed.
    *
    * @param var1 new on value changed
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
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
