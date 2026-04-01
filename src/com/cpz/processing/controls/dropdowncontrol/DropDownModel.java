package com.cpz.processing.controls.dropdowncontrol;

import com.cpz.processing.controls.common.Enableable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DropDownModel implements Enableable {

    private List<String> items;
    private int selectedIndex;
    private boolean enabled;

    public DropDownModel(List<String> items) {
        this(items, -1);
    }

    public DropDownModel(List<String> items, int selectedIndex) {
        this.items = sanitizeItems(items);
        this.selectedIndex = normalizeSelectedIndex(selectedIndex, this.items.size());
        this.enabled = true;
    }

    public List<String> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void setItems(List<String> items) {
        this.items = sanitizeItems(items);
        selectedIndex = normalizeSelectedIndex(selectedIndex, this.items.size());
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = normalizeSelectedIndex(selectedIndex, items.size());
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private static List<String> sanitizeItems(List<String> items) {
        if (items == null || items.isEmpty()) {
            return List.of();
        }
        List<String> sanitized = new ArrayList<>(items.size());
        for (String item : items) {
            sanitized.add(item == null ? "" : item);
        }
        return Collections.unmodifiableList(sanitized);
    }

    private static int normalizeSelectedIndex(int selectedIndex, int size) {
        if (size <= 0) {
            return -1;
        }
        if (selectedIndex < 0) {
            return -1;
        }
        return Math.min(selectedIndex, size - 1);
    }
}
