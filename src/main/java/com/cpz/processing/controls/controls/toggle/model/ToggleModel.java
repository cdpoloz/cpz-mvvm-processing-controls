package com.cpz.processing.controls.controls.toggle.model;

import com.cpz.processing.controls.core.model.Enableable;
import com.cpz.processing.controls.core.util.ControlCode;

import java.util.Objects;

/**
 * Model for toggle model.
 *
 * Responsibilities:
 * - Store durable control state.
 * - Remain independent from rendering and input code.
 *
 * Behavior:
 * - Keeps state mutation independent from rendering concerns.
 *
 * Notes:
 * - This type belongs to the MVVM Model layer.
 *
 * @author CPZ
 */
public final class ToggleModel implements Enableable {
   private final String code;
   private int state;
   private int prevState;
   private int totalStates = 2;
   private boolean enabled = true;

   public ToggleModel() {
      this(ControlCode.auto("toggle"));
   }

   public ToggleModel(String text) {
      this.code = Objects.requireNonNull(text, "code");
   }

   public String getCode() {
      return this.code;
   }

   /**
    * Updates total states.
    *
    * @param value new total states
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setTotalStates(int value) {
      this.totalStates = value;
   }

   /**
    * Returns state.
    *
    * @return current state
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getState() {
      return this.state;
   }

   /**
    * Updates state.
    *
    * @param value new state
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setState(int value) {
      this.state = value;
   }

   /**
    * Returns prev state.
    *
    * @return current prev state
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getPrevState() {
      return this.prevState;
   }

   /**
    * Updates prev state.
    *
    * @param value new prev state
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setPrevState(int value) {
      this.prevState = value;
   }

   /**
    * Returns total states.
    *
    * @return current total states
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getTotalStates() {
      return this.totalStates;
   }

   /**
    * Returns whether enabled.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isEnabled() {
      return this.enabled;
   }

   /**
    * Updates enabled.
    *
    * @param enabled new enabled
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }
}
