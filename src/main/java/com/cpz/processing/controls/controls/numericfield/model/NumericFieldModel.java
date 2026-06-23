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
 *
 * @author CPZ
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
    * @param value initial numeric value
    * @param min minimum allowed value
    * @param max maximum allowed value
    * @param step increment step
    * @param allowNegative whether negative values are allowed
    * @param allowDecimal whether decimal values are allowed
    * @param scale decimal scale
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public NumericFieldModel(BigDecimal value, BigDecimal min, BigDecimal max, BigDecimal step, boolean allowNegative, boolean allowDecimal, int scale) {
      this(ControlCode.auto("numericfield"), value, min, max, step, allowNegative, allowDecimal, scale);
   }

   public NumericFieldModel(String text, BigDecimal value, BigDecimal min, BigDecimal max, BigDecimal step, boolean allowNegative, boolean allowDecimal, int scale) {
      this.code = Objects.requireNonNull(text, "code");
      this.min = min;
      this.max = max;
      if (this.min != null && this.max != null && this.max.compareTo(this.min) < 0) {
         BigDecimal originalMin = this.min;
         this.min = this.max;
         this.max = originalMin;
      }

      this.setScale(scale);
      this.setStep(step);
      this.allowNegative = allowNegative;
      this.allowDecimal = allowDecimal;
      this.setValue(value == null ? BigDecimal.ZERO : value);
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
    * @param bigDecimal new value
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setValue(BigDecimal bigDecimal) {
      this.value = this.clamp(bigDecimal == null ? BigDecimal.ZERO : bigDecimal);
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
      this.min = bigDecimal;
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
    * @param bigDecimal new max
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setMax(BigDecimal bigDecimal) {
      this.max = bigDecimal;
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
    * @param bigDecimal new step
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setStep(BigDecimal bigDecimal) {
      if (bigDecimal != null && bigDecimal.compareTo(BigDecimal.ZERO) > 0) {
         this.step = bigDecimal;
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
    * @param enabled new allow negative
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setAllowNegative(boolean enabled) {
      this.allowNegative = allowNegative;
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
    * @param enabled new allow decimal
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setAllowDecimal(boolean enabled) {
      this.allowDecimal = enabled;
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
    * @param value new scale
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setScale(int value) {
      this.scale = Math.max(0, scale);
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

   private BigDecimal clamp(BigDecimal bigDecimal) {
      if (this.min != null && bigDecimal.compareTo(this.min) < 0) {
         return this.min;
      } else {
         return this.max != null && bigDecimal.compareTo(this.max) > 0 ? this.max : bigDecimal;
      }
   }
}
