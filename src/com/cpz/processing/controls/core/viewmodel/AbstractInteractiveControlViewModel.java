package com.cpz.processing.controls.core.viewmodel;

import com.cpz.processing.controls.core.input.PointerInputTarget;
import com.cpz.processing.controls.core.model.Enableable;

/**
 * Base ViewModel for controls with pointer-driven interaction state.
 *
 * Responsibilities:
 * - Track hover and pressed flags.
 * - Provide common release-to-activate behavior.
 *
 * Behavior:
 * - Pointer geometry stays outside this class.
 * - Subclasses implement the activation action only.
 */
public abstract class AbstractInteractiveControlViewModel extends AbstractControlViewModel implements PointerInputTarget {
   private boolean hovered;
   private boolean pressed;

   /**
    * Creates an interactive ViewModel around the provided model.
    *
    * @param var1 backing model
    */
   protected AbstractInteractiveControlViewModel(Enableable var1) {
      super(var1);
   }

   /**
    * Handles pointer move.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public final void onPointerMove(boolean var1) {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      } else {
         this.hovered = var1;
      }
   }

   /**
    * Handles pointer press.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public final void onPointerPress(boolean var1) {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      } else {
         this.hovered = var1;
         this.pressed = var1;
      }
   }

   /**
    * Handles pointer release.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public final void onPointerRelease(boolean var1) {
      boolean var2 = this.shouldActivateOnRelease(var1);
      this.pressed = false;
      this.hovered = this.isInteractive() && var1;
      if (var2) {
         this.activate();
      }

   }

   /**
    * Returns whether hovered.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public final boolean isHovered() {
      return this.hovered;
   }

   /**
    * Returns whether pressed.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public final boolean isPressed() {
      return this.pressed;
   }

   /**
    * Returns whether pointer interaction is currently allowed.
    *
    * @return {@code true} when the control is both visible and enabled
    */
   protected final boolean isInteractive() {
      return this.isEnabled() && this.isVisible();
   }

   /**
    * Clears transient pointer state.
    */
   protected void resetInteractionState() {
      this.hovered = false;
      this.pressed = false;
   }

   /**
    * Clears transient interaction state when the control becomes unavailable.
    */
   protected final void onAvailabilityChanged() {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      }

   }

   /**
    * Returns whether a release should activate the control.
    *
    * @param var1 whether the release happened inside the control
    * @return {@code true} when activation should happen
    */
   protected boolean shouldActivateOnRelease(boolean var1) {
      return this.pressed && var1 && this.isInteractive();
   }

   /**
    * Executes the control action.
    *
    * Behavior:
    * - Called only after the base class has validated release-to-activate conditions.
    */
   protected abstract void activate();
}
