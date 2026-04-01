package com.cpz.processing.controls.input;

import java.util.ArrayList;
import java.util.List;

public class InputManager {

    private final List<InputLayer> layers = new ArrayList<>();

    public void registerLayer(InputLayer layer) {
        if (layer == null || layers.contains(layer)) {
            return;
        }
        layers.add(layer);
        sortLayers();
    }

    public void unregisterLayer(InputLayer layer) {
        layers.remove(layer);
    }

    private void sortLayers() {
        layers.sort((a, b) -> Integer.compare(b.getPriority(), a.getPriority()));
    }

    public void dispatchPointer(PointerEvent event) {
        for (InputLayer layer : layers) {
            if (!layer.isActive()) {
                continue;
            }
            if (!layer.isPointerCaptureEnabled()) {
                continue;
            }
            if (layer.handlePointerEvent(event)) {
                break;
            }
        }
    }

    public void dispatchKeyboard(KeyboardEvent event) {
        for (InputLayer layer : layers) {
            if (!layer.isActive()) {
                continue;
            }
            if (!layer.isKeyboardCaptureEnabled()) {
                continue;
            }
            if (layer.handleKeyboardEvent(event)) {
                break;
            }
        }
    }
}
