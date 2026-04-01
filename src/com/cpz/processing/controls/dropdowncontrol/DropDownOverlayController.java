package com.cpz.processing.controls.dropdowncontrol;

import com.cpz.processing.controls.common.focus.FocusManager;
import com.cpz.processing.controls.input.DefaultInputLayer;
import com.cpz.processing.controls.input.InputLayer;
import com.cpz.processing.controls.input.InputManager;
import com.cpz.processing.controls.input.KeyboardEvent;
import com.cpz.processing.controls.input.PointerEvent;
import com.cpz.processing.controls.overlay.OverlayEntry;
import com.cpz.processing.controls.overlay.OverlayManager;

public final class DropDownOverlayController {

    public interface TransferHandler {
        boolean handleTransfer(DropDownOverlayController source, PointerEvent event);
    }

    private final DropDownView view;
    private final DropDownViewModel viewModel;
    private final FocusManager focusManager;
    private final OverlayManager overlayManager;
    private final InputManager inputManager;
    private final int zIndex;
    private final InputLayer inputLayer;
    private final OverlayEntry overlayEntry;

    private TransferHandler transferHandler;
    private boolean registered;

    public DropDownOverlayController(DropDownView view,
                                     DropDownViewModel viewModel,
                                     FocusManager focusManager,
                                     OverlayManager overlayManager,
                                     InputManager inputManager,
                                     int zIndex) {
        this.view = view;
        this.viewModel = viewModel;
        this.focusManager = focusManager;
        this.overlayManager = overlayManager;
        this.inputManager = inputManager;
        this.zIndex = zIndex;
        this.inputLayer = new OverlayInputLayer(zIndex);
        this.overlayEntry = new OverlayEntry(zIndex, view::draw, inputLayer, this::closeOverlay, viewModel);
    }

    public void setTransferHandler(TransferHandler transferHandler) {
        this.transferHandler = transferHandler;
    }

    public void syncRegistration() {
        if (viewModel.isExpanded()) {
            register();
        } else {
            unregister();
        }
    }

    public void dispose() {
        unregister();
    }

    public DropDownView getView() {
        return view;
    }

    public DropDownViewModel getViewModel() {
        return viewModel;
    }

    public int getZIndex() {
        return zIndex;
    }

    public void closeOverlay() {
        viewModel.close();
        view.handleMouseMove(-1f, -1f);
    }

    private void register() {
        if (registered) {
            return;
        }
        overlayManager.register(overlayEntry);
        inputManager.registerLayer(inputLayer);
        registered = true;
    }

    private void unregister() {
        if (!registered) {
            return;
        }
        overlayManager.unregister(overlayEntry);
        inputManager.unregisterLayer(inputLayer);
        registered = false;
    }

    private final class OverlayInputLayer extends DefaultInputLayer {

        private OverlayInputLayer(int priority) {
            super(priority);
        }

        @Override
        public boolean isPointerCaptureEnabled() {
            return viewModel.isExpanded();
        }

        @Override
        public boolean isKeyboardCaptureEnabled() {
            return false;
        }

        @Override
        public boolean handlePointerEvent(PointerEvent event) {
            boolean wasExpanded = viewModel.isExpanded();
            if (!wasExpanded) {
                return false;
            }

            switch (event.getType()) {
                case MOVE:
                    view.handleMouseMove(event.getX(), event.getY());
                    return true;

                case PRESS:
                    if (transferHandler != null && transferHandler.handleTransfer(DropDownOverlayController.this, event)) {
                        return true;
                    }
                    view.handleMousePress(event.getX(), event.getY(), focusManager);
                    return true;

                case RELEASE:
                    view.handleMouseRelease(event.getX(), event.getY());
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public boolean handleKeyboardEvent(KeyboardEvent event) {
            return false;
        }
    }
}
