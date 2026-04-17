package com.cpz.processing.controls.controls.numericfield.model;

import com.cpz.processing.controls.core.model.Enableable;
import com.cpz.processing.controls.core.util.ControlCode;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Model for numeric field model.
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
 * - Internal fields such as min/max/step and parsing flags support the closed control
 *   implementation and are not exposed through the current NumericField facade.
 */
public final class NumericFieldModel implements Enableable {
   private final String code;
   private BigDecimal value;
   private BigDecimal min;
   private BigDecimal max;
   private BigDecimal step;
   private boolean allowNegative;
   private boolean allowDecimal;
   private int scale;
   private boolean enabled = true;

   /**
    * Creates a numeric field model.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    * @param var4 parameter used by this operation
    * @param var5 parameter used by this operation
    * @param var6 parameter used by this operation
    * @param var7 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public NumericFieldModel(BigDecimal var1, BigDecimal var2, BigDecimal var3, BigDecimal var4, boolean var5, boolean var6, int var7) {
      this(ControlCode.auto("numericfield"), var1, var2, var3, var4, var5, var6, var7);
   }

   public NumericFieldModel(String var1, BigDecimal var2, BigDecimal var3, BigDecimal var4, BigDecimal var5, boolean var6, boolean var7, int var8) {
      this.code = Objects.requireNonNull(var1, "code");
      this.min = var3;
      this.max = var4;
      if (this.min != null && this.max != null && this.max.compareTo(this.min) < 0) {
         BigDecimal var9 = this.min;
         this.min = this.max;
         this.max = var9;
      }

      this.setScale(var8);
      this.setStep(var5);
      this.allowNegative = var6;
      this.allowDecimal = var7;
      this.setValue(var2 == null ? BigDecimal.ZERO : var2);
   }

   public String getCode() {
      return this.code;
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
      this.value = this.clamp(var1 == null ? BigDecimal.ZERO : var1);
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
      this.min = var1;
      if (this.min != null && this.max != null && this.max.compareTo(this.min) < 0) {
         this.max = this.min;
      }

      this.value = this.clamp(this.value);
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
      this.max = var1;
      if (this.min != null && this.max != null && this.max.compareTo(this.min) < 0) {
         this.max = this.min;
      }

      this.value = this.clamp(this.value);
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
      if (var1 != null && var1.compareTo(BigDecimal.ZERO) > 0) {
         this.step = var1;
      } else {
         this.step = BigDecimal.ONE;
      }
   }

   /**
    * Returns whether allow negative.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isAllowNegative() {
      return this.allowNegative;
   }

   /**
    * Updates allow negative.
    *
    * @param var1 new allow negative
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setAllowNegative(boolean var1) {
      this.allowNegative = var1;
   }

   /**
    * Returns whether allow decimal.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isAllowDecimal() {
      return this.allowDecimal;
   }

   /**
    * Updates allow decimal.
    *
    * @param var1 new allow decimal
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setAllowDecimal(boolean var1) {
      this.allowDecimal = var1;
   }

   /**
    * Returns scale.
    *
    * @return current scale
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getScale() {
      return this.scale;
   }

   /**
    * Updates scale.
    *
    * @param var1 new scale
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setScale(int var1) {
      this.scale = Math.max(0, var1);
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

   private BigDecimal clamp(BigDecimal var1) {
      if (this.min != null && var1.compareTo(this.min) < 0) {
         return this.min;
      } else {
         return this.max != null && var1.compareTo(this.max) > 0 ? this.max : var1;
      }
   }
}
