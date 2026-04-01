package com.cpz.processing.controls.dropdowncontrol;

import com.cpz.processing.controls.common.focus.Focusable;
import com.cpz.processing.controls.common.viewmodel.AbstractControlViewModel;

import java.util.List;

public final class DropDownViewModel extends AbstractControlViewModel<DropDownModel> implements Focusable {

    private boolean hovered;
    private boolean pressed;
    private boolean focused;
    private boolean expanded;

    public DropDownViewModel(DropDownModel model) {
        super(model);
    }

    public List<String> getItems() {
        return model.getItems();
    }

    public void setItems(List<String> items) {
        model.setItems(items);
    }

    public int getSelectedIndex() {
        return model.getSelectedIndex();
    }

    public String getSelectedText() {
        int selectedIndex = model.getSelectedIndex();
        List<String> items = model.getItems();
        if (selectedIndex < 0 || selectedIndex >= items.size()) {
            return "";
        }
        return items.get(selectedIndex);
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered && isInteractive();
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed && isInteractive();
    }

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused && isInteractive();
        if (!this.focused) {
            close();
        }
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void toggleExpanded() {
        if (!isInteractive() || model.getItems().isEmpty()) {
            expanded = false;
            return;
        }
        expanded = !expanded;
    }

    public void close() {
        expanded = false;
        pressed = false;
    }

    public void selectIndex(int index) {
        if (!isInteractive()) {
            return;
        }
        model.setSelectedIndex(index);
    }

    @Override
    public void onFocusGained() {
        focused = isInteractive();
    }

    @Override
    public void onFocusLost() {
        focused = false;
        close();
    }

    @Override
    protected void onAvailabilityChanged() {
        if (!isInteractive()) {
            hovered = false;
            pressed = false;
            focused = false;
            expanded = false;
        }
    }

    private boolean isInteractive() {
        return isVisible() && isEnabled();
    }
}
