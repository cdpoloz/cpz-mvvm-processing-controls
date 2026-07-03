# Control

`Control` is the minimal public interface shared by the closed control facades of the framework.

It represents only the small transversal surface that is already common across the public controls. It does not expose MVVM internals, and it does not replace the control-specific API of each concrete facade.

---

## Purpose

`Control` exists to let sketches and host code treat heterogeneous controls uniformly when they only need the common public surface.

Typical examples include:

- storing controls in a `Map<String, Control>`
- iterating over a mixed collection of controls
- applying common visibility, enabled, drawing, or positioning logic
- consuming the result of `ControlConfigLoader`

In the composition-oriented JSON flow, `ControlConfigLoader` creates a `Map<String, Control>` from one document, the sketch resolves concrete facades by `code`, and the sketch then applies listeners or binding between those controls. `Control` is what makes that heterogeneous collection manageable without exposing MVVM internals.

---

## Contract

The interface contains only these members:

```java
String getCode();
void draw();
boolean isEnabled();
void setEnabled(boolean enabled);
boolean isVisible();
void setVisible(boolean visible);
void setPosition(float x, float y);
```

This surface is intentionally small:

- it keeps the facade layer closed and ergonomic
- it avoids exposing `View`, `ViewModel`, or other MVVM internals
- it does not try to flatten control-specific APIs into a richer shared hierarchy

---

## Scope

`Control` is implemented by the public control facades:

- `Button`
- `Checkbox`
- `Toggle`
- `Slider`
- `Label`
- `RadioGroup`
- `TextField`
- `NumericField`
- `DropDown`
- `Panel`

Each concrete control still owns its specific public API for text, value, selection, listeners, style, and any other domain-specific behavior.

`Panel` is a container facade. It implements `Control`, groups child controls,
and interprets child coordinates as local to the panel position. Input for a
panel is routed through `PanelInputLayer`, not through the base `Control`
contract.

Facade styles can share a sketch-owned `ThemeManager`, which keeps theming on the public facade side without exposing MVVM internals.

---

## Not Part Of This Contract

The following are intentionally outside `Control`:

- pointer and keyboard handling methods
- focus-specific methods
- size-specific methods
- style-specific methods
- listeners
- text, value, selection, items, or options APIs
- overlay lifecycle methods
- tooltip overlay management

Those concerns remain in the concrete facade where they belong.

Tooltips are intentionally modeled outside `Control`. Controls that support
tooltips expose the generic `TooltipTarget` contract, and arbitrary sketch
regions can use `TooltipArea` without implementing `Control`.

---

## Explicit Relative Measures

Relative coordinates are opt-in. The library does not infer that numeric values
between `0` and `1` are relative.

Use `ControlBounds` and `ControlMeasure` when a supported facade should resolve
geometry or text size against its parent:

```java
Panel panel = new Panel(this, "pnlSettings",
        ControlBounds.relative(0.1f, 0.1f, 0.4f, 0.3f));

Button button = new Button(this, "btnSave", "Save",
        ControlBounds.relative(0.5f, 0.5f, 0.3f, 0.12f));

Label label = new Label(this, "lblTitle", "Settings",
        ControlBounds.relative(0.08f, 0.12f, 0.6f, 0.1f));
label.setTextSize(ControlMeasure.relative(0.06f));
```

Relative bounds are supported by `Panel`, `Button`, `Label`, `Checkbox`,
`Toggle`, `Slider`, `TextField`, `NumericField`, `RadioGroup`, and `DropDown`.
For `RadioGroup`, the bounds height maps to item height because the group
height is derived from options and spacing.

Existing constructors and `setPosition(...)` / `setSize(...)` remain absolute
by default. JSON configuration does not support these relative measures yet.

Relative text size is also opt-in. `setTextSize(float)` is absolute; use
`setTextSize(ControlMeasure.relative(factor))` when the text should scale from
the parent height:

```java
Button save = new Button(this, "btnSave", "Save", 120, 80, 160, 44);
save.setTextSize(ControlMeasure.relative(0.04f));
```

Relative text size is supported by `Button`, `Label`, `TextField`,
`NumericField`, `RadioGroup`, `DropDown`, and the value text rendered by
`Slider`. `Checkbox` and `Toggle` do not expose text rendering in their public
facades.

---

## Distinction From ControlView

`Control` belongs to the public facade layer.

`ControlView` belongs to the internal MVVM view layer.

They are intentionally separate:

- `Control` is for host code using the public controls
- `ControlView` is for the internal rendering and layout layer

---

## See Also

- [Architecture](architecture.md)
- [Panel](panel.md)
- [JSON Configuration](json-configuration.md)
- [Tooltip](tooltip.md)
- [Theme](theme.md)
- [README](../README.md)
