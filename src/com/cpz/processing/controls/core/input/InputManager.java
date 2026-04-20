package com.cpz.processing.controls.core.input;

import java.util.ArrayList;
import java.util.List;

/**
 * Dispatches normalized input events through ordered input layers.
 *
 * <p>The manager is source-agnostic. Host adapters create {@link PointerEvent}
 * and {@link KeyboardEvent} instances and pass them here; layers decide whether
 * to consume each event and forward it to concrete facade methods, overlays, or
 * sketch-level shortcuts.</p>
 *
 * @author CPZ
 */
public class InputManager {
   private final List<InputLayer> layers = new ArrayList<>();

   /**
    * Registers a layer and keeps dispatch order sorted by descending priority.
    *
    * @param layer layer to register
    */
   public void registerLayer(InputLayer layer) {
      if (layer != null && !this.layers.contains(layer)) {
         this.layers.add(layer);
         this.sortLayers();
      }
   }

   /**
    * Removes a previously registered layer.
    *
    * @param layer layer to remove
    */
   public void unregisterLayer(InputLayer layer) {
      this.layers.remove(layer);
   }

   private void sortLayers() {
      this.layers.sort((left, right) -> Integer.compare(right.getPriority(), left.getPriority()));
   }

   /**
    * Dispatches a pointer event until an active pointer-capturing layer consumes it.
    *
    * @param event normalized pointer event
    */
   public void dispatchPointer(PointerEvent event) {
      for(InputLayer layer : this.layers) {
         if (layer.isActive() && layer.isPointerCaptureEnabled() && layer.handlePointerEvent(event)) {
            break;
         }
      }

   }

   /**
    * Dispatches a keyboard event until an active keyboard-capturing layer consumes it.
    *
    * @param event normalized keyboard event
    */
   public void dispatchKeyboard(KeyboardEvent event) {
      for(InputLayer layer : this.layers) {
         if (layer.isActive() && layer.isKeyboardCaptureEnabled() && layer.handleKeyboardEvent(event)) {
            break;
         }
      }

   }
}
