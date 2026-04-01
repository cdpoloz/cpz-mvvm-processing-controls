package com.cpz.processing.controls.controls.dropdown.model;

import com.cpz.processing.controls.core.model.Enableable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DropDownModel implements Enableable {
   private List<String> items;
   private int selectedIndex;
   private boolean enabled;

   public DropDownModel(List<String> var1) {
      this(var1, -1);
   }

   public DropDownModel(List<String> var1, int var2) {
      this.items = sanitizeItems(var1);
      this.selectedIndex = normalizeSelectedIndex(var2, this.items.size());
      this.enabled = true;
   }

   public List<String> getItems() {
      return Collections.unmodifiableList(this.items);
   }

   public void setItems(List<String> var1) {
      this.items = sanitizeItems(var1);
      this.selectedIndex = normalizeSelectedIndex(this.selectedIndex, this.items.size());
   }

   public int getSelectedIndex() {
      return this.selectedIndex;
   }

   public void setSelectedIndex(int var1) {
      this.selectedIndex = normalizeSelectedIndex(var1, this.items.size());
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   private static List<String> sanitizeItems(List<String> var0) {
      if (var0 != null && !var0.isEmpty()) {
         ArrayList<String> var1 = new ArrayList<>(var0.size());

         for(String var3 : var0) {
            var1.add(var3 == null ? "" : var3);
         }

         return Collections.unmodifiableList(var1);
      } else {
         return List.of();
      }
   }

   private static int normalizeSelectedIndex(int var0, int var1) {
      if (var1 <= 0) {
         return -1;
      } else {
         return var0 < 0 ? -1 : Math.min(var0, var1 - 1);
      }
   }
}
