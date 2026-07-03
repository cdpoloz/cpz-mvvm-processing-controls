package com.cpz.processing.controls.controls.geometry;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.label.Label;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.controls.panel.input.PanelInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipTarget;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RelativeGeometryControlTest {
    @Test
    void buttonAbsoluteBoundsKeepExistingBehavior() {
        PApplet sketch = sketch(800, 600);
        Button button = new Button(sketch, "btn", "Button", 120.0F, 90.0F, 80.0F, 30.0F);

        TooltipBounds bounds = button.getTooltipBounds();

        assertEquals(80.0F, bounds.x());
        assertEquals(75.0F, bounds.y());
        assertEquals(80.0F, bounds.width());
        assertEquals(30.0F, bounds.height());
    }

    @Test
    void panelRelativeBoundsResolveAgainstCanvas() {
        PApplet sketch = sketch(800, 600);
        Panel panel = new Panel(sketch, "panel", ControlBounds.relative(0.25F, 0.1F, 0.5F, 0.2F));

        assertEquals(200.0F, panel.getX());
        assertEquals(60.0F, panel.getY());
        assertEquals(300.0F, panel.getWidth());
        assertEquals(120.0F, panel.getHeight());
    }

    @Test
    void buttonRelativeBoundsResolveAgainstCanvas() {
        PApplet sketch = sketch(800, 600);
        Button button = new Button(sketch, "btn", "Button", ControlBounds.relative(0.25F, 0.1F, 0.5F, 0.2F));

        TooltipBounds bounds = button.getTooltipBounds();

        assertEquals(50.0F, bounds.x());
        assertEquals(0.0F, bounds.y());
        assertEquals(300.0F, bounds.width());
        assertEquals(120.0F, bounds.height());
    }

    @Test
    void labelRelativeBoundsAndTextSizeResolveAgainstCanvas() {
        PApplet sketch = sketch(800, 600);
        Label label = new Label(sketch, "lbl", "Label", ControlBounds.relative(0.25F, 0.1F, 0.5F, 0.2F));
        label.setTextSize(ControlMeasure.relative(0.05F));

        TooltipBounds bounds = label.getTooltipBounds();

        assertEquals(200.0F, bounds.x());
        assertEquals(60.0F, bounds.y());
        assertEquals(300.0F, bounds.width());
        assertEquals(120.0F, bounds.height());
        assertEquals(30.0F, label.getStyleConfig().textSize);
    }

    @Test
    void childRelativeBoundsResolveAgainstPanel() {
        PApplet sketch = sketch(800, 600);
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 400.0F, 200.0F);
        Button button = new Button(sketch, "btnChild", "Child", ControlBounds.relative(0.25F, 0.5F, 0.25F, 0.1F));
        AtomicInteger clicks = new AtomicInteger();
        button.setClickListener(clicks::incrementAndGet);
        panel.add(button);
        InputManager inputManager = new InputManager();
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 200.0F, 180.0F));
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, 200.0F, 180.0F));
        TooltipBounds bounds = panel.tooltipTarget(button).getTooltipBounds();

        assertEquals(1, clicks.get());
        assertEquals(175.0F, bounds.x());
        assertEquals(170.0F, bounds.y());
        assertEquals(50.0F, bounds.width());
        assertEquals(20.0F, bounds.height());
    }

    @Test
    void childLabelRelativeTextSizeResolvesAgainstPanelHeight() {
        PApplet sketch = sketch(800, 600);
        Panel panel = new Panel(sketch, "panel", 10.0F, 20.0F, 400.0F, 200.0F);
        Label label = new Label(sketch, "lblChild", "Child", ControlBounds.relative(0.1F, 0.2F, 0.5F, 0.1F));
        label.setTextSize(ControlMeasure.relative(0.1F));
        panel.add(label);

        TooltipTarget target = panel.tooltipTarget(label);
        TooltipBounds bounds = target.getTooltipBounds();

        assertEquals(50.0F, bounds.x());
        assertEquals(60.0F, bounds.y());
        assertEquals(100.0F, bounds.width());
        assertEquals(20.0F, bounds.height());
        assertEquals(20.0F, label.getStyleConfig().textSize);
    }

    @Test
    void relativePanelWithRelativeChildResolvesPanelAgainstCanvasAndChildAgainstPanel() {
        PApplet sketch = sketch(800, 600);
        Panel panel = new Panel(sketch, "panel", ControlBounds.relative(0.25F, 0.1F, 0.5F, 0.2F));
        Label label = new Label(sketch, "lblChild", "Child", ControlBounds.relative(0.2F, 0.25F, 0.5F, 0.1F));
        panel.add(label);

        TooltipBounds childBounds = panel.tooltipTarget(label).getTooltipBounds();

        assertEquals(200.0F, panel.getX());
        assertEquals(60.0F, panel.getY());
        assertEquals(300.0F, panel.getWidth());
        assertEquals(120.0F, panel.getHeight());
        assertEquals(260.0F, childBounds.x());
        assertEquals(90.0F, childBounds.y());
        assertEquals(60.0F, childBounds.width());
        assertEquals(12.0F, childBounds.height());
    }

    @Test
    void resizingPanelUpdatesRelativeChildThroughTooltipRoute() {
        PApplet sketch = sketch(800, 600);
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 400.0F, 200.0F);
        Label label = new Label(sketch, "lblChild", "Child", ControlBounds.relative(0.25F, 0.5F, 0.25F, 0.1F));
        panel.add(label);

        TooltipBounds initialBounds = panel.tooltipTarget(label).getTooltipBounds();
        panel.setSize(300.0F, 100.0F);
        TooltipBounds resizedBounds = panel.tooltipTarget(label).getTooltipBounds();

        assertEquals(200.0F, initialBounds.x());
        assertEquals(180.0F, initialBounds.y());
        assertEquals(50.0F, initialBounds.width());
        assertEquals(20.0F, initialBounds.height());
        assertEquals(175.0F, resizedBounds.x());
        assertEquals(130.0F, resizedBounds.y());
        assertEquals(25.0F, resizedBounds.width());
        assertEquals(10.0F, resizedBounds.height());
    }

    @Test
    void resizingPanelUpdatesRelativeButtonInputRoute() {
        PApplet sketch = sketch(800, 600);
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 400.0F, 200.0F);
        Button button = new Button(sketch, "btnChild", "Child", ControlBounds.relative(0.25F, 0.5F, 0.25F, 0.1F));
        AtomicInteger clicks = new AtomicInteger();
        button.setClickListener(clicks::incrementAndGet);
        panel.add(button);
        panel.setSize(300.0F, 100.0F);
        InputManager inputManager = new InputManager();
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 175.0F, 130.0F));
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, 175.0F, 130.0F));

        assertEquals(1, clicks.get());
    }

    @Test
    void setPositionAfterRelativeBoundsMakesPositionAbsoluteAndKeepsRelativeSize() {
        PApplet sketch = sketch(800, 600);
        Button button = new Button(sketch, "btn", "Button", ControlBounds.relative(0.25F, 0.1F, 0.5F, 0.2F));

        button.setPosition(300.0F, 240.0F);
        TooltipBounds bounds = button.getTooltipBounds();

        assertEquals(150.0F, bounds.x());
        assertEquals(180.0F, bounds.y());
        assertEquals(300.0F, bounds.width());
        assertEquals(120.0F, bounds.height());
    }

    @Test
    void setSizeAfterRelativeBoundsMakesSizeAbsoluteAndKeepsRelativePosition() {
        PApplet sketch = sketch(800, 600);
        Button button = new Button(sketch, "btn", "Button", ControlBounds.relative(0.25F, 0.1F, 0.5F, 0.2F));

        button.setSize(160.0F, 40.0F);
        TooltipBounds bounds = button.getTooltipBounds();

        assertEquals(120.0F, bounds.x());
        assertEquals(40.0F, bounds.y());
        assertEquals(160.0F, bounds.width());
        assertEquals(40.0F, bounds.height());
    }

    @Test
    void retainedRelativeChildUpdatesAfterPanelResizeWhenQueriedDirectlyAfterPanelRoute() {
        PApplet sketch = sketch(800, 600);
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 400.0F, 200.0F);
        Button button = new Button(sketch, "btnChild", "Child", ControlBounds.relative(0.25F, 0.5F, 0.25F, 0.1F));
        panel.add(button);

        panel.setSize(300.0F, 100.0F);
        panel.tooltipTarget(button).getTooltipBounds();
        TooltipBounds childLocalBounds = button.getTooltipBounds();

        assertEquals(62.5F, childLocalBounds.x());
        assertEquals(45.0F, childLocalBounds.y());
        assertEquals(25.0F, childLocalBounds.width());
        assertEquals(10.0F, childLocalBounds.height());
    }

    private static PApplet sketch(int width, int height) {
        PApplet sketch = new PApplet();
        sketch.width = width;
        sketch.height = height;
        return sketch;
    }
}
