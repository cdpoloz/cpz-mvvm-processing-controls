package com.cpz.processing.controls.common.binding;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Binding {
   private Binding() {
   }

   public static <T> void bind(Supplier<T> var0, Consumer<T> var1, Consumer<ValueListener<T>> var2) {
      var1.accept(var0.get());
      var2.accept(var1::accept);
   }
}
