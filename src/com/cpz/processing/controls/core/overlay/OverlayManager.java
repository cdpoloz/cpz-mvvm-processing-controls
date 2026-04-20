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
 *
 * @author CPZ
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
    * @param focusManager parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public OverlayManager(FocusManager focusManager) {
      this.focusManager = focusManager;
   }

   /**
    * Updates focus manager.
    *
    * @param focusManager new focus manager
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setFocusManager(FocusManager focusManager) {
      this.focusManager = focusManager;
   }

   /**
    * Performs register.
    *
    * @param overlayEntry parameter used by this operation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void register(OverlayEntry overlayEntry) {
      if (overlayEntry != null && !this.overlays.contains(overlayEntry)) {
         this.overlays.add(overlayEntry);
         this.sort();
         this.handleFocusOnRegister(overlayEntry);
      }
   }

   /**
    * Performs unregister.
    *
    * @param overlayEntry parameter used by this operation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void unregister(OverlayEntry overlayEntry) {
      if (overlayEntry != null) {
         this.overlays.remove(overlayEntry);
         this.handleFocusOnUnregister(overlayEntry);
      }
   }

   private void sort() {
      this.overlays.sort((left, right) -> Integer.compare(right.getZIndex(), left.getZIndex()));
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
    * @param right parameter used by this operation
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isTopOverlay(OverlayEntry right) {
      return right != null && !this.overlays.isEmpty() && this.overlays.get(0) == right;
   }

   /**
    * Clears all.
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void clearAll() {
      for(int color = this.focusManagedOverlays.size() - 1; color >= 0; --color) {
         OverlayEntry overlayEntry = (OverlayEntry)this.focusManagedOverlays.get(color);
         FocusManager.FocusToken token = (FocusManager.FocusToken)this.focusTokens.remove(overlayEntry);
         if (this.focusManager != null) {
            this.focusManager.discardFocus(token);
         }
      }

      this.focusManagedOverlays.clear();
      this.overlays.clear();
   }

   private void handleFocusOnRegister(OverlayEntry overlayEntry) {
      if (this.focusManager != null && overlayEntry.getFocusTarget() != null) {
         FocusManager.FocusToken token = this.focusManager.pushFocus();
         this.focusTokens.put(overlayEntry, token);
         this.focusManagedOverlays.add(overlayEntry);
         this.focusManager.requestFocus(overlayEntry.getFocusTarget());
      }
   }

   private void handleFocusOnUnregister(OverlayEntry overlayEntry) {
      FocusManager.FocusToken token = (FocusManager.FocusToken)this.focusTokens.remove(overlayEntry);
      if (token == null) {
         this.focusManagedOverlays.remove(overlayEntry);
      } else {
         int index = this.focusManagedOverlays.lastIndexOf(overlayEntry);
         boolean active = index == this.focusManagedOverlays.size() - 1;
         if (index >= 0) {
            this.focusManagedOverlays.remove(index);
         }

         if (this.focusManager != null) {
            if (active) {
               this.focusManager.popFocus(token);
            } else {
               this.focusManager.discardFocus(token);
            }

         }
      }
   }
}
