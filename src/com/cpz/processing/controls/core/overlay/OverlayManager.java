package com.cpz.processing.controls.core.overlay;

import com.cpz.processing.controls.core.focus.FocusManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OverlayManager {
   private final List<OverlayEntry> overlays = new ArrayList<>();
   private final List<OverlayEntry> focusManagedOverlays = new ArrayList<>();
   private final Map<OverlayEntry, FocusManager.FocusToken> focusTokens = new HashMap<>();
   private FocusManager focusManager;

   public OverlayManager() {
   }

   public OverlayManager(FocusManager var1) {
      this.focusManager = var1;
   }

   public void setFocusManager(FocusManager var1) {
      this.focusManager = var1;
   }

   public void register(OverlayEntry var1) {
      if (var1 != null && !this.overlays.contains(var1)) {
         this.overlays.add(var1);
         this.sort();
         this.handleFocusOnRegister(var1);
      }
   }

   public void unregister(OverlayEntry var1) {
      if (var1 != null) {
         this.overlays.remove(var1);
         this.handleFocusOnUnregister(var1);
      }
   }

   private void sort() {
      this.overlays.sort((var0, var1) -> Integer.compare(var1.getZIndex(), var0.getZIndex()));
   }

   public List<OverlayEntry> getActiveOverlays() {
      return Collections.unmodifiableList(this.overlays);
   }

   public Optional<OverlayEntry> getTopOverlay() {
      return this.overlays.isEmpty() ? Optional.empty() : Optional.of(this.overlays.get(0));
   }

   public boolean isTopOverlay(OverlayEntry var1) {
      return var1 != null && !this.overlays.isEmpty() && this.overlays.get(0) == var1;
   }

   public void clearAll() {
      for(int var1 = this.focusManagedOverlays.size() - 1; var1 >= 0; --var1) {
         OverlayEntry var2 = (OverlayEntry)this.focusManagedOverlays.get(var1);
         FocusManager.FocusToken var3 = (FocusManager.FocusToken)this.focusTokens.remove(var2);
         if (this.focusManager != null) {
            this.focusManager.discardFocus(var3);
         }
      }

      this.focusManagedOverlays.clear();
      this.overlays.clear();
   }

   private void handleFocusOnRegister(OverlayEntry var1) {
      if (this.focusManager != null && var1.getFocusTarget() != null) {
         FocusManager.FocusToken var2 = this.focusManager.pushFocus();
         this.focusTokens.put(var1, var2);
         this.focusManagedOverlays.add(var1);
         this.focusManager.requestFocus(var1.getFocusTarget());
      }
   }

   private void handleFocusOnUnregister(OverlayEntry var1) {
      FocusManager.FocusToken var2 = (FocusManager.FocusToken)this.focusTokens.remove(var1);
      if (var2 == null) {
         this.focusManagedOverlays.remove(var1);
      } else {
         int var3 = this.focusManagedOverlays.lastIndexOf(var1);
         boolean var4 = var3 == this.focusManagedOverlays.size() - 1;
         if (var3 >= 0) {
            this.focusManagedOverlays.remove(var3);
         }

         if (this.focusManager != null) {
            if (var4) {
               this.focusManager.popFocus(var2);
            } else {
               this.focusManager.discardFocus(var2);
            }

         }
      }
   }
}
