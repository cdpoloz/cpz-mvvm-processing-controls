package com.cpz.processing.controls.controls.slider.model;

import com.cpz.processing.controls.core.model.Enableable;
import com.cpz.processing.controls.core.util.ControlCode;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Model for slider model.
 *
 * Responsibilities:
 * - Store durable control state.
 * - Remain independent from rendering and input code.
 *
 * Behavior:
 * - Keeps state mutation independent from rendering concerns.
 *
 * Notes:
 * - This type belongs to the MVVM Model layer.
 *
 * @author CPZ
 */
public final class SliderModel implements Enableable {
   private static final BigDecimal DEFAULT_MIN;
   private static final BigDecimal DEFAULT_MAX;
   private static final BigDecimal DEFAULT_STEP;
   private static final BigDecimal DEFAULT_VALUE;
   private final String code;
   private BigDecimal min;
   private BigDecimal max;
   private BigDecimal step;
   private BigDecimal value;
   private SnapMode snapMode;
   private boolean enabled;

   /**
    * Creates a slider model.
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public SliderModel() {
      this(ControlCode.auto("slider"));
   }

   public SliderModel(String text) {
      this.code = Objects.requireNonNull(text, "code");
      this.min = DEFAULT_MIN;
      this.max = DEFAULT_MAX;
      this.step = DEFAULT_STEP;
      this.value = DEFAULT_VALUE;
      this.snapMode = SnapMode.ALWAYS;
      this.enabled = true;
      this.setValue(DEFAULT_VALUE);
   }

   public SliderModel(String text, BigDecimal bigDecimal, BigDecimal bigDecimal2, BigDecimal bigDecimal3, BigDecimal bigDecimal4, SnapMode snapMode) {
      this.code = Objects.requireNonNull(text, "code");
      BigDecimal bigDecimal5 = requireNonNull(bigDecimal, "min");
      BigDecimal bigDecimal6 = requireNonNull(bigDecimal2, "max");
      BigDecimal bigDecimal7 = requireNonNull(bigDecimal3, "step");
      BigDecimal bigDecimal8 = requireNonNull(bigDecimal4, "value");
      if (bigDecimal5.compareTo(bigDecimal6) >= 0) {
         throw new IllegalArgumentException("min must be < max");
      } else if (bigDecimal7.compareTo(BigDecimal.ZERO) <= 0) {
         throw new IllegalArgumentException("step must be > 0");
      } else {
         this.min = bigDecimal5;
         this.max = bigDecimal6;
         this.step = bigDecimal7;
         this.snapMode = snapMode == null ? SnapMode.ALWAYS : snapMode;
         this.enabled = true;
         this.value = this.normalizeValue(bigDecimal8, this.shouldSnapImmediately());
      }
   }

   public String getCode() {
      return this.code;
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
      return this.min;
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
      BigDecimal bigDecimal2 = requireNonNull(bigDecimal, "min");
      if (bigDecimal2.compareTo(this.max) >= 0) {
         throw new IllegalArgumentException("min must be < max");
      } else {
         this.min = bigDecimal2;
         this.value = this.clampValue(this.value);
      }
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
      return this.max;
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
      BigDecimal bigDecimal2 = requireNonNull(bigDecimal, "max");
      if (bigDecimal2.compareTo(this.min) <= 0) {
         throw new IllegalArgumentException("max must be > min");
      } else {
         this.max = bigDecimal2;
         this.value = this.clampValue(this.value);
      }
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
      return this.step;
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
      BigDecimal bigDecimal2 = requireNonNull(bigDecimal, "step");
      if (bigDecimal2.compareTo(BigDecimal.ZERO) <= 0) {
         throw new IllegalArgumentException("step must be > 0");
      } else {
         this.step = bigDecimal2;
         this.value = this.clampAndSnap(this.value);
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
      return this.value;
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
      this.value = this.normalizeValue(requireNonNull(bigDecimal, "value"), this.shouldSnapImmediately());
   }

   /**
    * Performs normalize value.
    *
    * @param value parameter used by this operation
    * @param snap parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public BigDecimal normalizeValue(BigDecimal value, boolean snap) {
      BigDecimal decimal = requireNonNull(value, "value");
      return snap ? this.clampAndSnap(decimal) : this.clampValue(decimal);
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
      return this.snapMode;
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
      this.snapMode = snapMode == null ? SnapMode.ALWAYS : snapMode;
      this.value = this.normalizeValue(this.value, this.shouldSnapImmediately());
   }

   /**
    * Returns whether enabled.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isEnabled() {
      return this.enabled;
   }

   /**
    * Updates enabled.
    *
    * @param enabled new enabled
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   private BigDecimal clampAndSnap(BigDecimal bigDecimal) {
      BigDecimal bigDecimal2 = this.clampValue(bigDecimal);
      BigDecimal bigDecimal3 = bigDecimal2.subtract(this.min);
      BigDecimal bigDecimal4 = bigDecimal3.divide(this.step, 0, RoundingMode.HALF_UP);
      BigDecimal bigDecimal5 = this.min.add(bigDecimal4.multiply(this.step));
      return bigDecimal5.max(this.min).min(this.max);
   }

   private BigDecimal clampValue(BigDecimal bigDecimal) {
      return bigDecimal.max(this.min).min(this.max);
   }

   private boolean shouldSnapImmediately() {
      return this.snapMode == SnapMode.ALWAYS;
   }

   private static BigDecimal requireNonNull(BigDecimal bigDecimal, String text) {
      if (bigDecimal == null) {
         throw new IllegalArgumentException(text + " must not be null");
      } else {
         return bigDecimal;
      }
   }

   static {
      DEFAULT_MIN = BigDecimal.ZERO;
      DEFAULT_MAX = BigDecimal.ONE;
      DEFAULT_STEP = new BigDecimal("0.01");
      DEFAULT_VALUE = BigDecimal.ZERO;
   }
}
