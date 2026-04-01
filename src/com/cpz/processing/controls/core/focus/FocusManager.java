package com.cpz.processing.controls.core.focus;

import com.cpz.processing.controls.core.input.KeyboardInputTarget;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class FocusManager {
   private final List focusables = new ArrayList();
   private final List focusHistory = new ArrayList();
   private final Map snapshots = new HashMap();
   private Focusable focused;
   private int focusedIndex = -1;

   public void register(Focusable var1) {
      if (var1 != null && !this.focusables.contains(var1)) {
         this.focusables.add(var1);
      }

   }

   public void requestFocus(Focusable var1) {
      if (var1 != null && var1.isVisible() && var1.isEnabled()) {
         this.register(var1);
         if (this.focused == var1) {
            var1.setFocused(true);
         } else {
            this.clearCurrentFocus();
            this.focused = var1;
            this.focusedIndex = this.focusables.indexOf(var1);
            this.focused.setFocused(true);
            this.focused.onFocusGained();
         }
      } else {
         this.clearFocus();
      }
   }

   public void clearFocus() {
      this.clearCurrentFocus();
      this.focused = null;
      this.focusedIndex = -1;
   }

   public boolean isFocused(Focusable var1) {
      return this.focused == var1 && var1 != null && var1.isFocused();
   }

   public FocusToken pushFocus() {
      FocusToken var1 = new FocusToken();
      this.snapshots.put(var1, new FocusSnapshot(this.focused, this.focusedIndex));
      this.focusHistory.add(var1);
      return var1;
   }

   public void popFocus(FocusToken var1) {
      this.releaseFocusToken(var1, true);
   }

   public void discardFocus(FocusToken var1) {
      this.releaseFocusToken(var1, false);
   }

   public void focusNext() {
      if (this.focusables.isEmpty()) {
         this.clearFocus();
      } else {
         int var1 = this.focusedIndex >= 0 ? this.focusedIndex : -1;

         for(int var2 = 1; var2 <= this.focusables.size(); ++var2) {
            int var3 = Math.floorMod(var1 + var2, this.focusables.size());
            Focusable var4 = (Focusable)this.focusables.get(var3);
            if (var4.isVisible() && var4.isEnabled()) {
               this.requestFocus(var4);
               return;
            }
         }

         this.clearFocus();
      }
   }

   public void focusPrevious() {
      if (this.focusables.isEmpty()) {
         this.clearFocus();
      } else {
         int var1 = this.focusedIndex >= 0 ? this.focusedIndex : 0;

         for(int var2 = 1; var2 <= this.focusables.size(); ++var2) {
            int var3 = Math.floorMod(var1 - var2, this.focusables.size());
            Focusable var4 = (Focusable)this.focusables.get(var3);
            if (var4.isVisible() && var4.isEnabled()) {
               this.requestFocus(var4);
               return;
            }
         }

         this.clearFocus();
      }
   }

   public KeyboardInputTarget getFocusedKeyboardTarget() {
      return this.focused instanceof KeyboardInputTarget ? (KeyboardInputTarget)this.focused : null;
   }

   public Focusable getFocused() {
      return this.focused;
   }

   private void clearCurrentFocus() {
      if (this.focused != null) {
         this.focused.setFocused(false);
         this.focused.onFocusLost();
      }

   }

   private void releaseFocusToken(FocusToken var1, boolean var2) {
      if (var1 != null) {
         int var3 = this.focusHistory.lastIndexOf(var1);
         if (var3 < 0) {
            this.snapshots.remove(var1);
         } else {
            boolean var4 = var3 == this.focusHistory.size() - 1;
            this.focusHistory.remove(var3);
            FocusSnapshot var5 = (FocusSnapshot)this.snapshots.remove(var1);
            if (var2 && var4 && var5 != null) {
               this.restoreSnapshot(var5);
            }
         }
      }
   }

   private void restoreSnapshot(FocusSnapshot var1) {
      if (var1.target == null) {
         this.clearFocus();
      } else if (var1.target.isVisible() && var1.target.isEnabled()) {
         this.requestFocus(var1.target);
         this.focusedIndex = var1.index >= 0 ? var1.index : this.focusables.indexOf(var1.target);
      } else {
         this.clearFocus();
      }
   }

   public static final class FocusToken {
      private final String id = UUID.randomUUID().toString();
   }

   private static final class FocusSnapshot {
      private final Focusable target;
      private final int index;

      private FocusSnapshot(Focusable var1, int var2) {
         this.target = var1;
         this.index = var2;
      }
   }
}
