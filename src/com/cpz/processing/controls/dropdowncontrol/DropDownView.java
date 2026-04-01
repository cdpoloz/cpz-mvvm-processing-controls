package com.cpz.processing.controls.dropdowncontrol;

import com.cpz.processing.controls.common.ControlView;
import com.cpz.processing.controls.common.focus.FocusManager;
import com.cpz.processing.controls.common.input.PointerInteractable;
import com.cpz.processing.controls.layout.LayoutConfig;
import com.cpz.processing.controls.layout.LayoutResolver;
import processing.core.PApplet;

import java.util.List;

public final class DropDownView implements ControlView, PointerInteractable {

    private final PApplet sketch;
    private final DropDownViewModel viewModel;
    private float x;
    private float y;
    private float width;
    private float height;
    private DefaultDropDownStyle style;
    private int hoveredIndex = -1;
    private LayoutConfig layoutConfig;

    public DropDownView(PApplet sketch,
                        DropDownViewModel viewModel,
                        float x,
                        float y,
                        float width,
                        float height) {
        this.sketch = sketch;
        this.viewModel = viewModel;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.style = new DefaultDropDownStyle(new DropDownStyleConfig());
    }

    @Override
    public void draw() {
        if (!viewModel.isVisible()) {
            return;
        }
        applyLayoutIfNeeded();
        style.render(sketch, buildViewState());
    }

    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setLayoutConfig(LayoutConfig layoutConfig) {
        this.layoutConfig = layoutConfig;
        applyLayoutIfNeeded();
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void setStyle(DefaultDropDownStyle style) {
        if (style != null) {
            this.style = style;
        }
    }

    @Override
    public boolean contains(float px, float py) {
        applyLayoutIfNeeded();
        return containsBase(px, py) || containsExpandedList(px, py);
    }

    public boolean handleMouseMove(float mx, float my) {
        applyLayoutIfNeeded();
        if (!isInteractive()) {
            hoveredIndex = -1;
            viewModel.setHovered(false);
            viewModel.setPressed(false);
            return false;
        }

        hoveredIndex = resolveHoveredIndex(mx, my);
        viewModel.setHovered(contains(mx, my));
        return contains(mx, my);
    }

    public boolean handleMousePress(float mx, float my, FocusManager focusManager) {
        applyLayoutIfNeeded();
        if (!isInteractive()) {
            hoveredIndex = -1;
            viewModel.close();
            viewModel.setHovered(false);
            viewModel.setPressed(false);
            return false;
        }

        if (containsBase(mx, my)) {
            if (focusManager != null) {
                focusManager.requestFocus(viewModel);
            }
            viewModel.setPressed(true);
            viewModel.toggleExpanded();
            hoveredIndex = resolveHoveredIndex(mx, my);
            viewModel.setHovered(true);
            return true;
        }

        int itemIndex = resolveHoveredIndex(mx, my);
        if (itemIndex >= 0) {
            if (focusManager != null) {
                focusManager.requestFocus(viewModel);
            }
            viewModel.selectIndex(itemIndex);
            viewModel.close();
            viewModel.setPressed(false);
            viewModel.setHovered(true);
            hoveredIndex = -1;
            return true;
        }

        hoveredIndex = -1;
        viewModel.setPressed(false);
        viewModel.setHovered(false);
        viewModel.close();
        return false;
    }

    public void handleMouseRelease(float mx, float my) {
        applyLayoutIfNeeded();
        viewModel.setPressed(false);
        viewModel.setHovered(contains(mx, my));
    }

    public boolean isExpanded() {
        return viewModel.isExpanded();
    }

    public int getHoveredIndex() {
        return hoveredIndex;
    }

    private DropDownViewState buildViewState() {
        return new DropDownViewState(
                x,
                y,
                width,
                height,
                viewModel.isExpanded(),
                viewModel.getSelectedText(),
                viewModel.getItems(),
                viewModel.getSelectedIndex(),
                hoveredIndex,
                viewModel.isHovered(),
                viewModel.isPressed(),
                viewModel.isFocused(),
                viewModel.isEnabled(),
                style.getItemHeight(),
                style.getMaxVisibleItems(),
                style.getTextPadding(),
                style.getArrowPadding(),
                style.getCornerRadius(),
                style.getListCornerRadius(),
                viewModel.isHovered() || viewModel.isFocused() ? style.getFocusedStrokeWeight() : style.getStrokeWeight()
        );
    }

    private boolean containsBase(float px, float py) {
        float halfWidth = width * 0.5f;
        float halfHeight = height * 0.5f;
        return px >= x - halfWidth
                && px <= x + halfWidth
                && py >= y - halfHeight
                && py <= y + halfHeight;
    }

    private boolean containsExpandedList(float px, float py) {
        if (!viewModel.isExpanded()) {
            return false;
        }
        List<String> items = viewModel.getItems();
        int visibleItems = Math.min(items.size(), style.getMaxVisibleItems());
        if (visibleItems <= 0) {
            return false;
        }
        float left = x - (width * 0.5f);
        float top = y + (height * 0.5f);
        float listHeight = visibleItems * style.getItemHeight();
        return px >= left
                && px <= left + width
                && py >= top
                && py <= top + listHeight;
    }

    private int resolveHoveredIndex(float mx, float my) {
        if (!viewModel.isExpanded()) {
            return -1;
        }
        List<String> items = viewModel.getItems();
        int visibleItems = Math.min(items.size(), style.getMaxVisibleItems());
        if (visibleItems <= 0) {
            return -1;
        }
        float left = x - (width * 0.5f);
        float top = y + (height * 0.5f);
        float itemHeight = style.getItemHeight();
        if (mx < left || mx > left + width || my < top || my > top + (visibleItems * itemHeight)) {
            return -1;
        }
        int index = (int) ((my - top) / itemHeight);
        return index >= 0 && index < visibleItems ? index : -1;
    }

    private boolean isInteractive() {
        return viewModel.isVisible() && viewModel.isEnabled();
    }

    private void applyLayoutIfNeeded() {
        if (layoutConfig == null) {
            return;
        }
        float resolvedLeft = LayoutResolver.resolveX(layoutConfig, width, sketch.width);
        float resolvedTop = LayoutResolver.resolveY(layoutConfig, height, sketch.height);
        x = resolvedLeft + (width * 0.5f);
        y = resolvedTop + (height * 0.5f);
    }
}
