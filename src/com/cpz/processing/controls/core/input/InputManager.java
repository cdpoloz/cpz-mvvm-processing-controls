package com.cpz.processing.controls.core.input;

import java.util.ArrayList;
import java.util.List;

/**
 * Input component for input manager.
 *
 * Responsibilities:
 * - Translate public input flow into control operations.
 * - Keep raw event handling outside business state.
 *
 * Behavior:
 * - Keeps the public role isolated from unrelated concerns.
 *
 * Notes:
 * - This type is part of the public project surface.
 */
public class InputManager {
   private final List<InputLayer> layers = new ArrayList<>();

   /**
    * Registers layer.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void registerLayer(InputLayer var1) {
      if (var1 != null && !this.layers.contains(var1)) {
         this.layers.add(var1);
         this.sortLayers();
      }
   }

   /**
    * Unregisters layer.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void unregisterLayer(InputLayer var1) {
      this.layers.remove(var1);
   }

   private void sortLayers() {
      this.layers.sort((var0, var1) -> Integer.compare(var1.getPriority(), var0.getPriority()));
   }

   /**
    * Performs dispatch pointer.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void dispatchPointer(PointerEvent var1) {
      for(InputLayer var3 : this.layers) {
         if (var3.isActive() && var3.isPointerCaptureEnabled() && var3.handlePointerEvent(var1)) {
            break;
         }
      }

   }

   /**
    * Performs dispatch keyboard.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public void dispatchKeyboard(KeyboardEvent var1) {
      for(InputLayer var3 : this.layers) {
         if (var3.isActive() && var3.isKeyboardCaptureEnabled() && var3.handleKeyboardEvent(var1)) {
            break;
         }
      }

   }
}
