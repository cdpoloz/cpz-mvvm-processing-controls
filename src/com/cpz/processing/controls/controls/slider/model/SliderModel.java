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

   public SliderModel(String var1) {
      this.code = Objects.requireNonNull(var1, "code");
      this.min = DEFAULT_MIN;
      this.max = DEFAULT_MAX;
      this.step = DEFAULT_STEP;
      this.value = DEFAULT_VALUE;
      this.snapMode = SnapMode.ALWAYS;
      this.enabled = true;
      this.setValue(DEFAULT_VALUE);
   }

   public SliderModel(String var1, BigDecimal var2, BigDecimal var3, BigDecimal var4, BigDecimal var5, SnapMode var6) {
      this.code = Objects.requireNonNull(var1, "code");
      BigDecimal var7 = requireNonNull(var2, "min");
      BigDecimal var8 = requireNonNull(var3, "max");
      BigDecimal var9 = requireNonNull(var4, "step");
      BigDecimal var10 = requireNonNull(var5, "value");
      if (var7.compareTo(var8) >= 0) {
         throw new IllegalArgumentException("min must be < max");
      } else if (var9.compareTo(BigDecimal.ZERO) <= 0) {
         throw new IllegalArgumentException("step must be > 0");
      } else {
         this.min = var7;
         this.max = var8;
         this.step = var9;
         this.snapMode = var6 == null ? SnapMode.ALWAYS : var6;
         this.enabled = true;
         this.value = this.normalizeValue(var10, this.shouldSnapImmediately());
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
    * @param var1 new min
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setMin(BigDecimal var1) {
      BigDecimal var2 = requireNonNull(var1, "min");
      if (var2.compareTo(this.max) >= 0) {
         throw new IllegalArgumentException("min must be < max");
      } else {
         this.min = var2;
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
    * @param var1 new max
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setMax(BigDecimal var1) {
      BigDecimal var2 = requireNonNull(var1, "max");
      if (var2.compareTo(this.min) <= 0) {
         throw new IllegalArgumentException("max must be > min");
      } else {
         this.max = var2;
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
    * @param var1 new step
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setStep(BigDecimal var1) {
      BigDecimal var2 = requireNonNull(var1, "step");
      if (var2.compareTo(BigDecimal.ZERO) <= 0) {
         throw new IllegalArgumentException("step must be > 0");
      } else {
         this.step = var2;
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
    * @param var1 new value
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setValue(BigDecimal var1) {
      this.value = this.normalizeValue(requireNonNull(var1, "value"), this.shouldSnapImmediately());
   }

   /**
    * Performs normalize value.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public BigDecimal normalizeValue(BigDecimal var1, boolean var2) {
      BigDecimal var3 = requireNonNull(var1, "value");
      return var2 ? this.clampAndSnap(var3) : this.clampValue(var3);
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
    * @param var1 new snap mode
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setSnapMode(SnapMode var1) {
      this.snapMode = var1 == null ? SnapMode.ALWAYS : var1;
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
    * @param var1 new enabled
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   private BigDecimal clampAndSnap(BigDecimal var1) {
      BigDecimal var2 = this.clampValue(var1);
      BigDecimal var3 = var2.subtract(this.min);
      BigDecimal var4 = var3.divide(this.step, 0, RoundingMode.HALF_UP);
      BigDecimal var5 = this.min.add(var4.multiply(this.step));
      return var5.max(this.min).min(this.max);
   }

   private BigDecimal clampValue(BigDecimal var1) {
      return var1.max(this.min).min(this.max);
   }

   private boolean shouldSnapImmediately() {
      return this.snapMode == SnapMode.ALWAYS;
   }

   private static BigDecimal requireNonNull(BigDecimal var0, String var1) {
      if (var0 == null) {
         throw new IllegalArgumentException(var1 + " must not be null");
      } else {
         return var0;
      }
   }

   static {
      DEFAULT_MIN = BigDecimal.ZERO;
      DEFAULT_MAX = BigDecimal.ONE;
      DEFAULT_STEP = new BigDecimal("0.01");
      DEFAULT_VALUE = BigDecimal.ZERO;
   }
}
