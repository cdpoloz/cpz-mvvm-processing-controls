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
 *
 * @author CPZ
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
    * @param model parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public SliderViewModel(SliderModel model) {
      super(model);
   }

   /**
    * Handles pointer moved.
    *
    * @param inside parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onPointerMoved(boolean inside) {
      this.hovered = this.isInteractive() && inside;
      if (!this.isInteractive()) {
         this.resetInteractionState();
      }

   }

   /**
    * Handles pointer pressed.
    *
    * @param wheelDelta mouse wheel delta
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onPointerPressed(float mouseX) {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      } else {
         this.hovered = true;
         this.pressed = true;
         this.dragging = true;
         this.applyNormalizedValue(mouseX, this.shouldSnapDuringInteraction());
      }
   }

   /**
    * Handles pointer dragged.
    *
    * @param wheelDelta mouse wheel delta
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onPointerDragged(float mouseX) {
      if (this.dragging && this.isInteractive()) {
         this.pressed = true;
         this.applyNormalizedValue(mouseX, this.shouldSnapDuringInteraction());
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
    * @param wheelDelta mouse wheel delta
    * @param coarseStep coarse-step modifier
    * @param fineStep fine-step modifier
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onMouseWheel(float wheelDelta, boolean coarseStep, boolean fineStep) {
      if (this.isInteractive() && this.hovered && wheelDelta != 0.0F) {
         BigDecimal bigDecimal = ((SliderModel)this.model).getStep();
         if (coarseStep) {
            bigDecimal = bigDecimal.multiply(BigDecimal.TEN);
         } else if (fineStep) {
            bigDecimal = bigDecimal.multiply(new BigDecimal("0.1"));
         }

         BigDecimal bigDecimal2 = ((SliderModel)this.model).getValue();
         BigDecimal bigDecimal3 = BigDecimal.valueOf((double)(-Math.signum(wheelDelta)));
         BigDecimal bigDecimal4 = bigDecimal2.add(bigDecimal3.multiply(bigDecimal));
         ((SliderModel)this.model).setValue(((SliderModel)this.model).normalizeValue(bigDecimal4, true));
         this.notifyValueChanged(bigDecimal2);
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
    * @param enabled new show text
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setShowText(boolean enabled) {
      this.showText = enabled;
   }

   /**
    * Adds listener.
    *
    * @param listener parameter used by this operation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void addListener(ValueListener<BigDecimal> listener) {
      if (listener != null) {
         this.listeners.add(listener);
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
    * @param bigDecimal new value
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setValue(BigDecimal bigDecimal) {
      BigDecimal bigDecimal2 = ((SliderModel)this.model).getValue();
      if (bigDecimal2 != null && bigDecimal2.equals(bigDecimal)) {
         return;
      }

      ((SliderModel)this.model).setValue(bigDecimal);
      this.notifyValueChanged(bigDecimal2);
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
    * @param bigDecimal new min
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setMin(BigDecimal bigDecimal) {
      BigDecimal bigDecimal2 = ((SliderModel)this.model).getValue();
      ((SliderModel)this.model).setMin(bigDecimal);
      this.notifyValueChanged(bigDecimal2);
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
    * @param bigDecimal new max
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setMax(BigDecimal bigDecimal) {
      BigDecimal bigDecimal2 = ((SliderModel)this.model).getValue();
      ((SliderModel)this.model).setMax(bigDecimal);
      this.notifyValueChanged(bigDecimal2);
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
    * @param bigDecimal new step
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setStep(BigDecimal bigDecimal) {
      BigDecimal bigDecimal2 = ((SliderModel)this.model).getValue();
      ((SliderModel)this.model).setStep(bigDecimal);
      this.notifyValueChanged(bigDecimal2);
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
    * @param snapMode new snap mode
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setSnapMode(SnapMode snapMode) {
      BigDecimal bigDecimal = ((SliderModel)this.model).getValue();
      ((SliderModel)this.model).setSnapMode(snapMode);
      this.notifyValueChanged(bigDecimal);
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
      BigDecimal decimal = ((SliderModel)this.model).getMax().subtract(((SliderModel)this.model).getMin());
      if (decimal.signum() == 0) {
         return 0.0F;
      } else {
         BigDecimal decimal2 = ((SliderModel)this.model).getValue().subtract(((SliderModel)this.model).getMin());
         return clampNormalized(decimal2.divide(decimal, 6, RoundingMode.HALF_UP).floatValue());
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
      BigDecimal decimal = ((SliderModel)this.model).getValue();
      if (this.formatter != null) {
         return (String)this.formatter.apply(decimal);
      } else {
         int decimal2 = Math.max(0, ((SliderModel)this.model).getStep().scale());
         return decimal.setScale(decimal2, RoundingMode.HALF_UP).toPlainString();
      }
   }

   /**
    * Updates formatter.
    *
    * @param function<BigDecimal parameter used by this operation
    * @param function new formatter
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setFormatter(Function<BigDecimal, String> function) {
      this.formatter = function;
   }

   /**
    * Updates on value changed.
    *
    * @param consumer new on value changed
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setOnValueChanged(Consumer<BigDecimal> consumer) {
      this.onValueChanged = consumer;
   }

   protected void onAvailabilityChanged() {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      }

   }

   private boolean isInteractive() {
      return this.isEnabled() && this.isVisible();
   }

   private void applyNormalizedValue(float x, boolean snap) {
      float value = clampNormalized(x);
      BigDecimal bigDecimal = ((SliderModel)this.model).getMin();
      BigDecimal bigDecimal2 = ((SliderModel)this.model).getMax();
      BigDecimal bigDecimal3 = bigDecimal2.subtract(bigDecimal);
      BigDecimal bigDecimal4 = bigDecimal.add(bigDecimal3.multiply(BigDecimal.valueOf((double)value)));
      BigDecimal bigDecimal5 = ((SliderModel)this.model).getValue();
      if (snap) {
         ((SliderModel)this.model).setValue(bigDecimal4);
      } else {
         ((SliderModel)this.model).setValue(((SliderModel)this.model).normalizeValue(bigDecimal4, false));
      }

      this.notifyValueChanged(bigDecimal5);
   }

   private boolean shouldSnapDuringInteraction() {
      return ((SliderModel)this.model).getSnapMode() == SnapMode.ALWAYS;
   }

   private void notifyValueChanged(BigDecimal bigDecimal) {
      BigDecimal bigDecimal2 = ((SliderModel)this.model).getValue();
      if (bigDecimal.compareTo(bigDecimal2) != 0) {
         if (this.onValueChanged != null) {
            this.onValueChanged.accept(bigDecimal2);
         }

         this.notifyListeners(bigDecimal2);
      }

   }

   private void notifyListeners(BigDecimal bigDecimal) {
      for(ValueListener<BigDecimal> listener : this.listeners) {
         listener.onChange(bigDecimal);
      }

   }

   private void resetInteractionState() {
      this.hovered = false;
      this.pressed = false;
      this.dragging = false;
   }

   private static float clampNormalized(float x) {
      return Math.max(0.0F, Math.min(1.0F, x));
   }
}
