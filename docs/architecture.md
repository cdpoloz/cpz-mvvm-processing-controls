# Architecture

## Pipeline

```text
Model -> ViewModel -> View -> ViewState -> Style -> RenderStyle -> Renderer
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
External Source -> Adapter -> InputManager -> InputLayer -> InputAdapter -> ViewModel
```

This same flow applies whether the source is Processing or another host environment.

## Binding

Binding is intentionally explicit.

- `Binding.bind(...)` supports unidirectional propagation between ViewModels
- the binding layer does not use reflection or control-specific contracts
- advanced bidirectional synchronization can be composed outside the core API when needed

See [Binding](binding.md).

## Related Documents

- [README](../README.md)
- [Input System](input-system.md)
- [Binding](binding.md)
