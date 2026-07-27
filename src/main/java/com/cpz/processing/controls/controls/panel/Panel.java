package com.cpz.processing.controls.controls.panel;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.KeyboardRoutableControl;
import com.cpz.processing.controls.controls.ParentContextAwareControl;
import com.cpz.processing.controls.controls.ParentSizeAwareControl;
import com.cpz.processing.controls.controls.PointerRoutableControl;
import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.geometry.ResolvedBounds;
import com.cpz.processing.controls.controls.panel.style.PanelStyle;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.focus.FocusManagerAware;
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
 * <p>Child coordinates are interpreted as local to the panel. Drawing is
 * translated by the panel position and pointer input is converted from sketch
 * coordinates to child-local coordinates before routing.</p>
 *
 * <p>The panel can render optional visual chrome through its live
 * {@link PanelStyle}. Panels are transparent by default to preserve the
 * historical behavior. Style changes do not modify bounds, padding, child
 * coordinates, or input routing.</p>
 *
 * <p>Controls that implement {@link ParentContextAwareControl}, such as
 * {@code DropDown}, receive the resolved parent offset so global overlays can
 * stay anchored to the effective sketch-space position without coupling this
 * container to a concrete child implementation.</p>
 *
 * <p>A panel is not a focus owner. While its input layer is registered, it
 * forwards the owning {@code InputManager}'s focus authority to focus-aware
 * descendants, including descendants of nested panels.</p>
 *
 * <p>The panel does not perform layout, clipping, padding, scroll, titles,
 * shadows, or declarative child loading from JSON.</p>
 *
 * @author CPZ
 */
public final class Panel implements PointerRoutableControl, KeyboardRoutableControl, ParentSizeAwareControl, FocusManagerAware {
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
    private PanelStyle style = new PanelStyle();
    private FocusManager focusManager;
    private int focusManagerAttachments;

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
            this.applyResolvedBounds();
            this.applyParentContextTo(requiredChild);
            this.applyCurrentAvailabilityTo(requiredChild);
            if (this.focusManager != null && requiredChild instanceof FocusManagerAware) {
                ((FocusManagerAware) requiredChild).attachFocusManager(this.focusManager);
            }
        }
        return this;
    }

    public boolean remove(Control child) {
        if (child == null || !this.children.remove(child)) {
            return false;
        }
        if (this.focusManager != null && child instanceof FocusManagerAware) {
            ((FocusManagerAware) child).detachFocusManager(this.focusManager);
        }
        this.clearParentContextFrom(child);
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

    @Override
    public void attachFocusManager(FocusManager focusManager) {
        FocusManager requiredManager = Objects.requireNonNull(focusManager, "focusManager");
        if (this.focusManager == requiredManager) {
            this.focusManagerAttachments++;
            return;
        }
        if (this.focusManager != null) {
            throw new IllegalStateException("Panel is already attached to another focus authority.");
        }

        this.focusManager = requiredManager;
        this.focusManagerAttachments = 1;
        for (Control child : this.children) {
            if (child instanceof FocusManagerAware) {
                ((FocusManagerAware) child).attachFocusManager(requiredManager);
            }
        }
    }

    @Override
    public void detachFocusManager(FocusManager focusManager) {
        if (focusManager == null || this.focusManager != focusManager) {
            return;
        }
        if (--this.focusManagerAttachments > 0) {
            return;
        }

        FocusManager previousManager = this.focusManager;
        this.focusManager = null;
        this.focusManagerAttachments = 0;
        for (Control child : this.children) {
            if (child instanceof FocusManagerAware) {
                ((FocusManagerAware) child).detachFocusManager(previousManager);
            }
        }
    }

    public void draw() {
        if (!this.visible) {
            return;
        }

        this.applyResolvedBounds();
        this.style.render(this.sketch, this.x, this.y, this.width, this.height);

        this.sketch.pushMatrix();
        try {
            this.sketch.translate(this.x, this.y);
            for (Control child : this.children) {
                this.applyParentContextTo(child);
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
        for (int i = this.children.size() - 1; i >= 0; i--) {
            Control child = this.children.get(i);
            this.applyParentContextTo(child);
            if (child instanceof PointerRoutableControl) {
                PointerRoutableControl routable = (PointerRoutableControl) child;
                routable.handlePointerEvent(localEvent);
                if (routable.canConsumePointerEvent(localEvent)) {
                    break;
                }
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
            this.applyParentContextTo(child);
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
            this.applyParentContextTo(child);
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

    /**
     * Returns the live mutable panel style.
     *
     * <p>Mutating the returned instance affects the next {@link #draw()} call.</p>
     *
     * @return the current live style instance
     */
    public PanelStyle getStyle() {
        return this.style;
    }

    /**
     * Replaces the live panel style.
     *
     * <p>Passing {@code null} resets the panel to a new default transparent
     * style.</p>
     *
     * @param style style to use, or {@code null} to reset defaults
     */
    public void setStyle(PanelStyle style) {
        this.style = style == null ? new PanelStyle() : style;
    }

    /**
     * Returns the effective background color, using the current theme fallback
     * when no explicit background color was configured.
     *
     * @return effective background color
     */
    public int getBackgroundColor() {
        return this.style.getBackgroundColor();
    }

    public void setBackgroundColor(int color) {
        this.style.setBackgroundColor(color);
    }

    public boolean isBackgroundVisible() {
        return this.style.isBackgroundVisible();
    }

    public void setBackgroundVisible(boolean visible) {
        this.style.setBackgroundVisible(visible);
    }

    /**
     * Returns the effective stroke color, using the current theme fallback when
     * no explicit stroke color was configured.
     *
     * @return effective stroke color
     */
    public int getStrokeColor() {
        return this.style.getStrokeColor();
    }

    public void setStrokeColor(int color) {
        this.style.setStrokeColor(color);
    }

    public boolean isStrokeVisible() {
        return this.style.isStrokeVisible();
    }

    public void setStrokeVisible(boolean visible) {
        this.style.setStrokeVisible(visible);
    }

    public float getStrokeWeight() {
        return this.style.getStrokeWeight();
    }

    public void setStrokeWeight(float weight) {
        this.style.setStrokeWeight(weight);
    }

    public float getCornerRadius() {
        return this.style.getCornerRadius();
    }

    public void setCornerRadius(float radius) {
        this.style.setCornerRadius(radius);
    }

    public void setPosition(float x, float y) {
        this.bounds = this.bounds.withPosition(ControlMeasure.absolute(x), ControlMeasure.absolute(y));
        this.applyResolvedBounds();
        this.refreshChildContexts();
    }

    public void setSize(float width, float height) {
        this.bounds = this.bounds.withSize(ControlMeasure.absolute(width), ControlMeasure.absolute(height));
        this.applyResolvedBounds();
        this.refreshChildContexts();
    }

    public void setBounds(ControlBounds bounds) {
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        this.applyResolvedBounds();
        this.refreshChildContexts();
    }

    public void setParentSize(float width, float height) {
        this.parentWidth = width;
        this.parentHeight = height;
        this.applyResolvedBounds();
        this.refreshChildContexts();
    }

    public void clearParentSize() {
        this.parentWidth = null;
        this.parentHeight = null;
        this.applyResolvedBounds();
        this.refreshChildContexts();
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

    private void applyParentContextTo(Control child) {
        if (child instanceof ParentSizeAwareControl) {
            ((ParentSizeAwareControl) child).setParentSize(this.width, this.height);
        }
        if (child instanceof ParentContextAwareControl) {
            ((ParentContextAwareControl) child).setParentOffset(this.x, this.y);
        }
    }

    private void clearParentContextFrom(Control child) {
        if (child instanceof ParentContextAwareControl) {
            ParentContextAwareControl contextAwareControl = (ParentContextAwareControl) child;
            contextAwareControl.onRemovedFromParent();
            contextAwareControl.clearParentOffset();
        }
        if (child instanceof ParentSizeAwareControl) {
            ((ParentSizeAwareControl) child).clearParentSize();
        }
    }

    private void refreshChildContexts() {
        for (Control child : this.children) {
            this.applyParentContextTo(child);
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
                Panel.this.applyParentContextTo((Control) this.delegate);
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
