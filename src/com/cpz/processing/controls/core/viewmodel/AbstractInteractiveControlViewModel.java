package com.cpz.processing.controls.core.viewmodel;

import com.cpz.processing.controls.core.input.PointerInputTarget;
import com.cpz.processing.controls.core.model.Enableable;

public abstract class AbstractInteractiveControlViewModel extends AbstractControlViewModel implements PointerInputTarget {
   private boolean hovered;
   private boolean pressed;

   protected AbstractInteractiveControlViewModel(Enableable var1) {
      super(var1);
   }

   public final void onPointerMove(boolean var1) {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      } else {
         this.hovered = var1;
      }
   }

   public final void onPointerPress(boolean var1) {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      } else {
         this.hovered = var1;
         this.pressed = var1;
      }
   }

   public final void onPointerRelease(boolean var1) {
      boolean var2 = this.shouldActivateOnRelease(var1);
      this.pressed = false;
      this.hovered = this.isInteractive() && var1;
      if (var2) {
         this.activate();
      }

   }

   public final boolean isHovered() {
      return this.hovered;
   }

   public final boolean isPressed() {
      return this.pressed;
   }

   protected final boolean isInteractive() {
      return this.isEnabled() && this.isVisible();
   }

   protected void resetInteractionState() {
      this.hovered = false;
      this.pressed = false;
   }

   protected final void onAvailabilityChanged() {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      }

   }

   protected boolean shouldActivateOnRelease(boolean var1) {
      return this.pressed && var1 && this.isInteractive();
   }

   protected abstract void activate();
}
