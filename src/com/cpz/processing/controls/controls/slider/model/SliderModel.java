package com.cpz.processing.controls.controls.slider.model;

import com.cpz.processing.controls.core.model.Enableable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class SliderModel implements Enableable {
   private static final BigDecimal DEFAULT_MIN;
   private static final BigDecimal DEFAULT_MAX;
   private static final BigDecimal DEFAULT_STEP;
   private static final BigDecimal DEFAULT_VALUE;
   private BigDecimal min;
   private BigDecimal max;
   private BigDecimal step;
   private BigDecimal value;
   private SnapMode snapMode;
   private boolean enabled;

   public SliderModel() {
      this.min = DEFAULT_MIN;
      this.max = DEFAULT_MAX;
      this.step = DEFAULT_STEP;
      this.value = DEFAULT_VALUE;
      this.snapMode = SnapMode.ALWAYS;
      this.enabled = true;
      this.setValue(DEFAULT_VALUE);
   }

   public BigDecimal getMin() {
      return this.min;
   }

   public void setMin(BigDecimal var1) {
      BigDecimal var2 = requireNonNull(var1, "min");
      if (var2.compareTo(this.max) >= 0) {
         throw new IllegalArgumentException("min must be < max");
      } else {
         this.min = var2;
         this.value = this.clampValue(this.value);
      }
   }

   public BigDecimal getMax() {
      return this.max;
   }

   public void setMax(BigDecimal var1) {
      BigDecimal var2 = requireNonNull(var1, "max");
      if (var2.compareTo(this.min) <= 0) {
         throw new IllegalArgumentException("max must be > min");
      } else {
         this.max = var2;
         this.value = this.clampValue(this.value);
      }
   }

   public BigDecimal getStep() {
      return this.step;
   }

   public void setStep(BigDecimal var1) {
      BigDecimal var2 = requireNonNull(var1, "step");
      if (var2.compareTo(BigDecimal.ZERO) <= 0) {
         throw new IllegalArgumentException("step must be > 0");
      } else {
         this.step = var2;
         this.value = this.clampAndSnap(this.value);
      }
   }

   public BigDecimal getValue() {
      return this.value;
   }

   public void setValue(BigDecimal var1) {
      this.value = this.normalizeValue(requireNonNull(var1, "value"), this.shouldSnapImmediately());
   }

   public BigDecimal normalizeValue(BigDecimal var1, boolean var2) {
      BigDecimal var3 = requireNonNull(var1, "value");
      return var2 ? this.clampAndSnap(var3) : this.clampValue(var3);
   }

   public SnapMode getSnapMode() {
      return this.snapMode;
   }

   public void setSnapMode(SnapMode var1) {
      this.snapMode = var1 == null ? SnapMode.ALWAYS : var1;
      this.value = this.normalizeValue(this.value, this.shouldSnapImmediately());
   }

   public boolean isEnabled() {
      return this.enabled;
   }

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
