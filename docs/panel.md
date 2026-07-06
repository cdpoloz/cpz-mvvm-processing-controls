# Panel

`Panel` is a public container facade for grouping controls in a local
coordinate space.

It implements `Control`, through the optional routable-control contracts, so it
can be drawn, moved, shown, hidden, enabled, and disabled like the other public
facades.

---

## Purpose

Use `Panel` when a set of controls should move or become available as one
group.

The first implementation is intentionally small:

- it stores an ordered list of child `Control` instances
- it draws children relative to the panel position
- it routes pointer input from global coordinates into panel-local coordinates
- it exposes group-level `visible`, `enabled`, and `setPosition(...)`

It does not perform automatic layout or visual container rendering.

---

## Local Coordinates

Children keep their own coordinates, but those coordinates are interpreted as
local to the panel.

If the panel is at `(100, 80)` and a child button is at `(10, 20)`, the button
is rendered and hit-tested at global position `(110, 100)`.

Moving the panel with `setPosition(...)` changes the panel origin only. It does
not rewrite child positions.

---

## Explicit Relative Bounds

Controls that implement the relative geometry API can opt into explicit
relative bounds with `ControlBounds.relative(...)`. This includes `Panel`,
`Button`, `Label`, `Checkbox`, `Toggle`, `Slider`, `TextField`,
`NumericField`, `RadioGroup`, and `Indicator`. `DropDown` can use relative
bounds as a root control, but full `DropDown` support inside a panel remains
outside the panel MVP because its expanded list uses a global overlay.

Absolute constructors and setters remain the default behavior.

```java
Panel panel = new Panel(this, "pnlSettings",
        ControlBounds.relative(0.1f, 0.1f, 0.45f, 0.35f));

Button save = new Button(this, "btnSave", "Save",
        ControlBounds.relative(0.5f, 0.5f, 0.3f, 0.12f));

Label title = new Label(this, "lblTitle", "Settings",
        ControlBounds.relative(0.08f, 0.12f, 0.6f, 0.1f));
title.setTextSize(ControlMeasure.relative(0.06f));

panel.add(title).add(save);
```

Resolution rules are explicit:

- relative `x` uses `parentWidth * factor`
- relative `y` uses `parentHeight * factor`
- relative `width` uses `parentHeight * factor`
- relative `height` uses `parentHeight * factor`
- relative text size uses `parentHeight * factor`

For root controls, the parent is the sketch canvas. For children inside a
panel, the parent is the panel's resolved width and height. The resolved child
coordinates are still local to the panel. Child text controls that support
`setTextSize(ControlMeasure.relative(...))` also resolve text size against the
panel height.

Relative text size is supported by `Button`, `Label`, `TextField`,
`NumericField`, `RadioGroup`, `DropDown`, and the value text rendered by
`Slider`. `DropDown` is still not supported as an interactive panel child.

For `RadioGroup`, relative bounds height maps to the item height; the total
group height is still derived from options and item spacing.

JSON supports relative bounds for the registered root controls, including
controls that can later be added to a panel from Java. `Panel` itself is not a
JSON control type yet, and JSON does not define `Panel.children` in this
iteration.

---

## Drawing

`Panel.draw()` uses the Processing matrix stack:

```java
pushMatrix();
translate(panelX, panelY);
child.draw();
popMatrix();
```

The matrix is restored in a `finally` block, so one child draw failure cannot
leave the sketch in a translated coordinate system.

`Panel` currently does not draw a background, border, padding, or clipping
region. A sketch can draw a simple frame behind it when needed.

---

## Input

Register panels with `PanelInputLayer`:

```java
InputManager inputManager = new InputManager();
Panel panel = new Panel(this, "pnlSettings", 100, 80, 320, 220);

inputManager.registerLayer(new PanelInputLayer(0, panel));
```

The layer receives normal sketch-space `PointerEvent` instances. The panel
subtracts its own `x` and `y` before routing the event to compatible children.

Keyboard routing is available for children that implement the optional
`KeyboardRoutableControl` contract, such as `TextField`, `NumericField`, and
`RadioGroup`.

---

## Supported Children

The current panel input route supports these child controls:

- `Panel`
- `Button`
- `Checkbox`
- `Toggle`
- `Slider`
- `TextField`
- `NumericField`
- `RadioGroup`
- non-interactive `Control` children such as `Label`
- non-interactive status controls such as `Indicator`

`DropDown` is not supported as an interactive panel child yet. Its collapsed
control can use relative bounds as a root control, but its expanded list is
managed through a global overlay and is not translated through `PanelInputLayer`.

---

## Visibility And Enabled State

`setVisible(false)` hides the panel and suppresses child visibility. An
invisible panel does not draw and does not interact.

`setEnabled(false)` disables the panel and suppresses child enabled state. A
disabled visible panel does not activate children, but pointer events inside
the panel bounds are still consumed so lower input layers do not receive
click-through.

When the panel is made visible or enabled again, child states that existed
before the panel-level suppression are restored.

---

## Child Management

The public child API is:

```java
panel.add(control);
panel.remove(control);
panel.remove("controlCode");
panel.clear();
panel.children();
```

`children()` returns an unmodifiable ordered view.

---

## Tooltips

`Panel` is not `TooltipAttachable`.

For child controls that already implement `TooltipAttachable`, use
`panel.tooltipTarget(child)` when registering with `TooltipOverlayController`.
The wrapper reports bounds in sketch coordinates by adding the panel offset to
the child tooltip bounds.

```java
Button childButton = new Button(this, "btnChild", "Save", 10, 20, 120, 40)
        .setTooltip("Save settings");

panel.add(childButton);
tooltips.registerTarget(panel.tooltipTarget(childButton));
```

Non-interactive tooltip targets such as `Indicator` use the same route:

```java
Indicator indicator = new Indicator(this, "indStatus",
        ControlBounds.relative(0.1f, 0.1f, 0.08f, 0.08f));

panel.add(indicator);
tooltips.registerTarget(panel.tooltipTarget(indicator));
```

---

## Minimal Example

```java
Panel panel = new Panel(this, "pnlSettings", 100, 80, 320, 220);

Label title = new Label(this, "lblTitle", "Settings", 16, 16, 220, 28);
Button save = new Button(this, "btnSave", "Save", 24, 70, 120, 40);
TextField name = new TextField(this, "txtName", "Project", 24, 130, 220, 40);

panel.add(title)
     .add(save)
     .add(name);

InputManager inputManager = new InputManager();
inputManager.registerLayer(new PanelInputLayer(0, panel));

public void draw() {
    background(32);
    panel.draw();
}
```

Pointer callbacks still dispatch sketch-space events:

```java
public void mousePressed() {
    inputManager.dispatchPointer(
            new PointerEvent(PointerEvent.Type.PRESS, mouseX, mouseY, mouseButton)
    );
}
```

---

## Example Sketch

The interactive example is:

```text
src/main/java/com/cpz/processing/controls/examples/panel/PanelVisualTest.java
```

It shows local child coordinates, group movement, visible/enabled toggles,
keyboard routing to a child `TextField`, and a child tooltip registered through
the panel tooltip wrapper.

---

## Current Limitations

The MVP intentionally does not include:

- JSON creation or hierarchical JSON
- automatic layout
- clipping
- formal background, border, or padding API
- full `DropDown` support inside a panel

`DropDown` uses overlay registration in sketch coordinates, so it remains
outside the panel MVP.

---

## See Also

- [Control](control.md)
- [Input System](input-system.md)
- [Tooltip](tooltip.md)
- [Composition Patterns](composition-patterns.md)
