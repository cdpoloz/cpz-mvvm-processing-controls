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

Each concrete control still owns its specific public API for text, value, selection, listeners, style, and any other domain-specific behavior.

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

Those concerns remain in the concrete facade where they belong.

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
- [JSON Configuration](json-configuration.md)
- [README](../README.md)
