package com.cpz.processing.controls.common.binding;

/**
 * Listener contract for explicit value propagation.
 *
 * Responsibilities:
 * - Support explicit ViewModel synchronization.
 * - Avoid coupling to control-specific abstractions.
 *
 * Behavior:
 * - Declares the contract without prescribing implementation details.
 *
 * Notes:
 * - The binding API remains explicit and control-agnostic.
 */
@FunctionalInterface
public interface ValueListener<T> {
   /**
    * Handles a source value change.
    *
    * @param var1 new source value
    */
   void onChange(T var1);
}
