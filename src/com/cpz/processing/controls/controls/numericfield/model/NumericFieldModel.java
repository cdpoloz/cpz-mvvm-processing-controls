package com.cpz.processing.controls.controls.numericfield.model;

import com.cpz.processing.controls.core.model.Enableable;
import java.math.BigDecimal;

public final class NumericFieldModel implements Enableable {
   private BigDecimal value;
   private BigDecimal min;
   private BigDecimal max;
   private BigDecimal step;
   private boolean allowNegative;
   private boolean allowDecimal;
   private int scale;
   private boolean enabled = true;

   public NumericFieldModel(BigDecimal var1, BigDecimal var2, BigDecimal var3, BigDecimal var4, boolean var5, boolean var6, int var7) {
      this.min = var2 == null ? BigDecimal.ZERO : var2;
      this.max = var3 == null ? this.min : var3;
      if (this.max.compareTo(this.min) < 0) {
         BigDecimal var8 = this.min;
         this.min = this.max;
         this.max = var8;
      }

      this.setScale(var7);
      this.setStep(var4);
      this.allowNegative = var5;
      this.allowDecimal = var6;
      this.setValue(var1 == null ? this.min : var1);
   }

   public BigDecimal getValue() {
      return this.value;
   }

   public void setValue(BigDecimal var1) {
      this.value = this.clamp(var1 == null ? this.min : var1);
   }

   public BigDecimal getMin() {
      return this.min;
   }

   public void setMin(BigDecimal var1) {
      this.min = var1 == null ? BigDecimal.ZERO : var1;
      if (this.max.compareTo(this.min) < 0) {
         this.max = this.min;
      }

      this.value = this.clamp(this.value);
   }

   public BigDecimal getMax() {
      return this.max;
   }

   public void setMax(BigDecimal var1) {
      this.max = var1 == null ? this.min : var1;
      if (this.max.compareTo(this.min) < 0) {
         this.max = this.min;
      }

      this.value = this.clamp(this.value);
   }

   public BigDecimal getStep() {
      return this.step;
   }

   public void setStep(BigDecimal var1) {
      if (var1 != null && var1.compareTo(BigDecimal.ZERO) > 0) {
         this.step = var1;
      } else {
         this.step = BigDecimal.ONE;
      }
   }

   public boolean isAllowNegative() {
      return this.allowNegative;
   }

   public void setAllowNegative(boolean var1) {
      this.allowNegative = var1;
   }

   public boolean isAllowDecimal() {
      return this.allowDecimal;
   }

   public void setAllowDecimal(boolean var1) {
      this.allowDecimal = var1;
   }

   public int getScale() {
      return this.scale;
   }

   public void setScale(int var1) {
      this.scale = Math.max(0, var1);
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   private BigDecimal clamp(BigDecimal var1) {
      if (var1.compareTo(this.min) < 0) {
         return this.min;
      } else {
         return var1.compareTo(this.max) > 0 ? this.max : var1;
      }
   }
}
