package com.cpz.processing.controls.controls.dropdown;

import com.cpz.processing.controls.common.binding.ValueListener;
import com.cpz.processing.controls.controls.ParentContextAwareControl;
import com.cpz.processing.controls.controls.ParentSizeAwareControl;
import com.cpz.processing.controls.controls.PointerRoutableControl;
import com.cpz.processing.controls.controls.dropdown.config.DropDownStyleConfig;
import com.cpz.processing.controls.controls.dropdown.input.DropDownInputAdapter;
import com.cpz.processing.controls.controls.dropdown.model.DropDownModel;
import com.cpz.processing.controls.controls.dropdown.style.DefaultDropDownStyle;
import com.cpz.processing.controls.controls.dropdown.util.DropDownOverlayController;
import com.cpz.processing.controls.controls.dropdown.view.DropDownView;
import com.cpz.processing.controls.controls.dropdown.viewmodel.DropDownViewModel;
import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.geometry.ResolvedBounds;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.tooltip.Tooltip;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipAttachable;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipSupport;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.util.ControlCode;
import com.cpz.processing.controls.core.util.FontLoader;
import processing.core.PApplet;
import processing.core.PFont;

import java.util.List;
import java.util.Objects;

/**
 * Convenience facade for the drop down control.
 *
 * <p>Standalone usage routes the collapsed field through
 * {@code DropDownInputLayer}. When the same facade is added to a {@code Panel},
 * the panel routes pointer input to the collapsed field in panel-local
 * coordinates. In both cases the expanded list remains a global overlay managed
 * through the supplied {@code OverlayManager} and {@code InputManager}.</p>
 *
 * <p>The facade implements {@code ParentContextAwareControl} so a parent
 * container can supply its resolved sketch-space offset without depending on
 * drop-down-specific code.</p>
 *
 * @author CPZ
 */
public final class DropDown implements PointerRoutableControl, ParentSizeAwareControl, ParentContextAwareControl, TooltipAttachable {
    private static final int DEFAULT_OVERLAY_Z_INDEX = 100;

    private final PApplet sketch;
    private final DropDownModel model;
    private final DropDownViewModel viewModel;
    private final DropDownView view;
    private final FocusManager focusManager;
    private final DropDownOverlayController overlayController;
    private final DropDownInputAdapter inputAdapter;
    private final TooltipSupport tooltipSupport;
    private ControlBounds bounds;
    private ControlMeasure textSize;
    private boolean textSizeStyleIsolated;
    private Float parentWidth;
    private Float parentHeight;
    private float parentOffsetX;
    private float parentOffsetY;
    private float localX;
    private float localY;
    private float resolvedWidth;
    private float resolvedHeight;

    public DropDown(PApplet sketch, OverlayManager overlayManager, InputManager inputManager, List<String> items, float x, float y, float width, float height) {
        this(sketch, overlayManager, inputManager, ControlCode.auto("dropdown"), items, -1, x, y, width, height);
    }

    public DropDown(PApplet sketch, OverlayManager overlayManager, InputManager inputManager, List<String> items, int selectedIndex, float x, float y, float width, float height) {
        this(sketch, overlayManager, inputManager, ControlCode.auto("dropdown"), items, selectedIndex, x, y, width, height);
    }

    public DropDown(PApplet sketch, OverlayManager overlayManager, InputManager inputManager, String code, List<String> items, float x, float y, float width, float height) {
        this(sketch, overlayManager, inputManager, code, items, -1, x, y, width, height);
    }

    public DropDown(PApplet sketch, OverlayManager overlayManager, InputManager inputManager, String code, List<String> items, int selectedIndex, float x, float y, float width, float height) {
        this(sketch, overlayManager, inputManager, code, items, selectedIndex, ControlBounds.absolute(x, y, width, height));
    }

    public DropDown(PApplet sketch, OverlayManager overlayManager, InputManager inputManager, List<String> items, ControlBounds bounds) {
        this(sketch, overlayManager, inputManager, ControlCode.auto("dropdown"), items, -1, bounds);
    }

    public DropDown(PApplet sketch, OverlayManager overlayManager, InputManager inputManager, List<String> items, int selectedIndex, ControlBounds bounds) {
        this(sketch, overlayManager, inputManager, ControlCode.auto("dropdown"), items, selectedIndex, bounds);
    }

    public DropDown(PApplet sketch, OverlayManager overlayManager, InputManager inputManager, String code, List<String> items, ControlBounds bounds) {
        this(sketch, overlayManager, inputManager, code, items, -1, bounds);
    }

    public DropDown(PApplet sketch, OverlayManager overlayManager, InputManager inputManager, String code, List<String> items, int selectedIndex, ControlBounds bounds) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
        Objects.requireNonNull(overlayManager, "overlayManager");
        Objects.requireNonNull(inputManager, "inputManager");
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        ResolvedBounds resolvedBounds = this.resolveBounds();
        this.model = new DropDownModel(code, items, selectedIndex);
        this.viewModel = new DropDownViewModel(this.model);
        this.view = new DropDownView(
                sketch,
                this.viewModel,
                resolvedBounds.x(),
                resolvedBounds.y(),
                resolvedBounds.width(),
                resolvedBounds.height()
        );
        this.focusManager = new FocusManager();
        this.overlayController = new DropDownOverlayController(
                this.view,
                this.viewModel,
                this.focusManager,
                overlayManager,
                inputManager,
                DEFAULT_OVERLAY_Z_INDEX,
                this::applyGlobalGeometry,
                this::syncViewGeometry
        );
        this.inputAdapter = new DropDownInputAdapter(this.view, this.viewModel, this.focusManager, this.overlayController);
        this.tooltipSupport = new TooltipSupport(this::getLocalTooltipBounds, this::isVisible);
        this.captureResolvedGeometry(resolvedBounds);
    }

    public void draw() {
        this.applyResolvedGeometryAndTextSize();
        this.syncOverlayState();
        if (!this.viewModel.isExpanded()) {
            this.view.draw();
        }
    }

    public void handlePointerEvent(PointerEvent event) {
        this.applyResolvedGeometryAndTextSize();
        if (event != null) {
            this.inputAdapter.handlePointerEvent(event);
            this.syncOverlayState();
        }
    }

    public boolean canConsumePointerEvent(PointerEvent event) {
        this.applyResolvedGeometryAndTextSize();
        return event != null
                && event.getType() != PointerEvent.Type.WHEEL
                && this.isVisible()
                && this.containsLocalBase(event.getX(), event.getY());
    }

    public void dispose() {
        this.overlayController.dispose();
    }

    public String getCode() {
        return this.model.getCode();
    }

    public List<String> getItems() {
        return this.model.getItems();
    }

    public void setItems(List<String> items) {
        this.viewModel.setItems(items);
        this.syncOverlayState();
    }

    public int getSelectedIndex() {
        return this.viewModel.getSelectedIndex();
    }

    public void setSelectedIndex(int selectedIndex) {
        this.viewModel.selectIndex(selectedIndex);
    }

    public String getSelectedItem() {
        int selectedIndex = this.viewModel.getSelectedIndex();
        List<String> items = this.model.getItems();
        return selectedIndex >= 0 && selectedIndex < items.size() ? items.get(selectedIndex) : null;
    }

    public boolean isExpanded() {
        return this.viewModel.isExpanded();
    }

    public boolean isFocused() {
        return this.viewModel.isFocused();
    }

    public void setChangeListener(ValueListener<Integer> listener) {
        this.viewModel.setOnSelectionChanged(listener == null ? null : listener::onChange);
    }

    public boolean isEnabled() {
        return this.viewModel.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        this.viewModel.setEnabled(enabled);
        this.syncOverlayState();
    }

    public boolean isVisible() {
        return this.viewModel.isVisible();
    }

    public void setVisible(boolean visible) {
        this.viewModel.setVisible(visible);
        this.syncOverlayState();
    }

    public void setStyle(DefaultDropDownStyle style) {
        this.view.setStyle(style);
        this.textSizeStyleIsolated = false;
        this.applyResolvedTextSize();
    }

    public void setTextSize(float textSize) {
        this.setTextSize(ControlMeasure.absolute(textSize));
    }

    public void setTextSize(ControlMeasure textSize) {
        this.textSize = Objects.requireNonNull(textSize, "textSize");
        this.applyResolvedTextSize();
    }

    public void setPosition(float x, float y) {
        this.bounds = this.bounds.withPosition(ControlMeasure.absolute(x), ControlMeasure.absolute(y));
        this.applyResolvedGeometryAndTextSize();
    }

    public void setSize(float width, float height) {
        this.bounds = this.bounds.withSize(ControlMeasure.absolute(width), ControlMeasure.absolute(height));
        this.applyResolvedGeometryAndTextSize();
    }

    public void setBounds(ControlBounds bounds) {
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        this.applyResolvedGeometryAndTextSize();
    }

    public void setParentSize(float width, float height) {
        this.parentWidth = width;
        this.parentHeight = height;
        this.applyResolvedGeometryAndTextSize();
    }

    public void clearParentSize() {
        this.parentWidth = null;
        this.parentHeight = null;
        this.applyResolvedGeometryAndTextSize();
    }

    public void setParentOffset(float x, float y) {
        this.parentOffsetX = x;
        this.parentOffsetY = y;
        this.applyResolvedGeometryAndTextSize();
    }

    public void clearParentOffset() {
        this.parentOffsetX = 0.0F;
        this.parentOffsetY = 0.0F;
        this.applyResolvedGeometryAndTextSize();
    }

    public void onRemovedFromParent() {
        this.overlayController.closeOverlay();
        this.syncViewGeometry();
    }

    public DropDown setTooltip(String text) {
        this.tooltipSupport.setTooltip(text);
        return this;
    }

    public DropDown setTooltip(Tooltip tooltip) {
        this.tooltipSupport.setTooltip(tooltip);
        return this;
    }

    public DropDown setTooltipText(String text) {
        this.tooltipSupport.setTooltipText(text);
        return this;
    }

    public DropDown setTooltipStyle(TooltipStyleConfig styleConfig) {
        this.tooltipSupport.setTooltipStyle(styleConfig);
        return this;
    }

    public DropDown setTooltipFont(PFont font) {
        this.tooltipSupport.setTooltipFont(font);
        return this;
    }

    public DropDown setTooltipTextSize(float size) {
        this.tooltipSupport.setTooltipTextSize(size);
        return this;
    }

    public DropDown setTooltipBackgroundColor(int argb) {
        this.tooltipSupport.setTooltipBackgroundColor(argb);
        return this;
    }

    public DropDown setTooltipTextColor(int argb) {
        this.tooltipSupport.setTooltipTextColor(argb);
        return this;
    }

    public DropDown setTooltipBorderColor(int argb) {
        this.tooltipSupport.setTooltipBorderColor(argb);
        return this;
    }

    public DropDown setTooltipPadding(float padding) {
        this.tooltipSupport.setTooltipPadding(padding);
        return this;
    }

    public DropDown setTooltipOffset(float offset) {
        this.tooltipSupport.setTooltipOffset(offset);
        return this;
    }

    public DropDown clearTooltip() {
        this.tooltipSupport.clearTooltip();
        return this;
    }

    public TooltipBounds getTooltipBounds() {
        this.applyResolvedGeometryAndTextSize();
        return this.tooltipSupport.getTooltipBounds();
    }

    public Tooltip getTooltip() {
        return this.tooltipSupport.getTooltip();
    }

    public boolean isTooltipTargetVisible() {
        return this.tooltipSupport.isTooltipTargetVisible();
    }

    public boolean isTooltipTargetEnabled() {
        return this.tooltipSupport.isTooltipTargetEnabled();
    }

    private ResolvedBounds resolveBounds() {
        return this.bounds.resolve(this.parentWidth(), this.parentHeight());
    }

    private float parentWidth() {
        return this.parentWidth != null ? this.parentWidth : this.sketch.width;
    }

    private float parentHeight() {
        return this.parentHeight != null ? this.parentHeight : this.sketch.height;
    }

    private void applyResolvedGeometryAndTextSize() {
        ResolvedBounds resolvedBounds = this.resolveBounds();
        this.captureResolvedGeometry(resolvedBounds);
        this.syncViewGeometry();
        this.applyResolvedTextSize();
    }

    private void captureResolvedGeometry(ResolvedBounds resolvedBounds) {
        this.localX = resolvedBounds.x();
        this.localY = resolvedBounds.y();
        this.resolvedWidth = resolvedBounds.width();
        this.resolvedHeight = resolvedBounds.height();
        this.view.setSize(this.resolvedWidth, this.resolvedHeight);
    }

    private void applyResolvedTextSize() {
        if (this.textSize == null) {
            return;
        }
        this.ensureTextSizeStyleIsolated();
        DropDownStyleConfig styleConfig = this.view.getStyle().getDropDownStyleConfig();
        if (styleConfig != null) {
            styleConfig.textSize = this.textSize.resolve(this.parentHeight());
            styleConfig.font = FontLoader.resolve(styleConfig.fontResolver, this.sketch, styleConfig.textSize, styleConfig.font);
        }
    }

    private void ensureTextSizeStyleIsolated() {
        if (this.textSizeStyleIsolated) {
            return;
        }
        DefaultDropDownStyle style = this.view.getStyle();
        if (style != null) {
            this.view.setStyle(style.copy());
        }
        this.textSizeStyleIsolated = true;
    }

    private void syncOverlayState() {
        this.overlayController.syncRegistration();
        this.syncViewGeometry();
    }

    private void syncViewGeometry() {
        if (this.viewModel.isExpanded()) {
            this.applyGlobalGeometry();
        } else {
            this.view.setPosition(this.localX, this.localY);
        }
    }

    private void applyGlobalGeometry() {
        this.view.setPosition(this.globalX(), this.globalY());
    }

    private float globalX() {
        return this.localX + this.parentOffsetX;
    }

    private float globalY() {
        return this.localY + this.parentOffsetY;
    }

    private boolean containsLocalBase(float x, float y) {
        float halfWidth = this.resolvedWidth * 0.5F;
        float halfHeight = this.resolvedHeight * 0.5F;
        return x >= this.localX - halfWidth
                && x <= this.localX + halfWidth
                && y >= this.localY - halfHeight
                && y <= this.localY + halfHeight;
    }

    private TooltipBounds getLocalTooltipBounds() {
        return new TooltipBounds(
                this.localX - this.resolvedWidth * 0.5F,
                this.localY - this.resolvedHeight * 0.5F,
                this.resolvedWidth,
                this.resolvedHeight
        );
    }
}
