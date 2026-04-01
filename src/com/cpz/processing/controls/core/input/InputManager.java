package com.cpz.processing.controls.core.input;

import java.util.ArrayList;
import java.util.List;

public class InputManager {
   private final List<InputLayer> layers = new ArrayList<>();

   public void registerLayer(InputLayer var1) {
      if (var1 != null && !this.layers.contains(var1)) {
         this.layers.add(var1);
         this.sortLayers();
      }
   }

   public void unregisterLayer(InputLayer var1) {
      this.layers.remove(var1);
   }

   private void sortLayers() {
      this.layers.sort((var0, var1) -> Integer.compare(var1.getPriority(), var0.getPriority()));
   }

   public void dispatchPointer(PointerEvent var1) {
      for(InputLayer var3 : this.layers) {
         if (var3.isActive() && var3.isPointerCaptureEnabled() && var3.handlePointerEvent(var1)) {
            break;
         }
      }

   }

   public void dispatchKeyboard(KeyboardEvent var1) {
      for(InputLayer var3 : this.layers) {
         if (var3.isActive() && var3.isKeyboardCaptureEnabled() && var3.handleKeyboardEvent(var1)) {
            break;
         }
      }

   }
}
