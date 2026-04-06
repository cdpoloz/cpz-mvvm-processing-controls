package com.cpz.processing.controls.common.binding;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Explicit binding helper for ViewModel composition.
 *
 * Responsibilities:
 * - Support explicit ViewModel synchronization.
 * - Avoid coupling to control-specific abstractions.
 *
 * Behavior:
 * - Keeps the public role isolated from unrelated concerns.
 *
 * Notes:
 * - The binding API remains explicit and control-agnostic.
 */
public final class Binding {
   private Binding() {
   }

   /**
    * Performs bind.
    *
    * @param var0 parameter used by this operation
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static <T> void bind(Supplier<T> var0, Consumer<T> var1, Consumer<ValueListener<T>> var2) {
      var1.accept(var0.get());
      var2.accept(var1::accept);
   }
}
