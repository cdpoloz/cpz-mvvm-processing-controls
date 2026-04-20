package com.cpz.processing.controls.core.focus;

import com.cpz.processing.controls.core.input.KeyboardInputTarget;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Public class for focus manager.
 *
 * Responsibilities:
 * - Expose a public architectural role.
 * - Keep responsibilities explicit in the API surface.
 *
 * Behavior:
 * - Keeps the public role isolated from unrelated concerns.
 *
 * Notes:
 * - This type is part of the public project surface.
 *
 * @author CPZ
 */
public final class FocusManager {
   private final List focusables = new ArrayList();
   private final List focusHistory = new ArrayList();
   private final Map snapshots = new HashMap();
   private Focusable focused;
   private int focusedIndex = -1;

   /**
    * Performs register.
    *
    * @param focusable parameter used by this operation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void register(Focusable focusable) {
      if (focusable != null && !this.focusables.contains(focusable)) {
         this.focusables.add(focusable);
      }

   }

   /**
    * Performs request focus.
    *
    * @param focusable parameter used by this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void requestFocus(Focusable focusable) {
      if (focusable != null && focusable.isVisible() && focusable.isEnabled()) {
         this.register(focusable);
         if (this.focused == focusable) {
            focusable.setFocused(true);
         } else {
            this.clearCurrentFocus();
            this.focused = focusable;
            this.focusedIndex = this.focusables.indexOf(focusable);
            this.focused.setFocused(true);
            this.focused.onFocusGained();
         }
      } else {
         this.clearFocus();
      }
   }

   /**
    * Clears focus.
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void clearFocus() {
      this.clearCurrentFocus();
      this.focused = null;
      this.focusedIndex = -1;
   }

   /**
    * Returns whether focused.
    *
    * @param focusable parameter used by this operation
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isFocused(Focusable focusable) {
      return this.focused == focusable && focusable != null && focusable.isFocused();
   }

   /**
    * Performs push focus.
    *
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public FocusToken pushFocus() {
      FocusToken focusable = new FocusToken();
      this.snapshots.put(focusable, new FocusSnapshot(this.focused, this.focusedIndex));
      this.focusHistory.add(focusable);
      return focusable;
   }

   /**
    * Performs pop focus.
    *
    * @param token parameter used by this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void popFocus(FocusToken token) {
      this.releaseFocusToken(token, true);
   }

   /**
    * Performs discard focus.
    *
    * @param token parameter used by this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void discardFocus(FocusToken token) {
      this.releaseFocusToken(token, false);
   }

   /**
    * Updates focus for next.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void focusNext() {
      if (this.focusables.isEmpty()) {
         this.clearFocus();
      } else {
         int value = this.focusedIndex >= 0 ? this.focusedIndex : -1;

         for(int value2 = 1; value2 <= this.focusables.size(); ++value2) {
            int candidateIndex = Math.floorMod(value + value2, this.focusables.size());
            Focusable candidate = (Focusable)this.focusables.get(candidateIndex);
            if (candidate.isVisible() && candidate.isEnabled()) {
               this.requestFocus(candidate);
               return;
            }
         }

         this.clearFocus();
      }
   }

   /**
    * Updates focus for previous.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void focusPrevious() {
      if (this.focusables.isEmpty()) {
         this.clearFocus();
      } else {
         int value = this.focusedIndex >= 0 ? this.focusedIndex : 0;

         for(int value2 = 1; value2 <= this.focusables.size(); ++value2) {
            int candidateIndex = Math.floorMod(value - value2, this.focusables.size());
            Focusable candidate = (Focusable)this.focusables.get(candidateIndex);
            if (candidate.isVisible() && candidate.isEnabled()) {
               this.requestFocus(candidate);
               return;
            }
         }

         this.clearFocus();
      }
   }

   /**
    * Returns focused keyboard target.
    *
    * @return current focused keyboard target
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public KeyboardInputTarget getFocusedKeyboardTarget() {
      return this.focused instanceof KeyboardInputTarget ? (KeyboardInputTarget)this.focused : null;
   }

   /**
    * Returns focused.
    *
    * @return current focused
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public Focusable getFocused() {
      return this.focused;
   }

   private void clearCurrentFocus() {
      if (this.focused != null) {
         this.focused.setFocused(false);
         this.focused.onFocusLost();
      }

   }

   private void releaseFocusToken(FocusToken token, boolean focused) {
      if (token != null) {
         int index = this.focusHistory.lastIndexOf(token);
         if (index < 0) {
            this.snapshots.remove(token);
         } else {
            boolean active = index == this.focusHistory.size() - 1;
            this.focusHistory.remove(index);
            FocusSnapshot snapshot = (FocusSnapshot)this.snapshots.remove(token);
            if (focused && active && snapshot != null) {
               this.restoreSnapshot(snapshot);
            }
         }
      }
   }

   private void restoreSnapshot(FocusSnapshot snapshot) {
      if (snapshot.target == null) {
         this.clearFocus();
      } else if (snapshot.target.isVisible() && snapshot.target.isEnabled()) {
         this.requestFocus(snapshot.target);
         this.focusedIndex = snapshot.index >= 0 ? snapshot.index : this.focusables.indexOf(snapshot.target);
      } else {
         this.clearFocus();
      }
   }

   /**
    * Public class for focus token.
    *
    * Responsibilities:
    * - Expose a public architectural role.
    * - Keep responsibilities explicit in the API surface.
    *
    * Behavior:
    * - Keeps the public role isolated from unrelated concerns.
    *
    * Notes:
    * - This type is part of the public project surface.
    */
   public static final class FocusToken {
      private final String id = UUID.randomUUID().toString();
   }

   private static final class FocusSnapshot {
      private final Focusable target;
      private final int index;

      private FocusSnapshot(Focusable focusable, int value) {
         this.target = focusable;
         this.index = value;
      }
   }
}
