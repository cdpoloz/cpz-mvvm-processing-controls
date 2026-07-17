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
- it can draw optional runtime-configurable background and border chrome
- it routes pointer input from global coordinates into panel-local coordinates
- it exposes group-level `visible`, `enabled`, and `setPosition(...)`

It does not perform automatic layout, padding, clipping, scroll, headers, or
titles.

---

## Local Coordinates

Children keep their own coordinates, but those coordinates are interpreted as
local to the panel.

If the panel is at `(100, 80)` and a child button is at `(10, 20)`, the button
is rendered and hit-tested at global position `(110, 100)`.

Moving the panel with `setPosition(...)` changes the panel origin only. It does
not rewrite child positions.

For composed `DropDown`, the same local-coordinate rule applies to the closed
field. The panel renders that closed field under its own matrix translation and
routes collapsed-field input by converting sketch-space pointer events into
panel-local coordinates before delegating to the child.

---

## Explicit Relative Bounds

Controls that implement the relative geometry API can opt into explicit
relative bounds with `ControlBounds.relative(...)`. This includes `Panel`,
`Button`, `Label`, `Checkbox`, `Toggle`, `Slider`, `TextField`,
`NumericField`, `RadioGroup`, `Indicator`, `ProgressBar`, and `DropDown`.

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

For a `DropDown` child specifically:

- relative `x` uses the resolved panel width
- relative `y` uses the resolved panel height
- relative `width` uses the resolved panel height
- relative `height` uses the resolved panel height

This keeps the current uniform-height geometry contract unchanged. Relative
width intentionally uses panel height as its scale factor.

Relative text size is supported by `Button`, `Label`, `TextField`,
`NumericField`, `RadioGroup`, `DropDown`, and the value text rendered by
`Slider`.

For `RadioGroup`, relative bounds height maps to the item height; the total
group height is still derived from options and item spacing.

JSON supports relative bounds for the registered root controls, including
`Panel` and controls that can later be added to a panel from Java. JSON does
not define `Panel.children` in this iteration.

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

The visual chrome is drawn before the local child transform. Panel style does
not add padding and does not change child coordinates.

If a child `DropDown` is expanded, `Panel.draw()` still renders only the
collapsed field inside the translated panel space. The expanded list remains a
global overlay. The child uses its local resolved coordinates plus the panel's
resolved `(x, y)` offset to place that overlay in sketch space.

---

## Runtime Style

`Panel` supports visual styling through direct setters, a mutable `PanelStyle`
object, or the JSON `style` block documented below:

```java
Panel panel = new Panel(this, "pnlSettings", 100, 80, 320, 220);

panel.setBackgroundColor(0xFF20242A);
panel.setBackgroundVisible(true);

panel.setStrokeColor(0xFF6D7682);
panel.setStrokeVisible(true);
panel.setStrokeWeight(2.0f);

panel.setCornerRadius(10.0f);
```

Equivalent style-object usage:

```java
PanelStyle style = new PanelStyle()
        .setBackgroundColor(0xFF20242A)
        .setBackgroundVisible(true)
        .setStrokeColor(0xFF6D7682)
        .setStrokeVisible(true)
        .setStrokeWeight(2.0f)
        .setCornerRadius(10.0f);

panel.setStyle(style);
```

The available runtime properties are:

- `backgroundColor`
- `backgroundVisible`
- `strokeColor`
- `strokeVisible`
- `strokeWeight`
- `cornerRadius`

Defaults preserve the previous panel appearance: background and stroke are both
hidden. Default `strokeWeight` is `1.0f`; default `cornerRadius` is `0.0f`.

Color getters return effective colors. If no explicit color was set, the panel
resolves `backgroundColor` from the current theme `surface` token and
`strokeColor` from the current theme `border` token. Visibility getters and
numeric getters return the configured runtime values.

`getStyle()` returns the live mutable `PanelStyle` instance, matching the simple
style-object precedent used by controls such as `ProgressBar`. `setStyle(null)`
resets the panel to a new default style.

Negative `strokeWeight` and `cornerRadius` values are normalized to `0.0f`.
`NaN` and infinite values throw `IllegalArgumentException`.

Hiding the background or stroke only affects drawing:

```java
panel.setBackgroundVisible(false); // children and input still work
panel.setStrokeVisible(false);     // background and children still draw
```

Style changes are applied on the next `draw()` call. They do not modify bounds,
do not move children, do not affect panel input routing, and do not alter
`DropDown` overlay positioning.

JSON style initializes the same live `PanelStyle` instance used by the runtime
API, so later setter calls override the loaded values without rebuilding the
panel.

---

## JSON Style

`Panel` JSON accepts an optional nested `style` block:

```json
{
  "type": "panel",
  "code": "settingsPanel",
  "x": 100,
  "y": 80,
  "width": 320,
  "height": 220,
  "enabled": true,
  "visible": true,
  "style": {
    "backgroundVisible": true,
    "backgroundColor": "#20242A",
    "strokeVisible": true,
    "strokeColor": "#6D7682",
    "strokeWeight": 2.0,
    "cornerRadius": 10.0
  }
}
```

All style properties are optional. An empty style block preserves the same
defaults as an omitted style block. A partial style is valid:

```json
{
  "style": {
    "backgroundVisible": true
  }
}
```

Supported properties:

- `backgroundVisible`: whether the background is drawn
- `backgroundColor`: background color using the common JSON color parser
- `strokeVisible`: whether the border is drawn
- `strokeColor`: border color using the common JSON color parser
- `strokeWeight`: border thickness
- `cornerRadius`: corner radius

Missing color properties remain unset in `PanelStyle` and resolve dynamically
from the current theme: `surface` for background and `border` for stroke.
Explicit colors have priority over theme fallbacks. Missing visibility and
numeric properties keep the `PanelStyle` defaults: hidden background, hidden
stroke, `strokeWeight = 1.0f`, and `cornerRadius = 0.0f`.

Colors use the same formats as other JSON styles, including integer values,
`#RRGGBB`, and `#AARRGGBB`. Negative `strokeWeight` and `cornerRadius` values
are normalized to `0.0f`; invalid types or color formats fail during
configuration loading.

JSON style does not add padding, layout, clipping, scroll, titles, shadows, or
child declarations.

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
Children are consulted from last added to first added, so later children have
the first chance to consume pointer input.

For composed `DropDown`:

- collapsed-field input is converted from global to panel-local before the
  child handles it
- expanded-list input stays global because it is captured by the drop-down's
  overlay input layer
- render and hit testing use the same resolved local geometry for the closed
  field
- the overlay uses `local + parentOffset` after the panel has synchronized the
  child context

---

## Child Context Updates

`Panel` refreshes child parent size and parent offset when:

- `draw()` runs
- `setPosition(...)` runs
- `setSize(...)` runs
- `setBounds(...)` runs
- `setParentSize(...)` runs
- `clearParentSize()` runs

This means composed relative children update immediately for panel movement and
panel resizing.

For a direct sketch canvas resize, relative children are synchronized on the
next panel synchronization path. The supported flow is:

```java
panel.draw();
for (OverlayEntry entry : overlayManager.getActiveOverlays()) {
    entry.getRender().run();
}
```

Do not treat overlay rendering before the next panel synchronization after a
canvas resize as a supported contract for composed relative children.

Keyboard routing is available for children that implement the optional
`KeyboardRoutableControl` contract, such as `TextField`, `NumericField`, and
`RadioGroup`.

`DropDown` can also be added programmatically as a panel child:

```java
Panel panel = new Panel(this, "pnlSettings", 100, 80, 320, 220);

DropDown dropDown = new DropDown(
        this,
        overlayManager,
        inputManager,
        "ddSettings",
        List.of("Low", "Medium", "High"),
        0,
        0,
        160,
        28
);
dropDown.setPosition(20, 40);

panel.add(dropDown);
```

The drop-down base keeps its coordinates local to the panel, while the expanded
list still renders through `OverlayManager` in sketch/global space. The
expanded list also registers its normal overlay input layer with higher
priority than the panel layer, so the panel does not block selection outside
its bounds. Do not register a standalone `DropDownInputLayer` for the same
instance after adding it to a panel; the panel routes the collapsed field and
the dropdown keeps its own overlay input while expanded. JSON child definitions
for `Panel` remain outside this feature.

---

## JSON Composition

`Panel` can be loaded as a root control from `ControlConfigLoader`. The JSON
entry supports `type`, `code`, bounds, `visible`, `enabled`, and the optional
`style` block described above.

`Panel.children` is not a JSON feature yet. For a JSON-driven panel/dropdown
composition, load both controls from the same `controls[]` document and compose
them in Java:

```java
ControlConfigLoader loader = new ControlConfigLoader(this, overlayManager, inputManager);
Map<String, Control> controls = loader.load("data/config/panel-dropdown.json");

Panel panel = (Panel) controls.get("pnlJsonDropDown");
DropDown dropDown = (DropDown) controls.get("ddPanelMode");

panel.add(dropDown);
```

Register input after composing the final tree:

```java
inputManager.registerLayer(new PanelInputLayer(0, panel));
```

If the document also contains an external control behind the panel, register it
in its own lower-priority layer. Do not register the composed `DropDown` as a
standalone input target as well.

The `DropDown` coordinates from JSON become local to the panel after
`panel.add(dropDown)`. Its expanded list remains a global overlay managed by
`OverlayManager`, so it is not clipped by the panel bounds. The panel style in
`data/config/panel-dropdown.json` is loaded from JSON; the parent-child
relationship is still established in Java. See `PanelDropDownJsonTest`.

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
- `DropDown` for runtime/programmatic composition
- non-interactive `Control` children such as `Label`
- non-interactive status/display controls such as `Indicator` and `ProgressBar`

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

For controls with parent-aware overlays, such as `DropDown`, hiding or
disabling the panel suppresses the child and closes any active overlay through
the child control's normal visibility/enabled lifecycle.

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

Removing a child clears the parent context from controls that implement the
optional parent-context contract and restores any visibility/enabled state that
the panel had temporarily suppressed. `clear()` applies the same removal flow to
all children. A removed child can be reused as a standalone control or added to
another parent; parent-aware controls close active overlays when they are
removed so no panel-owned overlay remains interactive.

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

The same route works for `ProgressBar`:

```java
ProgressBar progressBar = new ProgressBar(this, "pbLoad",
        ControlBounds.relative(0.1f, 0.2f, 0.5f, 0.08f));

panel.add(progressBar);
tooltips.registerTarget(panel.tooltipTarget(progressBar));
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

The current implementation intentionally does not include:

- `Panel.children` in JSON
- automatic layout
- padding
- clipping
- scroll
- headers or titles
- shadows

---

## See Also

- [Control](control.md)
- [Input System](input-system.md)
- [Tooltip](tooltip.md)
- [Composition Patterns](composition-patterns.md)
