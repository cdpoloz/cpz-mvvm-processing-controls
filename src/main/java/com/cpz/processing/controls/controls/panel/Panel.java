package com.cpz.processing.controls.controls.panel;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.KeyboardRoutableControl;
import com.cpz.processing.controls.controls.ParentSizeAwareControl;
import com.cpz.processing.controls.controls.PointerRoutableControl;
import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.geometry.ResolvedBounds;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.tooltip.Tooltip;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipAttachable;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipTarget;
import com.cpz.processing.controls.core.util.ControlCode;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Public container facade that groups controls in a local coordinate space.
 *
 * <p>The panel is intentionally minimal: it groups children, translates drawing
 * and pointer input, and exposes visibility, enabled state, and positioning as
 * a single {@link Control}. It does not perform layout, clipping, padding,
 * background rendering, or JSON loading.</p>
 *
 * @author CPZ
 */
public final class Panel implements PointerRoutableControl, KeyboardRoutableControl, ParentSizeAwareControl {
    private final PApplet sketch;
    private final String code;
    private final List<Control> children = new ArrayList<>();
    private final Map<Control, Boolean> childVisibleBeforePanelHide = new IdentityHashMap<>();
    private final Map<Control, Boolean> childEnabledBeforePanelDisable = new IdentityHashMap<>();
    private ControlBounds bounds;
    private float x;
    private float y;
    private float width;
    private float height;
    private Float parentWidth;
    private Float parentHeight;
    private boolean visible = true;
    private boolean enabled = true;
    private boolean pointerPressedInside;

    public Panel(PApplet sketch, float x, float y, float width, float height) {
        this(sketch, ControlCode.auto("panel"), x, y, width, height);
    }

    public Panel(PApplet sketch, String code, float x, float y, float width, float height) {
        this(sketch, code, ControlBounds.absolute(x, y, width, height));
    }

    public Panel(PApplet sketch, ControlBounds bounds) {
        this(sketch, ControlCode.auto("panel"), bounds);
    }

    public Panel(PApplet sketch, String code, ControlBounds bounds) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
        this.code = Objects.requireNonNull(code, "code");
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        this.applyResolvedBounds();
    }

    public Panel add(Control child) {
        Control requiredChild = Objects.requireNonNull(child, "child");
        if (!this.children.contains(requiredChild)) {
            this.children.add(requiredChild);
            this.applyParentSizeTo(requiredChild);
            this.applyCurrentAvailabilityTo(requiredChild);
        }
        return this;
    }

    public boolean remove(Control child) {
        if (child == null || !this.children.remove(child)) {
            return false;
        }
        this.clearParentSizeFrom(child);
        this.restoreChildAvailability(child);
        return true;
    }

    public Control remove(String code) {
        for (Control child : new ArrayList<>(this.children)) {
            if (Objects.equals(child.getCode(), code)) {
                this.remove(child);
                return child;
            }
        }
        return null;
    }

    public void clear() {
        for (Control child : new ArrayList<>(this.children)) {
            this.remove(child);
        }
    }

    public List<Control> children() {
        return Collections.unmodifiableList(this.children);
    }

    public void draw() {
        if (!this.visible) {
            return;
        }

        this.applyResolvedBounds();
        this.sketch.pushMatrix();
        try {
            this.sketch.translate(this.x, this.y);
            for (Control child : this.children) {
                this.applyParentSizeTo(child);
                if (child.isVisible()) {
                    child.draw();
                }
            }
        } finally {
            this.sketch.popMatrix();
        }
    }

    public void handlePointerEvent(PointerEvent event) {
        if (event == null || !this.visible || !this.enabled || event.getType() == PointerEvent.Type.WHEEL) {
            return;
        }
        this.applyResolvedBounds();
        boolean inside = this.contains(event.getX(), event.getY());
        switch (event.getType()) {
            case PRESS:
                this.pointerPressedInside = inside;
                if (!inside) {
                    return;
                }
                break;
            case DRAG:
            case RELEASE:
                if (!inside && !this.pointerPressedInside) {
                    return;
                }
                break;
            case MOVE:
                break;
            default:
                return;
        }

        PointerEvent localEvent = this.toLocalEvent(event);
        for (Control child : this.children) {
            this.applyParentSizeTo(child);
            if (child instanceof PointerRoutableControl) {
                ((PointerRoutableControl) child).handlePointerEvent(localEvent);
            }
        }
        if (event.getType() == PointerEvent.Type.RELEASE) {
            this.pointerPressedInside = false;
        }
    }

    public boolean canConsumePointerEvent(PointerEvent event) {
        this.applyResolvedBounds();
        return event != null
                && event.getType() != PointerEvent.Type.WHEEL
                && this.visible
                && this.contains(event.getX(), event.getY());
    }

    public void handleKeyboardEvent(KeyboardEvent event) {
        if (event == null || !this.visible || !this.enabled) {
            return;
        }

        this.applyResolvedBounds();
        for (int i = this.children.size() - 1; i >= 0; i--) {
            Control child = this.children.get(i);
            this.applyParentSizeTo(child);
            if (child instanceof KeyboardRoutableControl) {
                KeyboardRoutableControl routable = (KeyboardRoutableControl) child;
                if (routable.canConsumeKeyboardEvent(event)) {
                    routable.handleKeyboardEvent(event);
                    return;
                }
            }
        }
    }

    public boolean canConsumeKeyboardEvent(KeyboardEvent event) {
        if (event == null || !this.visible || !this.enabled) {
            return false;
        }

        this.applyResolvedBounds();
        for (Control child : this.children) {
            this.applyParentSizeTo(child);
            if (child instanceof KeyboardRoutableControl
                    && ((KeyboardRoutableControl) child).canConsumeKeyboardEvent(event)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(float pointX, float pointY) {
        this.applyResolvedBounds();
        return pointX >= this.x
                && pointX <= this.x + this.width
                && pointY >= this.y
                && pointY <= this.y + this.height;
    }

    public TooltipTarget tooltipTarget(TooltipAttachable child) {
        Objects.requireNonNull(child, "child");
        if (!(child instanceof Control) || !this.children.contains((Control) child)) {
            throw new IllegalArgumentException("Tooltip target must be a child control of this panel.");
        }
        return new OffsetTooltipTarget(child);
    }

    public String getCode() {
        return this.code;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        this.enabled = enabled;
        if (enabled) {
            this.restoreChildEnabledStates();
        } else {
            this.pointerPressedInside = false;
            this.suppressChildEnabledStates();
        }
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        if (this.visible == visible) {
            return;
        }
        this.visible = visible;
        if (visible) {
            this.restoreChildVisibleStates();
        } else {
            this.pointerPressedInside = false;
            this.suppressChildVisibleStates();
        }
    }

    public void setPosition(float x, float y) {
        this.bounds = this.bounds.withPosition(ControlMeasure.absolute(x), ControlMeasure.absolute(y));
        this.applyResolvedBounds();
    }

    public void setSize(float width, float height) {
        this.bounds = this.bounds.withSize(ControlMeasure.absolute(width), ControlMeasure.absolute(height));
        this.applyResolvedBounds();
    }

    public void setBounds(ControlBounds bounds) {
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        this.applyResolvedBounds();
    }

    public void setParentSize(float width, float height) {
        this.parentWidth = width;
        this.parentHeight = height;
        this.applyResolvedBounds();
    }

    public void clearParentSize() {
        this.parentWidth = null;
        this.parentHeight = null;
        this.applyResolvedBounds();
    }

    public float getX() {
        this.applyResolvedBounds();
        return this.x;
    }

    public float getY() {
        this.applyResolvedBounds();
        return this.y;
    }

    public float getWidth() {
        this.applyResolvedBounds();
        return this.width;
    }

    public float getHeight() {
        this.applyResolvedBounds();
        return this.height;
    }

    private PointerEvent toLocalEvent(PointerEvent event) {
        return new PointerEvent(
                event.getType(),
                event.getX() - this.x,
                event.getY() - this.y,
                event.isPressed(),
                event.getButton(),
                event.getWheelDelta(),
                event.isShiftDown(),
                event.isControlDown(),
                event.isAltDown()
        );
    }

    private void applyResolvedBounds() {
        ResolvedBounds resolvedBounds = this.bounds.resolve(this.parentWidth(), this.parentHeight());
        this.x = resolvedBounds.x();
        this.y = resolvedBounds.y();
        this.width = Math.max(0.0F, resolvedBounds.width());
        this.height = Math.max(0.0F, resolvedBounds.height());
    }

    private float parentWidth() {
        return this.parentWidth != null ? this.parentWidth : this.sketch.width;
    }

    private float parentHeight() {
        return this.parentHeight != null ? this.parentHeight : this.sketch.height;
    }

    private void applyParentSizeTo(Control child) {
        if (child instanceof ParentSizeAwareControl) {
            ((ParentSizeAwareControl) child).setParentSize(this.width, this.height);
        }
    }

    private void clearParentSizeFrom(Control child) {
        if (child instanceof ParentSizeAwareControl) {
            ((ParentSizeAwareControl) child).clearParentSize();
        }
    }

    private void applyCurrentAvailabilityTo(Control child) {
        if (!this.visible) {
            this.childVisibleBeforePanelHide.putIfAbsent(child, child.isVisible());
            child.setVisible(false);
        }
        if (!this.enabled) {
            this.childEnabledBeforePanelDisable.putIfAbsent(child, child.isEnabled());
            child.setEnabled(false);
        }
    }

    private void restoreChildAvailability(Control child) {
        Boolean previousVisible = this.childVisibleBeforePanelHide.remove(child);
        if (previousVisible != null) {
            child.setVisible(previousVisible);
        }
        Boolean previousEnabled = this.childEnabledBeforePanelDisable.remove(child);
        if (previousEnabled != null) {
            child.setEnabled(previousEnabled);
        }
    }

    private void suppressChildVisibleStates() {
        for (Control child : this.children) {
            this.childVisibleBeforePanelHide.putIfAbsent(child, child.isVisible());
            child.setVisible(false);
        }
    }

    private void restoreChildVisibleStates() {
        for (Control child : new ArrayList<>(this.childVisibleBeforePanelHide.keySet())) {
            if (this.children.contains(child)) {
                child.setVisible(this.childVisibleBeforePanelHide.get(child));
            }
            this.childVisibleBeforePanelHide.remove(child);
        }
    }

    private void suppressChildEnabledStates() {
        for (Control child : this.children) {
            this.childEnabledBeforePanelDisable.putIfAbsent(child, child.isEnabled());
            child.setEnabled(false);
        }
    }

    private void restoreChildEnabledStates() {
        for (Control child : new ArrayList<>(this.childEnabledBeforePanelDisable.keySet())) {
            if (this.children.contains(child)) {
                child.setEnabled(this.childEnabledBeforePanelDisable.get(child));
            }
            this.childEnabledBeforePanelDisable.remove(child);
        }
    }

    private final class OffsetTooltipTarget implements TooltipTarget {
        private final TooltipAttachable delegate;

        private OffsetTooltipTarget(TooltipAttachable delegate) {
            this.delegate = delegate;
        }

        public TooltipBounds getTooltipBounds() {
            Panel.this.applyResolvedBounds();
            if (this.delegate instanceof Control) {
                Panel.this.applyParentSizeTo((Control) this.delegate);
            }
            TooltipBounds bounds = this.delegate.getTooltipBounds();
            if (bounds == null) {
                return new TooltipBounds(Panel.this.x, Panel.this.y, 0.0F, 0.0F);
            }
            return new TooltipBounds(
                    Panel.this.x + bounds.x(),
                    Panel.this.y + bounds.y(),
                    bounds.width(),
                    bounds.height()
            );
        }

        public Tooltip getTooltip() {
            return this.delegate.getTooltip();
        }

        public boolean isTooltipTargetVisible() {
            return Panel.this.visible && this.delegate.isTooltipTargetVisible();
        }

        public boolean isTooltipTargetEnabled() {
            return this.delegate.isTooltipTargetEnabled();
        }
    }
}
