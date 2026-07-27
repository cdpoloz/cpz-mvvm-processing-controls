package com.cpz.processing.controls.core.overlay;

import com.cpz.processing.controls.core.focus.FocusManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
   private boolean clearingAll;

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
    * @return snapshot of current active overlays
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    * - Uses a snapshot so overlays can safely register or unregister while a
    *   caller renders the returned entries.
    */
   public List<OverlayEntry> getActiveOverlays() {
      return List.copyOf(this.overlays);
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
    * Closes every registered overlay through its normal lifecycle callback.
    *
    * <p>The operation uses stable snapshots so callbacks may safely unregister
    * overlays or register follow-up overlays. Every entry encountered during
    * the operation receives at most one close callback; entries without a
    * callback are unregistered directly. Focus-managed overlays close in
    * reverse registration order so focus restoration matches individual
    * top-down closure.</p>
    *
    * <p>Runtime exceptions from callbacks are collected while the remaining
    * overlays are still closed, then the first exception is rethrown with
    * subsequent failures suppressed.</p>
    */
   public void clearAll() {
      if (this.clearingAll) {
         return;
      }

      this.clearingAll = true;
      RuntimeException failure = null;
      Set<OverlayEntry> notified = Collections.newSetFromMap(new IdentityHashMap<>());
      try {
         while (!this.overlays.isEmpty()) {
            for (OverlayEntry overlayEntry : this.closeOrderSnapshot()) {
               if (notified.add(overlayEntry) && overlayEntry.getOnClose() != null) {
                  try {
                     overlayEntry.getOnClose().run();
                  } catch (RuntimeException ex) {
                     if (failure == null) {
                        failure = ex;
                     } else {
                        failure.addSuppressed(ex);
                     }
                  }
               }
               if (this.overlays.contains(overlayEntry)) {
                  this.unregister(overlayEntry);
               }
            }
         }
      } finally {
         this.clearingAll = false;
      }

      if (failure != null) {
         throw failure;
      }
   }

   private List<OverlayEntry> closeOrderSnapshot() {
      List<OverlayEntry> closeOrder = new ArrayList<>();
      for (int index = this.focusManagedOverlays.size() - 1; index >= 0; index--) {
         OverlayEntry overlayEntry = this.focusManagedOverlays.get(index);
         if (this.overlays.contains(overlayEntry)) {
            closeOrder.add(overlayEntry);
         }
      }
      for (OverlayEntry overlayEntry : this.overlays) {
         if (!closeOrder.contains(overlayEntry)) {
            closeOrder.add(overlayEntry);
         }
      }
      return closeOrder;
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
