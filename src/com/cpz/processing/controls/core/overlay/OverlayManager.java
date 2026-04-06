package com.cpz.processing.controls.core.overlay;

import com.cpz.processing.controls.core.focus.FocusManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Overlay component for overlay manager.
 *
 * Responsibilities:
 * - Coordinate overlay-specific state or drawing flow.
 * - Keep overlay behavior isolated from base controls.
 *
 * Behavior:
 * - Keeps the public role isolated from unrelated concerns.
 *
 * Notes:
 * - This type is part of the public project surface.
 */
public class OverlayManager {
   private final List<OverlayEntry> overlays = new ArrayList<>();
   private final List<OverlayEntry> focusManagedOverlays = new ArrayList<>();
   private final Map<OverlayEntry, FocusManager.FocusToken> focusTokens = new HashMap<>();
   private FocusManager focusManager;

   /**
    * Creates a overlay manager.
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public OverlayManager() {
   }

   /**
    * Creates a overlay manager.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public OverlayManager(FocusManager var1) {
      this.focusManager = var1;
   }

   /**
    * Updates focus manager.
    *
    * @param var1 new focus manager
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setFocusManager(FocusManager var1) {
      this.focusManager = var1;
   }

   /**
    * Performs register.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void register(OverlayEntry var1) {
      if (var1 != null && !this.overlays.contains(var1)) {
         this.overlays.add(var1);
         this.sort();
         this.handleFocusOnRegister(var1);
      }
   }

   /**
    * Performs unregister.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void unregister(OverlayEntry var1) {
      if (var1 != null) {
         this.overlays.remove(var1);
         this.handleFocusOnUnregister(var1);
      }
   }

   private void sort() {
      this.overlays.sort((var0, var1) -> Integer.compare(var1.getZIndex(), var0.getZIndex()));
   }

   /**
    * Returns active overlays.
    *
    * @return current active overlays
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public List<OverlayEntry> getActiveOverlays() {
      return Collections.unmodifiableList(this.overlays);
   }

   /**
    * Returns top overlay.
    *
    * @return current top overlay
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public Optional<OverlayEntry> getTopOverlay() {
      return this.overlays.isEmpty() ? Optional.empty() : Optional.of(this.overlays.get(0));
   }

   /**
    * Returns whether top overlay.
    *
    * @param var1 parameter used by this operation
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isTopOverlay(OverlayEntry var1) {
      return var1 != null && !this.overlays.isEmpty() && this.overlays.get(0) == var1;
   }

   /**
    * Clears all.
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
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
