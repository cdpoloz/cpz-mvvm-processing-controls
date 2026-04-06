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

1. Input adapters translate sketch events into `ViewModel` operations.
2. The `ViewModel` updates its model and interaction state.
3. The `View` reads the `ViewModel`, computes geometry, and builds a `ViewState`.
4. The `Style` resolves visual values using the current `ThemeSnapshot`.
5. The `Renderer` draws the final frame.

This keeps behavior decisions out of the view and keeps rendering decisions out of the model.

## Theme Flow

- `ThemeProvider` exposes a cached `ThemeSnapshot`
- `ThemeManager` implements `ThemeProvider`
- `ThemeSnapshot` is rebuilt when the theme changes, not during every draw call
- views read the snapshot once per draw or measurement pass
- styles consume snapshot tokens directly and do not perform theme lookup of their own

## Input And Focus

- `InputManager` dispatches pointer and keyboard events through ordered `InputLayer`s
- `FocusManager` owns keyboard focus, traversal, and restoration
- controls that need keyboard interaction implement `KeyboardInputTarget`
- pointer-oriented controls expose hit testing through `PointerInteractable`

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
