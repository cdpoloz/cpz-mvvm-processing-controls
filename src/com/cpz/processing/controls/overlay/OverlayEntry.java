package com.cpz.processing.controls.overlay;

import com.cpz.processing.controls.input.InputLayer;

public class OverlayEntry {

    private final int zIndex;
    private final Runnable render;
    private final InputLayer inputLayer;
    private final Runnable onClose;

    public OverlayEntry(int zIndex, Runnable render, InputLayer inputLayer) {
        this(zIndex, render, inputLayer, null);
    }

    public OverlayEntry(int zIndex, Runnable render, InputLayer inputLayer, Runnable onClose) {
        this.zIndex = zIndex;
        this.render = render;
        this.inputLayer = inputLayer;
        this.onClose = onClose;
    }

    public int getZIndex() {
        return zIndex;
    }

    public Runnable getRender() {
        return render;
    }

    public InputLayer getInputLayer() {
        return inputLayer;
    }

    public Runnable getOnClose() {
        return onClose;
    }
}
