# Architecture

## Public Control Surface

The framework exposes controls through closed public facades such as `Button`, `Checkbox`, `Toggle`, `Slider`, `Label`, `RadioGroup`, `TextField`, `NumericField`, and `DropDown`.

Those facades share a minimal public interface, `Control`, that contains only the transversal facade surface:

- `getCode()`
- `draw()`
- `isEnabled()` / `setEnabled(boolean)`
- `isVisible()` / `setVisible(boolean)`
- `setPosition(float, float)`

This contract is intentionally small:

- it allows host code to treat heterogeneous controls uniformly when needed
- it does not expose `View`, `ViewModel`, or other MVVM internals
- it does not replace the control-specific API of each concrete facade

`Control` is distinct from `ControlView`. `Control` belongs to the public facade layer, while `ControlView` belongs to the internal MVVM view layer.

## Pipeline

```text
Model → ViewModel → View → ViewState → Style → RenderStyle → Renderer
```

## Layer Responsibilities

- `Model`: stores durable control state and constraints
- `ViewModel`: owns interaction, validation, focus-related state, and model updates
- `View`: owns layout, hit testing, text measurement, and `ViewState` creation
- `ViewState`: immutable per-frame data prepared for visual resolution
- `Style`: maps `ViewState` and `ThemeSnapshot` into concrete render values
- `Renderer`: performs drawing only and does not decide behavior

## Data Flow

1. An external adapter converts host input into framework-owned events.
2. `InputManager` dispatches those events through ordered `InputLayer`s.
3. A layer forwards the event to a control or overlay input adapter.
4. The input adapter translates event state and view geometry into `ViewModel` operations.
5. The `ViewModel` updates its model and interaction state.
6. The `View` reads the `ViewModel`, computes geometry, and builds a `ViewState`.
7. The `Style` resolves visual values using the current `ThemeSnapshot`.
8. The `Renderer` draws the final frame.

This keeps behavior decisions out of the view, rendering decisions out of the model, and source-specific event logic outside the framework core.

## Theme Flow

- `ThemeProvider` exposes a cached `ThemeSnapshot`
- `ThemeManager` implements `ThemeProvider`
- `ThemeSnapshot` is rebuilt when the theme changes, not during every draw call
- views read the snapshot once per draw or measurement pass
- styles consume snapshot tokens directly and do not perform theme lookup of their own

## Input And Focus

- the input system is source-agnostic
- external adapters are responsible for converting host callbacks into `PointerEvent` and `KeyboardEvent`
- `InputManager` dispatches pointer and keyboard events through ordered `InputLayer`s
- `InputManager` does not depend on Processing types
- `PointerEvent` carries `type`, `x`, `y`, `pressed`, `button`, `shift`, `ctrl`, `alt`, and `wheelDelta`
- `KeyboardEvent` carries key identity plus modifier state
- control input adapters handle framework-owned events directly and do not require Processing event objects
- `FocusManager` owns keyboard focus, traversal, and restoration
- controls that need keyboard interaction implement `KeyboardInputTarget`
- pointer-oriented controls expose hit testing through `PointerInteractable`

## Integration Flow

```text
External Source → Adapter → InputManager → InputLayer → InputAdapter → ViewModel
```

This same flow applies whether the source is Processing or another host environment.

## Public Facades And MVVM

The public controls remain facade types over the internal MVVM pipeline.

- sketches use concrete facades such as `Button` or `TextField`
- shared host-side logic may depend on the minimal `Control` interface
- MVVM internals remain behind the facade boundary

This keeps the public API lightweight without flattening the specific behavior of each control into a broad shared hierarchy.

## JSON Layer

The JSON layer is an optional configuration boundary in front of the public facades.

Primary flow:

```text
JSON → ControlConfigLoader → ControlFactoryRegistry → concrete factories → Map<String, Control>
```

Behavior:

- JSON describes one or more controls through a root `controls` array
- `ControlConfigLoader` returns a `Map<String, Control>`
- factories still create the same closed public facades
- MVVM internals remain hidden
- binding is not part of JSON and remains the responsibility of the sketch

## Binding

Binding is intentionally explicit.

- the sketch owns synchronization between public facades
- `ControlConfigLoader` and `Map<String, Control>` support composition, not declarative behavior
- JSON does not define binding
- the framework does not provide a separate low-level binding utility as part of the current public model
- the documented learning path starts with one-way sketch synchronization and extends it to bidirectional synchronization with one extra listener, one extra sync routine, and a local anti-loop guard

See [Binding](binding.md).

## Related Documents

- [Control](control.md)
- [JSON Configuration](json-configuration.md)
- [README](../README.md)
- [Input System](input-system.md)
- [Binding](binding.md)
