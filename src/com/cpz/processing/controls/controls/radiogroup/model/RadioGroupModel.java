package com.cpz.processing.controls.controls.radiogroup.model;

import com.cpz.processing.controls.core.model.Enableable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RadioGroupModel implements Enableable {
   private List<String> options;
   private int selectedIndex;
   private boolean enabled;

   public RadioGroupModel(List<String> var1) {
      this(var1, -1);
   }

   public RadioGroupModel(List<String> var1, int var2) {
      this.enabled = true;
      this.options = this.sanitizeOptions(var1);
      this.selectedIndex = this.normalizeIndex(var2, this.options.size());
   }

   public List<String> getOptions() {
      return Collections.unmodifiableList(this.options);
   }

   public void setOptions(List<String> var1) {
      this.options = this.sanitizeOptions(var1);
      this.selectedIndex = this.normalizeIndex(this.selectedIndex, this.options.size());
   }

   public int getSelectedIndex() {
      return this.selectedIndex;
   }

   public void setSelectedIndex(int var1) {
      this.selectedIndex = this.normalizeIndex(var1, this.options.size());
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   private List<String> sanitizeOptions(List<String> var1) {
      if (var1 != null && !var1.isEmpty()) {
         ArrayList<String> var2 = new ArrayList<>(var1.size());

         for(String var4 : var1) {
            var2.add(var4 == null ? "" : var4);
         }

         return Collections.unmodifiableList(var2);
      } else {
         return List.of();
      }
   }

   private int normalizeIndex(int var1, int var2) {
      if (var2 <= 0) {
         return -1;
      } else {
         return var1 >= -1 && var1 < var2 ? var1 : -1;
      }
   }
}
