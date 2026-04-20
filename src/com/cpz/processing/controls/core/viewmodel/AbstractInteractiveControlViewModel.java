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
 *
 * @author CPZ
 */
public abstract class AbstractInteractiveControlViewModel extends AbstractControlViewModel implements PointerInputTarget {
   private boolean hovered;
   private boolean pressed;

   /**
    * Creates an interactive ViewModel around the provided model.
    *
    * @param enableable backing model
    */
   protected AbstractInteractiveControlViewModel(Enableable enableable) {
      super(enableable);
   }

   /**
    * Handles pointer move.
    *
    * @param inside parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public final void onPointerMove(boolean inside) {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      } else {
         this.hovered = inside;
      }
   }

   /**
    * Handles pointer press.
    *
    * @param enabled parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public final void onPointerPress(boolean enabled) {
      if (!this.isInteractive()) {
         this.resetInteractionState();
      } else {
         this.hovered = enabled;
         this.pressed = enabled;
      }
   }

   /**
    * Handles pointer release.
    *
    * @param inside parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public final void onPointerRelease(boolean inside) {
      boolean shouldActivate = this.shouldActivateOnRelease(inside);
      this.pressed = false;
      this.hovered = this.isInteractive() && inside;
      if (shouldActivate) {
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
    * @param inside whether the release happened inside the control
    * @return {@code true} when activation should happen
    */
   protected boolean shouldActivateOnRelease(boolean inside) {
      return this.pressed && inside && this.isInteractive();
   }

   /**
    * Executes the control action.
    *
    * Behavior:
    * - Called only after the base class has validated release-to-activate conditions.
    */
   protected abstract void activate();
}
