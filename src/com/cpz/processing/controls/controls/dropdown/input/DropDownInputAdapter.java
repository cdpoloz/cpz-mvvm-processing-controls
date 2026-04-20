package com.cpz.processing.controls.controls.dropdown.input;

import com.cpz.processing.controls.controls.dropdown.util.DropDownOverlayController;
import com.cpz.processing.controls.controls.dropdown.view.DropDownView;
import com.cpz.processing.controls.controls.dropdown.viewmodel.DropDownViewModel;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.input.PointerEvent;

/**
 * Input component for drop down input adapter.
 *
 * @author CPZ
 */
public final class DropDownInputAdapter {
    private final DropDownView view;
    private final DropDownViewModel viewModel;
    private final FocusManager focusManager;
    private final DropDownOverlayController overlayController;

    public DropDownInputAdapter(DropDownView view, DropDownViewModel viewModel, FocusManager focusManager, DropDownOverlayController overlayController) {
        this.view = view;
        this.viewModel = viewModel;
        this.focusManager = focusManager;
        this.overlayController = overlayController;
        this.focusManager.register(viewModel);
    }

    public boolean handleMouseMove(float x, float y) {
        if (this.viewModel.isExpanded()) {
            return false;
        }
        return this.view.handleMouseMove(x, y);
    }

    public boolean handleMousePress(float x, float y) {
        if (this.viewModel.isExpanded()) {
            return false;
        }
        boolean handled = this.view.handleMousePress(x, y, this.focusManager);
        this.overlayController.syncRegistration();
        if (!handled && this.focusManager.isFocused(this.viewModel)) {
            this.focusManager.clearFocus();
        }
        return handled;
    }

    public boolean handleMouseRelease(float x, float y) {
        if (this.viewModel.isExpanded()) {
            return false;
        }
        this.view.handleMouseRelease(x, y);
        return this.view.contains(x, y);
    }

    public void handlePointerEvent(PointerEvent event) {
        if (event != null) {
            switch (event.getType()) {
                case MOVE:
                case DRAG:
                    this.handleMouseMove(event.getX(), event.getY());
                    break;
                case PRESS:
                    this.handleMousePress(event.getX(), event.getY());
                    break;
                case RELEASE:
                    this.handleMouseRelease(event.getX(), event.getY());
                    break;
                case WHEEL:
                    break;
            }
        }
    }
}
