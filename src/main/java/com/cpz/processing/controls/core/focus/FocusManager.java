package com.cpz.processing.controls.core.focus;

import com.cpz.processing.controls.core.input.KeyboardInputTarget;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Owns an exclusive set of focusable targets.
 *
 * <p>A standalone manager remains a complete local authority. A facade-owned
 * manager can also be attached to a host authority; requests then participate
 * in the host's exclusive focus scope while local clear operations affect only
 * targets registered by that facade.</p>
 *
 * @author CPZ
 */
public final class FocusManager {
   private final List<Focusable> focusables = new ArrayList<>();
   private final List<FocusToken> focusHistory = new ArrayList<>();
   private final Map<FocusToken, FocusSnapshot> snapshots = new HashMap<>();
   private Focusable focused;
   private int focusedIndex = -1;
   private FocusManager authority;
   private int authorityAttachments;

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
         if (this.authority != null) {
            this.authority.register(focusable);
         }
      }

   }

   /**
    * Removes a target from this manager.
    *
    * <p>If the target currently owns focus, focus is cleared before the target
    * is released. Snapshots referring to the target are discarded so the
    * manager does not retain it for later restoration.</p>
    *
    * @param focusable target to remove
    */
   public void unregister(Focusable focusable) {
      if (focusable == null || !this.focusables.remove(focusable)) {
         return;
      }
      List<FocusToken> discardedTokens = new ArrayList<>();
      for (Map.Entry<FocusToken, FocusSnapshot> entry : this.snapshots.entrySet()) {
         if (entry.getValue().target == focusable) {
            discardedTokens.add(entry.getKey());
         }
      }
      for (FocusToken token : discardedTokens) {
         this.snapshots.remove(token);
         this.focusHistory.remove(token);
      }
      if (this.authority != null) {
         this.authority.unregister(focusable);
         return;
      }

      if (this.focused == focusable) {
         this.clearFocus();
      } else if (this.focused != null) {
         this.focusedIndex = this.focusables.indexOf(this.focused);
      }
   }

   /**
    * Attaches this facade-local manager to a shared authority.
    *
    * <p>Repeated attachment to the same authority is reference-counted so a
    * control exposed through more than one registered layer is not detached
    * prematurely. Attaching one local manager to two different authorities at
    * the same time is rejected because a control cannot belong to two routing
    * scopes simultaneously.</p>
    *
    * @param focusManager shared authority
    */
   public void attachAuthority(FocusManager focusManager) {
      FocusManager shared = Objects.requireNonNull(focusManager, "focusManager").rootAuthority();
      if (shared == this) {
         return;
      }
      if (this.authority == shared) {
         this.authorityAttachments++;
         return;
      }
      if (this.authority != null) {
         throw new IllegalStateException("Focus manager is already attached to another authority.");
      }

      Focusable previouslyFocused = this.focused != null && this.focused.isFocused()
            ? this.focused
            : null;
      if (previouslyFocused != null) {
         this.clearCurrentFocus();
      }
      this.focused = null;
      this.focusedIndex = -1;
      this.authority = shared;
      this.authorityAttachments = 1;
      for (Focusable focusable : this.focusables) {
         shared.register(focusable);
      }
      if (previouslyFocused != null) {
         shared.requestFocus(previouslyFocused);
      }
   }

   /**
    * Detaches this facade-local manager from the supplied authority.
    *
    * @param focusManager authority previously supplied to
    *                     {@link #attachAuthority(FocusManager)}
    */
   public void detachAuthority(FocusManager focusManager) {
      if (focusManager == null || this.authority != focusManager.rootAuthority()) {
         return;
      }
      if (--this.authorityAttachments > 0) {
         return;
      }

      FocusManager previousAuthority = this.authority;
      this.authority = null;
      this.authorityAttachments = 0;
      for (Focusable focusable : this.focusables) {
         previousAuthority.unregister(focusable);
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
      if (this.authority != null) {
         if (focusable == null) {
            this.clearFocus();
            return;
         }
         this.register(focusable);
         this.authority.requestFocus(focusable);
         return;
      }
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
      if (this.authority != null) {
         Focusable sharedFocus = this.authority.getFocused();
         if (this.focusables.contains(sharedFocus)) {
            this.authority.clearFocus();
         }
         return;
      }
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
      if (this.authority != null) {
         return this.authority.isFocused(focusable);
      }
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
      if (this.authority != null) {
         return this.authority.pushFocus();
      }
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
      if (this.authority != null) {
         this.authority.popFocus(token);
         return;
      }
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
      if (this.authority != null) {
         this.authority.discardFocus(token);
         return;
      }
      this.releaseFocusToken(token, false);
   }

   /**
    * Updates focus for next.
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void focusNext() {
      if (this.authority != null) {
         this.authority.focusNext();
         return;
      }
      if (this.focusables.isEmpty()) {
         this.clearFocus();
      } else {
         int value = this.focusedIndex >= 0 ? this.focusedIndex : -1;

         for(int value2 = 1; value2 <= this.focusables.size(); ++value2) {
            int candidateIndex = Math.floorMod(value + value2, this.focusables.size());
            Focusable candidate = this.focusables.get(candidateIndex);
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
      if (this.authority != null) {
         this.authority.focusPrevious();
         return;
      }
      if (this.focusables.isEmpty()) {
         this.clearFocus();
      } else {
         int value = this.focusedIndex >= 0 ? this.focusedIndex : 0;

         for(int value2 = 1; value2 <= this.focusables.size(); ++value2) {
            int candidateIndex = Math.floorMod(value - value2, this.focusables.size());
            Focusable candidate = this.focusables.get(candidateIndex);
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
      if (this.authority != null) {
         KeyboardInputTarget sharedTarget = this.authority.getFocusedKeyboardTarget();
         return this.focusables.contains(sharedTarget) ? sharedTarget : null;
      }
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
      if (this.authority != null) {
         Focusable sharedFocus = this.authority.getFocused();
         return this.focusables.contains(sharedFocus) ? sharedFocus : null;
      }
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
            FocusSnapshot snapshot = this.snapshots.remove(token);
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

   private FocusManager rootAuthority() {
      return this.authority == null ? this : this.authority.rootAuthority();
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
